package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.FlowEntityDto;
import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.repository.FlowRepository;
import info.colinhan.sisyphus.server.utils.Response;
import info.colinhan.sisyphus.tartarus.TartarusService;
import lombok.Data;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.sql.Time;
import java.util.Collections;
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
    public Response<FlowEntityDto> createFlow(@RequestBody FlowEntityDto flowEntityDto) {
        return Response.of(new FlowEntityDto(flowRepository.save(flowEntityDto.toEntity())));
    }

    @PutMapping("/{flowId}")
    public Response<FlowEntityDto> updateFlow(@PathVariable Long flowId, @RequestBody FlowEntityDto flowEntityDto) {
        FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
        flowEntity.setName(flowEntityDto.getName());
        flowEntity.setDescription(flowEntityDto.getDescription());
        flowEntity.setCode(flowEntityDto.getCode());
        flowEntity.setUpdatedAt(new Time(new java.util.Date().getTime()));
        return Response.of(new FlowEntityDto(flowRepository.save(flowEntity)));
    }

    @GetMapping("/{flowId}/svg")
    public ResponseEntity<String> getFlowSvg(@PathVariable Long flowId) {
        FlowEntity flowEntity = flowRepository.findById(flowId).orElseThrow(() -> new RuntimeException("Flow not found"));
        String svg = TartarusService.generateSVG(flowEntity.getCode());
        return ResponseEntity.ok()
                .header("Content-Type", "image/svg+xml")
                .body(svg);
    }
}
