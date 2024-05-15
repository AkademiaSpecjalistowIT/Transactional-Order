package pl.akademiaspecjalistowit.transactionalorder.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderException;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<ProductEntity> productEntityList;

    private Integer quantity;

    public OrderEntity(List<ProductEntity> productEntityList, Integer quantity) {
        validate(quantity);
        this.productEntityList = productEntityList;
        this.quantity = quantity;
        productEntityList.forEach(e -> e.applyOrder(this));
    }

    private void validate(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderException("Zamówienie pownno zawierać nie ujemną ilość pozycji");
        }
    }
}
