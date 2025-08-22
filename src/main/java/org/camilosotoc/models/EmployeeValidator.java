package org.camilosotoc.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camilosotoc.utils.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeValidator {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeValidator.class);

    public static void validateAll(List<Employee> employees) {
        logger.info("Validando los registros de empleados.");

        Map<String, Long> rutCount = employees.stream()
                .filter(employee -> employee.getRut() != null)
                .collect(Collectors.groupingBy(Employee::getRut, Collectors.counting()));

        for (Employee e : employees) {
            String error = null;

            if (e.getRut() == null || e.getRut().trim().isEmpty()) {
                error = "RUT invalido";
            } else if (rutCount.get(e.getRut()) > 1) {
                error = "RUT duplicado";
            } else {
                error = validate(e);
            }

            e.setError(error);
        }

    }

    private static String validate(Employee e) {

        if (e.getBaseSalary() == null || e.getBaseSalary() < 0) {
            return "Salario base: valor invalido";
        }
        if (e.getBonus() == null || e.getBonus() < 0) {
            return "Bonos: valor invalido";
        }
        if (e.getDiscounts() == null || e.getDiscounts() < 0) {
            return "Descuentos: valor invalido";
        }

        if (e.getBaseSalary() < 400000) {
            return "Salario base menor a $400.000";
        }
        if (e.getBonus() > e.getBaseSalary() * 0.5) {
            return "Bonos superan el 50% del salario base";
        }
        if (e.getDiscounts() > e.getBaseSalary()) {
            return "Descuentos superan el salario base";
        }
        if (!validateDateFormat(e.getInDate())) {
            return "Fecha debe tener formato valido (yyyy-MM-dd).";
        }
        if (!validateInDate(e.getInDate())) {
            return "Fecha de ingreso en el futuro";
        }
        return null;
    }

    private static boolean validateDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private static boolean validateInDate(String inDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(inDate, formatter);
            return !date.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
