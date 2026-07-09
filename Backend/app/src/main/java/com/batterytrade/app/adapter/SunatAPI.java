package com.batterytrade.app.adapter;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter simulado para la integración con un servicio externo (SUNAT).
 */
public class SunatAPI {

    private static final Logger log = LoggerFactory.getLogger(SunatAPI.class);
    private final SunatAdapter adapter;

    public SunatAPI() {
        this(new SunatAdapter(new SunatExterna()));
    }

    public SunatAPI(SunatAdapter adapter) {
        this.adapter = adapter;
    }

    public String sendInvoice(Venta venta) {
        String mensaje = adapter.enviarFactura(venta);
        log.info("[SunatAPI Adapter] {}", mensaje);
        return mensaje;
    }
}
