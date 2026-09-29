package com.example.tableturn.orderitem.orderitemservice;

import com.example.tableturn.order.orderentity.FoodOrder;
import com.example.tableturn.order.orderrepository.FoodOrderRepository;
import com.example.tableturn.orderitem.orderitemdto.OrderItemDTO;
import com.example.tableturn.orderitem.orderitementity.OrderItem;
import com.example.tableturn.orderitem.orderitemrepository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final FoodOrderRepository foodOrderRepository;

    public OrderItemService(
            OrderItemRepository orderItemRepository,
            FoodOrderRepository foodOrderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.foodOrderRepository = foodOrderRepository;
    }

    public OrderItemDTO createOrderItem(OrderItemDTO dto) {

        validateOrderItem(dto);

        FoodOrder order = foodOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setItemName(dto.getItemName());
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());

        BigDecimal subtotal = dto.getUnitPrice()
                .multiply(BigDecimal.valueOf(dto.getQuantity()));

        item.setSubtotal(subtotal);

        return convertToDTO(orderItemRepository.save(item));
    }

    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<OrderItemDTO> getItemsByOrderId(Long orderId) {

        if (!foodOrderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }

        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public OrderItemDTO getOrderItemById(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        return convertToDTO(item);
    }

    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO dto) {

        validateOrderItem(dto);

        FoodOrder order = foodOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        item.setOrderId(order.getId());
        item.setItemName(dto.getItemName());
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());

        BigDecimal subtotal = dto.getUnitPrice()
                .multiply(BigDecimal.valueOf(dto.getQuantity()));

        item.setSubtotal(subtotal);

        return convertToDTO(orderItemRepository.save(item));
    }

    public void deleteOrderItem(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order item not found"));

        orderItemRepository.delete(item);
    }

    private void validateOrderItem(OrderItemDTO dto) {

        if (dto.getOrderId() == null) {
            throw new RuntimeException("Order ID is required");
        }

        if (dto.getItemName() == null || dto.getItemName().isBlank()) {
            throw new RuntimeException("Item name is required");
        }

        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        if (dto.getUnitPrice() == null
                || dto.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Unit price cannot be negative");
        }
    }

    private OrderItemDTO convertToDTO(OrderItem item) {

        OrderItemDTO dto = new OrderItemDTO();

        dto.setId(item.getId());
        dto.setOrderId(item.getOrderId());
        dto.setItemName(item.getItemName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());

        return dto;
    }
}
