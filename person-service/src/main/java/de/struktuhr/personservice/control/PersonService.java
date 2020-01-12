package de.struktuhr.personservice.control;

import de.struktuhr.personservice.entity.Person;
import de.struktuhr.personservice.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PersonService {

    private Map<Long, Person> persons;

    public PersonService() {
        initPersons();
    }

    public Person getPerson(Long id) {
        return internalGetPerson(id);
    }

    public Person updatePerson(Long id, Person person) {
        Person p = internalGetPerson(id);
        p.setBirthday(person.getBirthday());
        p.setFirstname(person.getFirstname());
        p.setLastname(person.getLastname());
        return p;
    }

    private Person internalGetPerson(Long id) {
        Person person = persons.get(id);
        if (person == null) {
            throw new NotFoundException("Cannot find a person with id " + id);
        }
        return person;
    }

    private void initPersons() {
        persons = new LinkedHashMap<>();
        persons.put(1L, Person.builder().id(1L).firstname("John").lastname("Doe").birthday(LocalDate.of(1967, Month.APRIL, 3)).build());
        persons.put(2L, Person.builder().id(2L).firstname("Linda").lastname("Love").birthday(LocalDate.of(1970, Month.MAY, 10)).build());
        persons.put(3L, Person.builder().id(3L).firstname("Peter").lastname("Pan").birthday(LocalDate.of(1980, Month.AUGUST, 25)).build());
    }
}
