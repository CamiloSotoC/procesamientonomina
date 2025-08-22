package org.camilosotoc.config;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class YamlLoader {
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadYaml(String filePath) {
        Yaml yaml = new Yaml();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Object data = yaml.load(reader);
            if (data instanceof Map) {
                return (Map<String, Object>) data;
            } else {
                throw new RuntimeException("El archivo YAML no contiene un objeto tipo Map en la raíz: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el archivo YAML: " + filePath, e);
        }
    }
}