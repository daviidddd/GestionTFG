package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.TFG;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class DetalleTFGController {
    // Componentes detalle
    @FXML
    private AnchorPane detalleTFG;
    @FXML
    private TextField txtCodigoDetalle;
    @FXML
    private TextArea txtTituloDetalle;
    @FXML
    private TextArea txtDescripcionDetalle;
    @FXML
    private ComboBox<String> cbTutorDetalle;
    @FXML
    private ComboBox<String> cbAsignaturaDetalle;
    @FXML
    private Button btnAnadirTutorDetalle;
    @FXML
    private Button btnAnadirAsignaturaDetalle;
    @FXML
    private Button btnModificarTFGDetalle;
    @FXML
    private Button btnEliminarTFGDetalle;
    @FXML
    private Button btnEliminarAsignaturas;
    @FXML
    private Button btnEliminarTutores;
    @FXML
    private Button btnHabilitarEdicion;
    @FXML
    private TextArea txtAsignaturasAsignadas;
    @FXML
    private TextArea txtTutoresAsignados;
    @FXML
    private Label lblTFGDetalle;
    private static String codigoAntiguo = null;

    // INICIALIZA LA INFORMACION DEL FORMULARIO
    public void initData(TFG tfg) {
        setCodigoAntiguo(tfg.getCodigo());
        configTextAreas();
        cargarAsignaturas();
        cargarTutores();
        estadoElementos(true);

        btnModificarTFGDetalle.setVisible(false);
        lblTFGDetalle.setText("/" + tfg.getCodigo());
        txtCodigoDetalle.setText(tfg.getCodigo() != null ? tfg.getCodigo() : "");
        txtAsignaturasAsignadas.setText(tfg.getAsignaturas() != null ? tfg.getAsignaturas() : "");
        txtTutoresAsignados.setText(tfg.getTutor() != null ? tfg.getTutor() : "");
        txtDescripcionDetalle.setText(tfg.getDescripcion() != null ? tfg.getDescripcion() : "");
        txtTituloDetalle.setText(tfg.getTitulo() != null ? tfg.getTitulo() : "");
    }

    public void setCodigoAntiguo(String codigo) {
        codigoAntiguo = codigo;
    }

    private void configTextAreas() {
        txtDescripcionDetalle.setWrapText(true);
        txtTituloDetalle.setWrapText(true);
        txtTutoresAsignados.setWrapText(true);
        txtAsignaturasAsignadas.setWrapText(true);
    }

    // CARGA LOS TUTORES DE LA BBDD EN COMBOBOX
    private void cargarTutores() {
        BDController bdController = new BDController();
        List<String> tutores = bdController.obtenerTutoresGrado();

        // Crear un ObservableList a partir de la lista de asignaturas
        ObservableList<String> items = FXCollections.observableArrayList(tutores);

        // Establecer los elementos
        cbTutorDetalle.setItems(items);

    }

    // CARGA LAS ASIGNATURAS DE LA BBDD EN COMBOBOX
    private void cargarAsignaturas() {
        BDController bdController = new BDController();
        List<String> asignaturas = bdController.obtenerAsignaturasGrado();

        // Crear un ObservableList a partir de la lista de asignaturas
        ObservableList<String> items = FXCollections.observableArrayList(asignaturas);

        // Establecer los elementos
        cbAsignaturaDetalle.setItems(items);

    }

    // CAMBIA EL ESTADO DE LOS ELEMENTOS DEPENDIENDO DE SI ESTA ACTIVA LA EDICION
    private void estadoElementos(Boolean estado) {
        txtCodigoDetalle.setDisable(estado);
        txtTituloDetalle.setDisable(estado);
        txtDescripcionDetalle.setDisable(estado);
        btnEliminarAsignaturas.setDisable(estado);
        btnEliminarTutores.setDisable(estado);
        cbAsignaturaDetalle.setDisable(estado);
        cbTutorDetalle.setDisable(estado);
        btnAnadirAsignaturaDetalle.setDisable(estado);
        btnAnadirTutorDetalle.setDisable(estado);
        txtTutoresAsignados.setDisable(estado);
        txtAsignaturasAsignadas.setDisable(estado);
    }

    // HABILITA EDICION
    @FXML
    private void habilitarEdicion() {
        estadoElementos(false);
        btnModificarTFGDetalle.setVisible(true);
    }

    // INSERTA ASIGNATURA EN TEXTO
    @FXML
    private void insertarTutor() {
        if (!cbTutorDetalle.getValue().isEmpty())
            txtTutoresAsignados.setText(txtTutoresAsignados.getText() + cbTutorDetalle.getValue() + ", ");
    }

    // INSERTA ASIGNATURA EN TEXTO
    @FXML
    private void insertarAsignatura() {
        if (!cbAsignaturaDetalle.getValue().isEmpty())
            txtAsignaturasAsignadas.setText(txtAsignaturasAsignadas.getText() + cbAsignaturaDetalle.getValue() + ", ");
    }

    // MODIFICA TFG EN BBDD
    @FXML
    private void modificarTFG() {
        try {
            String codigoNuevo = txtCodigoDetalle.getText();
            String titulo = txtTituloDetalle.getText();
            String descripcion = txtDescripcionDetalle.getText();
            String tutor = txtTutoresAsignados.getText();
            String asignaturas = txtAsignaturasAsignadas.getText();
            String tecnologias = "";

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Modificación de TFG");
            alert.setContentText("¿Está seguro de que quiere modificar " + codigoAntiguo + " ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean modificacionExitosa = bdController.modificarTFGPorCodigo(codigoNuevo, codigoAntiguo, titulo, descripcion, tutor, asignaturas, tecnologias);
                    if (modificacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle(titulo);
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

    // ELIMINA TFG EN BBDD
    @FXML
    private void eliminarTFG() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro de que quiere eliminar " + codigoAntiguo + " ?");
            alert.setContentText("Esta acción no es reversible.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BDController bdController = new BDController();
                    boolean eliminacionExitosa =  bdController.eliminarTFGPorCodigo(txtCodigoDetalle.getText());

                    if (eliminacionExitosa) {
                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle(codigoAntiguo);
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

    // BORRA EL TEXTO DE ASIGNATURAS
    @FXML
    private void limpiarAsignaturas() {
        txtAsignaturasAsignadas.setText("");
    }

    // BORRA EL TEXTO DE TUTORES
    @FXML
    private void limpiarTutores() {
        txtTutoresAsignados.setText("");
    }
}
