import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface Cliente {
  id: number;
  dni: String;
  nombre: string;
  apellido: string;
  telefono: String;
  direccion: String;
  correo: String;
}
export type NuevoCliente = Omit<Cliente, 'id'>;
@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private http = inject(HttpClient);

  private api = 'http://localhost:8080/api/clientes';

  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.api);
  }
  guardar(cliente: NuevoCliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.api, cliente);
  }

  actualizar(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.api}/${id}`, cliente);
  }

  eliminar(id: number) {
    return this.http.delete(`${this.api}/${id}`);
  }
}
