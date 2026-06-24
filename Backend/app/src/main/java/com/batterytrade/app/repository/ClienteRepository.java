package com.batterytrade.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.batterytrade.app.model.Cliente;

public interface ClienteRepository
        extends JpaRepository<Cliente, Long> {
}
