package com.david.gestiontfg.database;

import java.sql.*;

public class BDController {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion";
    private static final String USUARIO = "test@localhost";
    private static final String CONTRASENA = "test";

    public boolean iniciarSesionUsuario(String usuario, String contrasena) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT * FROM autorizados WHERE correo = ? AND contrasena = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contrasena);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Si hay un resultado, el usuario y la contraseña son válidos
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarUsuario(String usuario, String contrasena) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "INSERT INTO autorizados (correo, contrasena) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contrasena);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Si se insertó al menos una fila, el registro fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
