package com.david.gestiontfg.logs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogController {
    private static final String LOG_FILE_PATH = "src/main/resources/logs/activity_log.txt";

    public static void registrarAccion(String accion) {
        Path logFilePath = Paths.get(LOG_FILE_PATH);
        crearDirectorioSiNoExiste(logFilePath.getParent());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath.toFile(), true))) {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            writer.write("[" + timeStamp + " Acción: " + accion + "\n");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }

    // Método para limpiar el archivo de registro
    public static void limpiarRegistro() {
        Path logFilePath = Paths.get(LOG_FILE_PATH);

        try {
            Files.deleteIfExists(logFilePath);
        } catch (IOException e) {
            System.err.println("Error al limpiar el archivo de registro: " + e.getMessage());
        }
    }

    private static void crearDirectorioSiNoExiste(Path directorio) {
        try {
            Files.createDirectories(directorio);
        } catch (IOException e) {
            System.err.println("Error al crear el directorio: " + e.getMessage());
        }
    }
}
