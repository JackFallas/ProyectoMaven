/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.jackfallas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.jackfallas.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class DashboardController implements Initializable {
    
    private Main principal;
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void Clientes() {
        principal.Clientes();
    }
    
    public void Productos() {
        principal.Productos();
    }
    
    public void Factura() {
        principal.Factura();
    }
    
    public void Ajustes() {
        principal.Ajustes();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
