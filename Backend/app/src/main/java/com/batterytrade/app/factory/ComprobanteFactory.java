package com.batterytrade.app.factory;

public class ComprobanteFactory {

    public static Comprobante crear(
            String tipo) {

        switch (tipo.toUpperCase()) {

            case "FACTURA":
                return new Factura();

            case "BOLETA":
                return new Boleta();

            default:
                throw new IllegalArgumentException(
                        "Tipo inválido");
        }
    }
}