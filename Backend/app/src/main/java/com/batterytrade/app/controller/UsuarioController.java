package com.batterytrade.app.controller;

import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(
            UsuarioService service) {

        this.service = service;
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @PostMapping
    public Usuario guardar(
            @Valid @RequestBody Usuario usuario) {
        return service.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {

        usuario.setId(id);
        return service.guardar(usuario);
    }
}
