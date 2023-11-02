package info.colinhan.sisyphus.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProgramRequest {
    private long flowId;
    private int flowVersion;
    private Map<String, Integer> formVersions;
    private String name;
}
