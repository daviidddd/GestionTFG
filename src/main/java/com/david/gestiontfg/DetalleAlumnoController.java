package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Alumno;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;

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
    private static int idUcamSeleccionado = 0;

    public void initData(Alumno alumno) {
        lblAlumno.setText("/ " + alumno.getCorreo());
        estadoElementos(true);
        txtCorreoDetalle.setText(alumno.getCorreo());
        txtNombreDetalle.setText(String.valueOf(alumno.getNombre()));
        txtApellido1Detalle.setText(alumno.getApellido1());
        txtApellido2Detalle.setText(alumno.getApellido2());
        txtNIADetalle.setText(String.valueOf(alumno.getNIA()));
        txtIDUcamDetalle.setText(String.valueOf(alumno.getIdUcam()));

        if ("SI".equals(alumno.expedienteProperty().get())) {
            rdExpedienteSi.setSelected(true); // Marcar el RadioButton
            rdExpedienteNo.setSelected(false);
        } else {
            rdExpedienteSi.setSelected(false); // Desmarcar el RadioButton
            rdExpedienteNo.setSelected(true);
        }

        idUcamSeleccionado = alumno.getIdUcam();
    }

    @FXML
    private void modificarAlumno() {
        try {
            BDController bdController = new BDController();
            File carpetaExpedientes = new File(System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "expedientes");

            String expedienteAlumnoExiste = null;
            int idUcam = Integer.parseInt(txtIDUcamDetalle.getText());
            int NIA = Integer.parseInt(txtNIADetalle.getText());
            String correo = txtCorreoDetalle.getText();
            String nombre = txtNombreDetalle.getText();
            String apellido1 = txtApellido1Detalle.getText();
            String apellido2 = txtApellido2Detalle.getText();
            //boolean solicitud = bdController.solicitudAlumno();
            boolean expediente = false;


            if (carpetaExpedientes.exists() && carpetaExpedientes.isDirectory()) {
                File[] archivos = carpetaExpedientes.listFiles();
                if (archivos != null) {
                    for (File archivo : archivos) {
                        if (archivo.isFile() && archivo.getName().equals(NIA + ".txt")) {
                            expediente = true;
                            rdExpedienteSi.setSelected(true);
                            rdExpedienteNo.setSelected(false);
                            rdExpedienteNo.setDisable(true);
                            expedienteAlumnoExiste = "SI";
                        }
                    }
                } else {
                    rdExpedienteSi.setSelected(false);
                    rdExpedienteNo.setSelected(true);
                    rdExpedienteSi.setDisable(true);
                    expedienteAlumnoExiste = "NO";
                }
            } else {
                rdExpedienteSi.setSelected(false);
                rdExpedienteNo.setSelected(true);
                rdExpedienteSi.setDisable(true);
                expedienteAlumnoExiste = "NO";
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Modificación de alumno");
            alert.setContentText("¿Está seguro de que quiere modificar " + idUcamSeleccionado + " ?");
            String finalExpedienteAlumnoExiste = expedienteAlumnoExiste;
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean modificacionExitosa = bdController.modificarAlumnoPorCodigo(idUcam, idUcamSeleccionado, NIA, nombre, correo, apellido1, apellido2, finalExpedienteAlumnoExiste);
                    if (modificacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle("Alumno modificado");
                        alerta.setHeaderText(null);
                        alerta.setContentText("Modificación exitosa");
                        alerta.showAndWait();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarAlumno() {
        String alumno = txtIDUcamDetalle.getText();

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quiere eliminar " + alumno + " ?");
            alert.setContentText("Esta acción no es reversible.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean eliminacionExitosa =  bdController.eliminarAlumno(alumno);

                    if (eliminacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle(alumno);
                        alerta.setHeaderText(null);
                        alerta.setContentText("Eliminación exitosa");
                        alerta.showAndWait();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
