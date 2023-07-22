package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.server.model.FlowEntity;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

/**
 * DTO for {@link info.colinhan.sisyphus.server.model.FlowEntity}
 */
@Data
public class FlowEntityDto {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String createdBy;
    private Time createdAt;
    private Time updatedAt;

    public FlowEntityDto(FlowEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.code = entity.getCode();
        this.createdBy = entity.getCreatedBy().getUsername();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public FlowEntity toEntity() {
        FlowEntity entity = new FlowEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setCode(this.code);
        entity.setCreatedAt(this.createdAt);
        // set updatedAt to now
        entity.setUpdatedAt(new Time(new java.util.Date().getTime()));
        return entity;
    }
}
