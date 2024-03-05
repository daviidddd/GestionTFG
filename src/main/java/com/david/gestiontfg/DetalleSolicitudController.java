package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Solicitud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetalleSolicitudController {
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
    private TextField txtAlumnoSolicitud;
    @FXML
    private Label lblSolicitud;
    private String correoAntiguoAlumno;

    private Set<String> elementosSeleccionados = new HashSet<>();
    public void initData(Solicitud solicitud) {
        lblSolicitud.setText("/" + solicitud.getCorreoElectronico());
        txtAlumnoSolicitud.setText(solicitud.getCorreoElectronico());
        txtAlumnoSolicitud.setDisable(true);
        estadoCombo(true);
        cbDetalleTFG1.setValue(solicitud.getTfg1());
        cbDetalleTFG2.setValue(solicitud.getTfg2());
        cbDetalleTFG3.setValue(solicitud.getTfg3());
        cbDetalleTFG4.setValue(solicitud.getTfg4());
        cbDetalleTFG5.setValue(solicitud.getTfg5());
        correoAntiguoAlumno = solicitud.getCorreoElectronico();
        cargarTFGs();
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
            String correoNuevo = txtAlumnoSolicitud.getText();
            String tfg1 = cbDetalleTFG1.getValue();
            String tfg2 = cbDetalleTFG2.getValue();
            String tfg3 = cbDetalleTFG3.getValue();
            String tfg4 = cbDetalleTFG4.getValue();
            String tfg5 = cbDetalleTFG5.getValue();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Modificación de solicitud");
            alert.setContentText("¿Esta seguro de que desea modificar la solicitud de " + correoAntiguoAlumno + " ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean modificacionExitosa = bdController.modificarSolicitudPorCodigo(correoAntiguoAlumno, tfg1, tfg2, tfg3, tfg4, tfg5);
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
        estadoCombo(false);
    }

    private void estadoCombo(Boolean estado) {
        cbDetalleTFG1.setDisable(estado);
        cbDetalleTFG2.setDisable(estado);
        cbDetalleTFG3.setDisable(estado);
        cbDetalleTFG4.setDisable(estado);
        cbDetalleTFG5.setDisable(estado);
    }
}
