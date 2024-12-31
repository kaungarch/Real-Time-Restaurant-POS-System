package com.kaung_dev.RestaurantPOS.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private Long id;

    private String token;

}
