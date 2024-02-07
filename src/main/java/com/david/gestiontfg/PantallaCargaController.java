package com.david.gestiontfg;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class PantallaCargaController implements Initializable {

    @FXML
    private ImageView imgViewCarga;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Cargar la imagen desde el archivo
            FileInputStream inputStream = new FileInputStream("src/main/resources/image/ucam_azul.png");
            Image image = new Image(inputStream);

            // Establecer la imagen en el ImageView
            imgViewCarga.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
