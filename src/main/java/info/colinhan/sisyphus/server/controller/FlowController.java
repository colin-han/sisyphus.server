package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.FlowEntityDto;
import info.colinhan.sisyphus.server.dto.GetFlowSvgRequest;
import info.colinhan.sisyphus.server.dto.GetFlowSvgResponse;
import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.repository.FlowRepository;
import info.colinhan.sisyphus.server.utils.Response;
import info.colinhan.sisyphus.tartarus.TartarusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flows")
public class FlowController {
    @Autowired
    private FlowRepository flowRepository;

    @GetMapping("/")
    public Response<List<FlowEntityDto>> getFlows() {
        return Response.of(
                flowRepository.findAll().stream()
                        .map(FlowEntityDto::new)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/")
    public Response<FlowEntityDto> createFlow(
            @RequestBody FlowEntityDto flowEntityDto,
            Principal userPrincipal
    ) {
        return Response.of(new FlowEntityDto(flowRepository.save(flowEntityDto.toEntity(userPrincipal.getName()))));
    }

    @GetMapping("/{flowId}")
    public Response<FlowEntityDto> getFlow(@PathVariable Long flowId) {
        FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
        return Response.of(new FlowEntityDto(flowEntity));
    }

    @PutMapping("/{flowId}")
    public Response<FlowEntityDto> updateFlow(@PathVariable Long flowId, @RequestBody FlowEntityDto flowEntityDto) {
        FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
        flowEntity.setName(flowEntityDto.getName());
        flowEntity.setDescription(flowEntityDto.getDescription());
        flowEntity.setCode(flowEntityDto.getCode());
        flowEntity.setUpdatedAt(new Timestamp(new java.util.Date().getTime()));
        return Response.of(new FlowEntityDto(flowRepository.save(flowEntity)));
    }

    @PostMapping("/{flowId}/svg")
    public Response<GetFlowSvgResponse> getFlowSvg(
            @PathVariable Long flowId,
            @RequestBody GetFlowSvgRequest request) {
        String code = request.getCode();
        if (code == null) {
            FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
            code = flowEntity.getCode();
            if (code == null) {
                code = "";
            }
        }
        String svg = TartarusService.generateSVG(code);
        return Response.of(new GetFlowSvgResponse(svg, null));
    }
}
