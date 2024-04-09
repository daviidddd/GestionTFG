package com.david.gestiontfg;

import com.david.gestiontfg.modelos.TFG;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Solicitud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetalleSolicitudController {
    @FXML
    private TextField txtNotaMedia;
    @FXML
    private TextField txtCreditosRestantes;
    @FXML
    private ComboBox<String> cbDetalleTFG1;
    @FXML
    private ComboBox<String> cbDetalleTFG2;
    @FXML
    private ComboBox<String> cbDetalleTFG3;
    @FXML
    private ComboBox<String> cbDetalleTFG4;
    @FXML
    private ComboBox<String> cbDetalleTFG5;
    @FXML
    private TextField txtExpTFG1;
    @FXML
    private TextField txtExpTFG2;
    @FXML
    private TextField txtExpTFG3;
    @FXML
    private TextField txtExpTFG4;
    @FXML
    private TextField txtExpTFG5;
    @FXML
    private TextField txtAlumnoSolicitud;
    @FXML
    private Label lblSolicitud;
    @FXML
    private Button btnModificarSolicitudDetalle;
    @FXML
    private TextField txtPtoNota;
    @FXML
    private TextField txtPtoCreditos;
    @FXML
    private TextField txtPtoTFG1;
    @FXML
    private TextField txtPtoTFG2;
    @FXML
    private TextField txtPtoTFG3;
    @FXML
    private TextField txtPtoTFG4;
    @FXML
    private TextField txtPtoTFG5;
    private String correoAntiguoAlumno;

    private Set<String> elementosSeleccionados = new HashSet<>();
    public void initData(Solicitud solicitud) {
        btnModificarSolicitudDetalle.setVisible(false);
        txtAlumnoSolicitud.setDisable(true);
        txtPtoCreditos.setDisable(true);
        txtPtoNota.setDisable(true);
        txtPtoTFG1.setDisable(true);
        txtPtoTFG2.setDisable(true);
        txtPtoTFG3.setDisable(true);
        txtPtoTFG4.setDisable(true);
        txtPtoTFG5.setDisable(true);
        isEdicionInactiva(true);
        cargarTFGs();
        setValoresIniciales(solicitud);
        addComboBoxListener(cbDetalleTFG1);
        addComboBoxListener(cbDetalleTFG2);
        addComboBoxListener(cbDetalleTFG3);
        addComboBoxListener(cbDetalleTFG4);
        addComboBoxListener(cbDetalleTFG5);
    }

    private void cargarTFGs() {
        BDController bdController = new BDController();
        List<String> tfgs = bdController.obtenerTFGSCodigo();

        ObservableList<String> items = FXCollections.observableArrayList(tfgs);

        cbDetalleTFG1.setItems(items);
        cbDetalleTFG2.setItems(items);
        cbDetalleTFG3.setItems(items);
        cbDetalleTFG4.setItems(items);
        cbDetalleTFG5.setItems(items);
    }

    private void addComboBoxListener(ComboBox<String> comboBox) {
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selected = comboBox.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    elementosSeleccionados.add(selected);
                    updateComboBoxes();
                }
            }
        });
    }

    private void updateComboBoxes() {
        for (String item : elementosSeleccionados) {
            for (ComboBox<String> comboBox : getComboBoxes()) {
                if (!comboBox.equals(item)) {
                    //comboBox.getItems().;
                }
            }
        }
    }

    private void setValoresIniciales(Solicitud solicitud) {
        lblSolicitud.setText("/" + solicitud.getCorreoElectronico());
        txtAlumnoSolicitud.setText(solicitud.getCorreoElectronico());
        txtCreditosRestantes.setText(String.valueOf(solicitud.getCreditosRestantes()));
        txtNotaMedia.setText(String.valueOf(solicitud.getNotaMedia()));
        cbDetalleTFG1.setValue(solicitud.getTfg1());
        cbDetalleTFG2.setValue(solicitud.getTfg2());
        cbDetalleTFG3.setValue(solicitud.getTfg3());
        cbDetalleTFG4.setValue(solicitud.getTfg4());
        cbDetalleTFG5.setValue(solicitud.getTfg5());
        txtExpTFG1.setText(String.valueOf(solicitud.getExpTFG1()));
        txtExpTFG2.setText(String.valueOf(solicitud.getExpTFG2()));
        txtExpTFG3.setText(String.valueOf(solicitud.getExpTFG3()));
        txtExpTFG4.setText(String.valueOf(solicitud.getExpTFG4()));
        txtExpTFG5.setText(String.valueOf(solicitud.getExpTFG5()));
        correoAntiguoAlumno = solicitud.getCorreoElectronico();
        cargarPuntuacion(solicitud);
    }

    private ComboBox<String>[] getComboBoxes() {
        return new ComboBox[]{cbDetalleTFG1, cbDetalleTFG2, cbDetalleTFG3, cbDetalleTFG4, cbDetalleTFG5};
    }

    @FXML
    private void eliminarSolicitud() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quiere eliminar la solicitud " + correoAntiguoAlumno + " ?");
            alert.setContentText("Esta acción no es reversible.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean eliminacionExitosa =  bdController.eliminarSolicitudPorCorreo(correoAntiguoAlumno);

                    if (eliminacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle(correoAntiguoAlumno);
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
    private void modificarSolicitud() {
        try {
            Double notaMedia = Double.valueOf(txtNotaMedia.getText());
            Double creditosRestantes = Double.valueOf(txtCreditosRestantes.getText());
            String tfg1 = cbDetalleTFG1.getValue();
            String tfg2 = cbDetalleTFG2.getValue();
            String tfg3 = cbDetalleTFG3.getValue();
            String tfg4 = cbDetalleTFG4.getValue();
            String tfg5 = cbDetalleTFG5.getValue();
            Double expTFG1 = Double.valueOf(txtExpTFG1.getText());
            Double expTFG2 = Double.valueOf(txtExpTFG1.getText());
            Double expTFG3 = Double.valueOf(txtExpTFG1.getText());
            Double expTFG4 = Double.valueOf(txtExpTFG1.getText());
            Double expTFG5 = Double.valueOf(txtExpTFG1.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Modificación de solicitud");
            alert.setContentText("¿Esta seguro de que desea modificar la solicitud de " + correoAntiguoAlumno + " ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean modificacionExitosa = bdController.modificarSolicitudPorCodigo(notaMedia, creditosRestantes, correoAntiguoAlumno, tfg1, tfg2, tfg3, tfg4, tfg5, expTFG1, expTFG2, expTFG3, expTFG4, expTFG5);
                    if (modificacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle(txtAlumnoSolicitud.getText());
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
    private void habilitarEdicionSolicitud() {
        isEdicionInactiva(false);
        btnModificarSolicitudDetalle.setVisible(true);
    }

    private void cargarPuntuacion(Solicitud solicitud) {
        solicitud.calcularPuntuacionSimple(solicitud);
        txtPtoNota.setText(String.valueOf(solicitud.getPtosNotaMedia()));
        txtPtoCreditos.setText(String.valueOf(solicitud.getPtosCreditos()));
        txtPtoTFG1.setText(String.valueOf(solicitud.getPtosTFG1()));
        txtPtoTFG2.setText(String.valueOf(solicitud.getPtosTFG2()));
        txtPtoTFG3.setText(String.valueOf(solicitud.getPtosTFG3()));
        txtPtoTFG4.setText(String.valueOf(solicitud.getPtosTFG4()));
        txtPtoTFG5.setText(String.valueOf(solicitud.getPtosTFG5()));
    }

    private void isEdicionInactiva(Boolean estado) {
        txtNotaMedia.setDisable(estado);
        txtCreditosRestantes.setDisable(estado);
        cbDetalleTFG1.setDisable(estado);
        cbDetalleTFG2.setDisable(estado);
        cbDetalleTFG3.setDisable(estado);
        cbDetalleTFG4.setDisable(estado);
        cbDetalleTFG5.setDisable(estado);
        txtExpTFG1.setDisable(estado);
        txtExpTFG2.setDisable(estado);
        txtExpTFG3.setDisable(estado);
        txtExpTFG4.setDisable(estado);
        txtExpTFG5.setDisable(estado);
    }
}
