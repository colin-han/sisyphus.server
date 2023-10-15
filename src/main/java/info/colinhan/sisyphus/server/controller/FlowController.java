package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.*;
import info.colinhan.sisyphus.server.exception.BadRequestException;
import info.colinhan.sisyphus.server.exception.E;
import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.repository.FlowRepository;
import info.colinhan.sisyphus.server.repository.FlowVersionRepository;
import info.colinhan.sisyphus.server.service.ModelCompileService;
import info.colinhan.sisyphus.server.utils.Response;
import info.colinhan.sisyphus.tartarus.exceptions.TartarusParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flows")
public class FlowController {
    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private FlowVersionRepository flowVersionRepository;
    @Autowired
    private ModelCompileService modelCompileService;

    @GetMapping("/")
    public Response<List<FlowEntityDto>> getFlows() {
        return Response.of(
                flowRepository.findAll().stream()
                        .map(f -> new FlowEntityDto(f,
                                flowVersionRepository.findFirstByFlowIdOrderByVersionDesc(f.getId()).orElse(null)))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/")
    public Response<FlowEntityDto> createFlow(
            @RequestBody CreateFlowRequest request,
            Principal userPrincipal
    ) {
        FlowEntity flow = flowRepository.save(
                FlowEntity.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .createdByUsername(userPrincipal.getName())
                        .createdAt(new Timestamp(new Date().getTime()))
                        .build()
        );
        return Response.of(new FlowEntityDto(flow, null));
    }

    @GetMapping("/{flowId}")
    public Response<FlowEntityDto> getFlow(@PathVariable Long flowId) {
        FlowEntity flowEntity = E.assertPresent(flowRepository.findById(flowId),"Flow");
        FlowVersionEntity version = flowVersionRepository.findFirstByFlowIdOrderByVersionDesc(flowId).orElse(null);
        return Response.of(new FlowEntityDto(flowEntity, version));
    }

    @PutMapping("/{flowId}")
    public Response<UpdateFlowResponse> updateFlow(
            @PathVariable Long flowId,
            @RequestBody FlowEntityDto flowEntityDto,
            Principal userPrincipal
    ) {
        FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
        if (!flowEntity.getName().equals(flowEntityDto.getName())) {
            throw new BadRequestException("Flow name is readonly!");
        }

        FlowVersionEntity version = flowEntityDto.toVersion(userPrincipal.getName());
        version.setFlowId(flowId);

        // validate the flow
        try {
            modelCompileService.compileFlow(version);
        } catch (TartarusParserException e) {
            return Response.of(new UpdateFlowResponse(e.getErrors()));
        }
        version = flowVersionRepository.save(version);

        flowEntity.setDescription(flowEntityDto.getDescription());
        flowEntity = flowRepository.save(flowEntity);

        return Response.of(new UpdateFlowResponse(new FlowEntityDto(flowEntity, version)));
    }

    @PostMapping("/{flowId}/svg")
    public Response<GetFlowSvgResponse> getFlowSvg(
            @PathVariable Long flowId,
            @RequestBody ParseCodeRequest request) {
        String code = request.getCode();
        if (code == null) {
            FlowVersionEntity version = E.assertPresent(flowVersionRepository.findFirstByFlowIdOrderByVersionDesc(flowId), "Version");
            code = version.getCode();
            if (code == null) {
                code = "";
            }
        }
        try {
            String svg = modelCompileService.generateFlowSVG(code, flowId);
            return Response.of(new GetFlowSvgResponse(svg));
        } catch (TartarusParserException e) {
            return Response.of(new GetFlowSvgResponse(e.getErrors()));
        }
    }
}
