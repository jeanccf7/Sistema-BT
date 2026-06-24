import { DetalleVenta } from "./detalle-venta";

export interface Venta {

  id?: number;
  clienteId: number;
  vendedorId: number;
  fecha?: string;
  total?: number;

  detalles: DetalleVenta[];

}