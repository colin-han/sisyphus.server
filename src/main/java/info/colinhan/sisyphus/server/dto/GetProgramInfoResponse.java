package info.colinhan.sisyphus.server.dto;

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
    private List<FlowInfoWithPrograms> flows;
}
