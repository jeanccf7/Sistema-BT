package com.batterytrade.app.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class UsuarioJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializarUsuarioDebeIgnorarIdDeEntrada() throws Exception {
        String json = """
                {
                  "id": 99,
                  "codigo": "U-001",
                  "nombre": "Ana",
                  "apellido": "Pérez",
                  "correo": "ana@test.com",
                  "password": "1234",
                  "rol": "ADMIN"
                }
                """;

        Usuario usuario = objectMapper.readValue(json, Usuario.class);

        assertNull(usuario.getId());
    }
}
