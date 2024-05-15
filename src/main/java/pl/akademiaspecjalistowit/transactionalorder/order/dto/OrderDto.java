package pl.akademiaspecjalistowit.transactionalorder.order.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderDto {
    private List<String> products;
    private Integer quantity;
}
