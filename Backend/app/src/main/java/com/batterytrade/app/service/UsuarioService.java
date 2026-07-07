package com.batterytrade.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batterytrade.app.exception.BusinessException;
import com.batterytrade.app.exception.ResourceNotFoundException;
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

    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    // Se centraliza la validación para evitar duplicados y entradas incompletas.
    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessException("El usuario no puede ser nulo");
        }

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new BusinessException("El correo es obligatorio");
        }

        String correoNormalizado = usuario.getCorreo().trim();
        repository.findByCorreo(correoNormalizado)
                .filter(existente -> !existente.getId().equals(usuario.getId()))
                .ifPresent(existente -> {
                    throw new BusinessException("Ya existe un usuario con ese correo");
                });

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new BusinessException("La contraseña es obligatoria");
        }

        usuario.setCorreo(correoNormalizado);
        return repository.save(usuario);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        repository.deleteById(id);
    }
}