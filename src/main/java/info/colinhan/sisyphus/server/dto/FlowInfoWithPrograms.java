package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.model.VariableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowInfoWithPrograms {
    private long id;
    private String name;
    private String description;
    private Timestamp updatedAt;
    @Builder.Default
    private int version = 0;
    @Builder.Default
    private Map<String, Integer> formVersions = new HashMap<>();

    @Builder.Default
    private List<ProgramInfo> programs = new ArrayList<>();
    @Builder.Default
    private List<VariableInfo> variables = new ArrayList<>();
    @Builder.Default
    private List<FlowError> errors = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariableInfo {
        private String name;
        private VariableType type;
    }
}
