
package org.jackfallas.model;

import java.time.LocalDate;

/**
 *
 * @author 
 */
public class DetalleCompra {
    private int idDetalle;
    private int idCliente;
    private int idProducto;
    private LocalDate fecha;
    private double Total;

    public DetalleCompra() {
    }

    public DetalleCompra(int idDetalle, int idCliente, int idProducto, LocalDate fecha, double Total) {
        this.idDetalle = idDetalle;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.Total = Total;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    @Override
    public String toString() {
        return "ID: " + idDetalle + "Cliente: " + idCliente + "Total: " + Total ;
    }
    
    
}
