package com.kaung_dev.RestaurantPOS.domain;

import com.kaung_dev.RestaurantPOS.domain.enums.OrderStatus;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "order_info")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    public double getTotalCost() {
        if(this.orderDetailList.size() == 0) return  0;
        return this.orderDetailList.stream().mapToDouble(OrderDetail::getPrice).sum();
    }

}
