package org.camilosotoc.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilePathsTest {

    @Test
    void testValidFilePaths() {
        FilePaths paths = new FilePaths(
                "src/input.csv",
                "src/valid.csv",
                "src/invalid.csv");

        assertEquals("src/input.csv", paths.inputPath());
        assertEquals("src/valid.csv", paths.validOutputPath());
        assertEquals("src/invalid.csv", paths.invalidOutputPath());
    }

    @Test
    void testInputPathNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths(null, "valid.csv", "invalid.csv"));
        assertEquals("inputPath no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void testValidOutputPathNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths("input.csv", null, "invalid.csv"));
        assertEquals("validOutputPath no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void testInvalidOutputPathNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths("input.csv", "valid.csv", null));
        assertEquals("invalidOutputPath no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void testInputPathBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths("   ", "valid.csv", "invalid.csv"));
        assertEquals("inputPath no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void testValidOutputPathBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths("input.csv", "  ", "invalid.csv"));
        assertEquals("validOutputPath no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    void testInvalidOutputPathBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new FilePaths("input.csv", "valid.csv", ""));
        assertEquals("invalidOutputPath no puede ser nulo o vacío", ex.getMessage());
    }
}
