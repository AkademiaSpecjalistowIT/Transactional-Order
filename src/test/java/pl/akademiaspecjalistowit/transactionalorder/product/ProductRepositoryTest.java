//package pl.akademiaspecjalistowit.transactionalorder.product;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//class ProductRepositoryTest {
//    @Autowired
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    public void setUp() {
//        ProductEntity product1 = new ProductEntity();
//        product1.setName("Test Product 1");
//        product1.setQuantity(0);
//        productRepository.save(product1);
//
//        ProductEntity product2 = new ProductEntity();
//        product2.setName("Test Product 2");
//        product2.setQuantity(10);
//        productRepository.save(product2);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        productRepository.deleteAll();
//    }
//
//    @Test
//    public void testGetProductEntityByName() {
//        // given
//        String productName = "Test Product 1";
//
//        // when
//        Optional<ProductEntity> result = productRepository.getProductEntityByName(productName);
//
//        // then
//        assertTrue(result.isPresent());
//        assertEquals(productName, result.get().getName());
//    }
//
//    @Test
//    public void testRemoveBoughtOutProducts() {
//        // when
//        productRepository.removeBoughtOutProducts("Test Product 1");
//
//        // then
//        Optional<ProductEntity> result = productRepository.getProductEntityByName("Test Product 1");
//        assertTrue(result.isEmpty());
//    }
//}