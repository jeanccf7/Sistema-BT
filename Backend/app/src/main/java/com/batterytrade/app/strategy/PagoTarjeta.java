package com.batterytrade.app.strategy;

public class PagoTarjeta implements PagoStrategy {

    @Override
    public void procesarPago(double monto) {

        System.out.println("Pago con tarjeta: S/" + monto);
    }
}