package org.jackfallas.model;

/**
 *
 * @author 
 */
public class Client {
    private int idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String NIT;

    public Client() {
    }

    public Client(int idCliente, String nombreCliente, String apellidoCliente, String NIT) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.NIT = NIT;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    @Override
    public String toString() {
        return  "Cliente: " + nombreCliente + " " + apellidoCliente + "NIT: " + NIT + '}';
    }
    
    
}
