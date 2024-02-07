package com.david.gestiontfg;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class PantallaCargaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icono = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/ucam_tfg.icns")));
        stage.getIcons().add(icono);

        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(PantallaCargaApplication.class.getResource("pantalla-carga.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Configurar la escena
        stage.setTitle("UCAM - Gestión TFG");
        stage.setScene(scene);
        stage.show();

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
