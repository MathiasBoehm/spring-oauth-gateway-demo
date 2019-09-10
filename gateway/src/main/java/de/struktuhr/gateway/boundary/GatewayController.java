package de.struktuhr.gateway.boundary;

import de.struktuhr.gateway.orderservice.Order;
import de.struktuhr.gateway.orderservice.OrderService;
import de.struktuhr.gateway.service1.Service1;
import de.struktuhr.gateway.service1.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class GatewayController {

    @Autowired
    private Service1 service1;

    @Autowired
    private OrderService orderService;

    @GetMapping("/service1/persons/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return service1.getPerson(id);
    }

    @GetMapping("orderservice/orders/{id}")
    @PostAuthorize("isMemberOfOrganization(returnObject.organizationId) or isAdmin()")
    public Order getOrder(@PathVariable("id") String id) {
        return orderService.getOrder(id);
    }

    @GetMapping("demo/user")
    public String helloUser() {
        return "hello User";
    }

    @GetMapping("demo/admin")
    @PreAuthorize("isAdmin()")
    public String helloAdmin() {
        return "hello Admin";
    }
}
