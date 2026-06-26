import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, switchMap } from 'rxjs';
import { Venta, VentaService, NuevaVenta } from '../../services/venta.service';
import { Cliente, ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Ventas implements OnInit {
  private ventaService = inject(VentaService);
  private clienteService = inject(ClienteService);

  private reload$ = new BehaviorSubject<void>(undefined);

  ventas$ = this.reload$.pipe(switchMap(() => this.ventaService.listar()));

  clientes: Cliente[] = [];

  busqueda = '';

  mostrarFormulario = false;

  guardando = false;

  nuevaVenta: NuevaVenta = {
    clienteId: 0,
    vendedorId: 1,
    estadoPago: 'PENDIENTE',
    estadoEntrega: 'PENDIENTE',
    metodoPago: 'EFECTIVO',
    detalles: []
  };

  ngOnInit(): void {
    this.cargarClientes();
  }

  private recargar(): void {
    this.reload$.next();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes = data;
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  filtrar(ventas: Venta[]): Venta[] {
    const texto = this.busqueda.toLowerCase();

    return ventas.filter(
      (v) =>
        v.nombreCliente?.toLowerCase().includes(texto) ||
        v.nombreVendedor?.toLowerCase().includes(texto) ||
        v.estadoPago?.toLowerCase().includes(texto)||
        v.estadoEntrega?.toLowerCase().includes(texto)||
        v.metodoPago?.toLowerCase().includes(texto),
    );
  }

  abrirFormulario(): void {
    this.nuevaVenta = {
      clienteId: 0,
      vendedorId: 1,
      estadoPago: 'PENDIENTE',
      estadoEntrega: 'PENDIENTE',
      metodoPago: 'Efectivo',
      detalles:[]
    };

    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;

    this.guardando = false;
  }

  guardarVenta(): void {
    if (this.nuevaVenta.clienteId === 0) {
      alert('Seleccione un cliente');

      return;
    }

    if (this.guardando) {
      return;
    }

    this.guardando = true;

    this.ventaService.guardar(this.nuevaVenta).subscribe({
      next: () => {
        this.recargar();

        this.cerrarFormulario();
      },

      error: (err) => {
        console.error(err);

        this.guardando = false;
      },
    });
  }

  eliminarVenta(id: number): void {
    if (!confirm('¿Desea eliminar esta venta?')) {
      return;
    }

    this.ventaService.eliminar(id).subscribe({
      next: () => {
        this.recargar();
      },

      error: (err) => {
        console.error(err);
      },
    });
  }
}
