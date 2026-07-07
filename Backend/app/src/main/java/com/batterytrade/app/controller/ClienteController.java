package com.batterytrade.app.controller;

import com.batterytrade.app.model.Cliente;
import com.batterytrade.app.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(
            ClienteService service) {

        this.service = service;
    }


    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
    @PostMapping
    public Cliente guardar(
            @Valid @RequestBody Cliente cliente) {
        return service.guardar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Cliente cliente) {

        cliente.setId(id);
        return service.guardar(cliente);
    }
    
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
    service.eliminar(id);
    }
}