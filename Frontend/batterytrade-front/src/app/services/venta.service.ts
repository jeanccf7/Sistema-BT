import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Venta {
  id: number;
  fecha: string;
  nombreCliente: string;
  nombreVendedor: string;
  total: number;
  estadoPago: string;
  estadoEntrega: string;
  metodoPago: string;
}

export interface NuevaVenta {
  clienteId: number;
  vendedorId: number;
  estadoPago: string;
  estadoEntrega: string;
  metodoPago: string;
  detalles: any[];
}

@Injectable({
  providedIn: 'root',
})
export class VentaService {
  private http = inject(HttpClient);

  private api = 'http://localhost:8080/api/ventas';
  

  listar(): Observable<Venta[]> {
    return this.http.get<Venta[]>(this.api);
  }

  guardar(venta: NuevaVenta): Observable<Venta> {
    return this.http.post<Venta>(this.api, venta);
  }

  eliminar(id: number) {
    return this.http.delete(`${this.api}/${id}`);
  }
}
