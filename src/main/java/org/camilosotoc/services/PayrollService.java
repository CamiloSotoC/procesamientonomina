package org.camilosotoc.services;

import org.camilosotoc.models.Employee;
import org.camilosotoc.models.EmployeeValidator;
import org.camilosotoc.utils.CsvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PayrollService {

    private List<Employee> employees;
    private List<Employee> validEmployees;
    private List<Employee> invalidEmployees;

    private static final Logger logger = LoggerFactory.getLogger(PayrollService.class);

    public void processPayroll() throws Exception {
        logger.info("### Procesamiento Nomina - INICIO ####");
        employees = CsvUtil.readCsv();
        EmployeeValidator.validateAll(employees);
        CsvUtil.writeCsv(employees);
        logger.info("### Procesamiento Nomina - FIN ####");
    }

}
