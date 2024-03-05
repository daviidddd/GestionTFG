package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.ficheros.ArchivoController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;

public class GestionarTfgsController {
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtTutor;
    @FXML
    private TextField txtAsignaturas1;
    @FXML
    private TextField txtAsignaturas2;
    @FXML
    private TextField txtAsignaturas3;

    private BDController bdController;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GestionarTfgsController() {
        this.bdController = new BDController();
    }

    @FXML
    protected void altaTFGClick() {
        String codigo = txtCodigo.getText();
        String titulo = txtTitulo.getText();
        String descripcion = txtDescripcion.getText();
        String tutor = txtTutor.getText();
        String asignaturas1 = txtAsignaturas1.getText();
        String asignaturas2 = txtAsignaturas2.getText();
        String asignaturas3 = txtAsignaturas3.getText();
        String tecnologias = "";

        String asignaturas = String.valueOf(asignaturas1 + ", " + asignaturas2 + ", " + asignaturas3);
        String tfg = String.valueOf(codigo + " " + titulo);

        if(codigo.isEmpty() || titulo.isEmpty() || descripcion.isEmpty() || tutor.isEmpty() || asignaturas1.isEmpty())
            System.out.println("Campos vacios");
        else {
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
        archivoController.procesarTFGs();
        altaTFG();
    }

    private void altaTFG() {
        String directorio = "src/main/resources/tfgs";

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
                    titulo = br.readLine().trim();
                } else if (linea.startsWith("Tutor")) {
                    tutor = br.readLine().trim();
                } else if (linea.startsWith("Descr")) {
                    descripcion = br.readLine().trim();
                } else if (linea.startsWith("Tecnologias")) {
                    tecnologias = br.readLine().trim();
                } else if (linea.startsWith("Asignaturas")) {
                    asignaturas = br.readLine().trim();
                }
            }

            // Insertar valores
            bdController.registrarTFG(n, titulo, descripcion, tutor, asignaturas, tecnologias);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void limpiarFormulario() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtTutor.setText("");
        txtAsignaturas1.setText("");
        txtAsignaturas2.setText("");
        txtAsignaturas3.setText("");
    }

}
