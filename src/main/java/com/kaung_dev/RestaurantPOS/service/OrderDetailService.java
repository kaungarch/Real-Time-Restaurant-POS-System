package com.kaung_dev.RestaurantPOS.service;

import com.kaung_dev.RestaurantPOS.dto.OrderDto;

import java.util.List;

public interface OrderDetailService {

    OrderDto markOrderDetailAsPrepared(Long orderId, Long orderDetailId);

    OrderDto markOrderDetailAsServed(Long orderId, Long orderDetailId);

    OrderDto deleteExistingItem(Long orderId, Long orderDetailId);

}
