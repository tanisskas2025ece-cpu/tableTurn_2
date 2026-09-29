package com.example.tableturn.order.orderservice;

import com.example.tableturn.order.orderentity.FoodOrder;
import com.example.tableturn.order.orderrepository.FoodOrderRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodOrderService {

    private final FoodOrderRepository repository;

    public FoodOrderService(FoodOrderRepository repository) {
        this.repository = repository;
    }

    public FoodOrder createOrder(FoodOrder order) {
        return repository.save(order);
    }

    public List<FoodOrder> getAllOrders() {
        return repository.findAll();
    }

    public FoodOrder getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));
    }

    public FoodOrder updateOrder(Long id, FoodOrder updatedOrder) {

        FoodOrder order = getOrderById(id);

        order.setReservationId(updatedOrder.getReservationId());
        order.setCustomerName(updatedOrder.getCustomerName());
        order.setStatus(updatedOrder.getStatus());
        order.setTotalAmount(updatedOrder.getTotalAmount());

        return repository.save(order);
    }

    public void deleteOrder(Long id) {
        FoodOrder order = getOrderById(id);
        repository.delete(order);
    }
}