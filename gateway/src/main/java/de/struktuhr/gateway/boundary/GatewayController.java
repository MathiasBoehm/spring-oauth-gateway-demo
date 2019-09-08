package de.struktuhr.gateway.boundary;

import de.struktuhr.gateway.service1.Service1;
import de.struktuhr.gateway.service1.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class GatewayController {

    @Autowired
    private Service1 service1;

    @GetMapping("/service1/persons/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return service1.getPerson(id);
    }
}
