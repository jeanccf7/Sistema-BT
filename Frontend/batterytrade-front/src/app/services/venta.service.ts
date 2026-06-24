import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Venta {

  id: number;
  fecha: string;
  total: number;

}

@Injectable({
  providedIn: 'root'
})
export class VentaService {

  private http = inject(HttpClient);

  private api =
    'http://localhost:8080/api/ventas';

  listar(): Observable<Venta[]> {

    return this.http.get<Venta[]>(
      this.api
    );

  }

  buscar(id: number): Observable<Venta> {

    return this.http.get<Venta>(
      `${this.api}/${id}`
    );

  }

  eliminar(id: number) {

    return this.http.delete(
      `${this.api}/${id}`
    );

  }

}