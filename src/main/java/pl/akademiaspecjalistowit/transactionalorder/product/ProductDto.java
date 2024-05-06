package pl.akademiaspecjalistowit.transactionalorder.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    private String name;
    private Integer quantity;
}
