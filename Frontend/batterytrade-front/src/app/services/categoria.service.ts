import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
}

export type NuevaCategoria = Omit<Categoria, 'id'>;

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  private http = inject(HttpClient);

  private api =
    'http://localhost:8080/api/categorias';

  listar(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.api);
  }

  guardar(categoria: NuevaCategoria): Observable<Categoria> {
    return this.http.post<Categoria>(
      this.api,
      categoria
    );
  }

  actualizar(
    id: number,
    categoria: Categoria
  ): Observable<Categoria> {

    return this.http.put<Categoria>(
      `${this.api}/${id}`,
      categoria
    );
  }

  eliminar(id: number) {

    return this.http.delete(
      `${this.api}/${id}`
    );
  }
}