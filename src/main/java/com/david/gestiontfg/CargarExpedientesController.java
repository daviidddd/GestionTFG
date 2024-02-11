package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.ficheros.ArchivoController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CargarExpedientesController {

    @FXML
    private Button btnCargarExpediente;
    @FXML
    private Button btnCargarExpedientes;

    private BDController bdController;

    public CargarExpedientesController() {
        this.bdController = new BDController();
    }

    @FXML
    protected void altaExpedientesClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivos PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        File[] archivosPDF = fileChooser.showOpenMultipleDialog(new Stage()).toArray(new File[0]);

        if (archivosPDF != null) {
            for (File archivoPDF : archivosPDF) {
                byte[] contenidoPDF = leerArchivoPDF(archivoPDF);
                boolean exitosa = bdController.registrarExpediente(000000, archivoPDF.getName(), contenidoPDF);

                if (exitosa) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Alta de expediente");
                    alerta.setHeaderText("Expediente insertado");
                    alerta.setContentText("El expediente " + archivoPDF.getName() + " ha sido añadido correctamente.");
                    alerta.showAndWait();

                    LogController.registrarAccion("Alta expediente - " + archivoPDF.getName());
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Alta de expediente");
                    alerta.setHeaderText("Error: alta expediente");
                    alerta.setContentText("El expediente " + archivoPDF.getName() + " no se ha podido insertar correctamente.");
                    alerta.showAndWait();

                    LogController.registrarAccion("ERROR Alta expediente - " + archivoPDF.getName());
                }
            }
        }
    }

    @FXML
    protected void altaExpedienteClick() {
        /*FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        File archivoPDF = fileChooser.showOpenDialog(new Stage());

        if (archivoPDF != null) {
            byte[] contenidoPDF = leerArchivoPDF(archivoPDF);
            boolean exitosa = bdController.registrarExpediente(000000, archivoPDF.getName(), contenidoPDF);

            if(exitosa) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Alta de expediente");
                alerta.setHeaderText("Expediente insertado");
                alerta.setContentText("El expediente " + archivoPDF.getName() + " ha sido añadido correctamente.");
                alerta.showAndWait();

                LogController.registrarAccion("Alta expediente - " + archivoPDF.getName());
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Alta de expediente");
                alerta.setHeaderText("Error: alta expediente");
                alerta.setContentText("El expediente " + archivoPDF.getName() + " no se ha podido insertar correctamente.");
                alerta.showAndWait();

                LogController.registrarAccion("ERROR Alta expediente - " + archivoPDF.getName());
            }
        }*/
        ArchivoController archivoController = new ArchivoController();
        archivoController.procesarPDF();
    }

    public static byte[] leerArchivoPDF(File archivoPDF) {
        byte[] contenidoPDF = null;
        try (FileInputStream fis = new FileInputStream(archivoPDF)) {
            contenidoPDF = new byte[(int) archivoPDF.length()];
            fis.read(contenidoPDF);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo PDF: " + e.getMessage());
        }
        return contenidoPDF;
    }


}
