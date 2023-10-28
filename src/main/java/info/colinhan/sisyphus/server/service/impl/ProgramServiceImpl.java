package info.colinhan.sisyphus.server.service.impl;

import info.colinhan.sisyphus.exception.ParseError;
import info.colinhan.sisyphus.exception.ParseException;
import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.jacal.model.FormItem;
import info.colinhan.sisyphus.jacal.parser.JacalParser;
import info.colinhan.sisyphus.server.dto.FlowError;
import info.colinhan.sisyphus.server.dto.FlowInfo;
import info.colinhan.sisyphus.server.dto.GetProgramInfoResponse;
import info.colinhan.sisyphus.server.dto.ProgramInfo;
import info.colinhan.sisyphus.server.model.*;
import info.colinhan.sisyphus.server.repository.*;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.server.service.ProgramService;
import info.colinhan.sisyphus.server.utils.ProgramStatus;
import info.colinhan.sisyphus.server.utils.U;
import info.colinhan.sisyphus.tartarus.model.Action;
import info.colinhan.sisyphus.tartarus.model.Flow;
import info.colinhan.sisyphus.util.ResultOrErrors;
import info.colinhan.sisyphus.util.ResultWithErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Override
    public GetProgramInfoResponse buildProgramInfo(List<ProgramEntity> programs) {
        Map<FlowEntity, List<ProgramEntity>> flowMap = programs.stream()
                .collect(Collectors.groupingBy(this::findFlow));

        return new GetProgramInfoResponse(flowMap.keySet().stream()
                .map(flow -> GetProgramInfoResponse.FlowInfo.builder()
                                .id(flow.getId())
                                .name(flow.getName())
                                .programs(flowMap.get(flow).stream()
                                                .map(program -> ProgramInfo.builder()
                                                                .id(program.getId())
                                                                .name(program.getName())
                                                                .variables(Collections.emptyList())
//                                                                .variables(program.getVariables().stream()
//                                                                        .map(variable -> variable.getName())
//                                                                        .collect(Collectors.toList())
//                                                                )
                                                                .build()
                                                )
                                                .collect(Collectors.toList())
                                )
                                .variables(Collections.emptyList())
//                                .variables(flow.getVariables().stream()
//                                        .map(variable -> VariableInfo.builder()
//                                                  .name(variable.getName())
//                                                  .type(variable.getType())
//                                                  .build()
//                                        )
//                                        .collect(Collectors.toList())
//                                )
                                .build()
                )
                .collect(Collectors.toList()));
    }

    private FlowEntity findFlow(ProgramEntity programEntity) {
        return flowRepository.findById(flowVersionRepository.findById(programEntity.getFlowVersionId()).orElseThrow().getFlowId()).orElseThrow();
    }

    private ResultWithErrors<FlowInfo, FlowError> buildFlow(FlowEntity flowEntity) {
        Long flowId = flowEntity.getId();
        FlowInfo info = new FlowInfo(
                flowId,
                flowEntity.getName(),
                flowEntity.getDescription()
        );

        Optional<FlowVersionEntity> flowVersion = flowVersionRepository.findFirstByFlowIdOrderByVersionDesc(flowId);
        if (flowVersion.isEmpty()) {
            return ResultWithErrors.withError(info, new FlowError(
                    "flow",
                    flowEntity.getName(),
                    Collections.singletonList(
                            new ParseError(0, 0, 0, "Flow version not found", null)
                    )
            ));
        }

        Flow flow;
        try {
            flow = getFlowModel(flowVersion.get());
        } catch (ParseException e) {
            return ResultWithErrors.withError(info, new FlowError(
                    "flow",
                    flowEntity.getName(),
                    e.getErrors()
            ));
        }

        List<String> forms = getFormNames(flow);
        for (String formName : forms) {
            FormEntity formEntity = formRepository.findFirstByName(formName).orElseThrow();
            Optional<FormVersionEntity> formVersion = formVersionRepository.findFirstByFormIdOrderByVersionDesc(formEntity.getId());
            if (formVersion.isEmpty()) {
                return ResultWithErrors.withError(info, new FlowError(
                        "form",
                        formName,
                        Collections.singletonList(
                                new ParseError(0, 0, 0, "Form is not found", null)
                        )
                ));
            } else {
                FormVersionEntity formVersionEntity = formVersion.get();
                if (formVersionEntity.getModel() == null) {
                    try {
                        formVersionEntity.setModel(JacalParser.parse(formVersionEntity.getCode()));
                    } catch (ParseException e) {
                        return ResultWithErrors.withError(info, new FlowError(
                                "form",
                                formName,
                                e.getErrors()
                        ));
                    }
                }
            }
        }

        return ResultWithErrors.of(info);
    }

    @Override
    public List<ResultWithErrors<FlowInfo, FlowError>> getAvailableFlows() {
        return flowRepository.findAll().stream()
                .map(this::buildFlow)
                .collect(Collectors.toList());
    }

    @Override
    public ResultOrErrors<ProgramInfo, FlowError> createProgram(FlowVersionEntity flowVersion, String name, String creator) {
        FlowEntity flow = flowRepository.findById(flowVersion.getFlowId()).orElseThrow();
        ResultWithErrors<FlowInfo, FlowError> availableFlowInfo = buildFlow(flow);
        if (!availableFlowInfo.errors().isEmpty()) {
            return ResultOrErrors.error(availableFlowInfo.errors());
        }

        Set<FormVersionEntity> forms = getForms(flowVersion.getModel());
        ProgramEntity program = programRepository.save(ProgramEntity.builder()
                .name(name)
                .flowVersionId(flowVersion.getId())
                .createdBy(creator)
                .createdAt(U.timeNow())
                .updatedAt(U.timeNow())
                .status(ProgramStatus.IN_PROGRESS)
                .build());



        List<ProgramVariableEntity> variables = new ArrayList<>();
        for (FormVersionEntity form : forms) {
            Form formModel = form.getModel();
            for (FormItem item : formModel.getItems()) {
                variables.add(ProgramVariableEntity.builder()
                        .programId(program.getId())
                        .name(item.getName())
                        .type(item.getType().getValueType().getName())
                        .build());
            }
        }

        programVariableRepository.saveAll(variables);

        return ResultOrErrors.of(ProgramInfo.from(program));
    }

    private Flow getFlowModel(FlowVersionEntity flowVersion) throws ParseException {
        if (flowVersion.getModel() == null) {
            modelCompileService.compileFlow(flowVersion);
        }
        return flowVersion.getModel();
    }

    private List<String> getFormNames(Flow flow) {
        FormCollector collector = new FormCollector();
        collector.visit(flow);

        return collector.getForms().stream()
                .map(Action::getName)
                .collect(Collectors.toList());
    }

    private Set<FormVersionEntity> getForms(Flow flow) {
        FormCollector collector = new FormCollector();
        collector.visit(flow);

        return collector.getForms().stream()
                .map(Action::getName)
                .map(formRepository::findFirstByName)
                .map(Optional::get)
                .map(FormEntity::getId)
                .map(formVersionRepository::findFirstByFormIdOrderByVersionDesc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
