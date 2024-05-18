package com.david.gestiontfg.modelos;

import javafx.beans.property.SimpleStringProperty;

public class Asignatura {

    private final SimpleStringProperty nombre;

    public Asignatura(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }
}
