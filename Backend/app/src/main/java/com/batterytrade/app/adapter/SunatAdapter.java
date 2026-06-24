package com.batterytrade.app.adapter;

public class SunatAdapter {

    private SunatExterna sunat;

    public SunatAdapter(SunatExterna sunat) {
        this.sunat = sunat;
    }

    public String buscarCliente(String dni) {
        return sunat.consultarDni(dni);
    }
}