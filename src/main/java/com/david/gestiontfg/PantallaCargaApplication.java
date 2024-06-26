package com.david.gestiontfg;

import com.david.gestiontfg.configuracion.Configuracion;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PantallaCargaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(PantallaCargaApplication.class.getResource("pantalla-carga.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Configurar la escena
        stage.setTitle("UCAM - Gestión TFG");
        stage.setScene(scene);
        stage.show();

        // Transición de Xs
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(event -> {
            try {
                // Cargar la segunda vista (login-registro.fxml)
                FXMLLoader secondLoader = new FXMLLoader(PantallaCargaApplication.class.getResource("login-registro.fxml"));
                Scene secondScene = new Scene(secondLoader.load(), 600, 400);

                // Obtener la referencia al escenario actual y establecer la segunda escena
                Stage currentStage = (Stage) stage.getScene().getWindow();
                currentStage.setScene(secondScene);
                currentStage.setTitle("Bienvenido");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pauseTransition.play();

        stage.setOnCloseRequest(event -> {
            // Realiza las acciones necesarias para cerrar la aplicación
            Platform.exit();
        });
    }

    // METODO PRINCIPAL
    public static void main(String[] args) {
        Configuracion configuracion = Configuracion.getInstance();
        String pythonPath = configuracion.obtenerPythonPath();
        launch(args);
    }
}
