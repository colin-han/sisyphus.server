package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.server.model.ProgramEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramInfo {
    private long id;
    private String name;
    private List<String> variables;
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static ProgramInfo from(ProgramEntity program) {
        return new ProgramInfo(program.getId(),
                program.getName(),
                Collections.emptyList(),
                program.getCreatedBy(),
                program.getCreatedAt(),
                program.getUpdatedAt());
    }
}
