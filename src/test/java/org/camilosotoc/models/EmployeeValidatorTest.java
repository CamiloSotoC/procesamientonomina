package org.camilosotoc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class EmployeeValidatorTest {

    @Test
    void testRutNullOrEmpty() {

        Employee e1 = new Employee("John", "Doe", "", "Developer", 50000.0, 5000.0,
                2000.0, "2023-01-01", null);
        Employee e2 = new Employee("John", "Doe", null, "Developer", 50000.0, 5000.0,
                2000.0, "2023-01-01", null);

        EmployeeValidator.validateAll(List.of(e1, e2));

        assertEquals("RUT invalido", e1.getError());

    }

    @Test
    void testRutDuplicated() {
        Employee e1 = new Employee("John", "Doe", "12345678-9", "Developer", 50000.0, 5000.0,
                2000.0, "2023-01-01", null);
        Employee e2 = new Employee("Maria", "Paz", "12345678-9", "Developer", 50000.0, 5000.0,
                2000.0, "2023-01-01", null);

        EmployeeValidator.validateAll(List.of(e1, e2));

        assertEquals("RUT duplicado", e1.getError());
        assertEquals("RUT duplicado", e2.getError());
    }

    @Test
    void testBaseSalaryInvalid() {
        Employee e1 = new Employee("John", "Doe", "12345678-9", "Developer", null, 5000.0,
                2000.0, "2023-01-01", null);
        Employee e2 = new Employee("Maria", "Paz", "23456789-1", "Developer", -1.00, 5000.0,
                2000.0, "2023-01-01", null);
        Employee e3 = new Employee("Carla", "Muñóz", "34567891-2", "Developer", 399999.00, 5000.0,
                2000.0, "2023-01-01", null);
        EmployeeValidator.validateAll(List.of(e1, e2, e3));
        assertEquals("Salario base: valor invalido", e1.getError());
        assertEquals("Salario base: valor invalido", e2.getError());
        assertEquals("Salario base menor a $400.000", e3.getError());

    }

    @Test
    void testBonusTooHigh() {
        Employee e1 = new Employee("Maria", "Paz", "23456789-1", "Developer", 500000.00, null,
                2000.0, "2023-01-01", null);
        Employee e2 = new Employee("John", "Doe", "12345678-9", "Developer", 500000.00, -1.00,
                2000.0, "2023-01-01", null);
        Employee e3 = new Employee("Carla", "Muñóz", "34567891-2", "Developer", 500000.00, 300000.00,
                2000.0, "2023-01-01", null);

        EmployeeValidator.validateAll(List.of(e1, e2, e3));
        assertEquals("Bonos: valor invalido", e1.getError());
        assertEquals("Bonos: valor invalido", e2.getError());
        assertEquals("Bonos superan el 50% del salario base", e3.getError());
    }

    @Test
    void testDiscountsTooHigh() {
        Employee e1 = new Employee("Maria", "Paz", "23456789-1", "Developer", 500000.00, 200000.00,
                null, "2023-01-01", null);
        Employee e2 = new Employee("John", "Doe", "12345678-9", "Developer", 500000.00, 200000.00,
                -1.00, "2023-01-01", null);
        Employee e3 = new Employee("Carla", "Muñóz", "34567891-2", "Developer", 500000.00, 200000.00,
                500001.0, "2023-01-01", null);
        EmployeeValidator.validateAll(List.of(e1, e2, e3));
        assertEquals("Descuentos: valor invalido", e1.getError());
        assertEquals("Descuentos: valor invalido", e2.getError());
        assertEquals("Descuentos superan el salario base", e3.getError());
    }

    @Test
    void testInvalidDateFormat() {
        Employee e1 = new Employee("Maria", "Paz", "23456789-1", "Developer", 500000.00, 200000.00,
                2000.00, "01-01-2023", null);
        EmployeeValidator.validateAll(List.of(e1));
        assertEquals("Fecha debe tener formato valido (yyyy-MM-dd).", e1.getError());
    }

    @Test
    void testFutureInDate() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        Employee e1 = new Employee("Maria", "Paz", "23456789-1", "Developer", 500000.00, 200000.00,
                2000.00, futureDate, null);
        EmployeeValidator.validateAll(List.of(e1));
        assertEquals("Fecha de ingreso en el futuro", e1.getError());
    }

    @Test
    void testValidEmployee() {
        Employee e1 = new Employee("John", "Doe", "11111111-1", "Developer", 500000.0, 5000.0,
                2000.0, "2023-01-01", null);
        EmployeeValidator.validateAll(List.of(e1));
        assertNull(e1.getError());
    }
}
