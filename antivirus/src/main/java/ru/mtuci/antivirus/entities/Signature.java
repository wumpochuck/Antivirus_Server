package ru.mtuci.antivirus.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.mtuci.antivirus.entities.ENUMS.signature.STATUS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "signatures")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"auditRecords", "historyVersions"})
public class Signature {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Не понял что за GUID, пусть будет UUID

    @Column(name = "threat_name", columnDefinition = "TEXT")
    private String threatName;

    // мб блоб
    @Column(name = "first_bytes", columnDefinition = "BLOB")
    @Size(min = 8, max = 8)
    private byte[] firstBytes;

    private String remainderHash;

    private int remainderLength;

    private String fileType;

    private int offsetStart;

    private int offsetEnd;

    @Column(columnDefinition = "TEXT")
    private String digitalSignature;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    @OneToMany(mappedBy = "signature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SignatureHistory> historyVersions;

    @OneToMany(mappedBy = "signature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SignatureAudit> auditRecords;

    @Version
    @Column(name = "version")
    private Long version;
}
