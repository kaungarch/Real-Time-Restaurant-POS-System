package com.kaung_dev.RestaurantPOS.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PusherResponse {

    private String title;

    private String description;

    private Object data;

}
