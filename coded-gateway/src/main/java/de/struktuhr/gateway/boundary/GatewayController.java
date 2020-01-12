package de.struktuhr.gateway.boundary;

import de.struktuhr.gateway.clients.orderservice.OrderServiceClient;
import de.struktuhr.gateway.clients.orderservice.entity.Order;
import de.struktuhr.gateway.clients.personservice.PersonServiceClient;
import de.struktuhr.gateway.clients.personservice.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class GatewayController {

    @Autowired
    private PersonServiceClient personServiceClient;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @GetMapping("/personservice/persons/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return personServiceClient.getPerson(id);
    }

    @PutMapping("/personservice/persons/{id}")
    public Person updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        return personServiceClient.updatePerson(id, person);
    }

    @GetMapping("orderservice/orders/{id}")
    @PostAuthorize("isMemberOfOrganization(returnObject.organizationId) or isAdmin()")
    public Order getOrder(@PathVariable("id") String id) {
        return orderServiceClient.getOrder(id);
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
