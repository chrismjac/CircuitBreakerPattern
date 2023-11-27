package com.ecommersesystem.orderservice.controller;

import com.ecommersesystem.orderservice.entity.Orders;
import com.ecommersesystem.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping
    @Operation(summary = "Add a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order added successfully"),
            @ApiResponse(responseCode = "500", description = "Unable to add order")})
    private ResponseEntity<String> addNewOrder(@RequestBody Orders order){
        return orderService.addNewOrder(order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specified order details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No order with specified Id found")})
    private ResponseEntity<?> getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }


}
