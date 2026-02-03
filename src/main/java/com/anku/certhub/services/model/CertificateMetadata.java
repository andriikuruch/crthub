package com.anku.certhub.services.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CertificateMetadata {
    private UUID id;
    private String fileName;
    private String commonName;
    private String issuer;
    private Date notBefore;
    private Date notAfter;
    private final Collection<List<?>> subjectAlternativeNames;
}

