package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.exception.ParseError;
import info.colinhan.sisyphus.exception.ParseException;
import info.colinhan.sisyphus.jacal.parser.JacalParser;
import info.colinhan.sisyphus.server.dto.*;
import info.colinhan.sisyphus.server.exception.E;
import info.colinhan.sisyphus.server.exception.NotFoundException;
import info.colinhan.sisyphus.server.model.*;
import info.colinhan.sisyphus.server.repository.*;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.server.service.ProgramService;
import info.colinhan.sisyphus.server.utils.ProgramStatus;
import info.colinhan.sisyphus.server.utils.U;
import info.colinhan.sisyphus.tartarus.action.BuiltInActions;
import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.util.ResultOrErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    private ModelCompileService modelCompileService;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private FormVersionRepository formVersionRepository;
    @Autowired
    private FlowVersionRepository flowVersionRepository;
    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private ProgramVariableRepository programVariableRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private ProgramFormVersionRepository programFormVersionRepository;

    @Override
    public GetProgramInfoResponse buildProgramInfo() {
        List<FlowInfoWithPrograms> flows = flowRepository.findAll().stream()
                .map(this::buildFlow)
                .sorted((a, b) -> {
                    if (a.getPrograms().isEmpty() && b.getPrograms().isEmpty()) {
                        return a.getUpdatedAt().compareTo(b.getUpdatedAt());
                    }
                    if (a.getPrograms().isEmpty()) {
                        return 1;
                    }
                    if (b.getPrograms().isEmpty()) {
                        return -1;
                    }
                    return a.getPrograms().stream()
                            .max(Comparator.comparing(ProgramInfo::getUpdatedAt))
                            .orElseThrow()
                            .getUpdatedAt()
                            .compareTo(
                                    b.getPrograms().stream()
                                            .max(Comparator.comparing(ProgramInfo::getUpdatedAt))
                                            .orElseThrow()
                                            .getUpdatedAt()
                            );
                })
                .collect(Collectors.toList());
        return new GetProgramInfoResponse(flows);
    }

    private FlowInfoWithPrograms buildFlow(FlowEntity flow) {
        Long flowId = flow.getId();

        FlowInfoWithPrograms info = FlowInfoWithPrograms.builder()
                .id(flowId)
                .name(flow.getName())
                .description(flow.getDescription())
                .build();
        Consumer<FlowError> flowErrorConsumer = info.getErrors()::add;

        FlowVersionEntity flowVersion = getFlowVersion(flow, flowErrorConsumer);
        if (flowVersion == null) {
            return FlowInfoWithPrograms.builder()
                    .id(flowId)
                    .name(flow.getName())
                    .description(flow.getDescription())
                    .build();
        }

        FlowInfoWithPrograms flowInfoWithPrograms = buildFlowVersion(flow, flowVersion);
        flowInfoWithPrograms.setPrograms(findAllPrograms(flowId));
        return flowInfoWithPrograms;
    }

    private List<ProgramInfo> findAllPrograms(long flowId) {
        List<Long> flowVersionIds = flowVersionRepository.findAllByFlowId(flowId).stream()
                .map(FlowVersionEntity::getId)
                .collect(Collectors.toList());
        return programRepository.findAllByFlowVersionIdInOrderByUpdatedAtDesc(flowVersionIds).stream()
                .map(ProgramInfo::from)
                .collect(Collectors.toList());
    }

    private FlowInfoWithPrograms buildFlowVersion(FlowEntity flow, FlowVersionEntity flowVersion) {
        FlowInfoWithPrograms info = FlowInfoWithPrograms.builder()
                .id(flow.getId())
                .name(flow.getName())
                .description(flow.getDescription())
                .build();
        Consumer<FlowError> flowErrorConsumer = info.getErrors()::add;
        info.setVersion(flowVersion.getVersion());
        info.setUpdatedAt(flowVersion.getCreatedAt());

        Flow flowModel = getFlowModel(flow.getName(), flowVersion, flowErrorConsumer);
        if (flowModel == null) {
            return info;
        }

        Map<String, Integer> formVersions = validateForms(flowModel, flowErrorConsumer);
        if (formVersions != null) {
            info.setFormVersions(formVersions);
        }

        return info;
    }

    private Map<String, Integer> validateForms(Flow flowModel, Consumer<FlowError> flowErrorConsumer) {
        List<String> forms = getFormNames(flowModel);
        Map<String, Integer> formVersions = new HashMap<>();
        for (String formName : forms) {
            Optional<FormEntity> formEntity = formRepository.findFirstByName(formName);
            if (formEntity.isEmpty()) {
                flowErrorConsumer.accept(new FlowError(
                        "form",
                        formName,
                        Collections.singletonList(
                                new ParseError(0, 0, 0, "Form not found", null)
                        )
                ));
                continue;
            }

            Optional<FormVersionEntity> formVersion = formVersionRepository.findFirstByFormIdOrderByVersionDesc(formEntity.get().getId());
            if (formVersion.isEmpty()) {
                flowErrorConsumer.accept(new FlowError(
                        "form",
                        formName,
                        Collections.singletonList(
                                new ParseError(0, 0, 0, "Form version not found", null)
                        )
                ));
            } else {
                FormVersionEntity formVersionEntity = formVersion.get();
                formVersions.put(formName, formVersionEntity.getVersion());
                if (formVersionEntity.getModel() == null) {
                    try {
                        formVersionEntity.setModel(JacalParser.parse(formVersionEntity.getCode()));
                    } catch (ParseException e) {
                        flowErrorConsumer.accept(new FlowError(
                                "form",
                                formName,
                                e.getErrors()
                        ));
                    }
                }
            }
        }
        return formVersions;
    }

    private Flow getFlowModel(String flowName, FlowVersionEntity flowVersion, Consumer<FlowError> flowErrorConsumer) {
        Flow flowModel = null;
        try {
            if (flowVersion.getModel() == null) {
                modelCompileService.compileFlow(flowVersion);
            }
            flowModel = flowVersion.getModel();
        } catch (ParseException e) {
            flowErrorConsumer.accept(new FlowError(
                    "flow",
                    flowName,
                    e.getErrors()
            ));
        }
        return flowModel;
    }

    private FlowVersionEntity getFlowVersion(FlowEntity flow, Consumer<FlowError> flowErrorConsumer) {
        Optional<FlowVersionEntity> flowVersion = flowVersionRepository.findFirstByFlowIdOrderByVersionDesc(flow.getId());
        if (flowVersion.isEmpty()) {
            FlowError error = new FlowError(
                    "flow",
                    flow.getName(),
                    Collections.singletonList(
                            new ParseError(0, 0, 0, "Flow version not found", null)
                    )
            );
            flowErrorConsumer.accept(error);
            return null;
        }
        return flowVersion.get();
    }

    private List<String> getFormNames(Flow flow) {
        FormCollector collector = new FormCollector();
        collector.visit(flow);

        return collector.getForms().stream()
                .map(action -> (String) action.getParameter(BuiltInActions.PARAM_NAME_FORM_NAME).getValue(null))
                .distinct()
                .collect(Collectors.toList());
    }

    private Set<FormVersionEntity> getForms(Flow flow, Map<String, Integer> formVersions) {
        FormCollector collector = new FormCollector();
        collector.visit(flow);

        Set<FormVersionEntity> set = new HashSet<>();
        Set<String> formNames = collector.getForms().stream()
                .map(action -> (String) action.getParameter(BuiltInActions.PARAM_NAME_FORM_NAME).getValue(null))
                .collect(Collectors.toSet());
        for (String formName : formNames) {
            FormEntity formEntity = formRepository.findFirstByName(formName).orElseThrow();
            Long flowId = formEntity.getId();
            if (formVersions.containsKey(formName)) {
                Optional<FormVersionEntity> formVersion = formVersionRepository.findOneByFormIdAndVersion(flowId, formVersions.get(formName));
                if (formVersion.isPresent()) {
                    set.add(formVersion.get());
                    continue;
                } else {
                    throw new NotFoundException(String.format("Form version '%d' for '%s' not found", formVersions.get(formName), formName));
                }
            }
            Optional<FormVersionEntity> firstByFormIdOrderByVersionDesc = formVersionRepository.findFirstByFormIdOrderByVersionDesc(flowId);
            if (firstByFormIdOrderByVersionDesc.isPresent()) {
                FormVersionEntity formVersionEntity = firstByFormIdOrderByVersionDesc.get();
                set.add(formVersionEntity);
            } else {
                throw new NotFoundException(String.format("Form version for '%s' not found", formName));
            }
        }
        return set;
    }

    private static final Consumer<FlowError> throwErrorConsumer = (error) -> {
        throw new RuntimeException(error.toString());
    };

    @Override
    public ResultOrErrors<ProgramInfo, FlowError> createProgram(CreateProgramRequest request, String creator) {
        FlowEntity flow = E.assertPresent(flowRepository.findById(request.getFlowId()));
        FlowVersionEntity flowVersion = E.assertPresent(flowVersionRepository.findOneByFlowIdAndVersion(request.getFlowId(), request.getFlowVersion()));

        FlowInfoWithPrograms availableFlowInfo = buildFlowVersion(flow, flowVersion);
        if (!availableFlowInfo.getErrors().isEmpty()) {
            return ResultOrErrors.error(availableFlowInfo.getErrors());
        }

        Flow flowModel = getFlowModel(flow.getName(), flowVersion, throwErrorConsumer);
        Set<FormVersionEntity> formVersions = getForms(flowModel, request.getFormVersions());

        ProgramEntity program = programRepository.save(ProgramEntity.builder()
                .name(request.getName())
                .flowVersionId(flowVersion.getId())
                .createdBy(creator)
                .createdAt(U.timeNow())
                .updatedAt(U.timeNow())
                .status(ProgramStatus.IN_PROGRESS)
                .build());

        programFormVersionRepository.saveAll(formVersions.stream()
                .map(formVersion -> ProgramFormVersionEntity.builder()
                        .programId(program.getId())
                        .formVersionId(formVersion.getId())
                        .build())
                .collect(Collectors.toList()));

//        List<ProgramVariableEntity> variables = new ArrayList<>();
//        for (FormVersionEntity form : forms) {
//            Form formModel = form.getModel();
//            for (FormItem item : formModel.getItems()) {
//                variables.add(ProgramVariableEntity.builder()
//                        .programId(program.getId())
//                        .name(item.getName())
//                        .type(item.getType().getValueType().getName())
//                        .build());
//            }
//        }
//

        return ResultOrErrors.of(ProgramInfo.from(program));
    }
}
