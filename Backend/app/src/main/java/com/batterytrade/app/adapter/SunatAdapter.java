package com.batterytrade.app.adapter;

import com.batterytrade.app.model.Venta;

public class SunatAdapter {

    private final SunatExterna sunat;

    public SunatAdapter(SunatExterna sunat) {
        this.sunat = sunat;
    }

    public String buscarCliente(String dni) {
        return sunat.consultarDni(dni);
    }

    public String enviarFactura(Venta venta) {
        return String.format("SUNAT adapter: comprobante enviado para la venta %d", venta.getId());
    }
}