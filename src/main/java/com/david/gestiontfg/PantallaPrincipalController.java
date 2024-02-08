package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.TFG;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PantallaPrincipalController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miInformacion;
    @FXML
    private MenuItem miCerrarSesion;
    @FXML
    private MenuItem miSalir;
    @FXML
    private MenuItem miConsultaAlumnosActivos;
    @FXML
    private MenuItem miConsultaTFGActivos;
    @FXML
    private MenuItem miConsultaPersonalizada;
    @FXML
    private MenuItem miVerPerfil;
    @FXML
    private MenuItem miModificarPerfil;
    @FXML
    private MenuItem miManualUsuario;
    @FXML
    private MenuItem miCargarAlumnos;
    @FXML
    private MenuItem miCargarTFG;

    @FXML
    private TableView tbTFGs;
    @FXML
    private TableColumn<TFG, String> colCodigoTFG;
    @FXML
    private TableColumn<TFG, String> colTituloTFG;
    @FXML
    private TableView<Alumno> tbAlumnos;
    @FXML
    private TableColumn<Alumno, Integer> colIDUcamAlumno;
    @FXML
    private TableColumn<Alumno, String> colCorreoAlumno;
    @FXML
    private TableColumn<Alumno, Integer> colNIA;
    @FXML
    private AnchorPane visualizadorPDF;
    @FXML
    private Label lblTFGEst;
    @FXML
    private Label lblAlumnosEst;
    @FXML
    private ImageView imgUcam;

    private final BDController bdController;

    public PantallaPrincipalController() {
        this.bdController = new BDController();
    }

    public void initialize() throws FileNotFoundException {
        // Configurar las columnas de las tablas
        colIDUcamAlumno.setCellValueFactory(cellData -> cellData.getValue().idUcamProperty().asObject());
        colCorreoAlumno.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colNIA.setCellValueFactory(cellData -> cellData.getValue().niaProperty().asObject());
        colCodigoTFG.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colTituloTFG.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());

        tbAlumnos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tbTFGs.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        colIDUcamAlumno.setStyle("-fx-alignment: CENTER;");
        colCorreoAlumno.setStyle("-fx-alignment: CENTER;");
        colNIA.setStyle("-fx-alignment: CENTER;");
        colCodigoTFG.setStyle("-fx-alignment: CENTER;");
        colTituloTFG.setStyle("-fx-alignment: CENTER;");

        lblAlumnosEst.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");
        lblTFGEst.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");

        // Obtener los datos de la base de datos y mostrarlos en la tabla
        cargarDatosAlumnos();
        cargarDatosTFGs();
    }

    private void cargarDatosAlumnos() {
        List<Alumno> listaAlumnos = bdController.obtenerAlumnos();
        tbAlumnos.getItems().addAll(listaAlumnos);
        lblAlumnosEst.setText(listaAlumnos.size() + " ALUMNOS");
    }

    private void cargarDatosTFGs() {
        List<TFG> listaTFG = bdController.obtenerTFGs();
        tbTFGs.getItems().addAll(listaTFG);
        lblTFGEst.setText(listaTFG.size() + " DISPONIBLES");
    }

    @FXML
    public void cargarAlumnosClick() {
        // Ventana para cargar alumnos manualmente o mediante fichero
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-alumnos.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de alumnos");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarTFGClick() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-tfgs.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de TFGs");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void cerrarSesionClick() {
        // Abrir la pantalla de inicio de sesión
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-registro.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene2 = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene2);
            newStage.setTitle("Inicio de sesión");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void salirClick() {
        Platform.exit();
    }

    /*public void LeerDocumento() {
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();

        // Configurar el controlador
        controller.setToolBarVisible(false);
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));

        // Crear un nodo Swing para integrar el visor de PDF en JavaFX
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(viewerComponentPanel);

        // Crear el contenedor de JavaFX
        AnchorPane root = new AnchorPane();
        root.getChildren().add(swingNode);

        // Anclar el nodo Swing al AnchorPane
        AnchorPane.setTopAnchor(swingNode, 0.0);
        AnchorPane.setRightAnchor(swingNode, 0.0);
        AnchorPane.setBottomAnchor(swingNode, 0.0);
        AnchorPane.setLeftAnchor(swingNode, 0.0);

        // Crear la escena y mostrarla
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Visor de PDF");
        primaryStage.show();

        // Cargar un PDF de ejemplo
        controller.openDocument("ruta/al/pdf.pdf");
    }*/

}
