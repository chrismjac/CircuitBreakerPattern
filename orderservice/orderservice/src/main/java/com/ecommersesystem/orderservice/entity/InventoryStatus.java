package com.ecommersesystem.orderservice.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class InventoryStatus {
    private String availabilityStatus;
    private String additionalInfo;
}
