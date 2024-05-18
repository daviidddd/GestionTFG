package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.ficheros.ArchivoController;
import com.david.gestiontfg.logs.LogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.io.*;
import java.text.Normalizer;
import java.util.List;

public class GestionarTFGController {
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextArea txtTecnologias;
    @FXML
    private TextArea txtTutor;
    @FXML
    private TextArea txtAsignaturas;
    @FXML
    private TextArea txtTitulo;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ComboBox cbTutor;
    @FXML
    private ComboBox cbAsignatura;
    @FXML
    private Button btnAltaTFGFichero;

    private BDController bdController;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GestionarTFGController() {
        this.bdController = new BDController();
    }

    public void initData() {
        cargarComboTutores();
        cargarComboAsignaturas();

        btnAltaTFGFichero.setBackground(Background.EMPTY);
    }

    protected void cargarComboTutores() {
        BDController bdController = new BDController();
        List<String> tutores = bdController.obtenerTutoresGrado();

        // Crear un ObservableList a partir de la lista de asignaturas
        ObservableList<String> items = FXCollections.observableArrayList(tutores);

        // Establecer los elementos
        cbTutor.setItems(items);
    }

    protected void cargarComboAsignaturas() {
        BDController bdController = new BDController();
        List<String> asignaturas = bdController.obtenerAsignaturasGrado();

        // Crear un ObservableList a partir de la lista de asignaturas
        ObservableList<String> items = FXCollections.observableArrayList(asignaturas);

        // Establecer los elementos
        cbAsignatura.setItems(items);
    }

    @FXML
    protected void anadirTutor() {
        String tutorExistente = "";
        tutorExistente = txtTutor.getText();
        txtTutor.setText(tutorExistente + cbTutor.getSelectionModel().getSelectedItem().toString() + ", ");
    }

    @FXML
    protected void anadirAsignatura() {
        String asignaturaExistente = "";
        asignaturaExistente = txtAsignaturas.getText();
        txtAsignaturas.setText(asignaturaExistente + cbAsignatura.getSelectionModel().getSelectedItem().toString() + ", ");
    }

    @FXML
    protected void borrarTutores() {
        txtTutor.setText("");
    }

    @FXML
    protected void borrarAsignaturas() {
        txtAsignaturas.setText("");
    }

    @FXML
    protected void altaTFGClick() {
        String codigo = txtCodigo.getText();
        String titulo = txtTitulo.getText();
        String descripcion = txtDescripcion.getText();
        String tutor = txtTutor.getText();
        String asignaturas = txtAsignaturas.getText();
        String tecnologias = txtTecnologias.toString();

        String tfg = String.valueOf(codigo + " " + titulo);

        if(codigo.isEmpty() || titulo.isEmpty() || descripcion.isEmpty() || tutor.isEmpty() || asignaturas.isEmpty() || tecnologias.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Hay campos vacíos");
            alerta.setContentText("Asegúrese de completar todos los campos.");
            alerta.showAndWait();
        } else {
            boolean altaExitosa = bdController.registrarTFG(codigo, titulo, descripcion, tutor, asignaturas, tecnologias);
            if(altaExitosa){
                LogController.registrarAccion("Alta TFG - " + tfg);

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("TFG Añadido");
                alerta.setHeaderText("TFG Añadido");
                alerta.setContentText("El TFG ha sido añadido correctamente.");

                alerta.showAndWait();
                limpiarFormulario();
            }
        }
    }

    @FXML
    protected void altaTFGFicheroClick() {
        ArchivoController archivoController = new ArchivoController();
        archivoController.procesarTFG();
        altaTFG();
    }

    private void altaTFG() {
        String directorio = System.getProperty("user.home") + File.separator + "GestorUCAM" + File.separator + "tfgs";
        //String directorio = "src/main/resources/tfgs";

        // Obtener una lista de archivos en el directorio
        File[] archivos = new File(directorio).listFiles();

        // Verificar si la lista de archivos no está vacía
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile()) {  // Verificar si es un archivo
                    // Procesar el archivo
                    procesarArchivo(archivo);
                }
            }
        } else {
            System.out.println("El directorio está vacío o no se pudo acceder.");
        }
    }

    private void procesarArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String n = null;
            String titulo = null;
            String tutor = null;
            String descripcion = null;
            String tecnologias = null;
            String asignaturas = null;

            while ((linea = br.readLine()) != null) {
                // Verificar si la línea es un encabezado y extraer el valor correspondiente
                if (linea.startsWith("Nº")) {
                    n = br.readLine().trim();// Lee la siguiente línea y elimina espacios en blanco al principio y al final
                    n = n.replace(" ", ""); // Quita el espacio en blanco  (CXXXX - XX --> CXXXX-XX)
                } else if (linea.startsWith("Titulo")) {
                    titulo = limpiarTexto(br.readLine().trim());
                } else if (linea.startsWith("Tutor")) {
                    tutor = limpiarTexto(br.readLine().trim());
                    tutor = tutor.replace(" y ", " , "); // Sustituye "y" por ","
                } else if (linea.startsWith("Descr")) {
                    descripcion = limpiarTexto(br.readLine().trim());
                } else if (linea.startsWith("Tecnologias")) {
                    tecnologias = limpiarTexto(br.readLine().trim());
                } else if (linea.startsWith("Asignaturas")) {
                    asignaturas = limpiarTexto(br.readLine().trim());
                }
            }

            // Insertar valores
            bdController.registrarTFG(n, titulo, descripcion, tutor, asignaturas, tecnologias);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String limpiarTexto(String texto) {
        // Normalizar caracteres
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Eliminar caracteres no válidos y reemplazarlos por espacios
        texto = texto.replaceAll("[^\\x00-\\x7F]", " ");
        // Escapar caracteres especiales si es necesario
        texto = texto.replace("'", "''"); // Por ejemplo, escapar comillas simples
        return texto;
    }

    protected void limpiarFormulario() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtTutor.setText("");
        txtAsignaturas.setText("");
        txtTecnologias.setText("");
    }

}
