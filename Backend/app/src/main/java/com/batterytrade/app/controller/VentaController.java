package com.batterytrade.app.controller;

import com.batterytrade.app.dto.VentaDTO;
import com.batterytrade.app.model.Venta;
import com.batterytrade.app.service.VentaService;
import jakarta.validation.Valid;
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
    public List<Venta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Venta buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
    
     @PostMapping
      public Venta registrar(@RequestBody Venta venta) {
      return service.registrarVenta(venta);
      }
    


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}