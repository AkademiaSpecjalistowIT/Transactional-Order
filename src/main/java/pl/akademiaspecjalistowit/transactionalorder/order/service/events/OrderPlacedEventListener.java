package pl.akademiaspecjalistowit.transactionalorder.order.service.events;

import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;

public interface OrderPlacedEventListener {
    void notifyOrderPlaced(OrderEntity orderEntityAfterValidations);
}
