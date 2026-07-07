import { CommonModule } from '@angular/common';
import { Component, OnInit, AfterViewInit, inject, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto, NuevoProducto } from '../../services/producto.service';
import { CategoriaService, Categoria } from '../../services/categoria.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrls: ['./productos.css'],
})
export class Productos implements OnInit, AfterViewInit {
  private productoService = inject(ProductoService);
  private categoriaService = inject(CategoriaService);
  private cdr = inject(ChangeDetectorRef);
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  categorias: Categoria[] = [];
  busqueda = '';
  mostrarFormulario = false;
  categoriaSeleccionadaId = 0;
  nuevoProducto: NuevoProducto = {
    nombre: '',
    marca: '',
    precio: 0,
    stock: 0,
    categoria: {
      id: 0,
      nombre: '',
      descripcion: '',
    },
  };

  ngOnInit(): void {
    console.log('Productos cargado');
    this.cargarProductos();
    this.cargarCategorias();
  }

  ngAfterViewInit(): void {
    this.cdr.detectChanges();
  }

  cargarCategorias(): void {
    this.categoriaService.listar().subscribe({
      next: (data) => {
        this.categorias = data;
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  cargarProductos(): void {
    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos = [...data];
        this.productosFiltrados = [...data];
        this.cdr.markForCheck();
        console.log('Productos recibidos:', data.length);
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  filtrar(): void {
    const texto = this.busqueda.toLowerCase();

    this.productosFiltrados = this.productos.filter(
      (producto) =>
        producto.nombre.toLowerCase().includes(texto) ||
        producto.marca.toLowerCase().includes(texto) ||
        producto.categoria?.nombre?.toLowerCase().includes(texto),
    );
  }

  abrirFormulario(): void {
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.categoriaSeleccionadaId = 0;

    this.nuevoProducto = {
      nombre: '',
      marca: '',
      precio: 0,
      stock: 0,

      categoria: {
        id: 0,
        nombre: '',
        descripcion: '',
      },
    };
  }

  guardarProducto(): void {
    if (this.categoriaSeleccionadaId === 0) {
      alert('Debe seleccionar una categoría');
      return;
    }

    const categoriaSeleccionada = this.categorias.find(
      (categoria) => categoria.id === this.categoriaSeleccionadaId,
    );

    const producto: NuevoProducto = {
      ...this.nuevoProducto,
      categoria: categoriaSeleccionada ?? {
        id: this.categoriaSeleccionadaId,
        nombre: '',
        descripcion: '',
      },
    };

    this.productoService.guardar(producto).subscribe({
      next: () => {
        this.cargarProductos();
        this.cerrarFormulario();
      },

      error: (err) => {
        console.error(err);
      },
    });
  }

  eliminarProducto(id: number): void {
    if (!confirm('¿Deseas eliminar este producto?')) {
      return;
    }

    this.productoService.eliminar(id).subscribe({
      next: () => {
        this.cargarProductos();
      },

      error: (err) => {
        console.error(err);
      },
    });
  }
}
