import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  menuItems = [
    {
      label: 'Dashboard',
      route: '/dashboard',
      icon: 'DB',
    },
    {
      label: 'Productos',
      route: '/productos',
      icon: 'PR',
    },
    {
      label: 'Categorias',
      route: '/categorias',
      icon: 'CA',
    },
    {
      label: 'Clientes',
      route: '/clientes',
      icon: 'CL',
    },
    {
      label: 'Ventas',
      route: '/ventas',
      icon: 'VE',
    },
  ];
}
