package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EfectivoStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(EfectivoStrategy.class);

    @Override
    public void handle(Venta venta) {
        // Para pagos en efectivo no aplicamos comisiones.
        log.info("[PaymentStrategy] Efectivo seleccionado para venta id={}", venta.getId());
    }
}
