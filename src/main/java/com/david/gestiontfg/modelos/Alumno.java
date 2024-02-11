package com.david.gestiontfg.modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Alumno {
    private final SimpleIntegerProperty idUcam;
    private final SimpleIntegerProperty nombre;
    private final SimpleStringProperty apellido1;
    private final SimpleStringProperty apellido2;
    private final SimpleStringProperty correo;
    private final SimpleIntegerProperty NIA;
    private final SimpleStringProperty expediente;

    public Alumno(int idUcam, int nombre, String apellido1, String apellido2, String correo, int NIA, String expediente) {
        this.idUcam = new SimpleIntegerProperty(idUcam);
        this.nombre = new SimpleIntegerProperty(nombre);
        this.apellido1 = new SimpleStringProperty(apellido1);
        this.apellido2 = new SimpleStringProperty(apellido2);
        this.correo = new SimpleStringProperty(correo);
        this.NIA = new SimpleIntegerProperty(NIA);
        this.expediente = new SimpleStringProperty(expediente);
    }

    public int getIdUcam() {
        return idUcam.get();
    }

    public SimpleIntegerProperty idUcamProperty() {
        return idUcam;
    }

    public void setIdUcam(int idUcam) {
        this.idUcam.set(idUcam);
    }

    public int getNombre() {
        return nombre.get();
    }

    public SimpleIntegerProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre.set(nombre);
    }

    public String getApellido1() {
        return apellido1.get();
    }

    public SimpleStringProperty apellido1Property() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1.set(apellido1);
    }

    public String getApellido2() {
        return apellido2.get();
    }

    public SimpleStringProperty apellido2Property() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2.set(apellido2);
    }

    public String getCorreo() {
        return correo.get();
    }

    public SimpleStringProperty correoProperty() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public SimpleIntegerProperty niaProperty() {
        return NIA;
    }

    public SimpleIntegerProperty NIAProperty() {
        return NIA;
    }

    public void setNIA(int NIA) {
        this.NIA.set(NIA);
    }

    public int getNIA() {
        return NIA.get();
    }

    public String getExpediente() {
        return expediente.get();
    }

    public SimpleStringProperty expedienteProperty() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente.set(expediente);
    }
}
