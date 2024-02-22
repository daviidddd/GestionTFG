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
    @FXML
    private ImageView imgViewCarga2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Cargar las imagenes desde resources
            FileInputStream inputStream = new FileInputStream("src/main/resources/image/ucam_transparente_azul.png");
            Image image = new Image(inputStream);

            FileInputStream inputStream2 = new FileInputStream("src/main/resources/image/logo_universidad.png");
            Image image2 = new Image(inputStream2);

            // Establecer las imagenes en el ImageView
            imgViewCarga.setImage(image);
            imgViewCarga2.setImage(image2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
