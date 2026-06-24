package com.batterytrade.app.factory;

public class Boleta
        implements Comprobante {

    @Override
    public String generar() {
        return "Boleta generada";
    }
}