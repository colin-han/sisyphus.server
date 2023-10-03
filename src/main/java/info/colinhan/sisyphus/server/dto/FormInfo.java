package info.colinhan.sisyphus.server.dto;

import info.colinhan.sisyphus.server.model.FlowEntity;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.model.FormEntity;
import info.colinhan.sisyphus.server.model.FormVersionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * DTO for {@link FlowEntity}
 */
@Data
@NoArgsConstructor
public class FormInfo {
    private Long id;
    private String name;
    private String description;
    private String code;
    private int version = 0;
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public FormInfo(FormEntity entity, FormVersionEntity version) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdBy = entity.getCreatedByUsername();
        this.createdAt = entity.getCreatedAt();
        if (version != null) {
            this.code = version.getCode();
            this.version = version.getVersion();
            this.updatedAt = version.getCreatedAt();
        } else {
            this.updatedAt = this.createdAt;
        }
    }

    public FormVersionEntity toVersion(String username) {
        FormVersionEntity.FormVersionEntityBuilder version = FormVersionEntity.builder()
                .version(this.version + 1)
                .createdByUsername(username)
                .createdAt(new Timestamp(new Date().getTime()))
                .code(this.code);
        if (this.id != null) {
            version.formId(this.id);
        }
        return version.build();
    }
}
