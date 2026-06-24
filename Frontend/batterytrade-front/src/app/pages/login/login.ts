import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  mensajeError = '';
  cargando = false;

  loginForm = this.fb.group({
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    recordar: [false],
  });

  get correo() {
    return this.loginForm.get('correo');
  }

  get password() {
    return this.loginForm.get('password');
  }

  
  iniciarSesion() {

  this.mensajeError = '';

  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.cargando = true;

  const datos = {
    correo: this.loginForm.value.correo!,
    password: this.loginForm.value.password!
  };

  this.authService.login(datos)
    .subscribe({

      next: (respuesta) => {

        localStorage.setItem(
          'token',
          respuesta.token
        );

        localStorage.setItem(
          'correo',
          respuesta.correo
        );

        localStorage.setItem(
          'nombre',
          respuesta.nombre
        );

        localStorage.setItem(
          'rol',
          respuesta.rol
        );


        this.router.navigate(['/dashboard']);

        this.cargando = false;

      },

      error: () => {

        this.cargando = false;

        this.mensajeError =
          'Correo o contrasena incorrectos.';

      }

    });

}
}
