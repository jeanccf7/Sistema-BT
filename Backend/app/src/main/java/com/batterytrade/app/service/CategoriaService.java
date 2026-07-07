package com.batterytrade.app.service;

import com.batterytrade.app.exception.ResourceNotFoundException;
import com.batterytrade.app.model.Categoria;
import com.batterytrade.app.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada");
        }
        repository.deleteById(id);
    }
}