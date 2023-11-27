package com.ecommersesystem.orderservice.service;

import com.ecommersesystem.orderservice.entity.InventoryStatus;
import com.ecommersesystem.orderservice.entity.Orders;
import com.ecommersesystem.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${inventoryService.baseUrl}")
    private String inventoryUrl;
    public ResponseEntity<String> addNewOrder(Orders order) {

            InventoryStatus checkInventoryForItem = checkInventory(order.getProductId(),order.getQty());
            if(checkInventoryForItem!=null && checkInventoryForItem.getAvailabilityStatus().equalsIgnoreCase("Available")) {
                orderRepository.save(order);
                return new ResponseEntity<>("Order Details added successfully", HttpStatus.OK);
            }else{
                assert checkInventoryForItem != null;
                return new ResponseEntity<>("Error occurred while adding new order" + checkInventoryForItem.getAdditionalInfo(), HttpStatus.NO_CONTENT);
            }

    }

    public ResponseEntity<?> getOrderById(Long id) {
        Optional<Orders> orderDetails = orderRepository.findById(id);
        if (orderDetails.isPresent()) {
            return ResponseEntity.ok(orderDetails.get());
        }
        return new ResponseEntity<>("No order with specified Id found", HttpStatus.NOT_FOUND);
    }
    @CircuitBreaker(name = "inventoryServiceCircuitBreaker",fallbackMethod = "defaultFallback")
    @RateLimiter(name = "inventoryServiceRateLimiter" ,fallbackMethod = "rateLimitFallback")
    @Retry(name = "inventoryServiceRetry" ,fallbackMethod = "retryFallback")
    public InventoryStatus checkInventory(Long itemId, int quantity) {
        return restTemplate.getForObject(inventoryUrl+itemId+"/"+quantity, InventoryStatus.class);
    }
    public InventoryStatus defaultFallback(Long itemId){
            return getUnavailableInventoryStatus("Product id:"+itemId+"not found as Inventory Service down");
    }
    public InventoryStatus rateLimitFallback(Long itemId){
        return getUnavailableInventoryStatus("Too many attempts within a  time .Failed to process order for product id: " +
                        itemId);
    }

    public InventoryStatus retryFallback(Long itemId){
        return getUnavailableInventoryStatus("All retry exhausted for product id: " +itemId);
    }
    private InventoryStatus getUnavailableInventoryStatus(String message){
        InventoryStatus inventoryStatus = new InventoryStatus();
        inventoryStatus.setAvailabilityStatus("Unavailable");
        inventoryStatus.setAdditionalInfo(message);
        return inventoryStatus;
    }
}
