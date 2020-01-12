package de.struktuhr.gateway.orderservice;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Order {

    private final String id;
    private final BigDecimal orderValue;
    private final LocalDate orderDate;
    private final String organizationId;

}
