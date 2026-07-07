package com.batterytrade.app.factory;

import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.model.Cliente;
import com.batterytrade.app.model.Venta;
import java.time.LocalDate;

/**
 * Factory para crear instancias de Venta.
 * Simplifica la creación centralizando valores por defecto.
 */
public class VentaFactory {

    public static Venta create(Cliente cliente, Usuario vendedor){
        Venta venta = new Venta();
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        return venta;
    }
}
