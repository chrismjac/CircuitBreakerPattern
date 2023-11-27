package com.ecommersesystem.inventoryservice.controller;


import com.ecommersesystem.inventoryservice.entity.Inventory;
import com.ecommersesystem.inventoryservice.entity.InventoryStatus;
import com.ecommersesystem.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    @PostMapping
    @Operation(summary = "Add a new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Unable to add product")})
    private ResponseEntity<String> addProductToInventory(@RequestBody  Inventory inventory){
        return inventoryService.addProductToInventory(inventory);
    }
    @GetMapping("/{itemId}")
    @Operation(summary = "Get a product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified product details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No product with specified Id found")})
    private ResponseEntity<?> retrieveProductFromInventory(@PathVariable  Long itemId){
        return inventoryService.retrieveProductFromInventory(itemId);
    }
    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified product details updated successfully")})
    private ResponseEntity<String> deleteProductFromInventory(@PathVariable  Long itemId){
        return inventoryService.deleteProductFromInventory(itemId);
    }
    @GetMapping("/{itemId}/{quantity}")
    @Operation(summary = "Check inventory status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")})
    private ResponseEntity<?> retrieveProductStatusFromInventory(@PathVariable  Long itemId,@PathVariable int quantity) throws InterruptedException {
        return inventoryService.retrieveProductStatusFromInventory(itemId,quantity);
    }
}
