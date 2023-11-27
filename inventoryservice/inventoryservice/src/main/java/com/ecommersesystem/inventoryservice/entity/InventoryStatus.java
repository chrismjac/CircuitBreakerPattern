package com.ecommersesystem.inventoryservice.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class InventoryStatus {
    private String status;
    private String additionalInfo;
}
