package com.kaung_dev.RestaurantPOS.domain;

import com.kaung_dev.RestaurantPOS.domain.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@jakarta.persistence.Table(name = "table_info")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

}
