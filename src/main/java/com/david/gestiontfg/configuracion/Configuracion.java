package com.david.gestiontfg.configuracion;

import java.io.*;
import java.nio.file.Files;
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

        // Verificar si el directorio GestorUCAM existe
        File gestorUCAMDir = new File(System.getProperty("user.home"), "GestorUCAM");
        if (!gestorUCAMDir.exists()) {
            // Si el directorio GestorUCAM no existe, crearlo con las propiedades predeterminadas
            String[] directories = {"scripts", "config", "fonts", "logs"};
            crearDirectorioHome(directories);
        }
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
                    }
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
            }
        }

        // Si carpetaExpedientes no existe, crearla
        if(!carpetaExpedientes.exists()) {
            if (carpetaExpedientes.mkdirs()) {
                carpetaExpedientes.setReadable(true);
                carpetaExpedientes.setWritable(true);
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
                    } else {
                        // Para otros archivos, leer el contenido y copiar como antes
                        InputStream inputStream = content.read();
                        if (inputStream != null) {
                            File localFile = new File(targetDir, fileName);
                            Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

