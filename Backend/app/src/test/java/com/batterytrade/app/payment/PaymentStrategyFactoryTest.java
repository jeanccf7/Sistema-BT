package com.batterytrade.app.payment;

import com.batterytrade.app.model.Venta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentStrategyFactoryTest {

    @Test
    void shouldReturnMessageForYape() {
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy("YAPE");
        Venta venta = new Venta();
        venta.setId(7L);

        String mensaje = strategy.handle(venta);

        assertEquals("Procesando pago YAPE para la venta 7", mensaje);
    }
}
