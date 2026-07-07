package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YapeStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(YapeStrategy.class);

    @Override
    public void handle(Venta venta) {
        // Simulación: en Yape marcamos como pagado automáticamente.
        log.info("[PaymentStrategy] Yape procesado para venta id={}", venta.getId());
    }
}
