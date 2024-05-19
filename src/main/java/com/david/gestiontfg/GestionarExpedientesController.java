package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.ficheros.ArchivoController;
import javafx.fxml.FXML;

public class GestionarExpedientesController {
    private BDController bdController;

    public GestionarExpedientesController() {
        this.bdController = new BDController();
    }

    // ALTA DE EXPEDIENTE MEDIANTE DOCUMENTO PDF
    @FXML
    protected void altaExpedienteClick() {
        ArchivoController archivoController = new ArchivoController();
        archivoController.procesarExpedientes();
    }

}
