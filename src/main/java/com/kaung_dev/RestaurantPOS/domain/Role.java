package com.kaung_dev.RestaurantPOS.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaung_dev.RestaurantPOS.domain.enums.StaffRole;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StaffRole name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Staff> staffs;

}
