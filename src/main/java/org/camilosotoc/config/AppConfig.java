package org.camilosotoc.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppConfig {
    private final FilePaths filePaths;

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private AppConfig() {
        String configPath = "src\\main\\resources\\application.yml";
        logger.info("Cargando configuracion desde: {}.", configPath);
        Map<String, Object> obj = YamlLoader.loadYaml("src\\main\\resources\\application.yml");
        Map<String, Object> paths = (Map<String, Object>) ((Map<String, Object>) obj.get("procesamientonomina"))
                .get("paths");

        String input = (String) paths.get("input");
        String validOutput = (String) paths.get("valid-output");
        String invalidOutput = (String) paths.get("invalid-output");

        filePaths = new FilePaths(input, validOutput, invalidOutput);
    }

    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    public FilePaths getFilePaths() {
        return filePaths;
    }
}
