package com.david.gestiontfg;

import com.david.gestiontfg.bbdd.BDController;
import com.david.gestiontfg.logs.LogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnRegistrarse;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label lblMainStatus;
    @FXML
    private TextField txtCorreoReg;
    @FXML
    private PasswordField txtContrasenaReg;
    private BDController bdController;

    public MainController() {
        this.bdController = new BDController();
    }

    // INICIO DE SESION
    @FXML
    protected void iniciarSesionClick() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            lblMainStatus.setText("Credenciales incorrectas");
            lblMainStatus.setTextFill(Color.RED);
            lblMainStatus.setTextAlignment(TextAlignment.CENTER);
        } else {
            boolean inicioExitoso = bdController.iniciarSesionUsuario(correo, contrasena);
            if (inicioExitoso) {
                lblMainStatus.setText("Usuario loggeado correctamente");
                lblMainStatus.setTextFill(Color.GREEN);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);

                LogController.registrarAccion("Inicio de sesión " + correo);

                Stage currentStage = (Stage) lblMainStatus.getScene().getWindow(); // Obtener el escenario actual
                currentStage.close(); // Cerrar la ventana actual

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pantalla-principal.fxml"));
                try {
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Ventana Principal");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                lblMainStatus.setText("Error al registrar usuario");
                lblMainStatus.setTextFill(Color.RED);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
            }
        }
    }

    // REGISTRO DE USUARIO
    @FXML
    protected void registrarseClick() {
        String correo = txtCorreoReg.getText();
        String contrasena = txtContrasenaReg.getText();

        if (correo.isEmpty() || contrasena.isEmpty()){
            lblMainStatus.setText("Campos vacíos");
            lblMainStatus.setTextFill(Color.RED);
            lblMainStatus.setTextAlignment(TextAlignment.CENTER);
        } else {
            boolean registroExitoso = bdController.registrarUsuario(correo, contrasena);
            if (registroExitoso) {
                lblMainStatus.setText("Usuario registrado correctamente");
                lblMainStatus.setTextFill(Color.GREEN);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
                LogController.registrarAccion("Registro de usuario " + correo);
            } else {
                lblMainStatus.setText("Error al registrar usuario");
                lblMainStatus.setTextFill(Color.RED);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
            }
        }
    }


}
