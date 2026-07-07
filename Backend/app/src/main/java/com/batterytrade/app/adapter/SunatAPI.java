package com.batterytrade.app.adapter;

import com.batterytrade.app.model.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter.
 * Este adaptador representa la integración con un servicio externo (SUNAT).
 * Actualmente realiza una acción simulada y escribe en el logger nada más.
 */
public class SunatAPI {

	private static final Logger log = LoggerFactory.getLogger(SunatAPI.class);

	public boolean sendInvoice(Venta venta) {
		// Aquí se adaptaría la estructura interna a la externa requerida por SUNAT.
		// Implementación simplificada: registramos mediante logger que se "envió".
		log.info("[SunatAPI Adapter] Enviando comprobante para venta id={}", venta.getId());
		return true;
	}
}
