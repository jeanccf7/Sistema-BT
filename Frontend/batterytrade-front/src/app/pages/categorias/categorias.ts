import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import { FormsModule } from '@angular/forms';

import {
  Categoria,
  CategoriaService,
  NuevaCategoria
} from '../../services/categoria.service';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css'
})
export class Categorias implements OnInit {

  private categoriaService =
    inject(CategoriaService);

  categorias: Categoria[] = [];
  categoriasFiltradas: Categoria[] = [];

  busqueda = '';

  mostrarFormulario = false;

  editando = false;

  categoriaSeleccionadaId = 0;

  nuevaCategoria: NuevaCategoria = {
    nombre: '',
    descripcion: ''
  };

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {

    this.categoriaService
      .listar()
      .subscribe({

        next: (data) => {

          this.categorias = data;
          this.categoriasFiltradas = data;

        },

        error: (err) => {
          console.error(err);
        }

      });

  }

  filtrar(): void {

    const texto =
      this.busqueda.toLowerCase();

    this.categoriasFiltradas =
      this.categorias.filter(c =>

        c.nombre
          .toLowerCase()
          .includes(texto)

        ||

        c.descripcion
          .toLowerCase()
          .includes(texto)

      );

  }

  abrirFormulario(): void {

    this.editando = false;

    this.nuevaCategoria = {
      nombre: '',
      descripcion: ''
    };

    this.mostrarFormulario = true;
  }

  editarCategoria(
    categoria: Categoria
  ): void {

    this.editando = true;

    this.categoriaSeleccionadaId =
      categoria.id;

    this.nuevaCategoria = {
      nombre: categoria.nombre,
      descripcion: categoria.descripcion
    };

    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {

    this.mostrarFormulario = false;

    this.nuevaCategoria = {
      nombre: '',
      descripcion: ''
    };

  }

  guardarCategoria(): void {

    if (!this.nuevaCategoria.nombre.trim()) {
      return;
    }

    if (this.editando) {

      const categoria: Categoria = {

        id: this.categoriaSeleccionadaId,

        nombre:
          this.nuevaCategoria.nombre,

        descripcion:
          this.nuevaCategoria.descripcion

      };

      this.categoriaService
        .actualizar(
          categoria.id,
          categoria
        )
        .subscribe({

          next: () => {

            this.cargarCategorias();
            this.cerrarFormulario();

          }

        });

    } else {

      this.categoriaService
        .guardar(this.nuevaCategoria)
        .subscribe({

          next: () => {

            this.cargarCategorias();
            this.cerrarFormulario();

          }

        });

    }

  }

  eliminarCategoria(
    id: number
  ): void {

    if (
      !confirm(
        '¿Deseas eliminar esta categoría?'
      )
    ) {
      return;
    }

    this.categoriaService
      .eliminar(id)
      .subscribe({

        next: () => {
          this.cargarCategorias();
        }

      });

  }

}