package de.struktuhr.orderservice.boundary;

import de.struktuhr.orderservice.control.OrderService;
import de.struktuhr.orderservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("orders/{id}")
    public Order getOrder(@PathVariable("id") String id) {
        return orderService.getOrder(id);
    }

}
