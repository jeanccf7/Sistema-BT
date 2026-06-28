import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BehaviorSubject, forkJoin, switchMap } from 'rxjs';
import { ProductoService, Producto } from '../../services/producto.service';
import { VentaService, Venta } from '../../services/venta.service';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Dashboard implements OnInit {
  private productoService = inject(ProductoService);
  private ventaService = inject(VentaService);
  private clienteService = inject(ClienteService);
  private cdr = inject(ChangeDetectorRef);

  nombre = localStorage.getItem('nombre') ?? 'Usuario';
  rol = localStorage.getItem('rol') ?? '';
  fechaActual = new Date();

  // ── Estado derivado ──────────────────────────────────────────────────────────
  productos: Producto[] = [];
  stockCritico: Producto[] = [];
  stockAgotado: Producto[] = [];
  ventasRecientes: Venta[] = [];
  totalClientes = 0;
  totalVentasMes = 0;
  totalVentasMesAnterior = 0;
  ventasHoy = 0;
  metodosPago: { metodo: string; count: number }[] = [];
  productosMasVendidos: { nombre: string; cantidad: number }[] = [];
  ventasPorDia: { dia: string; total: number }[] = [];

  // ── Checklist ────────────────────────────────────────────────────────────────
  tareas = [
    { texto: 'Revisar productos con stock bajo', completada: false, prioridad: 'alta' },
    { texto: 'Confirmar pagos pendientes', completada: false, prioridad: 'alta' },
    { texto: 'Actualizar precios', completada: false, prioridad: 'media' },
    { texto: 'Contactar proveedores', completada: false, prioridad: 'media' },
    { texto: 'Generar reporte mensual', completada: false, prioridad: 'baja' },
  ];

  // ── Accesos rápidos ──────────────────────────────────────────────────────────
  accesosRapidos = [
    { titulo: 'Nueva Venta', descripcion: 'Registrar una venta', ruta: '/ventas', icon: '🛒' },
    { titulo: 'Productos', descripcion: 'Administrar inventario', ruta: '/productos', icon: '📦' },
    { titulo: 'Clientes', descripcion: 'Gestionar clientes', ruta: '/clientes', icon: '👥' },
    { titulo: 'Categorías', descripcion: 'Organizar productos', ruta: '/categorias', icon: '🗂️' },
  ];

  // ── Skeleton loading ─────────────────────────────────────────────────────────
  cargando = true;

  ngOnInit(): void {
    const tareasGuardadas = localStorage.getItem('tareas');
    if (tareasGuardadas) this.tareas = JSON.parse(tareasGuardadas);
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;

    forkJoin({
      productos: this.productoService.listar(),
      ventas: this.ventaService.listar(),
      clientes: this.clienteService.listar(),
    }).subscribe({
      next: ({ productos, ventas, clientes }) => {
        // Productos
        this.productos = productos;
        this.stockCritico = productos
          .filter((p) => p.stock > 0 && p.stock < 10)
          .sort((a, b) => a.stock - b.stock);
        this.stockAgotado = productos.filter((p) => p.stock === 0);

        // Clientes
        this.totalClientes = clientes.length;

        // Ventas
        const hoy = new Date().toISOString().split('T')[0];
        const mesActual = new Date().getMonth();
        const mesAnterior = mesActual === 0 ? 11 : mesActual - 1;

        this.ventasRecientes = [...ventas].sort((a, b) => b.id - a.id).slice(0, 10);

        const ventasDeHoy = ventas.filter((v) => v.fecha === hoy);
        this.ventasHoy = ventasDeHoy.reduce((s, v) => s + v.total, 0);

        const ventasMes = ventas.filter(
          (v) => new Date(v.fecha).getMonth() === mesActual
        );
        this.totalVentasMes = ventasMes.reduce((s, v) => s + v.total, 0);

        const ventasMesAnt = ventas.filter(
          (v) => new Date(v.fecha).getMonth() === mesAnterior
        );
        this.totalVentasMesAnterior = ventasMesAnt.reduce((s, v) => s + v.total, 0);

        // Métodos de pago
        const mapaMetodos: Record<string, number> = {};
        ventas.forEach((v) => {
          if (v.metodoPago) mapaMetodos[v.metodoPago] = (mapaMetodos[v.metodoPago] ?? 0) + 1;
        });
        this.metodosPago = Object.entries(mapaMetodos)
          .map(([metodo, count]) => ({ metodo, count }))
          .sort((a, b) => b.count - a.count);

        // Ventas últimos 7 días (sparkline simplificado)
        const ultimos7: Record<string, number> = {};
        for (let i = 6; i >= 0; i--) {
          const d = new Date();
          d.setDate(d.getDate() - i);
          ultimos7[d.toISOString().split('T')[0]] = 0;
        }
        ventas.forEach((v) => {
          if (v.fecha in ultimos7) ultimos7[v.fecha] += v.total;
        });
        this.ventasPorDia = Object.entries(ultimos7).map(([dia, total]) => ({
          dia: dia.slice(5), // MM-DD
          total,
        }));

        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.cargando = false;
        this.cdr.markForCheck();
      },
    });
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────
  get crecimientoMes(): number {
    if (this.totalVentasMesAnterior === 0) return 0;
    return Math.round(
      ((this.totalVentasMes - this.totalVentasMesAnterior) / this.totalVentasMesAnterior) * 100
    );
  }

  get porcentajeTareas(): number {
    if (this.tareas.length === 0) return 0;
    return Math.round((this.tareas.filter((t) => t.completada).length / this.tareas.length) * 100);
  }

  stockPorcentaje(stock: number): number {
    return Math.min((stock / 10) * 100, 100);
  }

  stockColor(stock: number): string {
    if (stock <= 2) return '#ef4444';
    if (stock <= 5) return '#f59e0b';
    return '#f97316';
  }

  guardarTareas(): void {
    localStorage.setItem('tareas', JSON.stringify(this.tareas));
    this.cdr.markForCheck();
  }

  trackById(_: number, item: any): number {
    return item.id;
  }

  // ── Sparkline SVG inline ─────────────────────────────────────────────────────
  sparklinePath(data: { total: number }[]): string {
    if (!data.length) return '';
    const max = Math.max(...data.map((d) => d.total), 1);
    const w = 80;
    const h = 28;
    const pts = data.map((d, i) => {
      const x = (i / (data.length - 1)) * w;
      const y = h - (d.total / max) * h;
      return `${x},${y}`;
    });
    return `M ${pts.join(' L ')}`;
  }
}