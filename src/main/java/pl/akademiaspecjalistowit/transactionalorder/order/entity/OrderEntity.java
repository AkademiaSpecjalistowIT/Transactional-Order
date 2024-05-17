package pl.akademiaspecjalistowit.transactionalorder.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.akademiaspecjalistowit.transactionalorder.order.exception.OrderException;
import pl.akademiaspecjalistowit.transactionalorder.product.entity.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.exception.ProductException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<ProductEntity> productEntityList;

    private Integer quantity;

    public OrderEntity(List<ProductEntity> productEntityList, Integer quantity) {
        validate(quantity);
        this.productEntityList = productEntityList;
        this.quantity = quantity;
        try {
            productEntityList.forEach(e -> e.applyOrder(this));
        } catch (ProductException e) {
            throw new OrderException("Nie można utworzyć zamówienia, " +
                "ponieważ wymagana ilość przekracza dostępność produktu");
        }
    }

    private void validate(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderException("Zamówienie pownno zawierać nie ujemną ilość pozycji");
        }
    }
}
