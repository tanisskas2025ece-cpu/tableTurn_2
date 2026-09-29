package com.example.tableturn.order.orderrepository;

import com.example.tableturn.order.orderentity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodOrderRepository
        extends JpaRepository<FoodOrder, Long> {

}
