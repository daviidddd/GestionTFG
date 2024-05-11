package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.Solicitud;
import com.david.gestiontfg.modelos.TFG;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ModalBusquedaController {

    @FXML
    private TableView<TFG> tbResultadoTFG;
    @FXML
    private TableColumn<TFG, String> colCodigoTFG;
    @FXML
    private TableColumn<TFG, String> colTituloTFG;
    @FXML
    private TableColumn<TFG, Integer> colSolicitantesTFG;
    @FXML
    private TableColumn<TFG, Integer> colAdjudicadoTFG;
    @FXML
    private TableView<Alumno> tbResultadoAlumno;
    @FXML
    private TableColumn<Alumno, Integer> colNIA;
    @FXML
    private TableColumn<Alumno, Integer> colIDUcam;
    @FXML
    private TableColumn<Alumno, String> colCorreo;
    @FXML
    private TableColumn<Alumno, String> colExpediente;
    @FXML
    private TableColumn<Alumno, String> colSolicitud;
    @FXML
    private TableView<Solicitud> tbResultadoSolicitud;
    @FXML
    private TableColumn<Solicitud, String> colCorreoSolicitud;
    @FXML
    private TableColumn<Solicitud, Double> colNotaMedia;
    @FXML
    private TableColumn<Solicitud, Double> colCreditos;
    @FXML
    private TableColumn<Solicitud, String> colTFG1;
    @FXML
    private TableColumn<Solicitud, String> colTFG2;
    @FXML
    private TableColumn<Solicitud, String> colTFG3;
    @FXML
    private TableColumn<Solicitud, String> colTFG4;
    @FXML
    private TableColumn<Solicitud, String> colTFG5;

    private BDController bdController;
    private String valorBusqueda;
    public ModalBusquedaController() {
        this.bdController = new BDController();
    }

    @FXML
    public void initialize() {
        // Tabla TFG
        colCodigoTFG.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colTituloTFG.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colSolicitantesTFG.setCellValueFactory(cellData -> cellData.getValue().solicitantesProperty().asObject());
        colAdjudicadoTFG.setCellValueFactory(cellData -> cellData.getValue().adjudicadoProperty().asObject());
        tbResultadoTFG.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colCodigoTFG.setStyle("-fx-alignment: CENTER;");
        colTituloTFG.setStyle("-fx-alignment: CENTER;");
        colSolicitantesTFG.setStyle("-fx-alignment: CENTER;");
        colAdjudicadoTFG.setStyle("-fx-alignment: CENTER;");

        // Tabla Alumno
        colNIA.setCellValueFactory(cellData -> cellData.getValue().NIAProperty().asObject());
        colIDUcam.setCellValueFactory(cellData -> cellData.getValue().idUcamProperty().asObject());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colExpediente.setCellValueFactory(cellData -> cellData.getValue().expedienteProperty());
        tbResultadoAlumno.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colNIA.setStyle("-fx-alignment: CENTER;");
        colIDUcam.setStyle("-fx-alignment: CENTER;");
        colCorreo.setStyle("-fx-alignment: CENTER;");
        colExpediente.setStyle("-fx-alignment: CENTER;");

        // Tabla Solicitud
        colCorreoSolicitud.setCellValueFactory(cellData -> cellData.getValue().correoElectronicoProperty());
        colNotaMedia.setCellValueFactory(cellData -> cellData.getValue().notaMediaProperty().asObject());
        colCreditos.setCellValueFactory(cellData -> cellData.getValue().creditosRestantesProperty().asObject());
        colTFG1.setCellValueFactory(cellData -> cellData.getValue().tfg1Property());
        colTFG2.setCellValueFactory(cellData -> cellData.getValue().tfg2Property());
        colTFG3.setCellValueFactory(cellData -> cellData.getValue().tfg3Property());
        colTFG4.setCellValueFactory(cellData -> cellData.getValue().tfg4Property());
        colTFG5.setCellValueFactory(cellData -> cellData.getValue().tfg5Property());
        tbResultadoSolicitud.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colCorreoSolicitud.setStyle("-fx-alignment: CENTER;");
        colNotaMedia.setStyle("-fx-alignment: CENTER;");
        colCreditos.setStyle("-fx-alignment: CENTER;");
        colTFG1.setStyle("-fx-alignment: CENTER;");
        colTFG2.setStyle("-fx-alignment: CENTER;");
        colTFG3.setStyle("-fx-alignment: CENTER;");
        colTFG4.setStyle("-fx-alignment: CENTER;");
        colTFG5.setStyle("-fx-alignment: CENTER;");

        // Elemento de click
        tbResultadoTFG.setOnMouseClicked(event -> {
            TFG tfgSeleccionado = tbResultadoTFG.getSelectionModel().getSelectedItem();
            if (tfgSeleccionado != null) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Doble clic izquierdo
                    // Lógica para el doble clic izquierdo
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-tfg.fxml"));

                    try {
                        Parent root = fxmlLoader.load();
                        DetalleTFGController controller = fxmlLoader.getController();
                        controller.initData(tfgSeleccionado);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(tfgSeleccionado.getCodigo());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        tbResultadoTFG.getItems().clear();
                        cargarDatosTFGs();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) { // Clic derecho
                    // Lógica para el clic derecho
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar este TFG?");
                    alert.setTitle("Eliminar TFG");
                    alert.setHeaderText(null);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tbResultadoTFG.getItems().remove(tfgSeleccionado);
                        bdController.eliminarTFGPorCodigo(tfgSeleccionado.getCodigo());
                    }
                }
            }
        });

        tbResultadoAlumno.setOnMouseClicked(event -> {
            Alumno alumnoSeleccionado = tbResultadoAlumno.getSelectionModel().getSelectedItem();
            if (alumnoSeleccionado != null) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Doble clic izquierdo
                    // Lógica para el doble clic izquierdo
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-alumno.fxml"));

                    try {
                        Parent root = fxmlLoader.load();
                        DetalleAlumnoController controller = fxmlLoader.getController();
                        controller.initData(alumnoSeleccionado);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(alumnoSeleccionado.getCorreo());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        tbResultadoAlumno.getItems().clear();
                        cargarDatosAlumnos();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) { // Clic derecho
                    // Lógica para el clic derecho
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar este alumno?");
                    alert.setTitle("Eliminar alumno");
                    alert.setHeaderText(null);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tbResultadoAlumno.getItems().remove(alumnoSeleccionado);
                        bdController.eliminarAlumno(String.valueOf(alumnoSeleccionado.getIdUcam()));
                    }
                }
            }
        });

        tbResultadoSolicitud.setOnMouseClicked(event -> {
            Solicitud solicitudSeleccionada = tbResultadoSolicitud.getSelectionModel().getSelectedItem();
            if (solicitudSeleccionada != null) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Doble clic izquierdo
                    // Lógica para el doble clic izquierdo
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-solicitud.fxml"));

                    try {
                        Parent root = fxmlLoader.load();
                        DetalleSolicitudController controller = fxmlLoader.getController();
                        controller.initData(solicitudSeleccionada);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(solicitudSeleccionada.getCorreoElectronico());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        tbResultadoSolicitud.getItems().clear();
                        cargarDatosSolicitud();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) { // Clic derecho
                    // Lógica para el clic derecho
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea eliminar esta solicitud?");
                    alert.setTitle("Eliminar solicitud");
                    alert.setHeaderText(null);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        tbResultadoSolicitud.getItems().remove(solicitudSeleccionada);
                        bdController.eliminarSolicitudPorCorreo(solicitudSeleccionada.getCorreoElectronico());

                        // Eliminar las puntuaciones correspondientes a la solicitud
                        int NIA = bdController.obtenerNIAPorCorreo(solicitudSeleccionada.getCorreoElectronico());
                        bdController.eliminarPuntuacionPorSolicitud(NIA);

                        // Restar el numero de solicitantes en los TFGs seleccionados
                        bdController.restarSolicitanteTFG(solicitudSeleccionada.getTfg1());
                        bdController.restarSolicitanteTFG(solicitudSeleccionada.getTfg2());
                        bdController.restarSolicitanteTFG(solicitudSeleccionada.getTfg3());
                        bdController.restarSolicitanteTFG(solicitudSeleccionada.getTfg4());
                        bdController.restarSolicitanteTFG(solicitudSeleccionada.getTfg5());

                        // Borramos y recargamos la tabla de TFGS -> actualización de solicitantes
                        tbResultadoTFG.getItems().clear();
                        cargarDatosTFGs();
                    }
                }
            }
        });

    }

    public void cargarDatosTFGs() {
        List<TFG> listaTFG = bdController.obtenerTFGsFiltro(valorBusqueda);
        tbResultadoTFG.getItems().addAll(listaTFG);
    }

    public void cargarDatosAlumnos() {
        List<Alumno> listaAlumnos = bdController.obtenerAlumnosFiltro(valorBusqueda);
        tbResultadoAlumno.getItems().addAll(listaAlumnos);
    }

    public void cargarDatosSolicitud() {
        List<Solicitud> listaSolicitudes = bdController.obtenerSolicitudesFiltro(valorBusqueda);
        tbResultadoSolicitud.getItems().addAll(listaSolicitudes);
    }

    public void setValorBusqueda(String valorBusqueda) {
        this.valorBusqueda = valorBusqueda;
        cargarDatosTFGs();
        cargarDatosAlumnos();
        cargarDatosSolicitud();
    }

}
