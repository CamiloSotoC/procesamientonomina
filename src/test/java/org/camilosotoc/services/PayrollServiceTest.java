package org.camilosotoc.services;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.util.List;

import org.camilosotoc.models.Employee;
import org.camilosotoc.models.EmployeeValidator;
import org.camilosotoc.utils.CsvUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class PayrollServiceTest {

    private PayrollService payrollService;

    @BeforeEach
    void setUp() {
        payrollService = new PayrollService();
    }

    @Test
    void testProcessPayroll() throws Exception {
        Employee e1 = new Employee("Maria", "Salazar", "2222222-2", "Developer", 500000.0, 5000.0,
                2000.0, "2023-01-01", null);
        Employee e2 = new Employee("John", "Doe", "11111111-1", "Developer", 500000.0, 5000.0,
                2000.0, "2023-01-01", null);
        e2.setError("Error en datos");
        List<Employee> employees = List.of(e1, e2);

        try (MockedStatic<CsvUtil> csvMock = mockStatic(CsvUtil.class);
                MockedStatic<EmployeeValidator> validatorMock = mockStatic(EmployeeValidator.class)) {

            csvMock.when(CsvUtil::readCsv).thenReturn(employees);
            validatorMock.when(() -> EmployeeValidator.validateAll(employees)).then(invocation -> {
                e1.setError(null);
                return null;
            });

            payrollService.processPayroll();

            csvMock.verify(() -> CsvUtil.writeCsv(List.of(e1), true), times(1));
            csvMock.verify(() -> CsvUtil.writeCsv(List.of(e2), false), times(1));
        }
    }
}
