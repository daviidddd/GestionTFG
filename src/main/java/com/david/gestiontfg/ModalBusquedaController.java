package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.Solicitud;
import com.david.gestiontfg.modelos.TFG;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    public void setValorBusqueda(String valorBusqueda) {
        this.valorBusqueda = valorBusqueda;
        cargarDatosTFGs();
        cargarDatosAlumnos();
        cargarDatosSolicitud();
    }

}
