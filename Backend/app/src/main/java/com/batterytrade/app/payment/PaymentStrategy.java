package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;

/**
 * Strategy - interfaz para manejar pagos.
 */
public interface PaymentStrategy {
    void handle(Venta venta);
}
