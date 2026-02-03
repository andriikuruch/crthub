package com.anku.certhub.context.lifecycle;

import com.anku.certhub.services.CetrificateService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private final CetrificateService cetrificateService;

    public ShutdownListener(CetrificateService cetrificateService) {
        this.cetrificateService = cetrificateService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        cetrificateService.clearUploadsDir();
    }
}
