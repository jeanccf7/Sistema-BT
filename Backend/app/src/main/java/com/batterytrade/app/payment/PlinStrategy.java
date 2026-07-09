package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlinStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(PlinStrategy.class);

    @Override
    public String handle(Venta venta) {
        String mensaje = String.format("Procesando pago PLIN para la venta %d", venta.getId());
        log.info("[PaymentStrategy] {}", mensaje);
        return mensaje;
    }
}
