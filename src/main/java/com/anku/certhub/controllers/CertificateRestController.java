package com.anku.certhub.controllers;

import com.anku.certhub.services.CetrificateService;
import com.anku.certhub.services.model.CertificateMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
public class CertificateRestController {

    private final CetrificateService cetrificateService;

    public CertificateRestController(CetrificateService cetrificateService) {
        this.cetrificateService = cetrificateService;
    }

    @GetMapping
    public ResponseEntity<Collection<CertificateMetadata>> getCertificates() {
        return ResponseEntity.ok(cetrificateService.getAllCertificateMatadatas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateMetadata> getCertificateById(@PathVariable("id") UUID uuid) {
        return ResponseEntity.ok(cetrificateService.getMetadataById(uuid));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCertificate(@RequestPart MultipartFile certificateFile) {
        try {
            X509Certificate x509Certificate = cetrificateService.parse(certificateFile);
            cetrificateService.validate(x509Certificate);
            CertificateMetadata metadata = cetrificateService.getMetadata(x509Certificate);
            cetrificateService.save(metadata, certificateFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("Certificate is successfully uploaded\n");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
