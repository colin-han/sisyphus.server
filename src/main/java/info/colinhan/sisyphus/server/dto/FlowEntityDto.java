package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.server.model.FlowEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public FlowEntityDto(FlowEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.code = entity.getCode();
        this.createdBy = entity.getCreatedByUsername();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public FlowEntity toEntity(String username) {
        FlowEntity entity = new FlowEntity();
        if (this.id != null) {
            entity.setId(this.id);
            entity.setCode(this.code);
            entity.setCreatedAt(this.createdAt);
        } else {
            entity.setCreatedAt(new Timestamp(new java.util.Date().getTime()));
        }
        entity.setName(this.name);
        entity.setDescription(this.description);
        // set updatedAt to now
        entity.setUpdatedAt(new Timestamp(new java.util.Date().getTime()));
        entity.setCreatedByUsername(username);
        return entity;
    }
}
