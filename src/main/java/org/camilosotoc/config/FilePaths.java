package org.camilosotoc.config;

import lombok.Getter;


public record FilePaths(
        String inputPath,
        String validOutputPath,
        String invalidOutputPath
) {
    public FilePaths {
        if (inputPath == null || inputPath.isBlank())
            throw new IllegalArgumentException("inputPath no puede ser nulo o vacío");
        if (validOutputPath == null || validOutputPath.isBlank())
            throw new IllegalArgumentException("validOutputPath no puede ser nulo o vacío");
        if (invalidOutputPath == null || invalidOutputPath.isBlank())
            throw new IllegalArgumentException("invalidOutputPath no puede ser nulo o vacío");

    }
}
