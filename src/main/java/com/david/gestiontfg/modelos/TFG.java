package com.david.gestiontfg.modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TFG {
    private final SimpleStringProperty codigo;
    private final SimpleStringProperty titulo;
    private final SimpleStringProperty descripcion;
    private final SimpleStringProperty tutor;
    private final SimpleStringProperty asignaturas;
    private final SimpleIntegerProperty solicitantes;
    private final SimpleIntegerProperty adjudicado;

    public TFG(String codigo, String titulo, String descripcion, String tutor, String asignaturas, int solicitantes, int adjudicado) {
        this.codigo = new SimpleStringProperty(codigo);
        this.titulo = new SimpleStringProperty(titulo);
        this.tutor = new SimpleStringProperty(tutor);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.asignaturas = new SimpleStringProperty(asignaturas);
        this.solicitantes = new SimpleIntegerProperty(solicitantes);
        this.adjudicado = new SimpleIntegerProperty(adjudicado);
    }

    public String getCodigo() {
        return codigo.get();
    }

    public SimpleStringProperty codigoProperty() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public SimpleStringProperty tituloProperty() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getTutor() {
        return tutor.get();
    }

    public SimpleStringProperty tutorProperty() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor.set(tutor);
    }

    public String getAsignaturas() {
        return asignaturas.get();
    }

    public SimpleStringProperty asignaturasProperty() {
        return asignaturas;
    }

    public void setAsignaturas(String asignaturas) {
        this.asignaturas.set(asignaturas);
    }

    public int getSolicitantes() {
        return solicitantes.get();
    }

    public SimpleIntegerProperty solicitantesProperty() {
        return solicitantes;
    }

    public void setSolicitantes(int solicitantes) {
        this.solicitantes.set(solicitantes);
    }

    public int getAdjudicado() {
        return adjudicado.get();
    }

    public SimpleIntegerProperty adjudicadoProperty() {
        return adjudicado;
    }

    public void setAdjudicado(int adjudicado) {
        this.adjudicado.set(adjudicado);
    }
}
