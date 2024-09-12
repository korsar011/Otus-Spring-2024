package ru.otus.hw.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;

@Component
public class AppHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedHeapMemory = heapMemoryUsage.getUsed();
        long maxHeapMemory = heapMemoryUsage.getMax();
        long availableHeapMemory = maxHeapMemory - usedHeapMemory;

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double systemLoadAverage = osBean.getSystemLoadAverage();
        int availableProcessors = osBean.getAvailableProcessors();
        if (availableHeapMemory > 50 * 1024 * 1024 && systemLoadAverage / availableProcessors < 1.0) {
            return Health.up()
                    .withDetail("Available Heap Memory", availableHeapMemory / (1024 * 1024) + " MB")
                    .withDetail("System Load Average", systemLoadAverage)
                    .withDetail("Available Processors", availableProcessors)
                    .build();
        } else {
            return Health.down()
                    .withDetail("Available Heap Memory", availableHeapMemory / (1024 * 1024) + " MB")
                    .withDetail("System Load Average", systemLoadAverage)
                    .withDetail("Available Processors", availableProcessors)
                    .withDetail("Reason", "Insufficient resources")
                    .build();
        }
    }
}