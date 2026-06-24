package com.batterytrade.app.repository;

import com.batterytrade.app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository 
                   extends JpaRepository<Producto, Long>{
    
}
