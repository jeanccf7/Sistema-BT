import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Productos } from './pages/productos/productos';
import { Categorias } from './pages/categorias/categorias';
import { Clientes } from './pages/clientes/clientes';
import { Ventas } from './pages/ventas/ventas';

export const routes: Routes = [
    {
    path: '',
    component: Login
  },

  {
    path: 'dashboard',
    component: Dashboard
  },

  {
    path: 'productos',
    component: Productos
  },

  {
    path: 'categorias',
    component: Categorias
  },

  {
    path: 'clientes',
    component: Clientes
  },

  {
    path: 'ventas',
    component: Ventas
  }
];
