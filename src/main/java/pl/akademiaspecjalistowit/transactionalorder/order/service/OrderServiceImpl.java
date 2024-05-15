package pl.akademiaspecjalistowit.transactionalorder.order.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderServiceException;
import pl.akademiaspecjalistowit.transactionalorder.order.repository.OrderRepository;
import pl.akademiaspecjalistowit.transactionalorder.order.service.events.OrderPlacedEventListener;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.exception.ProductException;
import pl.akademiaspecjalistowit.transactionalorder.product.service.ProductReadService;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductReadService productReadService;
    private final OrderPlacedEventListener orderPlacedEventListener;

    @Override
    @Transactional
    public void placeAnOrder(OrderDto orderDto) {
        OrderEntity orderEntity = productReadService.getProductByName(orderDto.getProductName())
            .map(productEntity -> makeAnOrderWithWarehouseStateUpdate(orderDto, productEntity))
            .orElseThrow(() -> new OrderServiceException("Zamównie nie moze być realizowane, ponieważ " +
                "zawiera pozycje niedostępną w magazynie"));

        orderRepository.save(orderEntity);
//        orderPlacedEventListener.notifyOrderPlaced(orderEntity);
    }

    private OrderEntity makeAnOrderWithWarehouseStateUpdate(OrderDto orderDto, ProductEntity productEntity) {
        try {
            return new OrderEntity(
                productEntity,
                orderDto.getQuantity());
        } catch (ProductException e) {
            throw new OrderServiceException(
                "Zamównie nie może być zrealizowane ponieważ ilosć " +
                    "pozycji w magazynie jest niewystarczająca");
        }
    }

}
