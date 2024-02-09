package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.logs.LogController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.TFG;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PantallaPrincipalController {
    private static final long INTERVALO_ACTUALIZACION = 5000; // 50 segundos

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
    private TableView<TFG> tbTFGs;
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
    private Label lblTFGActivos;
    @FXML
    private Label lblAlumnosEst;
    @FXML
    private ImageView imgUcam;
    @FXML
    private ProgressBar ratioDisponiblesOcupados;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private Button btnBusqueda;

    private Timer timer;
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
        lblTFGActivos.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");

        /*cargarDatosAlumnos();
        cargarDatosTFGs();*/
        cargarProgressBar();
        timer = new Timer();
        // Realizar una consulta inicial al iniciar la aplicación
        actualizarTablas();
        // Programar consultas periódicas cada X segundos
        programarActualizacionesPeriodicas();
    }

    private void programarActualizacionesPeriodicas() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> actualizarTablas());
            }
        }, 0, 5000); // Consultar cada 5 segundos (ajusta este valor según tus necesidades)
    }

    private void actualizarTablas() {
        // Actualizar la tabla de alumnos
        tbAlumnos.getItems().clear();
        tbAlumnos.getItems().addAll(bdController.obtenerAlumnos());
        List<Alumno> listaAlumnos = bdController.obtenerAlumnos();
        lblAlumnosEst.setText(listaAlumnos.size() + " ALUMNOS ACTIVOS");

        // Actualizar la tabla de TFGs
        tbTFGs.getItems().clear();
        tbTFGs.getItems().addAll(bdController.obtenerTFGs());
        List<TFG> listaTFG = bdController.obtenerTFGs();
        lblTFGActivos.setText(listaTFG + " DISPONIBLES");
    }

    public void cargarDatosAlumnos() {
        List<Alumno> listaAlumnos = bdController.obtenerAlumnos();
        tbAlumnos.getItems().addAll(listaAlumnos);
        lblAlumnosEst.setText(listaAlumnos.size() + " ALUMNOS ACTIVOS");
    }

    public void cargarDatosTFGs() {
        List<TFG> listaTFG = bdController.obtenerTFGs();
        tbTFGs.getItems().addAll(listaTFG);
        lblTFGActivos.setText(listaTFG + " DISPONIBLES");
    }

    public void limpiarTablaAlumnos() {
        tbAlumnos.getItems().clear();
    }

    public void limpiarTablaTFGs() {
        tbTFGs.getItems().clear();
    }

    private void cargarProgressBar() {
        int contadorAdjudicados = bdController.obtenerTFGAdjudicado();
        int contadorTotal = bdController.obtenerTFGs().size();
        double ratio = (double) contadorAdjudicados / contadorTotal;

        ratioDisponiblesOcupados.setProgress(ratio);
        if(ratio == 1.0)
            ratioDisponiblesOcupados.setStyle("-fx-accent: green;");
        else
            ratioDisponiblesOcupados.setStyle("-fx-accent: #004379;");
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
            LogController.registrarAccion("Cierre de sesión");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void salirClick() {
        Platform.exit();
        LogController.registrarAccion("Cierre aplicación");
    }

    @FXML
    protected void busquedaClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modal-busqueda.fxml"));
        try {
            Region content = loader.load();
            // Crear un diálogo modal
            Dialog<Void> dialog = new Dialog<>();
            dialog.getDialogPane().setContent(content);
            // Configurar el título del diálogo
            dialog.setTitle("Resultado de la búsqueda");
            // Mostrar el diálogo
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
