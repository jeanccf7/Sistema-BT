package com.batterytrade.app.controller;

import com.batterytrade.app.dto.VentaDTO;
import com.batterytrade.app.model.Venta;
import com.batterytrade.app.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<VentaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public VentaDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public VentaDTO registrar(
            @Valid @RequestBody VentaDTO ventaDTO) {
        return service.registrar(ventaDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
