package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * DTO for {@link info.colinhan.sisyphus.server.model.FlowEntity}
 */
@Data
@NoArgsConstructor
public class FlowEntityDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private int version = 0;
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public FlowEntityDto(FlowEntity entity, FlowVersionEntity version) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdBy = entity.getCreatedBy();
        this.createdAt = entity.getCreatedAt();
        if (version != null) {
            this.code = version.getCode();
            this.version = version.getVersion();
            this.updatedAt = version.getCreatedAt();
        } else {
            this.updatedAt = this.createdAt;
        }
    }

    public FlowVersionEntity toVersion(String username) {
        FlowVersionEntity.FlowVersionEntityBuilder version = FlowVersionEntity.builder()
                .version(this.version + 1)
                .createdBy(username)
                .createdAt(new Timestamp(new Date().getTime()))
                .code(this.code);
        if (this.id != null) {
            version.flowId(this.id);
        }
        return version.build();
    }
}
