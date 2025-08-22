package org.camilosotoc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.camilosotoc.config.AppConfig;
import org.camilosotoc.config.FilePaths;
import org.camilosotoc.models.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

class CsvUtilTest {

    @TempDir
    Path tempDir;
    private AppConfig appConfig;
    private FilePaths filePaths;
    private File testInputFile;
    private File testValidOutputFile;
    private File testInvalidOutputFile;

    @BeforeEach
    void setUp() {
        appConfig = mock(AppConfig.class);
        filePaths = mock(FilePaths.class);

        testInputFile = tempDir.resolve("test_input.csv").toFile();
        testValidOutputFile = tempDir.resolve("test_valid.csv").toFile();
        testInvalidOutputFile = tempDir.resolve("test_invalid.csv").toFile();
        when(appConfig.getFilePaths()).thenReturn(filePaths);
        when(filePaths.inputPath()).thenReturn(testInputFile.getAbsolutePath());
        when(filePaths.validOutputPath()).thenReturn(testValidOutputFile.getAbsolutePath());
        when(filePaths.invalidOutputPath()).thenReturn(testInvalidOutputFile.getAbsolutePath());
    }

    @Test
    void testReadCsv_Successful() throws IOException {

        String csvContent = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso\n" +
                "Juan,Pérez,12345678-9,Developer,500000,100000,50000,2020-01-15\n" +
                "María,González,98765432-1,QA,450000,80000,30000,2019-05-20";
        try (FileWriter writer = new FileWriter(testInputFile)) {
            writer.write(csvContent);
        }
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);

            List<Employee> employees = CsvUtil.readCsv();

