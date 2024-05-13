package pl.akademiaspecjalistowit.transactionalorder.product.service;

import java.util.List;
import pl.akademiaspecjalistowit.transactionalorder.product.dto.ProductDto;

public interface ProductService {

    void addProduct(ProductDto productDto);

    List<ProductDto> getProducts();
}
