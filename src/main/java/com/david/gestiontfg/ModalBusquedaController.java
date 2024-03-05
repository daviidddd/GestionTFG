package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.Solicitud;
import com.david.gestiontfg.modelos.TFG;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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
        colCodigoTFG.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colTituloTFG.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colSolicitantesTFG.setCellValueFactory(cellData -> cellData.getValue().solicitantesProperty().asObject());
        colAdjudicadoTFG.setCellValueFactory(cellData -> cellData.getValue().adjudicadoProperty().asObject());
        tbResultadoTFG.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colCodigoTFG.setStyle("-fx-alignment: CENTER;");
        colTituloTFG.setStyle("-fx-alignment: CENTER;");
        colSolicitantesTFG.setStyle("-fx-alignment: CENTER;");
        colAdjudicadoTFG.setStyle("-fx-alignment: CENTER;");

        colNIA.setCellValueFactory(cellData -> cellData.getValue().NIAProperty().asObject());
        colIDUcam.setCellValueFactory(cellData -> cellData.getValue().idUcamProperty().asObject());
        colCorreo.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colExpediente.setCellValueFactory(cellData -> cellData.getValue().expedienteProperty());
        tbResultadoAlumno.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colNIA.setStyle("-fx-alignment: CENTER;");
        colIDUcam.setStyle("-fx-alignment: CENTER;");
        colCorreo.setStyle("-fx-alignment: CENTER;");
        colExpediente.setStyle("-fx-alignment: CENTER;");

        colCorreoSolicitud.setCellValueFactory(cellData -> cellData.getValue().correoElectronicoProperty());
        colTFG1.setCellValueFactory(cellData -> cellData.getValue().tfg1Property());
        colTFG2.setCellValueFactory(cellData -> cellData.getValue().tfg2Property());
        colTFG3.setCellValueFactory(cellData -> cellData.getValue().tfg3Property());
        colTFG4.setCellValueFactory(cellData -> cellData.getValue().tfg4Property());
        colTFG5.setCellValueFactory(cellData -> cellData.getValue().tfg5Property());
        tbResultadoSolicitud.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        colCorreoSolicitud.setStyle("-fx-alignment: CENTER;");
        colTFG1.setStyle("-fx-alignment: CENTER;");
        colTFG2.setStyle("-fx-alignment: CENTER;");
        colTFG3.setStyle("-fx-alignment: CENTER;");
        colTFG4.setStyle("-fx-alignment: CENTER;");
        colTFG5.setStyle("-fx-alignment: CENTER;");

        // Elemento de click
        tbResultadoTFG.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verificar si se hizo doble clic
                TFG tfgSeleccionado = tbResultadoTFG.getSelectionModel().getSelectedItem();

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-tfg.fxml"));

                if (tfgSeleccionado != null) {
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                        DetalleTFGController controller = fxmlLoader.getController();
                        controller.initData(tfgSeleccionado);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(tfgSeleccionado.getCodigo());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        actualizarTFG(tfgSeleccionado);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        tbResultadoAlumno.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verificar si se hizo doble clic
                Alumno alumnoSeleccionado = tbResultadoAlumno.getSelectionModel().getSelectedItem();

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-alumno.fxml"));

                if (alumnoSeleccionado != null) {
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                        DetalleAlumnoController controller = fxmlLoader.getController();
                        controller.initData(alumnoSeleccionado);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(alumnoSeleccionado.getCorreo());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        actualizarAlumno(alumnoSeleccionado);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }                }
            }
        });

        tbResultadoSolicitud.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Verificar si se hizo doble clic
                Solicitud solicitudSeleccionada = tbResultadoSolicitud.getSelectionModel().getSelectedItem();

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-solicitud.fxml"));

                if (solicitudSeleccionada != null) {
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                        DetalleSolicitudController controller = fxmlLoader.getController();
                        controller.initData(solicitudSeleccionada);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(solicitudSeleccionada.getCorreoElectronico());
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();

                        actualizarSolicitud(solicitudSeleccionada);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
        String valorParseado = valorBusqueda.replace(" ", "");
        List<Solicitud> listaSolicitudes = bdController.obtenerSolicitudesFiltro(valorParseado);
        tbResultadoSolicitud.getItems().addAll(listaSolicitudes);
    }

    public void actualizarAlumno(Alumno alumnoActualizado) {
        // Find the index of the updated Alumno in the table's items
        int index = tbResultadoAlumno.getItems().indexOf(alumnoActualizado);
        if (index != -1) {
            // Update the Alumno in the table's items list
            tbResultadoAlumno.getItems().set(index, alumnoActualizado);
        }
    }

    public void actualizarSolicitud(Solicitud solicitudActualizada) {
        // Find the index of the updated Solicitud in the table's items
        int index = tbResultadoSolicitud.getItems().indexOf(solicitudActualizada);
        if (index != -1) {
            // Update the Solicitud in the table's items list
            tbResultadoSolicitud.getItems().set(index, solicitudActualizada);
        }
    }

    public void actualizarTFG(TFG tfgActualizado) {
        // Find the index of the updated TFG in the table's items
        int index = tbResultadoTFG.getItems().indexOf(tfgActualizado);
        if (index != -1) {
            // Update the TFG in the table's items list
            tbResultadoTFG.getItems().set(index, tfgActualizado);
        }
    }

    public void setValorBusqueda(String valorBusqueda) {
        this.valorBusqueda = valorBusqueda;
        cargarDatosTFGs();
        cargarDatosAlumnos();
        cargarDatosSolicitud();
    }

}
