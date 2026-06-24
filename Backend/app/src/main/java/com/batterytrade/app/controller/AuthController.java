package com.batterytrade.app.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.batterytrade.app.config.JwtService;
import com.batterytrade.app.dto.LoginDTO;
import com.batterytrade.app.model.Usuario;
import com.batterytrade.app.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
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
            @RequestBody LoginDTO loginDTO) {

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(
                                loginDTO.getCorreo())
                        .orElse(null);

        if (usuario == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario no encontrado");
        }

        if (!usuario.getPassword()
                .equals(loginDTO.getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Contraseña incorrecta");
        }

        String token =
                jwtService.generarToken(
                        usuario.getCorreo());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "correo", usuario.getCorreo(),
                        "nombre", usuario.getNombre(),
                        "rol", usuario.getRol()
                ));
    }
}