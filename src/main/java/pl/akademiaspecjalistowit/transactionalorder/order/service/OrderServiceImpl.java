package pl.akademiaspecjalistowit.transactionalorder.order.service;

import java.util.List;
import java.util.Optional;
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

    @Override
    @Transactional
    public void placeAnOrder(OrderDto orderDto) {
        List<ProductEntity> productEntities = orderDto.getProducts().stream().map(productReadService::getProductByName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        OrderEntity orderEntity = makeAnOrderWithWarehouseStateUpdate(orderDto, productEntities);

        orderRepository.save(orderEntity);
    }

    private OrderEntity makeAnOrderWithWarehouseStateUpdate(OrderDto orderDto, List<ProductEntity> productEntity) {
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
