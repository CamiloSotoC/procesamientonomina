# 📊 Aplicación - Procesamiento de Nómina desde CSV

Esta aplicación permite procesar un archivo CSV de empleados, validar la información de cada registro, calcular antiguedad, bonificaciones, salario final, y generar archivos CSV de salida con empleados válidos e inválidos.

## 📂 Estructura del proyecto

    ```
    src/
     ├─ main/
     │   ├─ java/
     │   │   ├─ org.camilosotoc/
     │   │   │   ├─ Main.java
     │   │   │   ├─ config/
     │   │   │   │   ├─ AppConfig.java
     │   │   │   │   ├─ FilePaths.java
     │   │   │   │   └─ YamlLoader.java
     │   │   │   ├─ models/
     │   │   │   │   ├─ Employee.java
     │   │   │   │   └─ EmployeeValidator.java
     │   │   │   ├─ services/
     │   │   │   │   └─ PayrollService.java
     │   │   │   └─ utils/
     │   │   │       └─ CsvUtil.java
     │   └─ resources/
     │       └─ application.yml
    
    ```
Esta aplicación consta de 4 partes:
 - Primer paso: Leer el archivo de entrada


## ✨ Dependencias

- Java 17
- Lombok 1.18.38
- Snakeyaml 2.2
- Logback-classic 1.5.18
- Junit-jupiter 5.8.1
- Mockito-core 5.5.0

## ⚙️ Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/camilosotoc/procesamientonomina.git
   cd procesamientonomina
   ```

2. Compilar el proyecto usando Maven:

   ```bash
   mvn clean package
   ```

## 🚀 Uso

1. 🛤️ La configuración de las rutas de los archivos de entrada y salida se encuentran el en "application.yml".

   ```
   src/main/resources/application.yml
   ```

2. 📝 Esta es su configuración por defecto:

   ```
   procesamientonomina:
     paths:
        input: "src/main/resources/input/empleados.csv"
        valid-output: "src/main/resources/output/validos.csv"
        invalid-output: "src/main/resources/output/invalidos.csv"
   ```

3. 📝 El archivo de entrada debe tener el siguiente formato:

   ```
   Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso
   María,Muñoz,93922401-2,QA,497704,329207,178575,2025-08-11
   Sofía,Rojas,81440572-3,Líder Técnico,444110,102613,374146,2025-07-17
   ```
   *El proyecto incluye un archivo de entrada de ejemplo "empleados.csv", en la siguiente ruta:
   ```
   src/main/resources/input/empleados.csv
   ```

4. 🟢 Ejecutar la aplicación en la raíz del proyecto:
  
   ```bash
   java -jar .\target\procesamientonomina-1.0.jar
   ``` 
5. 📝 Ejemplo de ejecución exitosa:

   ```bash
    PS C:\Users\Usuario\Documents\GitHub\procesamientonomina> java -jar .\target\procesamientonomina-1.0.jar
    20:41:31.650 [main] INFO org.camilosotoc.services.PayrollService -- ### Procesamiento Nomina - INICIO ####
    20:41:31.655 [main] INFO org.camilosotoc.config.AppConfig -- Cargando configuracion desde: src\main\resources\application.yml.
    20:41:31.702 [main] INFO org.camilosotoc.utils.CsvUtil -- Leyendo CSV - Desde: src/main/resources/input/empleados.csv.
    20:41:32.422 [main] INFO org.camilosotoc.utils.CsvUtil -- Leyendo CSV - Empleados registrados: 1000000.
    20:41:32.422 [main] INFO org.camilosotoc.models.EmployeeValidator -- Validando los registros de empleados.
    20:41:33.587 [main] INFO org.camilosotoc.services.PayrollService -- Registros de empleados validos: 225168.
    20:41:33.587 [main] INFO org.camilosotoc.services.PayrollService -- Registros de empleados invalidos: 774832.
    20:41:33.587 [main] INFO org.camilosotoc.utils.CsvUtil -- Escribiendo CSV - Empleados validos: src/main/resources/output/validos.csv.
    20:41:34.502 [main] INFO org.camilosotoc.utils.CsvUtil -- Escribiendo CSV - Registros invalidos: src/main/resources/output/invalidos.csv.
    20:41:36.010 [main] INFO org.camilosotoc.services.PayrollService -- ### Procesamiento Nomina - FIN ####
    PS C:\Users\Usuario\Documents\GitHub\procesamientonomina>
   ```
6. La ejecución genera dos archivos de Salida:
- Formato del archivos de salida válidos:
   ```
   Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso,SalarioFinal
   Luis,Rojas,91203513-9,Analista,529909,44492,41225,2024-02-06,533176.00
   ```
- Formato del archivos de salida inválidos:
   ```
   Nombre,Apellido,RUT,Cargo,SalarioBase,Bonos,Descuentos,FechaIngreso,MotivoError
   María,Muñoz,93922401-2,QA,497704,329207,178575,2025-08-11,RUT duplicado
   Sofía,Contreras,38103085-4,QA,369023,124221,364323,2024-01-17,Salario menor a $400.000
   ```
## 🧪 Test unitarios
- Pruebas: Para ejecutar las pruebas unitarias, utiliza el siguiente comando:

   ```bash
   mvn test
   ```

## 📏 Reglas de negocio

- RUT único
- Salario base >= $400.000
- Bonos ≤ 50% del salario base
- Descuentos ≤ salario base
- Fecha ingreso válida y no futura
- Bonificación por antigüedad:
- +10% si > 5 años
- +5% si entre 3 y 5 años
- 0% si < 3 años

## 🫀 Cálculos requeridos

- Antigüedad: Calcular la cantidad de años desde la fecha de ingreso hasta hoy.
- Bonificación por antigüedad (se suma a los bonos):
  - Más de 5 años: +10% del salario base.
  - Entre 3 y 5 años: +5% del salario base.
  - Menos de 3 años: sin bonificación.
- Salario final:
  SalarioFinal = (SalarioBase + Bonos + BonificaciónAntigüedad) - Descuentos

## Requisitos técnicos

- Generar un proyecto Maven con Java 8 o superior.
- Manejo de archivos con BufferedReader / FileReader.
- Uso de clases como LocalDate, Period, DateTimeFormatter.
- Validación y manejo de errores con mensajes claros.
- Código limpio, modular y bien documentado.
- Debe contener un archivo README.md con la información del proyecto y sus instrucciones
  de instalación (como corresponde a un README)
- Se entregará un archivo CSV, para mejor entendimiento y facilitar el entendimiento y manejo
  de archivo, se requiere que su ruta de entrada este dentro del mismo proyecto en
  ```
  “src/main/resources/input/XXXX.csv”
  ```
