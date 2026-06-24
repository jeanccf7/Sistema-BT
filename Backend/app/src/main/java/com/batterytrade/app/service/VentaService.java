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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    

    public VentaService(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository   ) {

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

    public Venta registrarVenta(Venta venta) {

        double total = 0;

        venta.setFecha(LocalDate.now());

        for (DetalleVenta detalle : venta.getDetalles()) {

            Producto producto = productoRepository
                    .findById(detalle.getProducto().getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Producto no encontrado"));

            if (producto.getStock() < detalle.getCantidad()) {

                throw new RuntimeException(
                        "Stock insuficiente para "
                                + producto.getNombre());
            }

            detalle.setPrecioUnitario(
                    producto.getPrecio());

            detalle.setSubtotal(
                    detalle.getCantidad()
                            * producto.getPrecio());

            producto.setStock(
                    producto.getStock()
                            - detalle.getCantidad());

            productoRepository.save(producto);

            total += detalle.getSubtotal();
        }

        venta.setTotal(total);

        return ventaRepository.save(venta);
    }

    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    public VentaDTO registrar(VentaDTO ventaDTO) {

    Cliente cliente = clienteRepository
            .findById(ventaDTO.getClienteId())
            .orElseThrow(() ->
                    new RuntimeException("Cliente no encontrado"));

    Usuario vendedor = usuarioRepository
            .findById(ventaDTO.getVendedorId())
            .orElseThrow(() ->
                    new RuntimeException("Vendedor no encontrado"));

    Venta venta = new Venta();

    venta.setFecha(LocalDate.now());
    venta.setCliente(cliente);
    venta.setVendedor(vendedor);

    List<DetalleVenta> detalles = new ArrayList<>();

    double total = 0;

    for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {

        Producto producto = productoRepository
                .findById(detalleDTO.getProductoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado"));

        if (producto.getStock() < detalleDTO.getCantidad()) {

            throw new RuntimeException(
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

    return respuesta;
}
}