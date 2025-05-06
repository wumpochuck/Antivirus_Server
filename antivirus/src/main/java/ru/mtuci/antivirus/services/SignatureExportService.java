package ru.mtuci.antivirus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mtuci.antivirus.entities.ENUMS.signature.STATUS;
import ru.mtuci.antivirus.entities.Signature;
import ru.mtuci.antivirus.entities.SignatureExportFile;
import ru.mtuci.antivirus.repositories.SignatureRepository;
import ru.mtuci.antivirus.utils.SignatureExportUtil;


import java.util.List;

@Service
public class SignatureExportService {
    private final SignatureRepository signatureRepository;
    private final SignatureExportUtil signatureExportUtil;

    @Autowired
    public SignatureExportService(SignatureRepository signatureRepository, SignatureExportUtil signatureExportUtil) {
        this.signatureRepository = signatureRepository;
        this.signatureExportUtil = signatureExportUtil;
    }

    public MultipartFile exportSignatures() throws Exception {
        List<Signature> actualSignatures = signatureRepository.findByStatus(STATUS.ACTUAL)
                .orElseThrow(() -> new RuntimeException("Актуальные сигнатуры не найдены"));

        // Формируем манифест
        byte[] manifestBytes = signatureExportUtil.createManifest(actualSignatures);

        // Бинари сигнатур
        byte[] signaturesBytes = signatureExportUtil.createSignaturesBinary(actualSignatures);

        // Экспорт (кладем манифест, бинари, уникальное имя)
        return new SignatureExportFile(
                manifestBytes,
                signaturesBytes,
                "signatures_export_" + System.currentTimeMillis() + ".dat" // Или UUID
        );
    }

}