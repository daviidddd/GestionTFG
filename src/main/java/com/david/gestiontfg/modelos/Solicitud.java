package com.david.gestiontfg.modelos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Solicitud {
    private final SimpleStringProperty correoElectronico;
    private final SimpleStringProperty tfg1;
    private final SimpleStringProperty tfg2;
    private final SimpleStringProperty tfg3;
    private final SimpleStringProperty tfg4;
    private final SimpleStringProperty tfg5;

    public Solicitud(String correoElectronico, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5) {
        this.correoElectronico = new SimpleStringProperty(correoElectronico);
        this.tfg1 = new SimpleStringProperty(tfg1);
        this.tfg2 = new SimpleStringProperty(tfg2);
        this.tfg3 = new SimpleStringProperty(tfg3);
        this.tfg4 = new SimpleStringProperty(tfg4);
        this.tfg5 = new SimpleStringProperty(tfg5);
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
}
