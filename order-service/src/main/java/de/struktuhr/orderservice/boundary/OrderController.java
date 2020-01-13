package de.struktuhr.orderservice.boundary;

import de.struktuhr.orderservice.control.OrderService;
import de.struktuhr.orderservice.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("orders/{id}")
    public Order getOrder(@PathVariable("id") String id, @RequestHeader(required = false, name = "X-USER-INFO") String userInfo) {
        log.info("UserInfo = " + userInfo);
        return orderService.getOrder(id);
    }

}
