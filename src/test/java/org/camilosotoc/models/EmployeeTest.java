package org.camilosotoc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class EmployeeTest {

        @Test
        void testGetSeniority() {
                Employee tenYearEmployee = new Employee("John", "Doe", "12345678-9", "Developer", 50000.0, 5000.0,
                                2000.0,
                                LocalDate.now().minusYears(10).toString(), null);
                assertEquals(10, tenYearEmployee.getSeniority());

                Employee newEmployee = new Employee("Jane", "Smith", "11111111-1", "Tester", 40000.0, 3000.0, 1000.0,
                                LocalDate.now().toString(), null);
                assertEquals(0, newEmployee.getSeniority());

                Employee oneYearEmployee = new Employee("Bob", "Brown", "22222222-2", "Analyst", 45000.0, 3500.0,
                                1500.0,
                                LocalDate.now().minusYears(1).toString(), null);
                assertEquals(1, oneYearEmployee.getSeniority());

                Employee futureEmployee = new Employee("Alice", "White", "33333333-3", "Designer", 42000.0, 3200.0,
                                1200.0,
                                LocalDate.now().plusYears(2).toString(), null);
                assertEquals(0, futureEmployee.getSeniority());
        }

        @Test
        void testGetSeniorityBonus() {
                Employee employee1 = new Employee("John", "Doe", "12345678-9", "Developer", 50000.0, 5000.0, 2000.0,
                                LocalDate.of(2015, 1, 1).toString(), null);
                assertEquals(5000.0, employee1.getSeniorityBonus());
                Employee employee2 = new Employee("Jane", "Doe", "98765432-1", "Manager", 50000.0, 5000.0, 2000.0,
                                LocalDate.of(2018, 1, 1).toString(), null);
                assertEquals(5000.0, employee2.getSeniorityBonus());
                Employee employee3 = new Employee("Jim", "Beam", "12345678-0", "Intern", 50000.0, 5000.0, 2000.0,
                                LocalDate.of(2020, 1, 1).toString(), null);
                assertEquals(2500.0, employee3.getSeniorityBonus());
        }

        @Test
        void testGetFinalSalary() {
                Employee employee = new Employee("John", "Doe", "12345678-9", "Developer", 50000.0, 5000.0, 2000.0,
                                LocalDate.of(2015, 1, 1).toString(), null);
                assertEquals(58000.0, employee.getFinalSalary());
        }

        @Test
        void testGetFinalSalaryWithNullValues() {
                Employee employee = new Employee("John", "Doe", "12345678-9", "Developer", null, null, null,
                                LocalDate.of(2015, 1, 1).toString(), null);
                assertEquals(0.0, employee.getFinalSalary());
        }
}
