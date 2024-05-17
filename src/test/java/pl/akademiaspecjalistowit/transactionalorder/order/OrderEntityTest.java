package pl.akademiaspecjalistowit.transactionalorder.order;


import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderException;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;

class OrderEntityTest {

    @Test
    void should_create_order_for_valid_quantity() {
        //given
        int validQuantity = 10;
        ProductEntity pizza = new ProductEntity("pizza", 10);

        //when
        OrderEntity pizzaOrder = new OrderEntity(List.of(pizza), validQuantity);

        //then
        Assertions.assertThat(pizzaOrder).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void should_not_create_order_for_invalid_quantity(int invalidQuantity) {
        //given
        ProductEntity pizza = new ProductEntity("pizza", 10);

        //when
        Executable e = () -> new OrderEntity(List.of(pizza), invalidQuantity);

        //then
        assertThrows(OrderException.class, e);
    }

    @Test
    void order_should_not_created_when_product_quantity_is_less_than_order_required() {
        //given
        int validQuantity = 10;
        ProductEntity pizza = new ProductEntity("pizza", 8);

        //when
        Executable e = () -> new OrderEntity(List.of(pizza), validQuantity);

        //then
        assertThrows(OrderException.class, e);
    }
}