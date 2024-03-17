package com.david.gestiontfg.modelos;

import javafx.beans.property.*;

public class Solicitud {
    private final SimpleStringProperty correoElectronico;
    private final SimpleDoubleProperty notaMedia;
    private final SimpleDoubleProperty creditosRestantes;
    private final SimpleDoubleProperty totalMesesExperiencia;
    private final SimpleStringProperty meritos;
    private final SimpleStringProperty tfg1;
    private final SimpleIntegerProperty expTFG1;
    private final SimpleStringProperty tfg2;
    private final SimpleIntegerProperty expTFG2;
    private final SimpleStringProperty tfg3;
    private final SimpleIntegerProperty expTFG3;
    private final SimpleStringProperty tfg4;
    private final SimpleIntegerProperty expTFG4;
    private final SimpleStringProperty tfg5;
    private final SimpleIntegerProperty expTFG5;


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

    public Solicitud(String correoElectronico, String tfg1, String tfg2, String tfg3, String tfg4, String tfg5) {
        this.correoElectronico = new SimpleStringProperty(correoElectronico);
        this.notaMedia = new SimpleDoubleProperty(0.0);
        this.creditosRestantes = new SimpleDoubleProperty(0);
        this.totalMesesExperiencia = new SimpleDoubleProperty(0);
        this.meritos = new SimpleStringProperty("NO");
        this.tfg1 = new SimpleStringProperty(tfg1);
        this.tfg2 = new SimpleStringProperty(tfg2);
        this.tfg3 = new SimpleStringProperty(tfg3);
        this.tfg4 = new SimpleStringProperty(tfg4);
        this.tfg5 = new SimpleStringProperty(tfg5);
        this.expTFG1 = new SimpleIntegerProperty(0);
        this.expTFG2 = new SimpleIntegerProperty(0);
        this.expTFG3 = new SimpleIntegerProperty(0);
        this.expTFG4 = new SimpleIntegerProperty(0);
        this.expTFG5 = new SimpleIntegerProperty(0);
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
}
