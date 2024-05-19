package com.david.gestiontfg.ficheros;

import com.david.gestiontfg.configuracion.Configuracion;
import com.david.gestiontfg.logs.LogController;
import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Solicitud;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ArchivoController {

    // PROCESAR TODOS LOS EXPEDIENTES SELECCIONADOS
    public void procesarExpedientes() {
        int contador = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> archivosPDF = fileChooser.showOpenMultipleDialog(new Stage());

        if (archivosPDF != null && !archivosPDF.isEmpty()) {
            for (File archivoPDF : archivosPDF) {
                if (procesarExpedientePDF(archivoPDF))
                    contador++;
            }
            mostrarAlerta("Alta de expedientes", "Se han dado de alta " + contador + " expedientes.");
        }
    }

    // PROCESAR EXPEDIENTE EN FORMATO PDF - ASIGNATURAS, NOTAS y NIA
    private boolean procesarExpedientePDF(File archivoPDF) {
        try {
            // Obtener el NIA usando el script nia.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptNIA = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts" + File.separator + "nia.py";
            //String rutaScriptNIA = Paths.get("src", "main", "resources", "scripts", "nia.py").toAbsolutePath().toString();
            ProcessBuilder niaProcessBuilder = new ProcessBuilder();
            niaProcessBuilder.command(pythonPath, rutaScriptNIA, archivoPDF.getAbsolutePath());
            Process niaProcess = niaProcessBuilder.start();

            // Leer el resultado del script nia.py
            BufferedReader niaReader = new BufferedReader(new InputStreamReader(niaProcess.getInputStream()));
            String nia = niaReader.readLine();

            // Esperar a que el proceso termine
            int niaExitVal = niaProcess.waitFor();
            if (niaExitVal == 0) {
                if (nia != null) {
                    int numeroExpediente = Integer.parseInt(nia.trim());

                    // Ejecutar el script expediente.py
                    String rutaScriptExpediente = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts" + File.separator + "expediente.py";

                    //String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "expediente.py").toAbsolutePath().toString();
                    ProcessBuilder expedienteProcessBuilder = new ProcessBuilder();
                    expedienteProcessBuilder.command(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());
                    Process expedienteProcess = expedienteProcessBuilder.start();

                    // Leer el resultado del script expediente.py
                    BufferedReader expedienteReader = new BufferedReader(new InputStreamReader(expedienteProcess.getInputStream()));
                    StringBuilder resultado = new StringBuilder();
                    String linea;
                    while ((linea = expedienteReader.readLine()) != null) {
                        resultado.append(linea).append("\n");
                    }

                    // Esperar a que el proceso termine
                    int expedienteExitVal = expedienteProcess.waitFor();
                    if (expedienteExitVal == 0) {
                        // Guardar el resultado en un archivo con el nombre del NIA
                        guardarEnArchivo(numeroExpediente, resultado.toString());
                        String nombre = numeroExpediente + ".txt";
                        //mostrarAlerta2(nombre,"Expediente leído y procesado", resultado.toString());

                        // Actualizar alumno
                        BDController bdController = new BDController();
                        bdController.actualizarExpedienteEnBaseDeDatos(numeroExpediente, "SI");

                        LogController.registrarAccion("Alta y procesamiento expediente " + archivoPDF.getName());

                        return true;
                    } else {
                        mostrarAlerta("Error", "Hubo un error al procesar el archivo PDF.");
                        LogController.registrarAccion("ERROR Alta y procesamiento expediente " + archivoPDF.getName());
                    }
                } else {
                    mostrarAlerta("Error", "No se pudo obtener el NIA del archivo PDF.");
                    LogController.registrarAccion("ERROR Obtención de NIA fallida " + archivoPDF.getName());
                }
            } else {
                mostrarAlerta("Error", "Hubo un error al obtener el NIA del archivo PDF.");
                LogController.registrarAccion("ERROR Obtención de NIA fallida " + archivoPDF.getName());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un error al ejecutar los scripts de Python.");
            LogController.registrarAccion("ERROR Ejecucion scripts python " + archivoPDF.getName());
        }
        return false;
    }

    // PROCESAR DOCUMENTO PDF DE TFG
    public void procesarTFG() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> archivosPDF = fileChooser.showOpenMultipleDialog(new Stage());

        if (archivosPDF != null && !archivosPDF.isEmpty()) {
            for (File archivoPDF : archivosPDF) {
                purgarDirectorioTFG();
                procesarTfgPDF(archivoPDF);
                obtenerInfoTFG(archivoPDF);
                purgarTFG();
                mostrarAlerta("Alta de TFGs", "El fichero " + archivoPDF.getName() + " ha sido procesado satisfactoriamente");
            }
        }
    }

    // PROCESAR DOCUMENTO PDF DE SOLICITUDES
    public void procesarSolicitudes() {
        int contador = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> archivosPDF = fileChooser.showOpenMultipleDialog(new Stage());

        if (archivosPDF != null && !archivosPDF.isEmpty()) {
            for (File archivoPDF : archivosPDF) {
                if (procesarSolicitudPDF(archivoPDF))
                    contador++;
            }
            mostrarAlerta("Alta de solicitudes", "Se han dado de alta " + contador + " solicitudes.");
        }
    }

    // PROCESA TODA LA INFORMACION DE LA SOLICITUD + CALCULA PUNTUACION + INSERCION EN BBDD
    public boolean procesarSolicitudPDF(File archivoPDF) {
        try {
            // Definir la ruta del script tfg.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptExpediente = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts" + File.separator + "solicitud_info.py";

            //String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "solicitud_info.py").toAbsolutePath().toString();
            ProcessBuilder expedienteProcessBuilder = new ProcessBuilder();
            expedienteProcessBuilder.command(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());

            // Construir el proceso para ejecutar el script tfg.py
            ProcessBuilder tfgProcessBuilder = new ProcessBuilder(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());
            Process tfgProcess = tfgProcessBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(tfgProcess.getInputStream()));

            // Variables para almacenar los campos
            String correo = null;
            double creditosRestantes = 0;
            double mediaExpediente = 0;
            double totalMesesExperiencia = 0;
            String meritos = null;
            String tfg1 = null;
            String tfg2 = null;
            String tfg3 = null;
            String tfg4 = null;
            String tfg5 = null;
            int expTfg1 = 0;
            int expTfg2 = 0;
            int expTfg3 = 0;
            int expTfg4 = 0;
            int expTfg5 = 0;

            String linea;
            int contador = 0;
            while ((linea = reader.readLine()) != null) {
                // Parsear cada línea y almacenar los valores en las variables correspondientes
                switch (contador) {
                    case 0:
                        correo = linea;
                        break;
                    case 1:
                        creditosRestantes = Double.parseDouble(linea);
                        break;
                    case 2:
                        mediaExpediente = Double.parseDouble(linea);
                        break;
                    case 3:
                        totalMesesExperiencia = Double.parseDouble(linea);
                        break;
                    case 4:
                        meritos = linea;
                        break;
                    case 5:
                        expTfg1 = Integer.parseInt(linea);
                        break;
                    case 6:
                        expTfg2 = Integer.parseInt(linea);
                        break;
                    case 7:
                        expTfg3 = Integer.parseInt(linea);
                        break;
                    case 8:
                        expTfg4 = Integer.parseInt(linea);
                        break;
                    case 9:
                        expTfg5 = Integer.parseInt(linea);
                        break;
                    case 11:
                        tfg1 = linea;
                        break;
                    case 12:
                        tfg2 = linea;
                        break;
                    case 13:
                        tfg3 = linea;
                        break;
                    case 14:
                        tfg4 = linea;
                        break;
                    case 15:
                        tfg5 = linea;
                        break;
                }
                contador++;
            }

            // Esperar a que el proceso termine
            int exitVal = tfgProcess.waitFor();
            if (exitVal == 0) {
                BDController bdController = new BDController();
                Solicitud solicitud = new Solicitud(correo, mediaExpediente, creditosRestantes, totalMesesExperiencia, meritos, tfg1, tfg2, tfg3, tfg4, tfg5, expTfg1, expTfg2, expTfg3, expTfg4, expTfg5);
                solicitud.calcularPuntuacion(solicitud);

                boolean alta = bdController.registrarSolicitud(correo, solicitud.getNia(), mediaExpediente, creditosRestantes, totalMesesExperiencia, meritos, tfg1, tfg2, tfg3, tfg4, tfg5, expTfg1, expTfg2, expTfg3, expTfg4, expTfg5, solicitud.getPtosCreditos(), solicitud.getPtosNotaMedia(), solicitud.getPtosExperiencia(), solicitud.getPtosTFG1(), solicitud.getPtosTFG2(), solicitud.getPtosTFG3(), solicitud.getPtosTFG4(), solicitud.getPtosTFG5());
                if (alta) {

                    bdController.sumarSolicitanteTFG(solicitud.getTfg1());
                    bdController.sumarSolicitanteTFG(solicitud.getTfg2());
                    bdController.sumarSolicitanteTFG(solicitud.getTfg3());
                    bdController.sumarSolicitanteTFG(solicitud.getTfg4());
                    bdController.sumarSolicitanteTFG(solicitud.getTfg5());

                    return true;

                }
            }
            return false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    // FORMATEAR Y CREAR .txt CON LOS DATOS EN RAW DEL FICHERO ORIGINAL
    public void procesarTfgPDF(File archivoPDF) {
        try {
            // Definir la ruta del script tfg.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptExpediente = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts" + File.separator + "tfg.py";

            //String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "tfg.py").toAbsolutePath().toString();
            ProcessBuilder expedienteProcessBuilder = new ProcessBuilder();
            expedienteProcessBuilder.command(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());

            // Construir el proceso para ejecutar el script tfg.py
            ProcessBuilder tfgProcessBuilder = new ProcessBuilder(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());
            Process tfgProcess = tfgProcessBuilder.start();

            // Esperar a que el proceso termine
            int exitVal = tfgProcess.waitFor();
            if (exitVal == 0) {
                System.out.println("Proceso tfg.py completado exitosamente.");
            } else {
                System.out.println("Error al ejecutar el proceso tfg.py.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR EL RAW DE LOS TFG.TXT
    public void purgarTFG() {
        // Directorio que contiene los archivos
        String directorio = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "tfgs";

        //String directorio = "src/main/resources/tfgs";

        // Obtener una lista de archivos en el directorio
        File[] archivos = new File(directorio).listFiles();

        // Verificar si la lista de archivos no está vacía
        if (archivos != null) {
            // Iterar sobre cada archivo en el directorio
            for (File archivo : archivos) {
                // Verificar si el archivo no tiene el sufijo "_formateado.txt"
                if (!archivo.getName().endsWith("_formateado.txt")) {
                    // Eliminar el archivo
                    if (archivo.delete())
                        System.out.println("Se ha eliminado el archivo: " + archivo.getName());
                    else
                        System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        } else {
            System.out.println("El directorio está vacío o no se pudo acceder.");
        }
    }

    public void purgarDirectorioTFG() {
        // Directorio que contiene los archivos
        String directorio = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "tfgs";

        //String directorio = "src/main/resources/tfgs";

        // Obtener una lista de archivos en el directorio
        File[] archivos = new File(directorio).listFiles();

        // Verificar si la lista de archivos no está vacía
        if (archivos != null) {
            // Iterar sobre cada archivo en el directorio
            for (File archivo : archivos) {
                archivo.delete();
            }
        }
    }

    // FORMATEAR Y PREPARAR INFO DE LOS FICHEROS .txt DE LOS TFG
    public void obtenerInfoTFG(File archivoPDF) {
        try {
            // Definir la ruta del script tfg.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptExpediente = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "scripts" + File.separator + "obtener_info_tfg.py";

            //String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "obtener_info_tfg.py").toAbsolutePath().toString();
            ProcessBuilder expedienteProcessBuilder = new ProcessBuilder();
            expedienteProcessBuilder.command(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());

            // Construir el proceso para ejecutar el script tfg.py
            ProcessBuilder tfgProcessBuilder = new ProcessBuilder(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());
            Process tfgProcess = tfgProcessBuilder.start();

            // Esperar a que el proceso termine
            int exitVal = tfgProcess.waitFor();
            if (exitVal == 0) {
                System.out.println("Proceso obtener_info_tfg.py completado exitosamente.");
            } else {
                System.out.println("Error al ejecutar el proceso obtener_info_tfg.py.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // GUARDAR INFORMACION DEL EXPEDIENTE EN RESOURCES/EXPEDIENTES
    public void guardarEnArchivo(int numeroExpediente, String contenido) {
        // Directorio donde se guardarán los archivos
        //String rutaDirectorio = "src/main/resources/expedientes";
        String rutaDirectorio = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "expedientes";


        // Crear el archivo en el directorio con el nombre del número de expediente
        File archivoGuardado = new File(rutaDirectorio, numeroExpediente + ".txt");

        // Guardar el contenido en el archivo
        try (FileWriter writer = new FileWriter(archivoGuardado)) {
            writer.write(contenido);
            //System.out.println("El archivo se ha guardado correctamente en: " + archivoGuardado.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ALERTA ESTANDAR
    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // BORRA TODOS LOS ARCHIVOS DE UN DIRECTORIO
    public static void borrarArchivosEnDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
                        archivo.delete();
                    }
                }
            }
        }
    }

    // BORRA UN ARCHIVO ESPECIFICO EN UN DIRECTORIO
    public static void borrarArchivoEnDirectorio(String rutaDirectorio, String archivo) {
        File archivoAEliminar = new File(rutaDirectorio, archivo);
        // Verificar si el archivo existe
        if (archivoAEliminar.exists()) {
            // Intentar eliminar el archivo
            if (archivoAEliminar.delete()) {

            }
        }
    }

    // CUENTA LOS ARCHIVOS EN UN DIRECTORIO
    public static int contarArchivosDirectorio(String directorioPath) {
        File directorio = new File(directorioPath);
        int numArchivosTxt = 0;

        if (directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();

            if (archivos != null) {
                for (File archivo : archivos) {
                    // Verificar si el archivo termina en ".txt"
                    if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".txt")) {
                        numArchivosTxt++;
                    }
                }
            }
        }

        return numArchivosTxt;
    }

}