package com.batterytrade.app.service;

import com.batterytrade.app.dto.DetalleVentaDTO;
import com.batterytrade.app.dto.VentaDTO;
import com.batterytrade.app.model.Cliente;
import com.batterytrade.app.model.DetalleVenta;
import com.batterytrade.app.model.Producto;
import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.model.Venta;
import com.batterytrade.app.exception.ResourceNotFoundException;
import com.batterytrade.app.repository.ClienteRepository;
import com.batterytrade.app.repository.ProductoRepository;
import com.batterytrade.app.repository.UsuarioRepository;
import com.batterytrade.app.repository.VentaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.batterytrade.app.factory.VentaFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    // La transacción agrupa la creación de la venta y la actualización del stock.

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

@Transactional(readOnly = true)
public List<VentaDTO> listar() {
    return ventaRepository.findAll().stream()
            .map(this::mapToDto)
            .toList();
}

@Transactional(readOnly = true)
public VentaDTO buscar(Long id) {
    Venta venta = ventaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
    return mapToDto(venta);
}

public void eliminar(Long id) {
    if (!ventaRepository.existsById(id)) {
        throw new ResourceNotFoundException("Venta no encontrada");
    }
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

        Venta venta = VentaFactory.create(cliente, vendedor);

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

        detalle.setSubtotal(detalle.calcularSubtotal());

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

private VentaDTO mapToDto(Venta venta) {
    VentaDTO dto = new VentaDTO();
    dto.setId(venta.getId());
    dto.setFecha(venta.getFecha());

    if (venta.getCliente() != null) {
        dto.setClienteId(venta.getCliente().getId());
        dto.setNombreCliente(
                venta.getCliente().getNombre() + " " + venta.getCliente().getApellido());
    }

    if (venta.getVendedor() != null) {
        dto.setVendedorId(venta.getVendedor().getId());
        dto.setNombreVendedor(
                venta.getVendedor().getNombre() + " " + venta.getVendedor().getApellido());
    }

    dto.setTotal(venta.getTotal());
    dto.setEstadoPago(venta.getEstadoPago());
    dto.setEstadoEntrega(venta.getEstadoEntrega());
    dto.setMetodoPago(venta.getMetodoPago());
    dto.setObservacion(venta.getObservacion());

    return dto;
}

}
