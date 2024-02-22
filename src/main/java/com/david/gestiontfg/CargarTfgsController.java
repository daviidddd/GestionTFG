package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.Log;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CargarTfgsController {
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

    public CargarTfgsController() {
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

        String asignaturas = String.valueOf(asignaturas1 + ", " + asignaturas2 + ", " + asignaturas3);
        String tfg = String.valueOf(codigo + " " + titulo);

        if(codigo.isEmpty() || titulo.isEmpty() || descripcion.isEmpty() || tutor.isEmpty() || asignaturas1.isEmpty())
            System.out.println("Campos vacios");
        else {
            boolean altaExitosa = bdController.registrarTFG(codigo, titulo, descripcion, tutor, asignaturas);
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
        FileChooser selectorFichero = new FileChooser();
        selectorFichero.setTitle("Seleccionar archivo XLSX");
        selectorFichero.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos XLSX", "*.xlsx"));
        int contador = 0;

        // Se abre el buscador de archivos
        File archivoSeleccionado = selectorFichero.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            LogController.registrarAccion("Alta TFG por fichero " + "(" + archivoSeleccionado.getName() + ")" + ": ");
            try {
                // Crear un objeto FileInputStream para leer el archivo XLSX
                FileInputStream fileInputStream = new FileInputStream(archivoSeleccionado);

                // Crear un objeto XSSFWorkbook para representar el archivo XLSX
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

                // Obtener la primera hoja del libro de trabajo (workbook)
                XSSFSheet sheet = workbook.getSheetAt(0);

                // Iterar sobre las filas de la hoja
                for (Row row : sheet) {
                    // Ignorar la primera fila que contiene los encabezados
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    // Leer los valores de cada celda de la fila
                    String codigo = row.getCell(0).getStringCellValue();
                    String titulo = row.getCell(1).getStringCellValue();
                    String descripcion = row.getCell(2).getStringCellValue();
                    String tutor = row.getCell(3).getStringCellValue();
                    String asignaturas = row.getCell(4).getStringCellValue();
                    int solicitantes = (int) row.getCell(4).getNumericCellValue();
                    String adjudicado = row.getCell(5).getStringCellValue();

                    String tfg = String.valueOf(codigo + " " + titulo);

                    // Llamar al método para registrar al alumno con los valores leídos
                    boolean altaExitosa = bdController.registrarTFG(codigo, titulo, descripcion, tutor, asignaturas);
                    contador++;

                    // Manejar el resultado de la inserción
                    if (altaExitosa) {
                        System.out.println("TFG registrado correctamente: " + codigo);
                        LogController.registrarAccion("    Alta " + tfg);
                    } else {
                        System.out.println("Error al registrar tfg: " + codigo);
                        LogController.registrarAccion("    Error Alta " + tfg);
                    }
                }

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Alta de TFG");
                alerta.setHeaderText(contador + " TFGs añadidos");
                alerta.setContentText("Los TFGs han sido añadidos correctamente.");

                alerta.showAndWait();

                // Cerrar el FileInputStream y liberar recursos
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
