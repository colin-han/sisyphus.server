package info.colinhan.sisyphus.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFlowSvgResponse {
    private String svg;
    private String error;
}
