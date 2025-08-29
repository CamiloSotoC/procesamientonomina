package org.camilosotoc.utils;

import org.camilosotoc.config.AppConfig;
import org.camilosotoc.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvUtil {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    public static List<Employee> readCsv() throws IOException {
        String inputPath = AppConfig.getInstance().getFilePaths().inputPath();
        logger.info("Leyendo CSV - Desde: {}.", inputPath);
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("Nombre"))
                    continue;

                try {
                    Employee employee = parseEmployeeFromCsvLine(line, lineNumber);
                    employees.add(employee);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Error parseando CSV en línea " + lineNumber + ": " + line + " | " + e.getMessage(), e);
                }
            }
            logger.info("Leyendo CSV - Empleados registrados: {}.", employees.size());
        } catch (IOException e) {
            throw new FileNotFoundException("Archivo no encontrado: " + inputPath + " | " + e.getMessage());
        }
        return employees;
    }

    public static Employee parseEmployeeFromCsvLine(String line, int lineNumber) {
        String[] data = line.split(",");

        if (data.length < 8) {
            throw new IllegalArgumentException(
                    "Línea incompleta en CSV (línea " + lineNumber + "): " + line);
        }

        String name = data[0];
        String lastname = data[1];
        String rut = data[2];
        String position = data[3];
        Double baseSalary;
        try {
            baseSalary = (data[4] == null || data[4].trim().isEmpty()) ? null : Double.parseDouble(data[4]);
        } catch (NumberFormatException ex) {
            baseSalary = null;
        }

        Double bonus;
        try {
            bonus = (data[5] == null || data[5].trim().isEmpty()) ? null : Double.parseDouble(data[5]);
        } catch (NumberFormatException ex) {
            bonus = null;
        }

        Double discounts;
        try {
            discounts = (data[6] == null || data[6].trim().isEmpty()) ? null : Double.parseDouble(data[6]);
        } catch (NumberFormatException ex) {
            discounts = null;
        }
        String inDate = data[7];

        return new Employee(name, lastname, rut, position, baseSalary, bonus, discounts, inDate, null);
    }

    public static void writeCsv(List<Employee> employees) throws IOException {

        String validOutputPath = AppConfig.getInstance().getFilePaths().validOutputPath();
        logger.info("Escribiendo CSV - Empleados validos: {}.", validOutputPath);
        String headerValid = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso,SalarioFinal\n";

        String invalidOutputPath = AppConfig.getInstance().getFilePaths().invalidOutputPath();
        String headerInvalid = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso,MotivoError\n";
        logger.info("Escribiendo CSV - Registros invalidos: {}.", invalidOutputPath);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (
                BufferedWriter bwValid = new BufferedWriter(new FileWriter(validOutputPath));
                BufferedWriter bwInvalid = new BufferedWriter(new FileWriter(invalidOutputPath));) {
            bwValid.write(headerValid);
            bwInvalid.write(headerInvalid);
            for (Employee e : employees) {
                if (e.getError() == null) {
                    bwValid.write(String.format(Locale.US, "%s,%s,%s,%s,%.0f,%.0f,%.0f,%s,%.2f\n",
                            e.getName(), e.getLastname(), e.getRut(), e.getPosition(),
                            e.getBaseSalary(), e.getBonus(), e.getDiscounts(), e.getInDate(),
                            e.getFinalSalary()));
                } else {
                    bwInvalid.write(String.format(Locale.US, "%s,%s,%s,%s,%.0f,%.0f,%.0f,%s,%s\n",
                            e.getName(), e.getLastname(), e.getRut(), e.getPosition(),
                            e.getBaseSalary(), e.getBonus(), e.getDiscounts(), e.getInDate(),
                            e.getError()));
                }
            }
        }
    }
}
