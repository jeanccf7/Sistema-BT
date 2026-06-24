package com.batterytrade.app.controller;

import com.batterytrade.app.model.Cliente;
import com.batterytrade.app.service.ClienteService;

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

    @PostMapping
    public Cliente guardar(
            @RequestBody Cliente cliente) {

        return service.guardar(cliente);
    }
}