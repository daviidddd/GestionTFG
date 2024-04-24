package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.config.Configuracion;
import com.david.gestiontfg.ficheros.ArchivoController;
import com.david.gestiontfg.logs.LogController;
import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.TFG;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Document;

import java.io.*;
import java.lang.annotation.Documented;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PantallaPrincipalController {
    @FXML
    private AnchorPane panePrincipal;
    @FXML
    private MenuItem miCargarAlumnos;
    @FXML
    private MenuItem miCargarTFGS;
    @FXML
    private MenuItem miCargarExpedientes;
    @FXML
    private MenuItem miCargarSolicitudes;
    @FXML
    private MenuItem miAltaTFG;
    @FXML
    private MenuItem miAltaAlumnos;
    @FXML
    private MenuItem miAltaSolicitudes;
    @FXML
    private MenuItem miAltaExpedientes;
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
    private TableColumn<Alumno, String> colExp;
    @FXML
    private Label lblTFGActivos;
    @FXML
    private Label lblAlumnosActivos;
    @FXML
    private Label lblExpedientesActivos;
    @FXML
    private ProgressBar ratioDisponiblesOcupados;
    @FXML
    private ProgressBar ratioExpedientes;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private Label lblProgresoTFG;
    @FXML
    private Label lblProgresoExp;
    @FXML
    private ImageView imgWarning;
    @FXML
    private Button btnCargarConfiguracion;
    private Timer timer;
    private final BDController bdController;
    private final ArchivoController archivoController;
    private Stage stage;

    public PantallaPrincipalController() {
        this.bdController = new BDController();
        this.archivoController = new ArchivoController();
    }

    public void initialize() throws FileNotFoundException {
        // Configurar las columnas de las tablas
        colIDUcamAlumno.setCellValueFactory(cellData -> cellData.getValue().idUcamProperty().asObject());
        colCorreoAlumno.setCellValueFactory(cellData -> cellData.getValue().correoProperty());
        colNIA.setCellValueFactory(cellData -> cellData.getValue().NIAProperty().asObject());
        colCodigoTFG.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        colTituloTFG.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colExp.setCellValueFactory(cellData -> cellData.getValue().expedienteProperty());

        tbAlumnos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tbTFGs.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        colIDUcamAlumno.setStyle("-fx-alignment: CENTER;");
        colCorreoAlumno.setStyle("-fx-alignment: CENTER;");
        colNIA.setStyle("-fx-alignment: CENTER;");
        colCodigoTFG.setStyle("-fx-alignment: CENTER;");
        colTituloTFG.setStyle("-fx-alignment: CENTER;");
        colExp.setStyle("-fx-alignment: CENTER;");

        if (!Configuracion.configuracionInicial()) {
            configuracionInicial(true);
        } else {
            configuracionInicial(false);
        }

        /*lblAlumnosActivos.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");
        lblTFGActivos.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");
        lblExpedientesActivos.setStyle("-fx-font-weight: bold; -fx-font-style: italic;");*/

        timer = new Timer();

        // Poblar tablas con los registros de la BBDD
        cargarExpedientes();
        cargarTablaAlumnos();
        cargarTablaTFG();
    }

    private void cargarTablaAlumnos() {
        tbAlumnos.getItems().clear();
        tbAlumnos.getItems().addAll(bdController.obtenerAlumnos());
        List<Alumno> listaAlumnos = bdController.obtenerAlumnos();
        lblAlumnosActivos.setText(listaAlumnos.size() + " ALUMNOS ACTIVOS");

        cargarProgressBar();
    }

    private void cargarTablaTFG() {
        tbTFGs.getItems().clear();
        tbTFGs.getItems().addAll(bdController.obtenerTFGs());
        List<TFG> listaTFG = bdController.obtenerTFGs();

        if(listaTFG.isEmpty())
            lblTFGActivos.setText("0 TFG DISPONIBLES");

        cargarProgressBar();
    }

    private void cargarExpedientes() {
        int expedientes = bdController.obtenerExpedientes();
        lblExpedientesActivos.setText(expedientes + " EXPEDIENTES DISPONIBLES");

        cargarProgressBar();
    }

    private void configuracionInicial(Boolean estado) throws FileNotFoundException {
        InputStream inputStream = getClass().getResourceAsStream("/image/warning.png");
        Image image = new Image(inputStream);
        imgWarning.setImage(image);
        imgWarning.setVisible(estado);
        btnCargarConfiguracion.setVisible(estado);

        // Si no esta configurado el sistema no se pueden cargar elementos
        miCargarAlumnos.setDisable(estado);
        miCargarTFGS.setDisable(estado);
        miCargarExpedientes.setDisable(estado);
        miCargarSolicitudes.setDisable(estado);
        miAltaAlumnos.setDisable(estado);
        miAltaTFG.setDisable(estado);
        miAltaExpedientes.setDisable(estado);
        miAltaSolicitudes.setDisable(estado);
    }

    private void cargarProgressBar() {
        int contadorAdjudicados = bdController.obtenerTFGAdjudicado();
        int contadorTotal = bdController.obtenerTFGs().size();
        double ratioTFG = (double) contadorAdjudicados / contadorTotal;

        int contadorSi = bdController.obtenerExpedientes();
        int contadorNo = bdController.obtenerAlumnosTam();
        double ratioExpedientes = (double) contadorSi / contadorNo;

        String progresoExp = String.format("%.2f%%", ratioExpedientes * 100);
        String progresoTFG = String.format("%.2f%%", ratioTFG * 100);

        lblProgresoExp.setText(progresoExp);
        lblProgresoTFG.setText(progresoTFG);

        ratioDisponiblesOcupados.setProgress(ratioTFG);
        if(ratioTFG == 1.0)
            ratioDisponiblesOcupados.setStyle("-fx-accent: green;");
        else
            ratioDisponiblesOcupados.setStyle("-fx-accent: #004379;");

        this.ratioExpedientes.setProgress(ratioExpedientes);
        if(ratioExpedientes == 1.0)
            this.ratioExpedientes.setStyle("-fx-accent: green;");
        else
            this.ratioExpedientes.setStyle("-fx-accent: #004379;");
    }

    @FXML
    public void cargarAlumnosClick() {
        // Ventana para cargar alumnos manualmente o mediante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-alumnos.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de alumnos");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                cargarTablaAlumnos();
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarTFGClick() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-tfgs.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de TFGs");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                cargarTablaTFG();
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarExpendientesClick() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-expedientes.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de Expedientes");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarSolicitudesClick() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cargar-solicitudes.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Subida de Solicitudes");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void cargarInformacionSistema() {
        // Obtener la información del sistema
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String javaVersion = System.getProperty("java.version");
        Configuracion configuracion = Configuracion.getInstance();

        // Crear el contenido de la alerta
        Label osLabel = new Label("Sistema Operativo: " + osName + " " + osVersion);
        Label javaLabel = new Label("Versión de Java: " + javaVersion);
        Label pythonLabel = new Label("Python: " + configuracion.obtenerPythonPath());
        
        VBox content = new VBox(10, osLabel, javaLabel, pythonLabel);
        content.setPadding(new Insets(20));

        // Crear la alerta
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Sistema");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(content);

        // Mostrar la alerta
        alert.showAndWait();
    }

    @FXML
    protected void cargarConfiguracion() {
        // Ventana para cargar TFGs manualmente o mendiante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("configurar-rutas.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Configuración");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                if (!Configuracion.configuracionInicial()) {
                    try {
                        configuracionInicial(true);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        configuracionInicial(false);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void cerrarSesionClick() {
        // Crear una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cierre de sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas cerrar la sesión?");

        // Obtener el resultado de la alerta
        ButtonType resultado = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Si el usuario confirma, cerrar la sesión y abrir la pantalla de inicio de sesión
        if (resultado == ButtonType.OK) {

            Stage stagePane = (Stage) panePrincipal.getScene().getWindow();
            stagePane.close();

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
    }



    @FXML
    protected void salirClick() {
        Platform.exit();
        LogController.registrarAccion("Cierre aplicación");
    }

    @FXML
    protected void busquedaClick() {
        String valorBusqueda = txtBusqueda.getText();

        // Deshabilita el panel principal
        panePrincipal.setDisable(true);
        // Abrir la pantalla de inicio de sesión
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modal-busqueda.fxml"));
        try {
            Parent root = fxmlLoader.load();
            ModalBusquedaController modalController = fxmlLoader.getController();
            modalController.setValorBusqueda(valorBusqueda); // Pasar el valor de búsqueda al modal
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Resultados de la búsqueda");

            // Abre modal y no deja interactuar con el padre hasta que se cierra
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait(); // Esperar hasta que se cierre el modal
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void formatearSistemaClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Estás seguro de que quieres borrar todo el contenido?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tbTFGs.getItems().clear();
                tbAlumnos.getItems().clear();
                bdController.limpiarAlumnos();
                bdController.limpiarTFGs();
                bdController.limpiarSolicitudes();
                bdController.limpiarSolicitantes();
                ArchivoController.borrarArchivosEnDirectorio("src/main/resources/expedientes/");
                ArchivoController.borrarArchivosEnDirectorio("src/main/resources/tfgs/");
                mostrarAlerta("Borrado exitoso", "El sistema se ha reestablecido correctamente.");
                LogController.registrarAccion("IMPORTANTE: Formateo del sistema");
            } else {

            }
        });
    }

    @FXML
    protected void borrarAsignaciones() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de que desea borrar todas las asignaciones?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bdController.limpiarAsignaciones();
                mostrarAlerta("Borrado exitoso", "Las asignaciones se han restablecido éxitosamente.");
                LogController.registrarAccion("IMPORTANTE: Limpiar asignaciones");
            } else {

            }
        });
    }

    @FXML
    protected void exportarActividad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.setInitialFileName("Actividad");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        // Mostrar el cuadro de diálogo y obtener la ruta de destino
        File fileDestino = fileChooser.showSaveDialog(stage);
        if (fileDestino != null) {
            try {
                // Ruta del archivo de texto de origen
                //String rutaTexto = "src/main/resources/logs/activity_log.txt";
                String rutaTexto = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "logs" + File.separator + "activity_log.txt";


                // Crear documento PDF
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                // Configurar stream para escribir en el documento PDF
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Leer contenido del archivo de texto
                try (BufferedReader reader = new BufferedReader(new FileReader(rutaTexto))) {
                    float margin = 50;
                    float y = page.getMediaBox().getHeight() - margin; // Posición inicial en la página
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Verificar si hay espacio en la página actual
                        if (y - margin < 0) {
                            // Si no hay espacio, añadir una nueva página
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            y = page.getMediaBox().getHeight() - margin; // Resetear la posición inicial
                        }
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                        contentStream.newLineAtOffset(margin, y); // Mover a la siguiente línea
                        contentStream.showText(line); // Escribir la línea en el PDF
                        contentStream.endText();
                        y -= 15; // Espacio entre líneas
                    }
                }

                // Cerrar stream y guardar el documento
                contentStream.close();
                document.save(fileDestino);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void exportarAsignaciones() {
        try {
            // Configurar el diálogo de selección de archivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar asignaciones de TFGs");
            fileChooser.setInitialFileName("asignaciones_tfgs_" + Calendar.getInstance().get(Calendar.YEAR) + ".pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

            // Mostrar el diálogo de selección de archivo
            File fileDestino = fileChooser.showSaveDialog(null);
            if (fileDestino != null) {
                // Establecer conexión con la base de datos
                Connection conn = DriverManager.getConnection(BDController.URL, BDController.USUARIO, BDController.CONTRASENA);

                // Consulta para obtener las asignaciones de TFGs
                String query = "SELECT codigo, titulo, adjudicado FROM tfgs WHERE adjudicado IS NOT NULL";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                // Rutas a las fuentes
                String arialPath = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "fonts" + File.separator + "arial-unicode.TTF";
                String palatinoPath = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "fonts" + File.separator + "palatino-bold.ttf";

                // Crear documento PDF
                PDDocument document = new PDDocument();
                PDType0Font arial = PDType0Font.load(document, new File(arialPath));
                PDType0Font palatino = PDType0Font.load(document, new File(palatinoPath));

                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                // Configurar stream para escribir en el documento PDF
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                float margin = 50;
                float marginRight = 90; // Margen derecho
                float width = page.getMediaBox().getWidth() - margin - marginRight; // Ancho disponible para el texto
                float y = page.getMediaBox().getHeight() - margin;

                // UCAM
                contentStream.beginText();
                contentStream.setFont(palatino, 22);
                contentStream.newLineAtOffset(margin, y);
                contentStream.showText("UCAM");
                contentStream.newLineAtOffset(0, -20);
                contentStream.endText();
                y -= 20;

                // Escribir encabezado
                contentStream.beginText();
                contentStream.setFont(arial, 16);
                contentStream.newLineAtOffset(margin, y);
                contentStream.showText("Asignaciones TFG - Año " + Calendar.getInstance().get(Calendar.YEAR));
                contentStream.newLineAtOffset(0, -20);
                contentStream.endText();
                y -= 20;

                // Escribir subtítulo
                contentStream.beginText();
                contentStream.setFont(arial, 14); // Tamaño de fuente para el subtítulo
                contentStream.newLineAtOffset(margin, y);
                contentStream.showText("Grado en Ingeniería Informática");
                contentStream.newLineAtOffset(0, -20);
                contentStream.endText();
                y -= 20;

                // Separación
                y -= 30;

                // Iterar sobre los resultados de la consulta y escribir en el PDF
                while (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String nombre = rs.getString("titulo");
                    int asignado = rs.getInt("adjudicado");

                    // Dividir el texto para que termine justo en el margen derecho
                    List<String> lineas = dividirTexto("TFG: " + codigo + " -- " + nombre + ", Adjudicado (NIA): " + asignado, arial, 12, width);
                    for (String linea : lineas) {
                        contentStream.beginText();
                        contentStream.setFont(arial, 12);
                        contentStream.newLineAtOffset(margin, y);
                        contentStream.showText(linea);
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.endText();
                        y -= 20;
                    }
                    y -= 10;
                }

                // Cerrar stream y guardar el documento en la ubicación seleccionada por el usuario
                contentStream.close();
                document.save(fileDestino);
                document.close();

                // Cerrar conexión con la base de datos
                conn.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> dividirTexto(String texto, PDType0Font font, int fontSize, float width) throws IOException {
        List<String> lineas = new ArrayList<>();
        String[] palabras = texto.split("\\s+");
        StringBuilder lineaActual = new StringBuilder();
        float lineaActualWidth = 0;

        for (String palabra : palabras) {
            float palabraWidth = font.getStringWidth(palabra) / 1000 * fontSize;
            if (lineaActualWidth + palabraWidth < width) {
                lineaActual.append(palabra).append(" ");
                lineaActualWidth += palabraWidth;
            } else {
                if (palabraWidth > width) {
                    // La palabra es demasiado larga para caber en una línea
                    // Dividir la palabra en fragmentos que quepan en una línea
                    List<String> fragmentos = dividirPalabra(palabra, font, fontSize, width);
                    for (String fragmento : fragmentos) {
                        if (lineaActualWidth + font.getStringWidth(fragmento) / 1000 * fontSize < width) {
                            lineaActual.append(fragmento).append(" ");
                            lineaActualWidth += font.getStringWidth(fragmento) / 1000 * fontSize;
                        } else {
                            lineas.add(lineaActual.toString().trim());
                            lineaActual = new StringBuilder(fragmento + " ");
                            lineaActualWidth = font.getStringWidth(fragmento) / 1000 * fontSize;
                        }
                    }
                } else {
                    lineas.add(lineaActual.toString().trim());
                    lineaActual = new StringBuilder(palabra + " ");
                    lineaActualWidth = palabraWidth;
                }
            }
        }

        lineas.add(lineaActual.toString().trim());

        return lineas;
    }

    private List<String> dividirPalabra(String palabra, PDType0Font font, int fontSize, float width) throws IOException {
        List<String> fragmentos = new ArrayList<>();
        StringBuilder fragmentoActual = new StringBuilder();
        float fragmentoActualWidth = 0;

        for (int i = 0; i < palabra.length(); i++) {
            char caracter = palabra.charAt(i);
            float caracterWidth = font.getStringWidth(String.valueOf(caracter)) / 1000 * fontSize;

            if (fragmentoActualWidth + caracterWidth < width) {
                fragmentoActual.append(caracter);
                fragmentoActualWidth += caracterWidth;
            } else {
                fragmentos.add(fragmentoActual.toString());
                fragmentoActual = new StringBuilder(String.valueOf(caracter));
                fragmentoActualWidth = caracterWidth;
            }
        }

        fragmentos.add(fragmentoActual.toString());

        return fragmentos;
    }

    @FXML
    protected void asignacionesClick() {
        // Ventana para cargar alumnos manualmente o mediante fichero
        panePrincipal.setDisable(true);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detalle-asignaciones.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Asignaciones");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el comportamiento de cierre del Stage asociado al modal
            stage.setOnCloseRequest(event -> {
                // Habilitar la interacción con la pantalla principal cuando se cierra el modal
                panePrincipal.setDisable(false);
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
