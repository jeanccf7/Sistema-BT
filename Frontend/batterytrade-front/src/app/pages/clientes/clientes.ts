import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, switchMap } from 'rxjs';
import { Cliente, ClienteService, NuevoCliente } from '../../services/cliente.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Clientes {
  private clienteService = inject(ClienteService);
  private reload$ = new BehaviorSubject<void>(undefined);

  clientes$ = this.reload$.pipe(switchMap(() => this.clienteService.listar()));
  busqueda = '';
  mostrarFormulario = false;
  editando = false;
  guardando = false;
  clienteSeleccionadoId = 0;
  nuevoCliente: NuevoCliente = {
    dni: '',
    nombre: '',
    apellido: '',
    telefono: '',
    direccion: '',
    correo: '',
  };

  private recargar(): void {
    this.reload$.next();
  }

  filtrar(clientes: Cliente[]): Cliente[] {
    const texto = this.busqueda.toLowerCase();

    return clientes.filter(
      (c) =>
        c.dni.toLowerCase().includes(texto) ||
        c.nombre.toLowerCase().includes(texto) ||
        c.apellido.toLowerCase().includes(texto) ||
        c.telefono.toLowerCase().includes(texto),
    );
  }

  abrirFormulario(): void {
    this.editando = false;

    this.nuevoCliente = {
      dni: '',
      nombre: '',
      apellido: '',
      telefono: '',
      direccion: '',
      correo: '',
    };

    this.mostrarFormulario = true;
  }

  editarCliente(cliente: Cliente): void {
    this.editando = true;

    this.clienteSeleccionadoId = cliente.id;

    this.nuevoCliente = {
      dni: cliente.dni,
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
      correo: cliente.correo,
    };

    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;

    this.guardando = false;
  }

  guardarCliente(): void {
    if (!this.nuevoCliente.nombre.trim()) {
      return;
    }

    this.guardando = true;
    const accion$ = this.editando
      ? this.clienteService.actualizar(
          this.clienteSeleccionadoId,

          {
            id: this.clienteSeleccionadoId,

            ...this.nuevoCliente,
          },
        )
      : this.clienteService.guardar(this.nuevoCliente);

    accion$.subscribe({
      next: () => {
        this.recargar();

        this.cerrarFormulario();
      },

      error: () => {
        this.guardando = false;
      },
    });
  }

  eliminarCliente(id: number): void {
    if (!confirm('¿Eliminar cliente?')) {
      return;
    }

    this.clienteService.eliminar(id).subscribe({
      next: () => this.recargar(),
    });
  }
}