            assertEquals(2, employees.size());
            Employee firstEmployee = employees.get(0);
            assertEquals("Juan", firstEmployee.getName());
            assertEquals("Pérez", firstEmployee.getLastname());
            assertEquals("12345678-9", firstEmployee.getRut());
            assertEquals("Developer", firstEmployee.getPosition());
            assertEquals(500000.0, firstEmployee.getBaseSalary());
            assertEquals(100000.0, firstEmployee.getBonus());
            assertEquals(50000.0, firstEmployee.getDiscounts());
            assertEquals("2020-01-15", firstEmployee.getInDate());
            Employee secondEmployee = employees.get(1);
            assertEquals("María", secondEmployee.getName());
            assertEquals("González", secondEmployee.getLastname());
            assertEquals("98765432-1", secondEmployee.getRut());
            assertEquals("QA", secondEmployee.getPosition());
            assertEquals(450000.0, secondEmployee.getBaseSalary());
            assertEquals(80000.0, secondEmployee.getBonus());
            assertEquals(30000.0, secondEmployee.getDiscounts());
            assertEquals("2019-05-20", secondEmployee.getInDate());
        }
    }

    @Test
    void testReadCsv_WithNullValues() throws IOException {
        String csvContent = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso\n" +
                "Carlos,Rodríguez,11111111-1,Manager,null,50000,10000,2021-03-10\n" +
                "Ana,López,22222222-2,Analyst,300000,null,5000,2022-07-15";
        try (FileWriter writer = new FileWriter(testInputFile)) {
            writer.write(csvContent);
        }
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);

            List<Employee> employees = CsvUtil.readCsv();

            assertEquals(2, employees.size());
            Employee firstEmployee = employees.get(0);
            assertNull(firstEmployee.getBaseSalary());
            assertEquals(50000.0, firstEmployee.getBonus());
            assertEquals(10000.0, firstEmployee.getDiscounts());
            Employee secondEmployee = employees.get(1);
            assertEquals(300000.0, secondEmployee.getBaseSalary());
            assertNull(secondEmployee.getBonus());
            assertEquals(5000.0, secondEmployee.getDiscounts());
        }
    }

    @Test
    void testReadCsv_WithEmptyValues() throws IOException {
        String csvContent = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso\n" +
                "Luis,Martínez,33333333-3,Designer,,25000,,2023-01-01";
        try (FileWriter writer = new FileWriter(testInputFile)) {
            writer.write(csvContent);
        }
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);

            List<Employee> employees = CsvUtil.readCsv();

            assertEquals(1, employees.size());
            Employee employee = employees.get(0);
            assertNull(employee.getBaseSalary());
            assertEquals(25000.0, employee.getBonus());
            assertNull(employee.getDiscounts());
        }
    }

    @Test
    void testReadCsv_WithInvalidNumberFormat() throws IOException {
        String csvContent = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso\n" +
                "Pedro,Sánchez,44444444-4,Tester,invalid,50000,10000,2021-03-10";
        try (FileWriter writer = new FileWriter(testInputFile)) {
            writer.write(csvContent);
        }
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);

            List<Employee> employees = CsvUtil.readCsv();

            assertEquals(1, employees.size());
            Employee employee = employees.get(0);
            assertNull(employee.getBaseSalary()); // Debería ser null debido al formato inválido
            assertEquals(50000.0, employee.getBonus());
            assertEquals(10000.0, employee.getDiscounts());
        }
    }

    @Test
    void testReadCsv_IncompleteLine() throws IOException {
        String csvContent = "Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso\n" +
                "Elena,Castro,55555555-5,Manager,600000,70000"; // Faltan campos
        try (FileWriter writer = new FileWriter(testInputFile)) {
            writer.write(csvContent);
        }
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);
            Exception exception = assertThrows(IllegalArgumentException.class, CsvUtil::readCsv);
            assertTrue(exception.getMessage().contains("Línea incompleta"));
        }
    }

    @Test
    void testReadCsv_FileNotFound() {
        when(filePaths.inputPath()).thenReturn("/ruta/inexistente/archivo.csv");
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);
            Exception exception = assertThrows(FileNotFoundException.class, CsvUtil::readCsv);
            assertTrue(exception.getMessage().contains("Archivo no encontrado:"));
        }
    }

    @Test
    void testWriteCsv_ValidEmployees() throws IOException {
        List<Employee> employees = List.of(
                new Employee("Juan", "Pérez", "12345678-9", "Developer", 500000.0, 100000.0, 50000.0, "2020-01-15",
                        null),
                new Employee("María", "González", "98765432-1", "QA", 450000.0, 80000.0, 30000.0, "2019-05-20", null));
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);
            CsvUtil.writeCsv(employees, true);
            assertTrue(testValidOutputFile.exists());
        }
    }

    @Test
    void testWriteCsv_InvalidEmployees() throws IOException {
        List<Employee> employees = List.of(
                new Employee("Carlos", "Rodríguez", "11111111-1", "Manager", null, 50000.0, 10000.0, "2021-03-10",
                        "Salario base nulo"),
                new Employee("Ana", "López", "22222222-2", "Analyst", 300000.0, null, 5000.0, "2022-07-15",
                        "Bono nulo"));
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);
            CsvUtil.writeCsv(employees, false);
            assertTrue(testInvalidOutputFile.exists());
        }
    }

    @Test
    void testWriteCsv_EmptyList() throws IOException {

        List<Employee> employees = List.of();
        try (MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {
            mockedAppConfig.when(AppConfig::getInstance).thenReturn(appConfig);
            CsvUtil.writeCsv(employees, true);
            assertTrue(testValidOutputFile.exists());
        }
    }

    @Test
    void testParseEmployeeFromCsvLine_CompleteData() {
        String line = "Juan,Pérez,12345678-9,Developer,500000,100000,50000,2020-01-15";
        Employee employee = CsvUtil.parseEmployeeFromCsvLine(line, 1);
        assertEquals("Juan", employee.getName());
        assertEquals("Pérez", employee.getLastname());
        assertEquals("12345678-9", employee.getRut());
        assertEquals("Developer", employee.getPosition());
        assertEquals(500000.0, employee.getBaseSalary());
        assertEquals(100000.0, employee.getBonus());
        assertEquals(50000.0, employee.getDiscounts());
        assertEquals("2020-01-15", employee.getInDate());
    }

    @Test
    void testParseEmployeeFromCsvLine_IncompleteLine() {
        String line = "Juan,Pérez,12345678-9,Developer,500000";
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CsvUtil.parseEmployeeFromCsvLine(line, 1));
        assertTrue(exception.getMessage().contains("Línea incompleta"));
    }

}
