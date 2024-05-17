package pl.akademiaspecjalistowit.transactionalorder.order.service;

import java.util.UUID;
import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;

public interface OrderService {

    void placeAnOrder(OrderDto orderDto);

    void cancelOrder(UUID orderId);
}
