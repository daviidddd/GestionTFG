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

import org.apache.commons.io.FileUtils;
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

        // Verificar si el directorio GestorUCAM existe
        File gestorUCAMDir = new File(System.getProperty("user.home"), "GestorUCAM");
        if (!gestorUCAMDir.exists()) {
            // Si el directorio GestorUCAM no existe, crearlo con las propiedades predeterminadas
            String[] directories = {"scripts", "config", "fonts", "logs"};
            crearDirectorioHome(directories);
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
        // Guardar la configuración predeterminada en el archivo de configuración
        guardarConfiguracion();
    }

    public void crearDirectorioHome(String[] directories) {
        String owner = "daviidddd"; // Nombre del propietario del repositorio
        String repo = "GestionTFG"; // Nombre del repositorio
        String basePath = System.getProperty("user.home") + File.separator + "GestorUCAM";
        File baseDir = new File(basePath);

        File carpetaTFG = new File(basePath +  File.separator + "tfgs");
        File carpetaExpedientes = new File(baseDir + File.separator + "expedientes");

        try {
            GitHub github = GitHub.connectAnonymously();

            for (String directory : directories) {
                File targetDir = new File(baseDir, directory);

                if (!targetDir.exists()) {
                    if (targetDir.mkdirs()) {
                        System.out.println("Carpeta " + targetDir.getPath() + " creada.");

                        // Obtener los contenidos del directorio en el repositorio
                        GHRepository repository = github.getRepository(owner + "/" + repo);
                        List<GHContent> directoryContents = repository.getDirectoryContent("src/main/resources/" + directory);

                        System.out.println(directoryContents.size());

                        // Descargar los archivos en un hilo separado
                        Thread downloadThread = new Thread(() -> descargarArchivos(directoryContents, targetDir));
                        downloadThread.start();
                    } else {
                        System.err.println("No se pudo crear la carpeta " + targetDir.getPath());
                    }
                } else {
                    System.out.println("Carpeta " + targetDir.getPath() + " ya existe.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    }

    private void descargarArchivos(List<GHContent> directoryContents, File targetDir) {
        for (GHContent content : directoryContents) {
            String fileName = content.getName();
            if (!fileName.startsWith("__")) { // Evitar archivos que comienzan con "__"
                System.out.println(fileName);
                try {
                    if (fileName.toLowerCase().endsWith(".ttf")) {
                        // Para archivos .ttf, copiar directamente sin leer su contenido
                        File localFile = new File(targetDir, fileName);
                        Files.copy(content.read(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Archivo descargado: " + fileName);
                    } else {
                        // Para otros archivos, leer el contenido y copiar como antes
                        InputStream inputStream = content.read();
                        if (inputStream != null) {
                            File localFile = new File(targetDir, fileName);
                            Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Archivo descargado: " + fileName);
                        } else {
                            System.err.println("El contenido de " + fileName + " es nulo.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

