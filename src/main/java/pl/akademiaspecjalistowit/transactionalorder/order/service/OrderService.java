package pl.akademiaspecjalistowit.transactionalorder.order.service;

import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;

public interface OrderService {

    void placeAnOrder(OrderDto orderDto);
}
