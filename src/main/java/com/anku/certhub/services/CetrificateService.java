package com.anku.certhub.services;

import com.anku.certhub.services.model.CertificateMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.UUID;

public interface CetrificateService {

    X509Certificate parse(MultipartFile certificate);

    void validate(X509Certificate certificate);

    CertificateMetadata getMetadata(X509Certificate x509Certificate);

    void save(CertificateMetadata metadata, MultipartFile certificate);

    CertificateMetadata getMetadataById(UUID uuid);

    Collection<CertificateMetadata> getAllCertificateMatadatas();

    void clearUploadsDir();
}
