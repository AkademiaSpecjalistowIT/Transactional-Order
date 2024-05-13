package pl.akademiaspecjalistowit.transactionalorder.order.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.order.repository.OrderRepository;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderServiceException;
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
        OrderEntity orderEntity = new OrderEntity(
            orderDto.getProductName(),
            orderDto.getQuantity());
        Optional<ProductEntity> productByName = productReadService.getProductByName(orderEntity.getProductName());

        OrderEntity orderEntityAfterValidations = updateWarehouseState(orderEntity,productByName);
        orderRepository.save(orderEntityAfterValidations);
        orderPlacedEventListener.notifyOrderPlaced(orderEntityAfterValidations);
    }

    private OrderEntity updateWarehouseState(OrderEntity orderEntity,
                                             Optional<ProductEntity> productByName) {
        return productByName.map(product -> {
            try {
                product.applyOrder(orderEntity);
            } catch (ProductException e) {
                throw new OrderServiceException(
                    "Zamównie nie może być zrealizowane ponieważ ilosć " +
                        "pozycji w magazynie jest niewystarczająca");
            }
            return orderEntity;
        }).orElseThrow(() -> new OrderServiceException("Zamównie nie moze być realizowane, ponieważ " +
            "zawiera pozycje niedostępną w magazynie"));
    }

}
