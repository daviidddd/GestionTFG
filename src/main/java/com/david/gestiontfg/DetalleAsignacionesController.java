package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import javafx.fxml.FXML;

public class DetalleAsignacionesController {


    @FXML
    protected void asignacionAutomatica() {
        BDController bdController = new BDController();
        bdController.asignacionTFGAutomatica();
    }

    @FXML
    protected void asignacionManual() {

    }

}
