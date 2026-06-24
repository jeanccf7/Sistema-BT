package com.batterytrade.app.strategy;

public class PagoEfectivo implements PagoStrategy {

    @Override
    public void procesarPago(double monto) {

        System.out.println("Pago en efectivo: S/" + monto);
    }
}