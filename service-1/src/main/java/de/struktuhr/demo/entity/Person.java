package de.struktuhr.demo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Person {

    private Long id;

    private String firstname;

    private String lastname;

    private LocalDate birthday;
}
