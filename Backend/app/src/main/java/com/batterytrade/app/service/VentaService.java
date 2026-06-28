package com.batterytrade.app.service;

import com.batterytrade.app.dto.DetalleVentaDTO;
import com.batterytrade.app.dto.VentaDTO;
import com.batterytrade.app.model.Cliente;
import com.batterytrade.app.model.DetalleVenta;
import com.batterytrade.app.model.Producto;
import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.model.Venta;
import com.batterytrade.app.repository.ClienteRepository;
import com.batterytrade.app.repository.ProductoRepository;
import com.batterytrade.app.repository.UsuarioRepository;
import com.batterytrade.app.repository.VentaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

private final VentaRepository ventaRepository;
private final ProductoRepository productoRepository;
private final ClienteRepository clienteRepository;
private final UsuarioRepository usuarioRepository;

public VentaService(
        VentaRepository ventaRepository,
        ProductoRepository productoRepository,
        ClienteRepository clienteRepository,
        UsuarioRepository usuarioRepository) {

    this.ventaRepository = ventaRepository;
    this.productoRepository = productoRepository;
    this.clienteRepository = clienteRepository;
    this.usuarioRepository = usuarioRepository;
}

public List<Venta> listar() {
    return ventaRepository.findAll();
}

public Venta buscar(Long id) {
    return ventaRepository.findById(id)
            .orElse(null);
}

public void eliminar(Long id) {
    ventaRepository.deleteById(id);
}

@Transactional
public VentaDTO registrar(VentaDTO ventaDTO) {

    if (ventaDTO.getClienteId() == null) {
        throw new IllegalArgumentException("Debe seleccionar un cliente");
    }

    if (ventaDTO.getVendedorId() == null) {
        throw new IllegalArgumentException("Debe seleccionar un vendedor");
    }

    if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
        throw new IllegalArgumentException("Debe agregar al menos un producto");
    }

    Cliente cliente = clienteRepository
            .findById(ventaDTO.getClienteId())
            .orElseThrow(() ->
                    new IllegalArgumentException("Cliente no encontrado"));

    Usuario vendedor = usuarioRepository
            .findById(ventaDTO.getVendedorId())
            .orElseThrow(() ->
                    new IllegalArgumentException("Vendedor no encontrado"));

    Venta venta = new Venta();

    venta.setFecha(LocalDate.now());

    venta.setCliente(cliente);

    venta.setVendedor(vendedor);

    venta.setEstadoPago(
            ventaDTO.getEstadoPago() == null
                    ? "PENDIENTE"
                    : ventaDTO.getEstadoPago());

    venta.setEstadoEntrega(
            ventaDTO.getEstadoEntrega() == null
                    ? "PENDIENTE"
                    : ventaDTO.getEstadoEntrega());

    venta.setMetodoPago(
            ventaDTO.getMetodoPago() == null
                    ? "EFECTIVO"
                    : ventaDTO.getMetodoPago());

    
    List<DetalleVenta> detalles = new ArrayList<>();

    double total = 0;
    
    for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {

        if (detalleDTO.getProductoId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto");
        }

        if (detalleDTO.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Producto producto = productoRepository
                .findById(detalleDTO.getProductoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < detalleDTO.getCantidad()) {

            throw new IllegalArgumentException(
                    "Stock insuficiente para "
                            + producto.getNombre());
        }

        DetalleVenta detalle = new DetalleVenta();

        detalle.setVenta(venta);

        detalle.setProducto(producto);

        detalle.setCantidad(
                detalleDTO.getCantidad());

        detalle.setPrecioUnitario(
                producto.getPrecio());

        detalle.setSubtotal(
                detalleDTO.getCantidad()
                        * producto.getPrecio());

        producto.setStock(
                producto.getStock()
                        - detalleDTO.getCantidad());

        productoRepository.save(producto);

        total += detalle.getSubtotal();

        detalles.add(detalle);
    }

    venta.setDetalles(detalles);

    venta.setTotal(total);

    Venta ventaGuardada =
            ventaRepository.save(venta);

    VentaDTO respuesta = new VentaDTO();

    respuesta.setId(
            ventaGuardada.getId());

    respuesta.setFecha(
            ventaGuardada.getFecha());

    respuesta.setClienteId(
            cliente.getId());

    respuesta.setNombreCliente(
            cliente.getNombre());

    respuesta.setVendedorId(
            vendedor.getId());

    respuesta.setNombreVendedor(
            vendedor.getNombre());

    respuesta.setTotal(
            ventaGuardada.getTotal());

    respuesta.setEstadoPago(
            ventaGuardada.getEstadoPago());

    respuesta.setEstadoEntrega(
            ventaGuardada.getEstadoEntrega());

    respuesta.setMetodoPago(
            ventaGuardada.getMetodoPago());

    return respuesta;
}


}
