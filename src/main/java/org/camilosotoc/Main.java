package org.camilosotoc;

import org.camilosotoc.services.PayrollService;

public class Main {
    public static void main(String[] args) throws Exception {
        PayrollService payrollService = new PayrollService();
        payrollService.processPayroll();
    }
}