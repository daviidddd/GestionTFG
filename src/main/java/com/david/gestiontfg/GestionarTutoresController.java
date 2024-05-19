package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class GestionarTutoresController {
    @FXML
    private TextField txtTutor;

    // ALTA DE TUTOR MANUAL
    @FXML
    protected void altaTutorClick() {
        String asignatura = txtTutor.getText().trim();

        if (asignatura.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campo vacío");
            alert.setContentText("Por favor, introduzca un tutor de.");
            alert.showAndWait();
        } else {
            BDController bdController = new BDController();
            if (bdController.registrarTutor(asignatura)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tutor insertado");
                alert.setHeaderText(asignatura);
                alert.setContentText("Tutor dado de alta correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Tutor duplicado");
                alert.setContentText("El tutor ya existe.");
                alert.showAndWait();
            }
        }
    }

}
