package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.ficheros.ArchivoController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.File;

public class GestionarAsignaturasController {
    @FXML
    private TextField txtAsignatura;

    // ALTA DE ASIGNATURA MANUAL
    @FXML
    protected void altaAsignaturaClick() {
        String asignatura = txtAsignatura.getText().trim();

        // Comprueba que no este vacia
        if (asignatura.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("Por favor, introduzca un nombre de asignatura.");
            alert.showAndWait();
        } else {
            BDController bdController = new BDController();
            if (bdController.registrarAsignatura(asignatura)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Asignatura insertada");
                alert.setHeaderText(asignatura);
                alert.setContentText("Asignatura dada de alta correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Asignatura duplicada");
                alert.setContentText("La asignatura ya existe.");
                alert.showAndWait();
            }
        }
    }

}
