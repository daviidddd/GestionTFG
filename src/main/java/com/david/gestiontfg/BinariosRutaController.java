package com.david.gestiontfg;

import com.david.gestiontfg.config.Configuracion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class BinariosRutaController {
    @FXML
    private Hyperlink hyperlinkMySQL;
    @FXML
    private Hyperlink hyperlinkPython;
    @FXML
    private TextField txtRutaPython;
    @FXML
    private TextField txtRutaMySQL;
    @FXML
    private TextArea txtPython;
    @FXML
    private TextArea txtMySQL;
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Cargar la ruta actual de Python en el TextField
        String rutaPythonActual = Configuracion.getInstance().obtenerPythonPath();
        String rutaMySQLActual = Configuracion.getInstance().obtenerMySQLPath();
        txtRutaPython.setText(rutaPythonActual);
        txtRutaMySQL.setText(rutaMySQLActual);

        String urlPython;
        String textoHipervinculoPython = "";

        String urlMySQL;
        String textoHipervinculoMySQL = "";

        String textoPython = "";
        String textoMySQL = "";

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            urlPython = "https://www.python.org/downloads/windows/";
            textoHipervinculoPython = "Descargar Python para Windows";
            textoPython = "El ejecutable de Python se suele encontrar en C:\\Users\\{nombre_de_usuario}\\AppData\\Local\\Programs\\Python\\Python{versión}";

            urlMySQL = "https://www.mysql.com/downloads/";
            textoHipervinculoMySQL = "Descargar MySQL Community para Windows";
            textoMySQL = "El ejecutable de Python se suele encontrar en C:\\Program Files\\MySQL\\MySQL Server {versión}\\bin";

        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            urlPython = "https://docs.brew.sh/Homebrew-and-Python";
            textoHipervinculoPython = "Descargar Python para macOS";
            textoPython = "Si la instalación de Python se ha realizado mediante Homebrew, el binario se encuentra localizado en /opt/homebrew/bin/python{version}";

            urlMySQL = "https://dev.mysql.com/downloads/mysql/";
            textoHipervinculoMySQL = "Descargar MySQL para macOS";
            textoMySQL = "Si la instalación de MySQL se ha realizado mediante Homebrew, el binario se encuentra localizado en /opt/homebrew/Cellar/mysql/{version}/bin/mysql";

        } else {
            urlMySQL = "";
            urlPython = "";
        }

        txtPython.setText(textoPython);
        hyperlinkPython.setText(textoHipervinculoPython);
        hyperlinkPython.setOnAction(event -> {
            abrirURL(urlPython);
        });

        txtMySQL.setText(textoMySQL);
        hyperlinkMySQL.setText(textoHipervinculoMySQL);
        hyperlinkMySQL.setOnAction(event -> {
            abrirURL(urlMySQL);
        });

        // Establecer el color de enfoque (focus color)
        txtMySQL.setStyle("-fx-focus-color: transparent;");
        txtPython.setStyle("-fx-focus-color: transparent;");


    }

    private void abrirURL(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("El sistema no admite la apertura de enlaces en el navegador.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarRutaPython() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar ejecutable de Python");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtRutaPython.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void buscarRutaMySQL() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar ejecutable de MySQL");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtRutaMySQL.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void instalarPython() {
        // Configurar el DirectoryChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Seleccionar directorio de instalación de Python");

        // Mostrar el diálogo de selección de directorio
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            // El usuario ha seleccionado un directorio de instalación
            String directorioInstalacion = selectedDirectory.getAbsolutePath();

            // Iniciar el proceso de instalación de Python3 y PyPDF2
            try {
                // Instalar Python3
                Process procesoPython = new ProcessBuilder("python3", "--version")
                        .directory(new File(directorioInstalacion))
                        .start();
                procesoPython.waitFor();

                // Instalar PyPDF2
                Process procesoPyPDF2 = new ProcessBuilder("pip3", "install", "PyPDF2")
                        .directory(new File(directorioInstalacion))
                        .start();
                procesoPyPDF2.waitFor();

                // Verificar si la instalación fue exitosa
                if (procesoPython.exitValue() == 0 && procesoPyPDF2.exitValue() == 0) {
                    System.out.println("Python3 y PyPDF2 instalados correctamente en " + directorioInstalacion);
                } else {
                    System.err.println("Error durante la instalación de Python3 o PyPDF2.");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // El usuario ha cancelado la selección del directorio
            System.out.println("Selección de directorio cancelada.");
        }
    }

    @FXML
    protected void guardarRutaPython() {
        String nuevaRutaPython = txtRutaPython.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de que desea guardar la nueva ruta de Python?");
        alert.setContentText("Nueva ruta de Python: " + nuevaRutaPython);

        // Mostrar la alerta y esperar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // El usuario hizo clic en "Aceptar", guardar la nueva ruta de Python
            Configuracion.getInstance().asignarPython(nuevaRutaPython);

            // Mostrar información de que la ruta se ha guardado
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Información");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("La nueva ruta de Python se ha guardado correctamente.");
            infoAlert.showAndWait();
        }
    }

    @FXML
    protected void guardarRutaMySQL() {
        String nuevaRutaMySQL = txtRutaMySQL.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de que desea guardar la nueva ruta de MySQL?");
        alert.setContentText("Nueva ruta de MySQL: " + nuevaRutaMySQL);

        // Mostrar la alerta y esperar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // El usuario hizo clic en "Aceptar", guardar la nueva ruta de Python
            Configuracion.getInstance().asignarMySQL(nuevaRutaMySQL);

            // Mostrar información de que la ruta se ha guardado
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Información");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("La nueva ruta de MySQL se ha guardado correctamente.");
            infoAlert.showAndWait();
        }
    }

}
