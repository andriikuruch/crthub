package com.anku.certhub.services;

import com.anku.certhub.services.model.CertificateMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
public class CetrificateServiceImpl implements CetrificateService {
    private static final Logger logger = LoggerFactory.getLogger(CetrificateServiceImpl.class);
    private static final Path uploads = Paths.get(System.getProperty("user.dir"),"uploads");
    private final CertificateCache cache;

    public CetrificateServiceImpl(CertificateCache cache) {
        this.cache = cache;
    }

    @Override
    public X509Certificate parse(MultipartFile certificate) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return  (X509Certificate) certificateFactory
                    .generateCertificate(certificate.getInputStream());
        } catch (CertificateException | IOException e) {
            logger.error("Incorrect certificate", e);
            throw new RuntimeException("Incorrect certificate", e);
        }
    }

    @Override
    public void validate(X509Certificate certificate) {
        try {
            certificate.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            logger.error("Invalid certificate", e);
            throw new RuntimeException("Invalid certificate", e);
        }
    }

    @Override
    public CertificateMetadata getMetadata(X509Certificate x509Certificate) {
        try {
            UUID id = UUID.randomUUID();
            String commonName = x509Certificate.getSubjectX500Principal().getName();
            String issuer = x509Certificate.getIssuerX500Principal().getName();
            Date notBefore = x509Certificate.getNotBefore();
            Date notAfter = x509Certificate.getNotAfter();
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            return CertificateMetadata.builder()
                    .id(id)
                    .commonName(commonName)
                    .issuer(issuer)
                    .notBefore(notBefore)
                    .notAfter(notAfter)
                    .subjectAlternativeNames(subjectAlternativeNames)
                    .build();
        } catch (CertificateParsingException e) {
            logger.error("Unable to get metadata", e);
            throw new RuntimeException("Unable to get metadata", e);
        }
    }

    @Override
    public void save(CertificateMetadata metadata, MultipartFile certificate) {
        try {
            Path dir2save = uploads.resolve(metadata.getId().toString());
            Files.createDirectories(dir2save);
            metadata.setFileName(certificate.getOriginalFilename());
            cache.put(metadata);
            System.out.println(dir2save.resolve(Objects.requireNonNull(certificate.getOriginalFilename())));
            certificate.transferTo(dir2save.resolve(Objects.requireNonNull(certificate.getOriginalFilename())));
        } catch (IOException e) {
            logger.error("Cannot save certificate", e);
            throw new RuntimeException("Cannot save certificate", e);
        }
    }

    @Override
    public CertificateMetadata getMetadataById(UUID uuid) {
        return cache.getById(uuid);
    }

    @Override
    public Collection<CertificateMetadata> getAllCertificateMatadatas() {
        return cache.getAll();
    }

    @Override
    public void clearUploadsDir() {
        try {
            Files.deleteIfExists(uploads);
        } catch (IOException e) {
            logger.error("Cannot clear uploads directory", e);
            throw new RuntimeException("Cannot clear uploads directory", e);
        }
    }
}
