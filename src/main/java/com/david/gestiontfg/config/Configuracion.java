package com.david.gestiontfg.config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

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
            configurarPropiedadesPredeterminadas2();
        }
    }

    private void configurarPropiedadesPredeterminadas() {
        // Configuración predeterminada
        properties.setProperty("python.path", "");
        properties.setProperty("mysql.path", "");
        properties.setProperty("configuracion_inicial", "false");

        File carpetaScripts = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts");
        File carpetaTFG = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "tfgs");
        File carpetaExpedientes = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "expedientes");
        File carpetaBaremos = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "baremos");
        File carpetaLogs = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "logs");
        File carpetaFuentes = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "fuentes");


        // Verificar si al menos una de las carpetas no existe
        if (!carpetaScripts.exists() || !carpetaTFG.exists() || !carpetaExpedientes.exists() || !carpetaBaremos.exists() || !carpetaLogs.exists() || !carpetaFuentes.exists()) {

            // Si carpetaScripts no existe, crearla
            if (carpetaScripts.mkdirs()) {
                System.out.println("Carpeta UCAM/scripts/ creada.");
                carpetaScripts.setReadable(true);
                carpetaScripts.setWritable(true);
                // Obtener la URL del directorio de recursos "scripts"
                // Obtener la carpeta de recursos "scripts"
                InputStream recursosStream = getClass().getClassLoader().getResourceAsStream("scripts");
                if (recursosStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(recursosStream))) {
                        String fileName;
                        while ((fileName = reader.readLine()) != null) {
                            // Copiar cada archivo de la carpeta de recursos "scripts" al directorio correspondiente en el sistema de archivos del usuario
                            InputStream archivoStream = getClass().getClassLoader().getResourceAsStream("scripts/" + fileName);
                            if (archivoStream != null) {
                                Path destino = Paths.get(System.getProperty("user.home"), "GestorUCAM", "scripts", fileName);
                                Files.copy(archivoStream, destino, StandardCopyOption.REPLACE_EXISTING);
                                archivoStream.close();
                            } else {
                                System.err.println("No se pudo cargar el archivo de recursos: " + fileName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No se pudo obtener la carpeta de recursos \"scripts\".");
                }
            } else {
                System.err.println("No se pudo crear la carpeta UCAM/scripts/");
            }

            // Si carpetaBaremos no existe, crearla
            if (!carpetaBaremos.exists()) {
                if (carpetaBaremos.mkdirs()) {
                    System.out.println("Carpeta UCAM/baremos/ creada.");
                    carpetaBaremos.setReadable(true);
                    carpetaBaremos.setWritable(true);
                    // Copiar los archivos desde /resources/scripts/ a la carpeta scripts
                    try {
                        File carpetaRecursos = new File(getClass().getClassLoader().getResource("config").toURI());
                        for (File archivo : carpetaRecursos.listFiles()) {
                            File nuevoArchivo = new File(carpetaBaremos, archivo.getName());
                            Files.copy(archivo.toPath(), nuevoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No se pudo crear la carpeta UCAM/baremos/");
                }
            }

            // Si carpetaLogs no existe, crearla
            if (!carpetaLogs.exists()) {
                if (carpetaLogs.mkdirs()) {
                    System.out.println("Carpeta UCAM/logs/ creada.");
                    carpetaLogs.setReadable(true);
                    carpetaLogs.setWritable(true);
                    // Copiar los archivos desde /resources/logs/ a la carpeta scripts
                    try {
                        File carpetaRecursos = new File(getClass().getClassLoader().getResource("logs").toURI());
                        for (File archivo : carpetaRecursos.listFiles()) {
                            File nuevoArchivo = new File(carpetaLogs, archivo.getName());
                            Files.copy(archivo.toPath(), nuevoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No se pudo crear la carpeta UCAM/logs/");
                }
            }

            // Si carpetaScripts no existe, crearla
            if (!carpetaFuentes.exists()) {
                if (carpetaFuentes.mkdirs()) {
                    System.out.println("Carpeta UCAM/fuentes/ creada.");
                    carpetaFuentes.setReadable(true);
                    carpetaFuentes.setWritable(true);
                    // Copiar los archivos desde /resources/scripts/ a la carpeta scripts
                    try {
                        File carpetaRecursos = new File(getClass().getClassLoader().getResource("fonts").toURI());
                        for (File archivo : carpetaRecursos.listFiles()) {
                            File nuevoArchivo = new File(carpetaFuentes, archivo.getName());
                            Files.copy(archivo.toPath(), nuevoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (URISyntaxException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("No se pudo crear la carpeta UCAM/fuentes/");
                }
            }

            // Si carpetaTFG no existe, crearla
            if (!carpetaTFG.exists()) {
                if (carpetaTFG.mkdirs()) {
                    carpetaTFG.setReadable(true);
                    carpetaTFG.setWritable(true);
                    System.out.println("Carpeta UCAM/tfgs/ creada.");
                } else {
                    System.err.println("No se pudo crear la carpeta UCAM/tfgs/");
                }
            }

            // Si carpetaExpedientes no existe, crearla
            if(!carpetaExpedientes.exists()) {
                if (carpetaExpedientes.mkdirs()) {
                    carpetaExpedientes.setReadable(true);
                    carpetaExpedientes.setWritable(true);
                    System.out.println("Carpeta UCAM/tfgs/ creada.");
                }
            }


        } else {
            // Ambas carpetas ya existen
            System.out.println("Ambas carpetas UCAM/scripts/ y UCAM/tfgs/ ya existen.");
        }

        // Guardar la configuración predeterminada en el archivo de configuración
        guardarConfiguracion();
    }

    private void configurarPropiedadesPredeterminadas2() {
        // Configuración predeterminada
        properties.setProperty("python.path", "");
        properties.setProperty("mysql.path", "");
        properties.setProperty("configuracion_inicial", "false");

        String owner = "daviidddd"; // Nombre del propietario del repositorio
        String repo = "GestionTFG"; // Nombre del repositorio

        // Directorios a crear dentro de "GestorUCAM"
        String[] subdirectorios = {
                "scripts",
                "tfgs",
                "expedientes",
                "config",
                "logs",
                "fonts"
        };

        // Crear los subdirectorios dentro de "GestorUCAM"
        for (String subdirectorio : subdirectorios) {
            File carpeta = new File(System.getProperty("user.home"), "GestorUCAM" + File.separator + subdirectorio);

            // Verificar si la carpeta ya existe
            if (!carpeta.exists() && carpeta.mkdirs()) {
                carpeta.setReadable(true);
                carpeta.setWritable(true);
                System.out.println("Carpeta " + carpeta.getPath() + " creada.");

                // Obtener el contenido del directorio en el repositorio de GitHub
                try {
                    GitHub github = GitHub.connectAnonymously();
                    GHRepository repository = github.getRepository(owner + "/" + repo);
                    List<GHContent> directoryContents = repository.getDirectoryContent("src/main/resources/" + subdirectorio);

                    // Descargar archivos del directorio en el repositorio y guardarlos localmente
                    for (GHContent content : directoryContents) {
                        String fileName = content.getName();
                        File localFile = new File(carpeta, fileName);

                        try (InputStream inputStream = content.read();
                             FileOutputStream outputStream = new FileOutputStream(localFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            System.out.println("Archivo descargado: " + fileName);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error al descargar archivos desde GitHub: " + e.getMessage());
                }
            } else {
                System.err.println("No se pudo crear la carpeta " + carpeta.getPath());
            }
        }

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

