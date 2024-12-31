package com.kaung_dev.RestaurantPOS.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaung_dev.RestaurantPOS.domain.enums.OrderDetailStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_info_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private int quantity;

    private double price;

    @Enumerated(EnumType.STRING)
    private OrderDetailStatus status;

    public void calculatePrice() {
        if (menuItem != null) this.price = quantity * menuItem.getPrice();
        else this.price = 0;
    }

}
