package com.david.gestiontfg.modelos;

import java.time.LocalDateTime;

public class Usuario {
    private String correo;
    private LocalDateTime ultimoInicioSesion;

    public Usuario(String correo, LocalDateTime ultimoInicioSesion) {
        this.correo = correo;
        this.ultimoInicioSesion = ultimoInicioSesion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDateTime getUltimoInicioSesion() {
        return ultimoInicioSesion;
    }

    public void setUltimoInicioSesion(LocalDateTime ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }
}
