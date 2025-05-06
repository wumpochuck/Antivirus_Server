package ru.mtuci.antivirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.antivirus.entities.SignatureAudit;

import java.util.List;
import java.util.UUID;

@Repository
public interface SignatureAuditRepository extends JpaRepository<SignatureAudit, UUID> {
    List<SignatureAudit> findBySignatureIdOrderByChangedAtDesc(UUID signatureId);
}
