package com.david.gestiontfg.config;

import java.io.*;
import java.util.Properties;

public class Configuracion {
    private static final String ARCHIVO_CONFIG = System.getProperty("user.home") + File.separator + ".config_ucam";
    private static Configuracion instancia;
    private static Properties properties;

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
        properties.setProperty("python.path", "");
        properties.setProperty("mysql.path", "");
        properties.setProperty("configuracion_inicial", "false");
        // Guardar la configuración predeterminada en el archivo de configuración
        guardarConfiguracion();
    }

    public void asignarPython(String path) {
        properties.setProperty("python.path", path);
        guardarConfiguracion();
    }

    public void asignarMySQL(String path) {
        properties.setProperty("mysql.path", path);
        guardarConfiguracion();
    }

    public static boolean configuracionInicial() {
        if (!(properties.getProperty("mysql.path").isEmpty() || properties.getProperty("python.path").isEmpty())){
            properties.setProperty("configuracion_inicial", "true");
            guardarConfiguracion();
            return true;
        } else {
            properties.setProperty("configuracion_inicial", "false");
            guardarConfiguracion();
            return false;
        }
    }

    public static void guardarConfiguracion() {
        try (OutputStream output = new FileOutputStream(ARCHIVO_CONFIG)) {
            properties.store(output, "CONFIGURACION GESTION TFGS UCAM");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String obtenerPythonPath() {
        return properties.getProperty("python.path");
    }

    public String obtenerMySQLPath() {
        return properties.getProperty("mysql.path");
    }

}

