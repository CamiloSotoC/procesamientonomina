package org.camilosotoc.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class YamlLoaderTest {

    @Test
    void testLoadYamlSuccess(@TempDir Path tempDir) throws IOException {

        String yamlContent = """
                procesamientonomina:
                  paths:
                    input: "src/main/resources/input/empleados.csv"
                    valid-output: "src/main/resources/output/validos.csv"
                    invalid-output: "src/main/resources/output/invalidos.csv"
                """;

        Path yamlFile = tempDir.resolve("application.yml");
        Files.write(yamlFile, yamlContent.getBytes());

        Map<String, Object> result = YamlLoader.loadYaml(yamlFile.toString());

        assertTrue(result.containsKey("procesamientonomina"));

        Map<String, Object> procesamiento = (Map<String, Object>) result.get("procesamientonomina");
        assertTrue(procesamiento.containsKey("paths"));

        Map<String, Object> paths = (Map<String, Object>) procesamiento.get("paths");
        assertEquals("src/main/resources/input/empleados.csv", paths.get("input"));
        assertEquals("src/main/resources/output/validos.csv", paths.get("valid-output"));
        assertEquals("src/main/resources/output/invalidos.csv", paths.get("invalid-output"));
    }

    @Test
    void testLoadYamlFileNotFound() {
        String nonExistentPath = "nonexistent.yaml";
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            YamlLoader.loadYaml(nonExistentPath);
        });
        assertTrue(ex.getMessage().contains("Error leyendo el archivo YAML:"));
    }

    @Test
    void testLoadYamlNotMapRoot(@TempDir Path tempDir) throws IOException {
        String yamlContent = "- item1\n- item2\n";
        Path yamlFile = tempDir.resolve("list.yaml");
        Files.write(yamlFile, yamlContent.getBytes());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            YamlLoader.loadYaml(yamlFile.toString());
        });
        assertTrue(ex.getMessage().contains("El archivo YAML no contiene un objeto tipo Map en la raíz:"));
    }
}
