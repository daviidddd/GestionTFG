package com.david.gestiontfg.bbdd;

import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.TFG;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public boolean registrarTFG(String codigo, String titulo, String descripcion, String tutor, String asignaturas) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "INSERT INTO tfgs (codigo, titulo, descripcion, tutor, asignaturas) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codigo);
            statement.setString(2, titulo);
            statement.setString(3, descripcion);
            statement.setString(4, tutor);
            statement.setString(5, asignaturas);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Si se insertó al menos una fila, el registro fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Alumno> obtenerAlumnos() {
        List<Alumno> alumnosActivos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT * FROM alumnos";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_ucam");
                int nombre = resultSet.getInt("nombre");
                String apellido1 = resultSet.getString("apellido1");
                String apellido2 = resultSet.getString("apellido2");
                String correo = resultSet.getString("correo");
                int nia = resultSet.getInt("nia");

                Alumno alumno = new Alumno(id, nombre, apellido1, apellido2, correo, nia); // Aquí crea el objeto Alumno con los datos obtenidos
                alumnosActivos.add(alumno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnosActivos;
    }

    public List<TFG> obtenerTFGs() {
        List<TFG> tfgActivos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT * FROM tfgs";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String codigo = resultSet.getString("codigo");
                String titulo = resultSet.getString("titulo");
                String descripcion = resultSet.getString("descripcion");
                String tutor = resultSet.getString("tutor");
                String asignaturas = resultSet.getString("asignaturas");
                int solicitantes = resultSet.getInt("solicitantes");
                int adjudicado = resultSet.getInt("adjudicado");

                TFG tfg = new TFG(codigo, titulo, descripcion, tutor, asignaturas, solicitantes, adjudicado); // Aquí crea el objeto Alumno con los datos obtenidos
                tfgActivos.add(tfg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tfgActivos;
    }

}
