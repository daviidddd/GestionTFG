package com.david.gestiontfg;

import com.david.gestiontfg.ficheros.ArchivoController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GestionarSolicitudesController {

    @FXML
    public Button btnCargarSolicitudes;

    @FXML
    protected void altaSolicitudesClick() {
        ArchivoController archivoController = new ArchivoController();
        archivoController.procesarSolicitudes();
    }

}