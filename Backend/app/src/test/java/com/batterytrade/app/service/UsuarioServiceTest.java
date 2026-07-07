package com.batterytrade.app.service;

import com.batterytrade.app.exception.BusinessException;
import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void guardarDebeRechazarCorreoDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setCorreo("test@mail.com");
        usuario.setPassword("1234");

        when(repository.existsByCorreo("test@mail.com")).thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> service.guardar(usuario)
        );

        assertEquals("Ya existe un usuario con ese correo", ex.getMessage());
        verify(repository, never()).save(any(Usuario.class));
    }
}
