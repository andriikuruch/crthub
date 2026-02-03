package com.anku.certhub.services;

import com.anku.certhub.services.model.CertificateMetadata;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class CertificateCache {
    private final Map<UUID, CertificateMetadata> cache = new HashMap<>();

    public CertificateMetadata getById(UUID uuid) {
        return cache.getOrDefault(uuid, CertificateMetadata.builder().build());
    }

    public void put(CertificateMetadata metadata) {
        cache.putIfAbsent(metadata.getId(), metadata);
    }

    public Collection<CertificateMetadata> getAll() {
        return cache.values();
    }
}
