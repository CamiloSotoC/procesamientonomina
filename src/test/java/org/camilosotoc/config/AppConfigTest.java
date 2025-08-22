package org.camilosotoc.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AppConfigTest {

    private Map<String, Object> mockYamlContent() {
        Map<String, Object> paths = new HashMap<>();
        paths.put("input", "input.csv");
        paths.put("valid-output", "valid.csv");
        paths.put("invalid-output", "invalid.csv");

        Map<String, Object> procesamientonomina = new HashMap<>();
        procesamientonomina.put("paths", paths);

        Map<String, Object> root = new HashMap<>();
        root.put("procesamientonomina", procesamientonomina);

        return root;
    }

    @Test
    void testAppConfigSingletonWithMockedYamlLoader() {
        Map<String, Object> yamlMap = mockYamlContent();

        try (MockedStatic<YamlLoader> mocked = Mockito.mockStatic(YamlLoader.class)) {
            mocked.when(() -> YamlLoader.loadYaml(Mockito.anyString())).thenReturn(yamlMap);

            AppConfig config = AppConfig.getInstance();
            FilePaths filePaths = config.getFilePaths();

            assertEquals("input.csv", filePaths.inputPath());
            assertEquals("valid.csv", filePaths.validOutputPath());
            assertEquals("invalid.csv", filePaths.invalidOutputPath());
        }
    }

    @Test
    void testSingletonReturnsSameInstance() {
        Map<String, Object> yamlMap = mockYamlContent();

        try (MockedStatic<YamlLoader> mocked = Mockito.mockStatic(YamlLoader.class)) {
            mocked.when(() -> YamlLoader.loadYaml(Mockito.anyString())).thenReturn(yamlMap);

            AppConfig instance1 = AppConfig.getInstance();
            AppConfig instance2 = AppConfig.getInstance();

            assertSame(instance1, instance2, "AppConfig debería ser singleton");
        }
    }
}
