package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.model.VariableType;
import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.model.ProgramEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProgramInfoResponse {
    private List<FlowInfo> flows;

    public static GetProgramInfoResponse from(List<ProgramEntity> programs) {
        Map<FlowEntity, List<ProgramEntity>> flowMap = programs.stream()
                .collect(Collectors.groupingBy(ProgramEntity::getFlow));

        return new GetProgramInfoResponse(flowMap.keySet().stream()
                .map(flow -> FlowInfo.builder()
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlowInfo {
        private long id;
        private String name;
        private List<ProgramInfo> programs;
        private List<VariableInfo> variables;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgramInfo {
        private long id;
        private String name;
        private List<String> variables;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariableInfo {
        private String name;
        private VariableType type;
    }
}
