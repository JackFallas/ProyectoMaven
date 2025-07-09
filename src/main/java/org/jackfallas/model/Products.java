
package org.jackfallas.model;

/**
 *
 * @author 
 */
public class Products {
    private int idProducto;
    private String nombreProducto;
    private String categoria;
    private String descripción;
    private Double precio;

    public Products() {
    }

    public Products(int idProducto, String nombreProducto, String categoria, String descripción, Double precio) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.descripción = descripción;
        this.precio = precio;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ID" + idProducto + ", Nombre" + nombreProducto + ", Categoria" + categoria + ", Precio" + precio;
    }
    
    
}
