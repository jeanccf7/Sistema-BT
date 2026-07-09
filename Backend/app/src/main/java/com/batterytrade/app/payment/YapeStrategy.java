package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YapeStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(YapeStrategy.class);

    @Override
    public String handle(Venta venta) {
        String mensaje = String.format("Procesando pago YAPE para la venta %d", venta.getId());
        log.info("[PaymentStrategy] {}", mensaje);
        return mensaje;
    }
}
