import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BehaviorSubject, switchMap } from 'rxjs';
import { Categoria, CategoriaService, NuevaCategoria } from '../../services/categoria.service';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Categorias {
  private categoriaService = inject(CategoriaService);
  private reload$ = new BehaviorSubject<void>(undefined);

  categorias$ = this.reload$.pipe(
    switchMap(() => this.categoriaService.listar())
  );

  busqueda = '';
  mostrarFormulario = false;
  editando = false;
  guardando = false;           // 👈 bloquea el botón mientras la petición está en vuelo
  categoriaSeleccionadaId = 0;
  nuevaCategoria: NuevaCategoria = { nombre: '', descripcion: '' };

  private recargar(): void {
    this.reload$.next();
  }

  filtrar(categorias: Categoria[]): Categoria[] {
    const texto = this.busqueda.toLowerCase();
    return categorias.filter(
      (c) =>
        c.nombre.toLowerCase().includes(texto) ||
        c.descripcion.toLowerCase().includes(texto)
    );
  }

  abrirFormulario(): void {
    this.editando = false;
    this.nuevaCategoria = { nombre: '', descripcion: '' };
    this.mostrarFormulario = true;
  }

  editarCategoria(categoria: Categoria): void {
    this.editando = true;
    this.categoriaSeleccionadaId = categoria.id;
    this.nuevaCategoria = { nombre: categoria.nombre, descripcion: categoria.descripcion };
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
    this.guardando = false;
    this.nuevaCategoria = { nombre: '', descripcion: '' };
  }

  guardarCategoria(): void {
    if (!this.nuevaCategoria.nombre.trim() || this.guardando) return;  // 👈 corta si ya hay una petición activa

    this.guardando = true;

    const accion$ = this.editando
      ? this.categoriaService.actualizar(this.categoriaSeleccionadaId, {
          id: this.categoriaSeleccionadaId,
          ...this.nuevaCategoria,
        })
      : this.categoriaService.guardar(this.nuevaCategoria);

    accion$.subscribe({
      next: () => {
        this.recargar();
        this.cerrarFormulario();  // ya resetea guardando a false
      },
      error: () => {
        this.guardando = false;   // 👈 si falla, desbloquea el botón para que pueda reintentar
      },
    });
  }

  eliminarCategoria(id: number): void {
    if (!confirm('¿Deseas eliminar esta categoría?')) return;
    this.categoriaService.eliminar(id).subscribe({
      next: () => this.recargar(),
    });
  }
}