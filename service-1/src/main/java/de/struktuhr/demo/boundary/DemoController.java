package de.struktuhr.demo.boundary;

import de.struktuhr.demo.control.DemoService;
import de.struktuhr.demo.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("persons/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return demoService.getPerson(id);
    }

    @PutMapping("persons/{id}")
    public Person updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        return demoService.updatePerson(id, person);
    }
}
