package com.david.gestiontfg.modelos;

import com.david.gestiontfg.bbdd.BDController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.*;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Solicitud {
    private final SimpleStringProperty correoElectronico;
    private SimpleIntegerProperty nia;
    private final SimpleDoubleProperty notaMedia;
    private final SimpleDoubleProperty creditosRestantes;
    private final SimpleDoubleProperty totalMesesExperiencia;
    private final SimpleStringProperty meritos;
    private final SimpleStringProperty tfg1;
    private final SimpleStringProperty tfg2;
    private final SimpleStringProperty tfg3;
    private final SimpleStringProperty tfg4;
    private final SimpleStringProperty tfg5;
    private final SimpleIntegerProperty expTFG1;
    private final SimpleIntegerProperty expTFG2;
    private final SimpleIntegerProperty expTFG3;
    private final SimpleIntegerProperty expTFG4;
    private final SimpleIntegerProperty expTFG5;
    private SimpleIntegerProperty ptosCreditos;
    private SimpleIntegerProperty ptosNotaMedia;
    private SimpleIntegerProperty ptosExperiencia;
    private SimpleIntegerProperty ptosTFG1;
    private SimpleIntegerProperty ptosTFG2;
    private SimpleIntegerProperty ptosTFG3;
    private SimpleIntegerProperty ptosTFG4;
    private SimpleIntegerProperty ptosTFG5;

    public Solicitud(String correoElectronico, double nota, double creditos, double mesesExperiencia, String meritos, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5, int expTfg1, int expTfg2, int expTfg3,int expTfg4, int expTfg5) {
        this.correoElectronico = new SimpleStringProperty(correoElectronico);
        this.notaMedia = new SimpleDoubleProperty(nota);
        this.creditosRestantes = new SimpleDoubleProperty(creditos);
        this.totalMesesExperiencia = new SimpleDoubleProperty(mesesExperiencia);
        this.meritos = new SimpleStringProperty(meritos);
        this.tfg1 = new SimpleStringProperty(tfg1);
        this.tfg2 = new SimpleStringProperty(tfg2);
        this.tfg3 = new SimpleStringProperty(tfg3);
        this.tfg4 = new SimpleStringProperty(tfg4);
        this.tfg5 = new SimpleStringProperty(tfg5);
        this.expTFG1 = new SimpleIntegerProperty(expTfg1);
        this.expTFG2 = new SimpleIntegerProperty(expTfg2);
        this.expTFG3 = new SimpleIntegerProperty(expTfg3);
        this.expTFG4 = new SimpleIntegerProperty(expTfg4);
        this.expTFG5 = new SimpleIntegerProperty(expTfg5);
    }

    public void calcularPuntuacion(Solicitud solicitud) {
        this.ptosTFG1 = new SimpleIntegerProperty(0);
        this.ptosTFG2 = new SimpleIntegerProperty(0);
        this.ptosTFG3 = new SimpleIntegerProperty(0);
        this.ptosTFG4 = new SimpleIntegerProperty(0);
        this.ptosTFG5 = new SimpleIntegerProperty(0);

        this.nia = new SimpleIntegerProperty(BDController.obtenerNiaPorCorreo(solicitud.getCorreoElectronico()));
        double creditosRestantes = solicitud.getCreditosRestantes();
        double notaMedia = solicitud.getNotaMedia();
        double[] experienciasTFG = {solicitud.getExpTFG1(), solicitud.getExpTFG2(), solicitud.getExpTFG3(), solicitud.getExpTFG4(), solicitud.getExpTFG5()};
        String[] tfgSeleccionados = {solicitud.getTfg1(), solicitud.getTfg2(), solicitud.getTfg3(), solicitud.getTfg4(), solicitud.getTfg5()};

        try (InputStream inputStream = getClass().getResourceAsStream("/config/puntuacion.json");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            BDController bdController = new BDController();

            // Leer el contenido del archivo JSON línea por línea
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }

            // Convertir el contenido leído en una cadena JSON
            String content = contentBuilder.toString();

            // Convertir la cadena JSON en un objeto JsonObject
            JsonObject puntacion = JsonParser.parseString(content).getAsJsonObject();

            // Obtener el array de rangos para los créditos para terminar
            JsonArray creditosParaTerminar = puntacion.getAsJsonArray("creditos_restantes");

            // Iterar sobre los rangos y encontrar el punto correspondiente para créditos restantes
            for (int i = 0; i < creditosParaTerminar.size(); i++) {
                JsonObject rango = creditosParaTerminar.get(i).getAsJsonObject();
                int inicio = rango.getAsJsonArray("rango").get(0).getAsInt();
                int fin = rango.getAsJsonArray("rango").get(1).getAsInt();
                if (inicio <= creditosRestantes && (fin == -1 || creditosRestantes <= fin)) {
                    int puntos = rango.get("puntos").getAsInt();
                    this.ptosCreditos = new SimpleIntegerProperty(puntos);
                    break;
                }
            }

            // Obtener el array de rangos para el expediente académico
            JsonArray expedienteAcademico = puntacion.getAsJsonArray("expediente_academico");

            // Iterar sobre los rangos y encontrar el punto correspondiente para la nota media
            for (int i = 0; i < expedienteAcademico.size(); i++) {
                JsonObject rango = expedienteAcademico.get(i).getAsJsonObject();
                String nota = rango.get("nota").getAsString();
                int puntos = rango.get("puntos").getAsInt();

                // Comparar la nota media con las definiciones del rango
                if (nota.equals("Sobresaliente") && notaMedia >= 9.0) {
                    this.ptosNotaMedia = new SimpleIntegerProperty(puntos);
                    break;
                } else if (nota.equals("Notable") && notaMedia >= 7.0) {
                    this.ptosNotaMedia = new SimpleIntegerProperty(puntos);
                    break;
                } else if (nota.equals("Aprobado") && notaMedia >= 5.0) {
                    this.ptosNotaMedia = new SimpleIntegerProperty(puntos);
                    break;
                }
            }

            //Obtener el array de rangos para la experiencia profesional
            JsonArray experienciaProfesional = puntacion.getAsJsonArray("experiencia_profesional");

            // Iterar sobre las experiencia TFG y buscar la puntuación correspondiente
            for (double experiencia : experienciasTFG) {
                for (int j = 0; j < experienciaProfesional.size(); j++) {
                    JsonObject rango = experienciaProfesional.get(j).getAsJsonObject();
                    int inicio = rango.getAsJsonArray("rango").get(0).getAsInt();
                    int fin = rango.getAsJsonArray("rango").get(1).getAsInt();
                    if (inicio <= experiencia && (fin == -1 || experiencia <= fin)) {
                        int puntos = rango.get("puntos").getAsInt();
                        this.ptosExperiencia = new SimpleIntegerProperty(puntos);
                        break;
                    }
                }
            }

            /*for (int i = 0; i < experienciasTFG.length; i++) {
                double experiencia = experienciasTFG[i];

                for (int j = 0; j < experienciaProfesional.size(); j++) {
                    JsonObject rango = experienciaProfesional.get(j).getAsJsonObject();
                    int inicio = rango.getAsJsonArray("rango").get(0).getAsInt();
                    int fin = rango.getAsJsonArray("rango").get(1).getAsInt();
                    if (inicio <= experiencia && (fin == -1 || experiencia <= fin)) {
                        int puntos = rango.get("puntos").getAsInt();
                        // Asignar los puntos a las propiedades correspondientes
                        switch (i) {
                            case 0:
                                this.ptosTFG1 = new SimpleIntegerProperty(puntos);
                                break;
                            case 1:
                                this.ptosTFG2 = new SimpleIntegerProperty(puntos);
                                break;
                            case 2:
                                this.ptosTFG3 = new SimpleIntegerProperty(puntos);
                                break;
                            case 3:
                                this.ptosTFG4 = new SimpleIntegerProperty(puntos);
                                break;
                            case 4:
                                this.ptosTFG5 = new SimpleIntegerProperty(puntos);
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }*/

            //Obtener el array de rangos para la experiencia profesional
            JsonArray asignaturasRelacionadasPuntuacion = puntacion.getAsJsonArray("asignaturas_relacionadas");

            for (int i = 0; i < tfgSeleccionados.length; i++) {
                String tfg = tfgSeleccionados[i];
                String asignaturasTFG = bdController.obtenerAsignaturasTFG(tfg).toLowerCase();

                System.out.println(tfg.toLowerCase() + " -> " + asignaturasTFG);

                // Dividir las asignaturas relacionadas en un array
                String[] asignaturas = asignaturasTFG.split(", ");

                // Iterar sobre las asignaturas relacionadas del TFG actual
                for (String asignatura : asignaturas) {
                    // Iterar sobre los rangos de puntuación para las asignaturas relacionadas
                    for (int j = 0; j < asignaturasRelacionadasPuntuacion.size(); j++) {
                        JsonObject rango = asignaturasRelacionadasPuntuacion.get(j).getAsJsonObject();
                        JsonArray notaRange = rango.getAsJsonArray("nota");
                        double notaMin = notaRange.get(0).getAsDouble();
                        double notaMax = notaRange.get(1).getAsDouble();
                        int puntos = rango.get("puntos").getAsInt();

                        // Comparar la nota de la asignatura con el rango de puntuación
                        // Si la nota está dentro del rango, asignar los puntos correspondientes
                        // al TFG actual
                        double notaAsignatura = obtenerNotaAsignatura(asignatura);
                        if (notaAsignatura >= notaMin && notaAsignatura <= notaMax) {
                            // Asignar los puntos al TFG correspondiente
                            switch (i) {
                                case 0:
                                    this.ptosTFG1 = new SimpleIntegerProperty(puntos + this.ptosTFG1.get());
                                    System.out.println("Puntos tfg1: " + this.ptosTFG1);
                                    break;
                                case 1:
                                    this.ptosTFG2 = new SimpleIntegerProperty(puntos + this.ptosTFG2.get());
                                    System.out.println("Puntos tfg2: " + this.ptosTFG2);
                                    break;
                                case 2:
                                    this.ptosTFG3 = new SimpleIntegerProperty(puntos + this.ptosTFG3.get());
                                    System.out.println("Puntos tfg3: " + this.ptosTFG3);
                                    break;
                                case 3:
                                    this.ptosTFG4 = new SimpleIntegerProperty(puntos + this.ptosTFG4.get());
                                    System.out.println("Puntos tfg4: " + this.ptosTFG4);
                                    break;
                                case 4:
                                    this.ptosTFG5 = new SimpleIntegerProperty(puntos + this.ptosTFG5.get());
                                    System.out.println("Puntos tfg5: " + this.ptosTFG5);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                    }
                }
            }

            // Insertar puntuaciones de los TFG en BBDD
            bdController.registrarPuntuacionSolicitud(this.nia.get(), this.tfg1.get(), this.ptosTFG1.get());
            bdController.registrarPuntuacionSolicitud(this.nia.get(), this.tfg2.get(), this.ptosTFG2.get());
            bdController.registrarPuntuacionSolicitud(this.nia.get(), this.tfg3.get(), this.ptosTFG3.get());
            bdController.registrarPuntuacionSolicitud(this.nia.get(), this.tfg4.get(), this.ptosTFG4.get());
            bdController.registrarPuntuacionSolicitud(this.nia.get(), this.tfg5.get(), this.ptosTFG5.get());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double obtenerNotaAsignatura(String asignatura) throws IOException {
        // Ruta del archivo del expediente
        String expedientePath = "/expedientes/" + this.nia.get() + ".txt";

        // Abrir el archivo del expediente
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(expedientePath))))) {
            String linea;
            // Iterar sobre las líneas del archivo
            while ((linea = br.readLine()) != null) {
                // Separar la línea en sus componentes
                String[] partes = linea.split(" ");
                // Construir la asignatura de la línea
                StringBuilder asignaturaEnArchivoBuilder = new StringBuilder();
                for (int i = 1; i < partes.length; i++) {
                    asignaturaEnArchivoBuilder.append(partes[i]);
                    if (i < partes.length - 1) {
                        asignaturaEnArchivoBuilder.append(" "); // Agregar espacio entre palabras
                    }
                }
                String asignaturaEnArchivo = asignaturaEnArchivoBuilder.toString().trim();

                // Comparar los primeros caracteres de cada palabra en la asignatura del archivo con los primeros caracteres de la asignatura buscada
                boolean coincidencia = true;
                String[] palabrasAsignatura = asignatura.split(" ");
                for (String palabra : palabrasAsignatura) {
                    if (!asignaturaEnArchivo.toLowerCase().contains(palabra.toLowerCase())) {
                        coincidencia = false;
                        break;
                    }
                }
                // Si hay coincidencia, devolver la nota de esa asignatura
                if (coincidencia) {
                    System.out.println("Se encontró la asignatura: " + asignatura);
                    int indiceNota = partes.length - 3; // Obtener el índice de la nota
                    System.out.println(partes[indiceNota]);
                    return Double.parseDouble(partes[indiceNota]); // La nota está 5 posiciones antes del final
                }
            }
        }
        // Si no se encuentra la asignatura en el expediente, devolver 0.0
        System.out.println("No se encontró la asignatura: " + asignatura);
        return 0.0;
    }

    public String getCorreoElectronico() {
        return correoElectronico.get();
    }

    public SimpleStringProperty correoElectronicoProperty() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico.set(correoElectronico);
    }

    public String getTfg1() {
        return tfg1.get();
    }

    public SimpleStringProperty tfg1Property() {
        return tfg1;
    }

    public void setTfg1(String tfg1) {
        this.tfg1.set(tfg1);
    }

    public String getTfg2() {
        return tfg2.get();
    }

    public SimpleStringProperty tfg2Property() {
        return tfg2;
    }

    public void setTfg2(String tfg2) {
        this.tfg2.set(tfg2);
    }

    public String getTfg3() {
        return tfg3.get();
    }

    public SimpleStringProperty tfg3Property() {
        return tfg3;
    }

    public void setTfg3(String tfg3) {
        this.tfg3.set(tfg3);
    }

    public String getTfg4() {
        return tfg4.get();
    }

    public SimpleStringProperty tfg4Property() {
        return tfg4;
    }

    public void setTfg4(String tfg4) {
        this.tfg4.set(tfg4);
    }

    public String getTfg5() {
        return tfg5.get();
    }

    public SimpleStringProperty tfg5Property() {
        return tfg5;
    }

    public void setTfg5(String tfg5) {
        this.tfg5.set(tfg5);
    }

    public double getNotaMedia() {
        return notaMedia.get();
    }

    public SimpleDoubleProperty notaMediaProperty() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia.set(notaMedia);
    }

    public double getCreditosRestantes() {
        return creditosRestantes.get();
    }

    public SimpleDoubleProperty creditosRestantesProperty() {
        return creditosRestantes;
    }

    public void setCreditosRestantes(double creditosRestantes) {
        this.creditosRestantes.set(creditosRestantes);
    }

    public double getTotalMesesExperiencia() {
        return totalMesesExperiencia.get();
    }

    public SimpleDoubleProperty totalMesesExperienciaProperty() {
        return totalMesesExperiencia;
    }

    public void setTotalMesesExperiencia(double totalMesesExperiencia) {
        this.totalMesesExperiencia.set(totalMesesExperiencia);
    }

    public String isMeritos() {
        return meritos.get();
    }

    public SimpleStringProperty meritosProperty() {
        return meritos;
    }

    public void setMeritos(String meritos) {
        this.meritos.set(meritos);
    }

    public int getExpTFG1() {
        return expTFG1.get();
    }

    public SimpleIntegerProperty expTFG1Property() {
        return expTFG1;
    }

    public void setExpTFG1(int expTFG1) {
        this.expTFG1.set(expTFG1);
    }

    public int getExpTFG2() {
        return expTFG2.get();
    }

    public SimpleIntegerProperty expTFG2Property() {
        return expTFG2;
    }

    public void setExpTFG2(int expTFG2) {
        this.expTFG2.set(expTFG2);
    }

    public int getExpTFG3() {
        return expTFG3.get();
    }

    public SimpleIntegerProperty expTFG3Property() {
        return expTFG3;
    }

    public void setExpTFG3(int expTFG3) {
        this.expTFG3.set(expTFG3);
    }

    public int getExpTFG4() {
        return expTFG4.get();
    }

    public SimpleIntegerProperty expTFG4Property() {
        return expTFG4;
    }

    public void setExpTFG4(int expTFG4) {
        this.expTFG4.set(expTFG4);
    }

    public int getExpTFG5() {
        return expTFG5.get();
    }

    public SimpleIntegerProperty expTFG5Property() {
        return expTFG5;
    }

    public void setExpTFG5(int expTFG5) {
        this.expTFG5.set(expTFG5);
    }

    public int getNia() {
        return nia.get();
    }

    public SimpleIntegerProperty niaProperty() {
        return nia;
    }

    public void setNia(int nia) {
        this.nia.set(nia);
    }

    public String getMeritos() {
        return meritos.get();
    }

    public int getPtosCreditos() {
        return ptosCreditos.get();
    }

    public SimpleIntegerProperty ptosCreditosProperty() {
        return ptosCreditos;
    }

    public void setPtosCreditos(int ptosCreditos) {
        this.ptosCreditos.set(ptosCreditos);
    }

    public int getPtosNotaMedia() {
        return ptosNotaMedia.get();
    }

    public SimpleIntegerProperty ptosNotaMediaProperty() {
        return ptosNotaMedia;
    }

    public void setPtosNotaMedia(int ptosNotaMedia) {
        this.ptosNotaMedia.set(ptosNotaMedia);
    }

    public int getPtosExperiencia() {
        return ptosExperiencia.get();
    }

    public SimpleIntegerProperty ptosExperienciaProperty() {
        return ptosExperiencia;
    }

    public void setPtosExperiencia(int ptosExperiencia) {
        this.ptosExperiencia.set(ptosExperiencia);
    }

    public int getPtosTFG1() {
        return ptosTFG1.get();
    }

    public SimpleIntegerProperty ptosTFG1Property() {
        return ptosTFG1;
    }

    public void setPtosTFG1(int ptosTFG1) {
        this.ptosTFG1.set(ptosTFG1);
    }

    public int getPtosTFG2() {
        return ptosTFG2.get();
    }

    public SimpleIntegerProperty ptosTFG2Property() {
        return ptosTFG2;
    }

    public void setPtosTFG2(int ptosTFG2) {
        this.ptosTFG2.set(ptosTFG2);
    }

    public int getPtosTFG3() {
        return ptosTFG3.get();
    }

    public SimpleIntegerProperty ptosTFG3Property() {
        return ptosTFG3;
    }

    public void setPtosTFG3(int ptosTFG3) {
        this.ptosTFG3.set(ptosTFG3);
    }

    public int getPtosTFG4() {
        return ptosTFG4.get();
    }

    public SimpleIntegerProperty ptosTFG4Property() {
        return ptosTFG4;
    }

    public void setPtosTFG4(int ptosTFG4) {
        this.ptosTFG4.set(ptosTFG4);
    }

    public int getPtosTFG5() {
        return ptosTFG5.get();
    }

    public SimpleIntegerProperty ptosTFG5Property() {
        return ptosTFG5;
    }

    public void setPtosTFG5(int ptosTFG5) {
        this.ptosTFG5.set(ptosTFG5);
    }
}
