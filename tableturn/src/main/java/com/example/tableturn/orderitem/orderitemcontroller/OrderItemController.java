package com.example.tableturn.orderitem.orderitemcontroller;

import com.example.tableturn.orderitem.orderitemdto.OrderItemDTO;
import com.example.tableturn.orderitem.orderitemservice.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public OrderItemDTO createOrderItem(@RequestBody OrderItemDTO dto) {
        return orderItemService.createOrderItem(dto);
    }

    @GetMapping
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/order/{orderId}")
    public List<OrderItemDTO> getItemsByOrderId(
            @PathVariable Long orderId) {
        return orderItemService.getItemsByOrderId(orderId);
    }

    @GetMapping("/{id}")
    public OrderItemDTO getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    @PutMapping("/{id}")
    public OrderItemDTO updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItemDTO dto) {
        return orderItemService.updateOrderItem(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return "Order item deleted successfully";
    }
}