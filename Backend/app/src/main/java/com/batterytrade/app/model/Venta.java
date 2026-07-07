package com.batterytrade.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "ventas")
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;
    private double total;
    private String estadoPago; // COMPLETADO | PENDIENTE
    private String estadoEntrega; // COMPLETADO | PENDIENTE
    private String metodoPago;
    private String observacion;    
   

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

    public Venta() {
    }
    //getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstadoPago(){
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago){
        this.estadoPago= estadoPago;
    }

    public String getEstadoEntrega(){
        return estadoEntrega;
    }
    public void setEstadoEntrega(String estadoEntrega){
        this.estadoEntrega = estadoEntrega;
    }

    public String getMetodoPago(){
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago){
        this.metodoPago = metodoPago;
    }
    public String getObservacion() {
        return observacion;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }


}