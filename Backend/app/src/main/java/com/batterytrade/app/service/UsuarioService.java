package com.batterytrade.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(
            UsuarioRepository repository) {

        this.repository = repository;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario guardar(
            Usuario usuario) {

        return repository.save(usuario);
    }
}