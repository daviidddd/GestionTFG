package com.david.gestiontfg.modelos;

import javafx.beans.property.SimpleStringProperty;

public class Tutor {

    private final SimpleStringProperty tutor;

    public Tutor(String tutor) {
        this.tutor = new SimpleStringProperty(tutor);
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
}
