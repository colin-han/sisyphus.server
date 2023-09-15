package info.colinhan.sisyphus.server.model;

import info.colinhan.sisyphus.server.utils.ProgramStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "programs")
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    @Column(name = "flow_version_id", nullable = false)
    private long flowVersionId;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "flow_version_id", insertable = false, updatable = false)
    private FlowVersionEntity flowVersion;

    @Column(name = "status")
    private ProgramStatus status;

    @Column(name = "current_action")
    private UUID currentAction;

    @Column(name = "current_owner")
    private String currentOwner;

    @Column(name = "created_by", nullable = false)
    private String createdByUsername;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private UserEntity createdBy;

    @Column(name = "created_at")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<ProgramVariableEntity> variables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFlowVersionId() {
        return flowVersionId;
    }

    public void setFlowVersionId(long flowVersionId) {
        this.flowVersionId = flowVersionId;
    }

    public FlowVersionEntity getFlowVersion() {
        return flowVersion;
    }

    public UUID getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(UUID currentAction) {
        this.currentAction = currentAction;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProgramVariableEntity> getVariables() {
        return variables;
    }

    public FlowEntity getFlow() {
        return this.getFlowVersion().getFlow();
    }
}
