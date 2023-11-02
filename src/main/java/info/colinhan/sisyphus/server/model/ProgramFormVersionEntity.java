package info.colinhan.sisyphus.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "program_form_versions")
public class ProgramFormVersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "program_id", nullable = false)
    private Long programId;

    @Column(name = "form_version_id", nullable = false)
    private Long formVersionId;
}
