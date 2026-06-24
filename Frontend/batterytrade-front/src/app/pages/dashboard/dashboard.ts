import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import {
  ProductoService,
  Producto
} from '../../services/producto.service';

import {
  VentaService,
  Venta
} from '../../services/venta.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  private productoService =
    inject(ProductoService);

  private ventaService =
    inject(VentaService);

  nombre =
    localStorage.getItem('nombre');

  rol =
    localStorage.getItem('rol');

  fechaActual = new Date();

  productos: Producto[] = [];

  stockCritico: Producto[] = [];

  ventasRecientes: Venta[] = [];

  metricas = [
    {
      titulo: 'Ventas del día',
      valor: 'S/ 0',
      detalle: 'Actualizado en tiempo real',
      estado: 'positivo'
    },
    {
      titulo: 'Productos activos',
      valor: '0',
      detalle: 'Productos registrados',
      estado: 'normal'
    },
    {
      titulo: 'Clientes registrados',
      valor: '0',
      detalle: 'Pendiente de conectar',
      estado: 'normal'
    },
    {
      titulo: 'Stock bajo',
      valor: '0',
      detalle: 'Menos de 10 unidades',
      estado: 'alerta'
    }
  ];

  accesosRapidos = [
    {
      titulo: 'Registrar venta',
      descripcion: 'Crear una nueva venta',
      ruta: '/ventas',
      accion: 'Ir'
    },
    {
      titulo: 'Gestionar productos',
      descripcion: 'Administrar inventario',
      ruta: '/productos',
      accion: 'Abrir'
    },
    {
      titulo: 'Gestionar clientes',
      descripcion: 'Administrar clientes',
      ruta: '/clientes',
      accion: 'Abrir'
    }
  ];

  tareas = [
    {
      texto: 'Revisar productos con stock bajo',
      completada: false
    },
    {
      texto: 'Confirmar pagos pendientes',
      completada: false
    },
    {
      texto: 'Actualizar precios',
      completada: false
    }
  ];

  ngOnInit(): void {

    this.cargarProductos();

    this.cargarVentas();

    const tareasGuardadas =
      localStorage.getItem('tareas');

    if (tareasGuardadas) {

      this.tareas =
        JSON.parse(tareasGuardadas);

    }

  }

  cargarProductos(): void {

    this.productoService
      .listar()
      .subscribe({

        next: (data) => {

          this.productos = data;

          this.metricas[1].valor =
            data.length.toString();

          this.stockCritico =
            data.filter(
              p => p.stock < 10
            );

          this.metricas[3].valor =
            this.stockCritico.length.toString();

        }

      });

  }

  cargarVentas(): void {

    this.ventaService
      .listar()
      .subscribe({

        next: (data) => {

          this.ventasRecientes =
            [...data]
              .sort((a, b) =>
                b.id - a.id
              );

          const hoy =
            new Date()
              .toISOString()
              .split('T')[0];

          const ventasHoy =
            data.filter(
              venta =>
                venta.fecha === hoy
            );

          const totalHoy =
            ventasHoy.reduce(
              (suma, venta) =>
                suma + venta.total,
              0
            );

          this.metricas[0].valor =
            `S/ ${totalHoy.toFixed(2)}`;

        }

      });

  }

  guardarTareas(): void {

    localStorage.setItem(
      'tareas',
      JSON.stringify(this.tareas)
    );

  }

}