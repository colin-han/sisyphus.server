package info.colinhan.sisyphus.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

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

    @Column(name = "flow_version", nullable = false)
    private long flowVersionId;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "flow_version", insertable = false, updatable = false)
    private FlowVersionEntity flowVersion;

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
}
