package pl.akademiaspecjalistowit.transactionalorder.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderServiceException;
import pl.akademiaspecjalistowit.transactionalorder.order.repository.OrderRepository;
import pl.akademiaspecjalistowit.transactionalorder.order.service.OrderService;
import pl.akademiaspecjalistowit.transactionalorder.product.dto.ProductDto;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.repository.ProductRepository;
import pl.akademiaspecjalistowit.transactionalorder.product.service.ProductService;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void should_place_an_order_for_valid_input_and_when_products_are_available() {
        //given
        productForTestOrderIsAvailable("pizza");

        //and
        OrderDto orderDto = prepareValidOrderDto("pizza");

        //when
        orderService.placeAnOrder(orderDto);

        //then
        OrderEntity orderEntity = orderIsSavedInDatabase();
        //and
        theOrderMatchesInputValues(orderDto, orderEntity);
    }

    @Test
    public void order_for_multiple_products_can_be_placed_for_these_same_product_if_its_available() {
        //given
        productForTestOrderIsAvailable("pizza");
        productForTestOrderIsAvailable("car");

        //and
        OrderDto orderDto = prepareValidOrderDto("pizza","car");

        //when
        orderService.placeAnOrder(orderDto);

        //then
        OrderEntity orderEntity = orderIsSavedInDatabase();
        //and
        theOrderMatchesInputValues(orderDto, orderEntity);
    }

    @Test
    public void order_is_not_place_if_one_of_order_items_is_not_available() {
        //given
        productForTestOrderIsAvailable("pizza");
        productForTestOrderIsAvailable("car");

        //and
        OrderDto orderDto = prepareValidOrderDto("pizza","bike");

        //when
        Executable e = () -> orderService.placeAnOrder(orderDto);

        //then
        orderIsNotSavedInTheDatabase();
    }

    @Test
    public void order_will_not_be_placed_if_product_is_not_available() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto("pizza");

        //when
        Executable e = () -> orderService.placeAnOrder(validOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
        OrderServiceException orderServiceException = assertThrows(OrderServiceException.class, e);
        assertThat(orderServiceException.getMessage()).contains("Order is rejected, due to missing of some items in the warehouse");
    }

    @Test
    public void order_will_not_be_placed_if_product_availability_is_insufficient() {
        //given
        OrderDto validOrderDto = prepareValidOrderDto("pizza");
        productForTestOrderIsAvailableWithQuantity(validOrderDto, validOrderDto.getQuantity() -1);

        //when
        Executable e = () -> orderService.placeAnOrder(validOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
        OrderServiceException orderServiceException = assertThrows(OrderServiceException.class, e);
        assertThat(orderServiceException.getMessage()).contains("ilosć pozycji w magazynie jest niewystarczająca");
    }


    @Test
    public void order_will_not_be_placed_if_input_values_are_incorrect() {
        //given
        OrderDto invalidOrderDto = prepareInvalidOrderDto();

        //when
        Executable e = () -> orderService.placeAnOrder(invalidOrderDto);

        //then
        orderIsNotSavedInTheDatabase();
    }

    private void productForTestOrderIsAvailable(String pizza) {
        productService.addProduct(new ProductDto(pizza, 10));
    }

    private void productForTestOrderIsAvailableWithQuantity(OrderDto orderDto, int quantity) {
        productService.addProduct(new ProductDto(
            orderDto.getProducts().get(0),
            quantity));
    }

    private void theOrderMatchesInputValues(OrderDto orderDto, OrderEntity orderEntity) {
        assertThat(orderDto.getProducts()).containsExactlyInAnyOrderElementsOf(
            orderEntity.getProductEntityList().stream().map(ProductEntity::getName).toList());
        assertThat(orderDto.getQuantity()).isEqualTo(orderEntity.getQuantity());
    }

    private OrderEntity orderIsSavedInDatabase() {
        List<OrderEntity> all = orderRepository.findAll();
        assertThat(all).hasSize(1);
        return all.get(0);
    }

    private void orderIsNotSavedInTheDatabase() {
        List<OrderEntity> all = orderRepository.findAll();
        assertThat(all).hasSize(0);
    }

    private OrderDto prepareValidOrderDto(String... productNames) {
        int validQuantity = 10;
        return new OrderDto(Arrays.stream(productNames).toList(), validQuantity);
    }

    private OrderDto prepareInvalidOrderDto() {
        int validQuantity = -1;
        return new OrderDto(List.of("exampleProduct"), validQuantity);
    }
}