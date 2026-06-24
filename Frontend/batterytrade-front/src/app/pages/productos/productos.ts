import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto, NuevoProducto } from '../../services/producto.service';
import { CategoriaService, Categoria } from '../../services/categoria.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.html',
  styleUrl: './productos.css',
})
export class Productos implements OnInit {
  private productoService = inject(ProductoService);

  private categoriaService = inject(CategoriaService);

  private cdr = inject(ChangeDetectorRef);

  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];

  categorias: Categoria[] = [];

  busqueda = '';

  mostrarFormulario = false;

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
    this.cargarProductos();
    this.cargarCategorias();
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

        this.cdr.detectChanges();
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
        producto.categoria?.nombre.toLowerCase().includes(texto),
    );
  }

  abrirFormulario(): void {
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;

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
    this.productoService.guardar(this.nuevoProducto).subscribe({
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
