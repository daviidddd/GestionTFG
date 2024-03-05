package com.david.gestiontfg;

import com.david.gestiontfg.modelos.Alumno;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class DetalleAlumnoController {

    @FXML
    private TextField txtNIADetalle;
    @FXML
    private TextField txtIDUcamDetalle;
    @FXML
    private TextField txtCorreoDetalle;
    @FXML
    private TextField txtNombreDetalle;
    @FXML
    private TextField txtApellido1Detalle;
    @FXML
    private TextField txtApellido2Detalle;
    @FXML
    private RadioButton rdExpedienteSi;
    @FXML
    private RadioButton rdExpedienteNo;
    @FXML
    private RadioButton rdSolicitudSi;
    @FXML
    private RadioButton rdSolicitudNo;
    @FXML
    private Button btnHabilitarEdicion;
    @FXML
    private Button btnModificarAlumnoDetalle;
    @FXML
    private Label lblAlumno;

    public void initData(Alumno alumno) {
        lblAlumno.setText("/ " + alumno.getCorreo());
        estadoElementos(true);
        txtCorreoDetalle.setText(alumno.getCorreo());
        txtNombreDetalle.setText(String.valueOf(alumno.getNombre()));
        txtApellido1Detalle.setText(alumno.getApellido1());
        txtApellido1Detalle.setText(alumno.getApellido2());
        txtNIADetalle.setText(String.valueOf(alumno.getNIA()));
        txtIDUcamDetalle.setText(String.valueOf(alumno.getIdUcam()));

        if ("SI".equals(alumno.expedienteProperty().get())) {
            rdExpedienteSi.setSelected(true); // Marcar el RadioButton
            rdExpedienteNo.setSelected(false);
        } else {
            rdExpedienteSi.setSelected(false); // Desmarcar el RadioButton
            rdExpedienteNo.setSelected(true);
        }

    }

    @FXML
    private void modificarAlumno() {

    }

    @FXML
    private void eliminarAlumno() {

    }

    @FXML
    private void habilitarEdicion() {
        estadoElementos(false);
        btnModificarAlumnoDetalle.setVisible(true);
    }

    private void estadoElementos(Boolean estado){
        txtNIADetalle.setDisable(estado);
        txtIDUcamDetalle.setDisable(estado);
        txtNombreDetalle.setDisable(estado);
        txtCorreoDetalle.setDisable(estado);
        txtApellido1Detalle.setDisable(estado);
        txtApellido2Detalle.setDisable(estado);
        rdExpedienteNo.setDisable(estado);
        rdExpedienteSi.setDisable(estado);
        rdSolicitudNo.setDisable(estado);
        rdSolicitudSi.setDisable(estado);
        btnModificarAlumnoDetalle.setVisible(false);
    }



}
