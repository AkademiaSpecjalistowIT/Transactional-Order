package pl.akademiaspecjalistowit.transactionalorder.product.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.akademiaspecjalistowit.transactionalorder.order.entity.OrderEntity;
import pl.akademiaspecjalistowit.transactionalorder.order.service.events.OrderPlacedEventListener;
import pl.akademiaspecjalistowit.transactionalorder.product.dto.ProductDto;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.repository.ProductRepository;

@AllArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService, ProductReadService, OrderPlacedEventListener {

    private final ProductRepository productRepository;

    @Override
    public void addProduct(ProductDto productDto) {
        ProductEntity productEntity =
            new ProductEntity(productDto.getName(), productDto.getQuantity());

        productRepository.save(productEntity);
    }

    @Override
    public List<ProductDto> getProducts() {
        List<ProductEntity> all = productRepository.findAll();
        return all.stream().map(e -> new ProductDto(e.getName(), e.getQuantity())).toList();
    }

    @Override
    public Optional<ProductEntity> getProductByName(String productName) {
        return productRepository.getProductEntityByName(productName);
    }

    private void removeBoughtOutProductsByName(String productName){
        productRepository.removeBoughtOutProducts(productName);
    }

    @Override
    public void notifyOrderPlaced(OrderEntity orderEntityAfterValidations) {
        removeBoughtOutProductsByName(orderEntityAfterValidations.getProductName());
    }
}
