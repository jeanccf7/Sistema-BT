package com.batterytrade.app.factory;

public class Factura
        implements Comprobante {

    @Override
    public String generar() {
        return "Factura generada";
    }
}