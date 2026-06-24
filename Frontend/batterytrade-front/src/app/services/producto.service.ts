import { HttpClient } from '@angular/common/http';
import { Injectable, inject} from '@angular/core';
import { Observable } from 'rxjs';

export interface Producto {
  id: number;
  nombre: string;
  marca: string;
  precio: number;
  stock: number;
  categoria: Categoria;
}
export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
}

export type NuevoProducto = Omit<Producto, 'id'>;

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private http = inject(HttpClient);
  private api ='http://localhost:8080/api/productos';

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.api);
  }

  guardar(producto: NuevoProducto): Observable<Producto> {
    return this.http.post<Producto>(this.api, producto);
  }

  actualizar(id:number, producto:Producto) {
    return this.http.put(
      `${this.api}/${id}`,
      producto
    );
  }

  eliminar(id:number) {
    return this.http.delete(
      `${this.api}/${id}`
    );
  }

  
}
