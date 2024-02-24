package com.david.gestiontfg.bbdd;

import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.Solicitud;
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
            String query = "INSERT INTO alumnos (id_ucam, nombre, apellido1, apellido2, correo, nia, expediente) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idUcam);
            statement.setInt(2, nombre);
            statement.setString(3, apellido1);
            statement.setString(4, apellido2);
            statement.setString(5, correo);
            statement.setInt(6, nia);
            statement.setString(7, "NO");
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Si se insertó al menos una fila, el registro fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarTFG(String codigo, String titulo, String descripcion, String tutor, String asignaturas, String tecnologias) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "INSERT INTO tfgs (codigo, titulo, descripcion, tutor, asignaturas, tecnologias) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codigo);
            statement.setString(2, titulo);
            statement.setString(3, descripcion);
            statement.setString(4, tutor);
            statement.setString(5, asignaturas);
            statement.setString(6, tecnologias);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Si se insertó al menos una fila, el registro fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarSolicitud(String correo, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5){
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Crear la consulta SQL para insertar los valores
            String consulta = "INSERT INTO solicitudes (correo, tfg1, tfg2, tfg3, tfg4, tfg5) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, correo);
            statement.setString(2, tfg1);
            statement.setString(3, tfg2);
            statement.setString(4, tfg3);
            statement.setString(5, tfg4);
            statement.setString(6, tfg5);

            // Ejecutar la consulta SQL
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
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
                String expediente = resultSet.getString("expediente");

                Alumno alumno = new Alumno(id, nombre, apellido1, apellido2, correo, nia, expediente); // Aquí crea el objeto Alumno con los datos obtenidos
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

    public int obtenerTFGAdjudicado() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT COUNT(*) FROM tfgs WHERE adjudicado IS NOT NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int contador = resultSet.getInt(1);
                return contador;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<TFG> obtenerTFGsFiltro(String parametroBusqueda) {
        List<TFG> tfgActivos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM tfgs WHERE codigo LIKE ? OR titulo LIKE ? OR descripcion LIKE ? OR tutor LIKE ? OR asignaturas LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Asignar el parámetro de búsqueda a cada campo en la consulta SQL
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, "%" + parametroBusqueda + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String codigo = resultSet.getString("codigo");
                String titulo = resultSet.getString("titulo");
                String descripcion = resultSet.getString("descripcion");
                String tutor = resultSet.getString("tutor");
                String asignaturas = resultSet.getString("asignaturas");
                int solicitantes = resultSet.getInt("solicitantes");
                int adjudicado = resultSet.getInt("adjudicado");

                TFG tfg = new TFG(codigo, titulo, descripcion, tutor, asignaturas, solicitantes, adjudicado);
                tfgActivos.add(tfg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tfgActivos;
    }

    public List<Alumno> obtenerAlumnosFiltro(String parametroBusqueda) {
        List<Alumno> alumnosActivos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM alumnos WHERE id_ucam = ? OR nia = ? OR correo LIKE ? OR expediente LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Asignar el parámetro de búsqueda a cada campo en la consulta SQL
            for (int i = 1; i <= 4; i++) {
                statement.setString(i, "%" + parametroBusqueda + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id_ucam = resultSet.getInt("id_ucam");
                int nia = resultSet.getInt("nia");
                int nombre = resultSet.getInt("nombre");
                String apellido1 = resultSet.getString("apellido1");
                String apellido2 = resultSet.getString("apellido2");
                String correo = resultSet.getString("correo");
                String expediente = resultSet.getString("expediente");

                Alumno alumno = new Alumno(id_ucam, nombre, apellido1, apellido2, correo, nia, expediente);
                alumnosActivos.add(alumno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumnosActivos;
    }

    public List<Solicitud> obtenerSolicitudesFiltro(String parametroBusqueda) {
        List<Solicitud> solicitudesActivas = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM solicitudes WHERE correo LIKE ? OR tfg1 LIKE ? OR tfg2 LIKE ? OR tfg3 LIKE ? OR tfg4 LIKE ? OR tfg5 LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            // Asignar el parámetro de búsqueda a cada campo en la consulta SQL
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, "%" + parametroBusqueda + "%");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String correo = resultSet.getString("correo");
                String tfg1 = resultSet.getString("tfg1");
                String tfg2 = resultSet.getString("tfg2");
                String tfg3 = resultSet.getString("tfg3");
                String tfg4 = resultSet.getString("tfg4");
                String tfg5 = resultSet.getString("tfg5");

                Solicitud solicitud = new Solicitud(correo, tfg1, tfg2, tfg3, tfg4, tfg5);
                solicitudesActivas.add(solicitud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudesActivas;
    }

    public boolean existeNIAEnBaseDeDatos(int nia) {
        // Establecer la conexión con la base de datos
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta para verificar si el NIA existe en la base de datos
            String consulta = "SELECT COUNT(*) FROM alumnos WHERE nia = ?";

            // Ejecutar la consulta
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setInt(1, nia);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void actualizarExpedienteEnBaseDeDatos(int nia) {
        // Establecer la conexión con la base de datos
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta para actualizar el campo expediente en la base de datos
            String consulta = "UPDATE alumnos SET expediente = 'SI' WHERE nia = ?";

            // Ejecutar la consulta
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setInt(1, nia);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int obtenerExpedientes() {
        int contador = 0;
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT COUNT(*) FROM alumnos WHERE expediente LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "SI"); // Índice 1 en lugar de 0

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                contador = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contador;
    }

    public int obtenerAlumnosTam() {
        int contador = 0;
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT COUNT(*) FROM alumnos";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                contador = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contador;
    }

    public void limpiarAlumnos() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM alumnos";
            PreparedStatement statement = connection.prepareStatement(query);
            int resultSet = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarTFGs() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM tfgs";
            PreparedStatement statement = connection.prepareStatement(query);
            int resultSet = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void eliminarAlumno(String id) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM alumnos WHERE id_ucam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
