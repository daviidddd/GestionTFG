package com.david.gestiontfg.ficheros;

import com.david.gestiontfg.config.Configuracion;
import com.david.gestiontfg.logs.LogController;
import com.david.gestiontfg.bbdd.BDController;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ArchivoController {

    // PROCESAR TODOS LOS EXPEDIENTES SELECCIONADOS
    public void procesarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> archivosPDF = fileChooser.showOpenMultipleDialog(new Stage());

        if (archivosPDF != null && !archivosPDF.isEmpty()) {
            for (File archivoPDF : archivosPDF) {
                procesarArchivoPDF(archivoPDF);
            }
        }
    }

    // PROCESAR EXPEDIENTE EN FORMATO PDF - ASIGNATURAS, NOTAS y NIA
    private void procesarArchivoPDF(File archivoPDF) {
        try {
            // Obtener el NIA usando el script nia.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptNIA = Paths.get("src", "main", "resources", "scripts", "nia.py").toAbsolutePath().toString();
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
                    String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "expediente.py").toAbsolutePath().toString();
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
                        mostrarAlerta2(nombre,"Expediente leído y procesado", resultado.toString());
                        LogController.registrarAccion("Alta y procesamiento expediente " + archivoPDF.getName());
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
    }

    // PROCESAR DOCUMENTO PDF DE TFGs
    public void procesarTFGs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        List<File> archivosPDF = fileChooser.showOpenMultipleDialog(new Stage());

        if (archivosPDF != null && !archivosPDF.isEmpty()) {
            for (File archivoPDF : archivosPDF) {
                procesarTfgPDF(archivoPDF);
                obtenerInfoTFG(archivoPDF);
                purgarTFG();
                mostrarAlerta("Alta de TFGs", "El fichero " + archivoPDF.getName() + " ha sido procesado satisfactoriamente");
            }
        }
    }

    // FORMATEAR Y CREAR .txt CON LOS DATOS EN RAW DEL FICHERO ORIGINAL
    public void procesarTfgPDF(File archivoPDF) {
        try {
            // Definir la ruta del script tfg.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "tfg.py").toAbsolutePath().toString();
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
        String directorio = "src/main/resources/tfgs";

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

    // FORMATEAR Y PREPARAR INFO DE LOS FICHEROS RAW .txt DE LOS TFG
    public void obtenerInfoTFG(File archivoPDF) {
        try {
            // Definir la ruta del script tfg.py
            Configuracion configuracion = Configuracion.getInstance();
            String pythonPath = configuracion.obtenerPythonPath();

            String rutaScriptExpediente = Paths.get("src", "main", "resources", "scripts", "obtener_info_tfg.py").toAbsolutePath().toString();
            ProcessBuilder expedienteProcessBuilder = new ProcessBuilder();
            expedienteProcessBuilder.command(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());

            // Construir el proceso para ejecutar el script tfg.py
            ProcessBuilder tfgProcessBuilder = new ProcessBuilder(pythonPath, rutaScriptExpediente, archivoPDF.getAbsolutePath());
            Process tfgProcess = tfgProcessBuilder.start();

            // Esperar a que el proceso termine
            int exitVal = tfgProcess.waitFor();
            if (exitVal == 0) {
                //System.out.println("Proceso obtener_info_tfg.py completado exitosamente.");
            } else {
                //System.out.println("Error al ejecutar el proceso obtener_info_tfg.py.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // OBSERVAR LOS CAMBIOS (ALTAS/BAJAS) DE LOS EXPEDIENTES EN RESOURCES/EXPEDIENTES
    public void observarDirectorio() {
        BDController bdController = new BDController();

        // Ruta al directorio de expedientes
        Path directorioExpedientes = Paths.get("src", "main", "resources", "expedientes");

        // Obtener la lista de archivos en el directorio
        File[] archivos = directorioExpedientes.toFile().listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().endsWith(".txt")) {
                    // Obtener el NIA del nombre del archivo
                    String nombreArchivo = archivo.getName();
                    int nia = Integer.parseInt(nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.')));

                    // Verificar si el NIA existe en la base de datos
                    if (bdController.existeNIAEnBaseDeDatos(nia))
                        // Actualizar el campo expediente en la base de datos
                        bdController.actualizarExpedienteEnBaseDeDatos(nia);

                }
            }
        }
    }

    // GUARDAR INFORMACION DEL EXPEDIENTE EN RESOURCES/EXPEDIENTES
    public void guardarEnArchivo(int numeroExpediente, String contenido) {
        // Directorio donde se guardarán los archivos
        String rutaDirectorio = "src/main/resources/expedientes";

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

    // ALERTA DE EXPEDIENTE LEIDO CORRECTAMENTE
    public void mostrarAlerta2(String nombre, String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        Label cabecera = new Label("Contenido leído y tratado:" + nombre);
        cabecera.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label ruta = new Label("src/main/resources/expedientes/" + nombre);
        ruta.setStyle("-fx-font-size: 13;");

        TextArea textArea = new TextArea(mensaje);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(400, 300);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(cabecera, scrollPane);

        // Ajustar el tamaño del SplitPane
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(30);
        gridPane.addRow(0, cabecera);
        gridPane.addRow(1, ruta);
        gridPane.addRow(2, scrollPane);

        AnchorPane anchorPane = new AnchorPane(gridPane);
        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);

        alerta.getDialogPane().setContent(anchorPane);

        Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        alerta.showAndWait();
    }

    public static void borrarArchivosEnDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        if (directorio.exists() && directorio.isDirectory()) {
            File[] archivos = directorio.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile()) {
                        archivo.delete();
                    }
                }
            } else {
                //System.out.println("El directorio está vacío.");
            }
        } else {
            //System.out.println("El directorio no existe o no es válido.");
        }
    }

}
