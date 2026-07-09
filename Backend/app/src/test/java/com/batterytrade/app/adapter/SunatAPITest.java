package com.batterytrade.app.adapter;

import com.batterytrade.app.model.Venta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SunatAPITest {

    @Test
    void sendInvoiceShouldReturnMessage() {
        SunatAPI sunatAPI = new SunatAPI();
        Venta venta = new Venta();
        venta.setId(42L);

        String mensaje = sunatAPI.sendInvoice(venta);

        assertTrue(mensaje.contains("SUNAT"));
        assertTrue(mensaje.contains("42"));
    }
}
