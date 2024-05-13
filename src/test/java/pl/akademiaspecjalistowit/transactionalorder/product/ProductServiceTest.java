package pl.akademiaspecjalistowit.transactionalorder.product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void should_add_new_product() {
        //given
        ProductDto exampleProduct = new ProductDto("test1", 45);
        ProductEntity referenceEntity = new ProductEntity(
            exampleProduct.getName(),
            exampleProduct.getQuantity());

        //when
        productService.addProduct(exampleProduct);

        //then
        List<ProductEntity> all = productRepository.findAll();
        assertThat(all).hasSize(1);
        ProductEntity productEntity = all.get(0);
        assertThat(productEntity).isEqualTo(referenceEntity);
    }


    @Test
    void should_get_product() {
        //given
        ProductDto exampleProduct = new ProductDto("test2", 20);
        productService.addProduct(exampleProduct);

        //when
        List<ProductDto> products = productService.getProducts();

        //when
        assertThat(products).containsExactlyInAnyOrder(exampleProduct);
    }

    @Test
    public void should_remove_bought_out_products() {
        // given
        ProductEntity productEntity = prepareProductEntity();
        productRepository.save(productEntity);

        // when
        productRepository.removeBoughtOutProducts("Test Product 1");

        //then
        Optional<ProductEntity> result = productRepository.getProductEntityByName("Test Product 1");
        assertTrue(result.isEmpty());
    }

    private ProductEntity prepareProductEntity(){
        ProductEntity product1 = new ProductEntity();
        product1.setName("Test Product 1");
        product1.setQuantity(0);
        return product1;
    }

}