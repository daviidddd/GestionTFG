package com.david.gestiontfg.bbdd;

import com.david.gestiontfg.modelos.Alumno;
import com.david.gestiontfg.modelos.Solicitud;
import com.david.gestiontfg.modelos.TFG;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public boolean registrarSolicitud(String correoElectronico, int nia, double nota, double creditos, double mesesExperiencia, String meritos, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5, int expTfg1, int expTfg2, int expTfg3,int expTfg4, int expTfg5, int ptosCreditos, int ptosNotaMedia, int ptosExperiencia, int ptosTFG1, int ptosTFG2, int ptosTFG3, int ptosTFG4, int ptosTFG5){
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Crear la consulta SQL para insertar los valores
            String consulta = "INSERT INTO solicitudes (correo, nota_media, creditos_restantes, meses_experiencia, meritos, tfg1, tfg2, tfg3, tfg4, tfg5, exp_tfg1, exp_tfg2, exp_tfg3, exp_tfg4, exp_tfg5, pto_creditos, pto_nota_media, pto_experiencia, pto_tfg1, pto_tfg2, pto_tfg3, pto_tfg4, pto_tfg5, nia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, correoElectronico);
            statement.setDouble(2, nota);
            statement.setDouble(3, creditos);
            statement.setDouble(4, mesesExperiencia);
            statement.setString(5, meritos);
            statement.setString(6, tfg1);
            statement.setString(7, tfg2);
            statement.setString(8, tfg3);
            statement.setString(9, tfg4);
            statement.setString(10, tfg5);
            statement.setInt(11, expTfg1);
            statement.setInt(12, expTfg2);
            statement.setInt(13, expTfg3);
            statement.setInt(14, expTfg4);
            statement.setInt(15, expTfg5);
            statement.setInt(16, ptosCreditos);
            statement.setInt(17, ptosNotaMedia);
            statement.setInt(18, ptosExperiencia);
            statement.setInt(19, ptosTFG1);
            statement.setInt(20, ptosTFG2);
            statement.setInt(21, ptosTFG3);
            statement.setInt(22, ptosTFG4);
            statement.setInt(23, ptosTFG5);
            statement.setInt(24, nia);

            // Ejecutar la consulta SQL
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarPuntuacionSolicitud(int nia, int orden, String tfg, int puntos){
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String consulta = "INSERT INTO puntuaciones(tfg, orden, alumno, puntuacion) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1, tfg);
            statement.setInt(2, orden);
            statement.setInt(3, nia);
            statement.setInt(4, puntos);

            // Ejecutar la consulta SQL
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sumarSolicitante(String tfg) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "UPDATE tfgs SET solicitantes = COALESCE(solicitantes, 0) + 1 WHERE codigo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, tfg);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restarSolicitante(String tfg) {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "UPDATE tfgs SET solicitantes = CASE WHEN solicitantes > 0 THEN solicitantes - 1 ELSE 0 END WHERE codigo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, tfg);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void asignacionTFGAutomatica() {
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta para obtener todas las puntuaciones ordenadas por orden y puntuación de mayor a menor
            String query = "SELECT tfg, alumno, puntuacion, orden FROM puntuaciones ORDER BY orden ASC, puntuacion DESC";

            // Conjunto para realizar un seguimiento de los TFG ya asignados
            Set<String> tfgsAsignados = new HashSet<>();

            try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tfg = rs.getString("tfg");
                    int alumno = rs.getInt("alumno");
                    int order = rs.getInt("orden");

                    // Verificar si el TFG ya está asignado a algún alumno o si el alumno ya tiene un TFG asignado
                    if (!tfgsAsignados.contains(tfg) && !alumnoTieneTFGAsignado(conn, alumno)) {
                        // Asignar el TFG al alumno actual
                        asignarTFG(conn, tfg, alumno);
                        tfgsAsignados.add(tfg);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para verificar si un alumno ya tiene un TFG asignado
    private boolean alumnoTieneTFGAsignado(Connection conn, int alumno) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM tfgs WHERE adjudicado = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, alumno);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }

        return false;
    }

    // Método para asignar un TFG a un alumno
    private void asignarTFG(Connection conn, String tfg, int alumno) throws SQLException {
        String query = "UPDATE tfgs SET adjudicado = ? WHERE codigo = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, alumno);
            stmt.setString(2, tfg);
            stmt.executeUpdate();
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

    public String obtenerAsignaturasTFG(String tfg) {
        String asignturas = "";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "SELECT asignaturas FROM tfgs WHERE codigo = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tfg);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                asignturas = resultSet.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asignturas;
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
                double notaMedia = resultSet.getDouble("nota_media");
                double creditosResultantes = resultSet.getDouble("creditos_restantes");
                double mesesExperiencia = resultSet.getDouble("meses_experiencia");
                String meritos = resultSet.getString("meritos");
                String tfg1 = resultSet.getString("tfg1");
                String tfg2 = resultSet.getString("tfg2");
                String tfg3 = resultSet.getString("tfg3");
                String tfg4 = resultSet.getString("tfg4");
                String tfg5 = resultSet.getString("tfg5");
                int expTfg1 = resultSet.getInt("exp_tfg1");
                int expTfg2 = resultSet.getInt("exp_tfg2");
                int expTfg3 = resultSet.getInt("exp_tfg3");
                int expTfg4 = resultSet.getInt("exp_tfg4");
                int expTfg5 = resultSet.getInt("exp_tfg5");

                Solicitud solicitud = new Solicitud(correo, notaMedia, creditosResultantes, mesesExperiencia, meritos, tfg1, tfg2, tfg3, tfg4, tfg5, expTfg1, expTfg2, expTfg3, expTfg4, expTfg5);
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

    public List<String> obtenerAsignaturasGrado() {
        List<String> asignaturasGrado = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM asignatura";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String asignatura = resultSet.getString("nombre");
                asignaturasGrado.add(asignatura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asignaturasGrado;
    }

    public List<String> obtenerTutoresGrado() {
        List<String> tutoresGrado = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM tutor";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tutores = resultSet.getString("nombre");
                tutoresGrado.add(tutores);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutoresGrado;
    }

    public List<String> obtenerTFGSCodigo() {
        List<String> tfgs = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT * FROM tfgs";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tfg = resultSet.getString("codigo");
                tfgs.add(tfg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tfgs;
    }

    public static int obtenerNiaPorCorreo(String correo) {
        int nia = 0;
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Consulta SQL con una cláusula WHERE para buscar en múltiples campos
            String query = "SELECT nia FROM alumnos WHERE correo = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, correo);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
                nia = resultSet.getInt("nia");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nia;
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

    public void limpiarSolicitudes() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM solicitudes";
            PreparedStatement statement = connection.prepareStatement(query);
            int resultSet = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarAsignaciones() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "UPDATE tfgs SET adjudicado = NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void limpiarSolicitantes() {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)){
            String query = "UPDATE tfgs SET solicitantes = 0";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean modificarTFGPorCodigo(String codigoNuevo, String codigoAntiguo, String titulo, String descripcion, String tutor, String asignaturas, String tecnologias) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Crear la consulta SQL para actualizar los valores
            String consulta = "UPDATE tfgs SET codigo = ?, titulo = ?, descripcion = ?, tutor = ?, asignaturas = ?, tecnologias = ? WHERE codigo = ?";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, codigoNuevo);
            statement.setString(2, titulo);
            statement.setString(3, descripcion);
            statement.setString(4, tutor);
            statement.setString(5, asignaturas);
            statement.setString(6, tecnologias);
            statement.setString(7, codigoAntiguo);

            // Ejecutar la consulta SQL de actualización
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificarSolicitudPorCodigo(Double notaMedia, Double creditosRestantes, String correoAntiguo, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5, Double expTFG1, Double expTFG2, Double expTFG3, Double expTFG4, Double expTFG5) {
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            // Crear la consulta SQL para actualizar los valores
            String consulta = "UPDATE solicitudes SET nota_media = ?, creditos_restantes = ?, tfg1 = ?, tfg2 = ?, tfg3 = ?, tfg4 = ?, tfg5 = ?, exp_tfg1 = ?, exp_tfg2 = ?, exp_tfg3 = ?, exp_tfg4 = ?, exp_tfg5 = ? WHERE correo = ?";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setDouble(1, notaMedia);
            statement.setDouble(2, creditosRestantes);
            statement.setString(3, tfg1);
            statement.setString(4, tfg2);
            statement.setString(5, tfg3);
            statement.setString(6, tfg4);
            statement.setString(7, tfg5);
            statement.setDouble(8, expTFG1);
            statement.setDouble(9, expTFG2);
            statement.setDouble(10, expTFG3);
            statement.setDouble(11, expTFG4);
            statement.setDouble(12, expTFG5);
            statement.setString(13, correoAntiguo);

            // Ejecutar la consulta SQL de actualización
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAlumno(String id) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM alumnos WHERE id_ucam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarTFGPorCodigo(String nombre) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM tfgs WHERE codigo = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarSolicitudPorCorreo(String correo) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            String query = "DELETE FROM solicitudes WHERE correo = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, correo);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
