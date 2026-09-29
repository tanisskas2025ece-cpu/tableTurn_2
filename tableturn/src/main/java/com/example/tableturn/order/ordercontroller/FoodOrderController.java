package com.example.tableturn.order.ordercontroller;

import com.example.tableturn.order.orderentity.FoodOrder;
import com.example.tableturn.order.orderservice.FoodOrderService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class FoodOrderController {

    private final FoodOrderService service;

    public FoodOrderController(FoodOrderService service) {
        this.service = service;
    }

    @PostMapping
    public FoodOrder createOrder(@RequestBody FoodOrder order) {
        return service.createOrder(order);
    }

    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public FoodOrder getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @PutMapping("/{id}")
    public FoodOrder updateOrder(
            @PathVariable Long id,
            @RequestBody FoodOrder order) {
        return service.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return "Order deleted successfully";
    }
}
