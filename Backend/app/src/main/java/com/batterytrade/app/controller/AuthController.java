package com.batterytrade.app.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.batterytrade.app.config.JwtService;
import com.batterytrade.app.dto.LoginDTO;
import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDTO loginDTO) {

        if (loginDTO.getCorreo() == null || loginDTO.getCorreo().isBlank()
                || loginDTO.getPassword() == null || loginDTO.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Correo y contraseña son obligatorios"));
        }

        Usuario usuario = usuarioRepository
                .findByCorreo(loginDTO.getCorreo().trim())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Usuario no encontrado"));
        }

        if (!usuario.getPassword().equals(loginDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Contraseña incorrecta"));
        }

        String token = jwtService.generarToken(usuario.getCorreo());

        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "token", token,
                "correo", usuario.getCorreo(),
                "nombre", usuario.getNombre(),
                "rol", usuario.getRol()));
    }
}
