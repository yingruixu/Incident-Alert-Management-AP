package com.shaun.incident_api.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IncidentMetrics {

    private final AtomicInteger openIncidentCount = new AtomicInteger(0);

    public IncidentMetrics(MeterRegistry registry) {

        Gauge.builder("incident_open_count", openIncidentCount, AtomicInteger::get)
                .description("Number of open incidents")
                .register(registry);
    }


    public void updateOpenIncidentCount(int count) {
        openIncidentCount.set(count);
    }


    public int getOpenIncidentCount() {
        return openIncidentCount.get();
    }
}
