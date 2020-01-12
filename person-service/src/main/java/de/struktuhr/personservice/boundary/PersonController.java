package de.struktuhr.personservice.boundary;

import de.struktuhr.personservice.control.PersonService;
import de.struktuhr.personservice.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("persons/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return personService.getPerson(id);
    }

    @PutMapping("persons/{id}")
    public Person updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        return personService.updatePerson(id, person);
    }
}
