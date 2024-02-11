package com.david.gestiontfg.ficheros;

import com.david.gestiontfg.logs.LogController;
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
import java.nio.file.Paths;

public class ArchivoController {

    public void procesarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        File archivoPDF = fileChooser.showOpenDialog(new Stage());

        if (archivoPDF != null) {
            try {
                // Ruta al script de Python
                String rutaScript = Paths.get("src", "main", "resources", "scripts", "expediente.py").toAbsolutePath().toString();

                // Crear el proceso con ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("python3", rutaScript, archivoPDF.getAbsolutePath());
                Process proceso = processBuilder.start();

                // Leer la salida del proceso
                BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                StringBuilder resultado = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    resultado.append(linea).append("\n");
                    System.out.println(linea);
                }

                // Esperar a que el proceso termine y obtener el código de salida
                int exitVal = proceso.waitFor();
                if (exitVal == 0) {
                    guardarEnArchivo(resultado.toString());
                    mostrarAlerta2("Expediente leído y procesado", resultado.toString());
                    LogController.registrarAccion("Alta y procesamiento expediente " + archivoPDF.getName());
                } else {
                    mostrarAlerta("Error", "Hubo un error al procesar el archivo PDF.");
                    LogController.registrarAccion("ERROR Alta y procesamiento expediente " + archivoPDF.getName());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "Hubo un error al ejecutar el script de Python.");
                LogController.registrarAccion("ERROR Ejecucion script python " + archivoPDF.getName());
            }
        }
    }

    public void guardarEnArchivo(String contenido) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt")
        );
        File archivoGuardado = fileChooser.showSaveDialog(new Stage());
        if (archivoGuardado != null) {
            try (FileWriter writer = new FileWriter(archivoGuardado)) {
                writer.write(contenido);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void mostrarAlerta2(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);

        Label mensajeLabel = new Label("Contenido leído y tratado");
        mensajeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TextArea textArea = new TextArea(mensaje);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(400, 300);

        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.getItems().addAll(mensajeLabel, scrollPane);

        // Ajustar el tamaño del SplitPane
        AnchorPane.setTopAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(30);
        gridPane.addRow(0, mensajeLabel);
        gridPane.addRow(1, scrollPane);

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
}
