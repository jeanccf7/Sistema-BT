package com.batterytrade.app.service;

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
        if (producto.getCategoria() == null ||
        producto.getCategoria().getId() == null ||
        producto.getCategoria().getId() == 0) {

        throw new IllegalArgumentException(
                "Debe seleccionar una categoría");
    }
        return repository.save(producto);
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}