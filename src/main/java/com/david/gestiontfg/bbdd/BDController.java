package com.david.gestiontfg.bbdd;

import java.sql.*;
import java.time.LocalDateTime;

public class BDController {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion";
    private static final String USUARIO = "test@localhost";
    private static final String CONTRASENA = "test";

    public boolean iniciarSesionUsuario(String correo, String contrasena) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT * FROM autorizados WHERE correo = ? AND contrasena = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, correo);
            statement.setString(2, contrasena);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Actualizar la fecha de inicio de sesión
                String updateQuery = "UPDATE autorizados SET ult_inicio_sesion = ? WHERE correo = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                updateStatement.setString(2, correo);
                updateStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public boolean registrarAlumno(int idUcam, int nombre, String apellido1, String apellido2, String correo, int nia) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "INSERT INTO alumnos (id_ucam, nombre, apellido1, apellido2, correo, nia) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idUcam);
            statement.setInt(2, nombre);
            statement.setString(3, apellido1);
            statement.setString(4, apellido2);
            statement.setString(5, correo);
            statement.setInt(6, nia);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Si se insertó al menos una fila, el registro fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
