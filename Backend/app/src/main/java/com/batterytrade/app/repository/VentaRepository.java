package com.batterytrade.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batterytrade.app.model.Venta;

public interface VentaRepository
        extends JpaRepository<Venta, Long> {
}
