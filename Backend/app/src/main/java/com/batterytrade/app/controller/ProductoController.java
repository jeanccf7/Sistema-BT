package com.batterytrade.app.controller;

import com.batterytrade.app.model.Producto;
import com.batterytrade.app.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(
            ProductoService service) {

        this.service = service;
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Producto buscar(
            @PathVariable Long id) {

        return service.buscar(id);
    }

    @PostMapping
public Producto guardar(
        @Valid
        @RequestBody Producto producto) {

    return service.guardar(producto);
}

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        service.eliminar(id);
    }
}