package de.struktuhr.orderservice.control;

import de.struktuhr.orderservice.entity.Order;
import de.struktuhr.orderservice.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OrderService {

    private Map<String, Order> data;

    public OrderService() {
        initData();
    }

    public Order getOrder(String id) {
        final Order order = data.get(id);
        if (order == null) {
            throw new NotFoundException("Cannot find order with id " + id);
        }
        return order;
    }

    private void initData() {
        data = new LinkedHashMap<>();
        data.put("1", new Order("1", BigDecimal.valueOf(2_500.00), LocalDate.of(2019, Month.SEPTEMBER, 10), "org_1"));
        data.put("2", new Order("2", BigDecimal.valueOf(3_500.00), LocalDate.of(2019, Month.JANUARY, 2), "org_1"));
        data.put("3", new Order("3", BigDecimal.valueOf(4_500.00), LocalDate.of(2019, Month.AUGUST, 18), "org_2"));
        data.put("4", new Order("4", BigDecimal.valueOf(5_500.00), LocalDate.of(2019, Month.SEPTEMBER, 1), "org_1"));
    }
}
