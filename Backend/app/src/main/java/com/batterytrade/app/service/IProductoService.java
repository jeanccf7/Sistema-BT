package com.batterytrade.app.service;
import java.util.List;

import com.batterytrade.app.model.Producto;

public interface IProductoService {

    Producto guardar(Producto producto);

    List<Producto> listar();

    Producto buscar(Long id);

    void eliminar(Long id);
}