package com.batterytrade.app.strategy;

public class PagoPlin implements PagoStrategy {

    @Override
    public void procesarPago(double monto) {
        System.out.println("Pago con Plin: S/" + monto);
    }

}
