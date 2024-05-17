package pl.akademiaspecjalistowit.transactionalorder.product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.exception.ProductException;

class ProductEntityTest {

    @Test
    public void should_not_throw_exception_if_product_quantity_is_sufficient() {
        //given
        ProductEntity pizza = new ProductEntity("pizza", 12);

        //when
        Executable e = () -> new OrderEntity(List.of(pizza), 12);

        //then
        assertDoesNotThrow(e);
    }

}