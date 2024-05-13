package pl.akademiaspecjalistowit.transactionalorder.product.service;

import java.util.Optional;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;

public interface ProductReadService {
    Optional<ProductEntity> getProductByName(String productName);
}
