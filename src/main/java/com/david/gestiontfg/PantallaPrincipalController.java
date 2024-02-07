package com.david.gestiontfg;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaPrincipalController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miInformacion;
    @FXML
    private MenuItem miCerrarSesion;
    @FXML
    private MenuItem miSalir;
    @FXML
    private MenuItem miConsultaAlumnosActivos;
    @FXML
    private MenuItem miConsultaTFGActivos;
    @FXML
    private MenuItem miConsultaPersonalizada;
    @FXML
    private MenuItem miVerPerfil;
    @FXML
    private MenuItem miModificarPerfil;
    @FXML
    private MenuItem miManualUsuario;
    @FXML
    private MenuItem miCargarAlumnos;
    @FXML
    private MenuItem miCargarTFG;

    @FXML
    public void cargarAlumnosClick() {
        // Ventana para cargar alumnos manualmente o mediante fichero
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-alumnos.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de alumnos");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarTFGClick() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-tfgs.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de TFGs");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void cerrarSesionClick() {
        // Abrir la pantalla de inicio de sesión
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pantalla-inicio-sesion.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene2 = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene2);
            newStage.setTitle("Inicio de sesión");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void salirClick() {
        Platform.exit();
    }

}
