package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CargarAlumnosController {

    @FXML
    private TextField txtIDUcam;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido1;
    @FXML
    private TextField txtApellido2;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtNIA;
    @FXML
    private Button btnAltaAlumno;
    @FXML
    private Button btnAltaAlumnoFichero;
    @FXML
    private TableView tbAlumnosAlta;
    private BDController bdController;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CargarAlumnosController() {
        this.bdController = new BDController();
    }

    @FXML
    protected void altaAlumnoClick() {
        int idUcam = Integer.parseInt(txtIDUcam.getText());
        int nombre = Integer.parseInt(txtNombre.getText());
        String apellido1 = txtApellido1.getText();
        String apellido2 = txtApellido2.getText();
        String correo = txtCorreo.getText();
        int nia = Integer.parseInt(txtNIA.getText());

        if(idUcam == 0 || nombre == 0 || apellido1.isEmpty() || apellido2.isEmpty() || correo.isEmpty() || nia == 0)
            System.out.println("Campos vacios");
        else {
            boolean altaExitosa = bdController.registrarAlumno(idUcam, nombre, apellido1, apellido2, correo, nia);
            if(altaExitosa){
                LogController.registrarAccion("Alta Alumno " + correo);

                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Usuario Añadido");
                alerta.setHeaderText("Usuario añadido");
                alerta.setContentText("El usuario ha sido añadido correctamente.");

                alerta.showAndWait();
                limpiarFormulario();
            }
        }
    }

    @FXML
    protected void altaAlumnoFicheroClick() {
        FileChooser selectorFichero = new FileChooser();
        selectorFichero.setTitle("Seleccionar archivo XLSX");
        selectorFichero.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos XLSX", "*.xlsx"));
        int contador = 0;

        // Se abre el buscador de archivos
        File archivoSeleccionado = selectorFichero.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            LogController.registrarAccion("Alta ALUMNO por fichero " + "(" + archivoSeleccionado.getName() + ")" + ": ");
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
                    String idUcam = row.getCell(0).getStringCellValue();
                    int nombre = (int) row.getCell(1).getNumericCellValue();
                    String apellido1 = row.getCell(2).getStringCellValue();
                    String apellido2 = row.getCell(3).getStringCellValue();
                    String correo = row.getCell(4).getStringCellValue();
                    String nia = row.getCell(5).getStringCellValue();

                    // Llamar al método para registrar al alumno con los valores leídos
                    boolean altaExitosa = bdController.registrarAlumno(Integer.parseInt(idUcam), nombre, apellido1, apellido2, correo, Integer.parseInt(nia));
                    contador++;

                    // Manejar el resultado de la inserción
                    if (altaExitosa) {
                        //System.out.println("Alumno registrado correctamente: " + idUcam);
                        LogController.registrarAccion("    Alta  " + correo);
                    } else {
                        //System.out.println("Error al registrar alumno: " + idUcam);
                        LogController.registrarAccion("    Error Alta  " + correo);
                    }
                }

                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Alta de alumno");
                alerta.setHeaderText(contador + " alumnos añadidos");
                alerta.setContentText("Los alumnos han sido añadidos correctamente.");

                alerta.showAndWait();

                // Cerrar el FileInputStream y liberar recursos
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void limpiarFormulario() {
        txtIDUcam.setText("");
        txtNombre.setText("");
        txtApellido1.setText("");
        txtApellido2.setText("");
        txtCorreo.setText("");
        txtNIA.setText("");
    }

}
