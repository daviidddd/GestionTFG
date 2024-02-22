package com.david.gestiontfg.config;

import java.io.*;
import java.util.Properties;

public class Configuracion {

    private static final String ARCHIVO_CONFIG = System.getProperty("user.home") + File.separator + ".config_ucam";
    private static Configuracion instancia;
    private Properties properties;

    private Configuracion() {
        cargarConfiguracion();
    }

    public static Configuracion getInstance() {
        if (instancia == null) {
            instancia = new Configuracion();
        }
        return instancia;
    }

    private void cargarConfiguracion() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(ARCHIVO_CONFIG)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            // Si hay un error al cargar el archivo de configuración, se utilizan las propiedades predeterminadas
            configurarPropiedadesPredeterminadas();
        }
    }

    private void configurarPropiedadesPredeterminadas() {
        // Configuración predeterminada
        properties.setProperty("python.path", "/opt/homebrew/bin/python3");
        // Guardar la configuración predeterminada en el archivo de configuración
        guardarConfiguracion();
    }

    private void guardarConfiguracion() {
        try (OutputStream output = new FileOutputStream(ARCHIVO_CONFIG)) {
            properties.store(output, "CONFIGURACION GESTION TFGS UCAM");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obtenerPythonPath() {
        return properties.getProperty("python.path");
    }

}

