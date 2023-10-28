package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.model.VariableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProgramInfoResponse {
    private List<FlowInfo> flows;

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
    public static class VariableInfo {
        private String name;
        private VariableType type;
    }
}
