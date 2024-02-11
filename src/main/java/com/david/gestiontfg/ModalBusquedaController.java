package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.TFG;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Timer;

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
    private BDController bdController;
    private String valorBusqueda;

    public ModalBusquedaController() {
        this.bdController = new BDController();
    }

    @FXML
    public void initialize() {
        // Configurar las columnas de las tablas
        colCodigoTFG.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colTituloTFG.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colSolicitantesTFG.setCellValueFactory(cellData -> cellData.getValue().solicitantesProperty().asObject());
        colAdjudicadoTFG.setCellValueFactory(cellData -> cellData.getValue().adjudicadoProperty().asObject());

        tbResultadoTFG.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        colCodigoTFG.setStyle("-fx-alignment: CENTER;");
        colTituloTFG.setStyle("-fx-alignment: CENTER;");
        colSolicitantesTFG.setStyle("-fx-alignment: CENTER;");
        colAdjudicadoTFG.setStyle("-fx-alignment: CENTER;");

        cargarDatosTFGs();
    }

    public void cargarDatosTFGs() {
        System.out.println(valorBusqueda);
        List<TFG> listaTFG = bdController.obtenerTFGsFiltro(valorBusqueda);
        tbResultadoTFG.getItems().addAll(listaTFG);
    }

}
