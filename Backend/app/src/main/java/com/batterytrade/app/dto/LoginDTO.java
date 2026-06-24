package com.batterytrade.app.dto;

public class LoginDTO {

    private String correo;
    private String password;
    
    // getters y setters
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}