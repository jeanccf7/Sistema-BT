import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, switchMap, forkJoin } from 'rxjs';
import { Venta, VentaService, NuevaVenta } from '../../services/venta.service';
import { Cliente, ClienteService } from '../../services/cliente.service';
import { Producto, ProductoService } from '../../services/producto.service';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Ventas {
  private ventaService = inject(VentaService);
  private clienteService = inject(ClienteService);
  private productoService = inject(ProductoService);

  private reload$ = new BehaviorSubject<void>(undefined);
  private modalReload$ = new BehaviorSubject<void>(undefined);

  ventas$ = this.reload$.pipe(
    switchMap(() => this.ventaService.listar())
  );

  // Se recarga cada vez que se abre el modal
  datosModal$ = this.modalReload$.pipe(
    switchMap(() =>
      forkJoin({
        clientes: this.clienteService.listar(),
        productos: this.productoService.listar(),
      })
    )
  );

  busqueda = '';
  mostrarFormulario = false;
  guardando = false;
  productoSeleccionado = 0;
  cantidadSeleccionada = 1;

  nuevaVenta: NuevaVenta = this.ventaVacia();

  private ventaVacia(): NuevaVenta {
    return {
      clienteId: 0,
      vendedorId: this.obtenerVendedorId(),
      estadoPago: 'PENDIENTE',
      estadoEntrega: 'PENDIENTE',
      metodoPago: 'EFECTIVO',
      detalles: [],
    };
  }

  private recargar(): void {
    this.reload$.next();
  }

  filtrar(ventas: Venta[]): Venta[] {
    const texto = this.busqueda.toLowerCase();
    return ventas.filter(
      (v) =>
        v.nombreCliente?.toLowerCase().includes(texto) ||
        v.nombreVendedor?.toLowerCase().includes(texto) ||
        v.estadoPago?.toLowerCase().includes(texto) ||
        v.estadoEntrega?.toLowerCase().includes(texto) ||
        v.metodoPago?.toLowerCase().includes(texto)
    );
  }

  abrirFormulario(): void {
    this.nuevaVenta = this.ventaVacia();
    this.productoSeleccionado = 0;
    this.cantidadSeleccionada = 1;
    this.mostrarFormulario = true;
    this.modalReload$.next(); // recarga clientes y productos frescos
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.guardando = false;
  }

  agregarProducto(productos: Producto[]): void {
    if (this.productoSeleccionado === 0) {
      alert('Seleccione un producto');
      return;
    }
    if (this.cantidadSeleccionada <= 0) {
      alert('La cantidad debe ser mayor a 0');
      return;
    }

    const existente = this.nuevaVenta.detalles.find(
      (d) => d.productoId === this.productoSeleccionado
    );

    if (existente) {
      existente.cantidad += this.cantidadSeleccionada;
    } else {
      this.nuevaVenta.detalles = [
        ...this.nuevaVenta.detalles,
        { productoId: this.productoSeleccionado, cantidad: this.cantidadSeleccionada },
      ];
    }

    this.productoSeleccionado = 0;
    this.cantidadSeleccionada = 1;
  }

  eliminarDetalle(index: number): void {
    this.nuevaVenta.detalles = this.nuevaVenta.detalles.filter((_, i) => i !== index);
  }

  obtenerNombreProducto(productos: Producto[], id: number): string {
    return productos.find((p) => p.id === id)?.nombre ?? '';
  }

  guardarVenta(): void {
    if (this.nuevaVenta.clienteId === 0) {
      alert('Seleccione un cliente');
      return;
    }
    if (this.nuevaVenta.vendedorId === 0) {
      alert('No se encontró el vendedor. Inicie sesión nuevamente.');
      return;
    }
    if (this.nuevaVenta.detalles.length === 0) {
      alert('Debe agregar al menos un producto');
      return;
    }
    if (this.guardando) return;

    this.guardando = true;
    console.log('VENTA A ENVIAR');
    console.log(JSON.stringify(this.nuevaVenta, null, 2));
    this.ventaService.guardar(this.nuevaVenta).subscribe({
      next: () => {
        this.recargar();
        this.cerrarFormulario();
      },
      error: (err) => {
        console.error('ERROR:', err);
        alert(JSON.stringify(err.error));
        this.guardando = false;
      },
    });
  }

  eliminarVenta(id: number): void {
    if (!confirm('¿Desea eliminar esta venta?')) return;
    this.ventaService.eliminar(id).subscribe({
      next: () => this.recargar(),
      error: (err) => console.error(err),
    });
  }

  private obtenerVendedorId(): number {
    return Number(localStorage.getItem('usuarioId') ?? 0);
  }
}