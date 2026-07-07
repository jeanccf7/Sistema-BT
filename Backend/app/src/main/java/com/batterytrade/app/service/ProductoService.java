package com.batterytrade.app.service;

import com.batterytrade.app.exception.BusinessException;
import com.batterytrade.app.exception.ResourceNotFoundException;
import com.batterytrade.app.model.Producto;
import com.batterytrade.app.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(
            ProductoRepository repository) {

        this.repository = repository;
    }

    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new BusinessException("El producto no puede ser nulo");
        }

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }

        if (producto.getPrecio() <= 0) {
            throw new BusinessException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }

        if (producto.getCategoria() == null
                || producto.getCategoria().getId() == null
                || producto.getCategoria().getId() == 0) {
            throw new BusinessException("Debe seleccionar una categoría");
        }

        return repository.save(producto);
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        repository.deleteById(id);
    }
}