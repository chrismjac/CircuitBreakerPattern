package com.ecommersesystem.inventoryservice.service;

import com.ecommersesystem.inventoryservice.entity.Inventory;
import com.ecommersesystem.inventoryservice.entity.InventoryStatus;
import com.ecommersesystem.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;
    public ResponseEntity<String> addProductToInventory(Inventory inventory) {
        try {
            inventoryRepository.save(inventory);
            return new ResponseEntity<>("Product Details added successfully", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Exception raised while adding new product" + exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> retrieveProductFromInventory(Long itemId) {
        Optional<Inventory> productDetails = inventoryRepository.findById(itemId);
        if (productDetails.isPresent()) {
            return ResponseEntity.ok(productDetails.get());
        }
        return new ResponseEntity<>("No product with specified Id found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteProductFromInventory(Long itemId) {
        inventoryRepository.deleteById(itemId);
        return new ResponseEntity<>("Product Details deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> retrieveProductStatusFromInventory(Long itemId,int quantity) throws InterruptedException {

        TimeUnit.MINUTES.sleep(10); // to test circuit breaker

        InventoryStatus inventoryStatus = new InventoryStatus();
        Optional<Inventory> productDetails = inventoryRepository.findById(itemId);
        if (productDetails.isPresent()) {
            Inventory product = productDetails.get();
            if(product.getQuantity()>=quantity){
                inventoryStatus.setStatus("Available");
                inventoryStatus.setAdditionalInfo("Balance after selling will be :"+(product.getQuantity()-quantity));
            }else{
                inventoryStatus.setStatus("Unavailable");
                inventoryStatus.setAdditionalInfo("Insufficient quantity available in inventory:"+product.getQuantity());
            }
        }else{
            inventoryStatus.setStatus("Unavailable");
            inventoryStatus.setAdditionalInfo("Product Not found in Inventory");
        }
        return new ResponseEntity<>(inventoryStatus,HttpStatus.OK);

    }

}
