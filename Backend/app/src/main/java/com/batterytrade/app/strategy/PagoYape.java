package com.batterytrade.app.strategy;

public class PagoYape implements PagoStrategy {

    @Override
    public void procesarPago(double monto) {

        System.out.println("Pago por Yape: S/" + monto);
    }
}