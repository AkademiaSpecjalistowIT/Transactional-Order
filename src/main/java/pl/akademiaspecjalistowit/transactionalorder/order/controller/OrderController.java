package pl.akademiaspecjalistowit.transactionalorder.order.controller;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.akademiaspecjalistowit.transactionalorder.order.dto.OrderDto;
import pl.akademiaspecjalistowit.transactionalorder.order.service.OrderService;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void placeAnOrder(@RequestBody OrderDto orderDto) {
        orderService.placeAnOrder(orderDto);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable UUID orderId){
        orderService.cancelOrder(orderId);
    }

}
