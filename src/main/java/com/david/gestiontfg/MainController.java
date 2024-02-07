package com.david.gestiontfg;

import com.david.gestiontfg.database.BDController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

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
    private BDController databaseManager;

    public MainController() {
        this.databaseManager = new BDController();
    }

    @FXML
    protected void iniciarSesionClick() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            lblMainStatus.setText("Credenciales incorrectas");
            lblMainStatus.setTextFill(Color.RED);
            lblMainStatus.setTextAlignment(TextAlignment.CENTER);
        } else {
            boolean inicioExitoso = databaseManager.iniciarSesionUsuario(correo, contrasena);
            if (inicioExitoso) {
                lblMainStatus.setText("Usuario loggeado correctamente");
                lblMainStatus.setTextFill(Color.GREEN);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);


            } else {
                lblMainStatus.setText("Error al registrar usuario");
                lblMainStatus.setTextFill(Color.RED);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
            }
        }
    }

    @FXML
    protected void registrarseClick() {
        String correo = txtCorreoReg.getText();
        String contrasena = txtContrasenaReg.getText();

        if (correo.isEmpty() || contrasena.isEmpty()){
            lblMainStatus.setText("Campos vacíos");
            lblMainStatus.setTextFill(Color.RED);
            lblMainStatus.setTextAlignment(TextAlignment.CENTER);
        } else {
            boolean registroExitoso = databaseManager.registrarUsuario(correo, contrasena);
            if (registroExitoso) {
                lblMainStatus.setText("Usuario registrado correctamente");
                lblMainStatus.setTextFill(Color.GREEN);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
            } else {
                lblMainStatus.setText("Error al registrar usuario");
                lblMainStatus.setTextFill(Color.RED);
                lblMainStatus.setTextAlignment(TextAlignment.CENTER);
            }
        }
    }


}
