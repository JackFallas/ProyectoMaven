package org.jackfallas.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL; 
import java.io.IOException;

import org.jackfallas.controller.*;
/**
 *
 * @author informatica
 */
public class Main extends Application {
   
    private String URL_BASE_FXML = "/view/";
    private Stage escenarioPrincipal;

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        escenarioPrincipal.setTitle("HyprLand");
        Login();
        escenarioPrincipal.show(); 
    }

    public FXMLLoader cambiarEscena(String fxmlFileName, double width, double height) {
        FXMLLoader loader = null;
        try {
     
            URL fxmlUrl = getClass().getResource(URL_BASE_FXML + fxmlFileName);

            if (fxmlUrl == null) {
                System.err.println("Error: No se pudo encontrar el archivo FXML: " + URL_BASE_FXML + fxmlFileName);
                return null;
            }

            loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.centerOnScreen();
            
            if (!escenarioPrincipal.isShowing()) {
                escenarioPrincipal.show();
            }

        } catch (IOException ex) {
            System.err.println("Error al cargar FXML '" + fxmlFileName + "': " + ex.getMessage());
            ex.printStackTrace();
            loader = null; 
        } catch (Exception ex) { 
            System.err.println("Ocurrió un error inesperado al cambiar de escena con '" + fxmlFileName + "': " + ex.getMessage());
            ex.printStackTrace();
            loader = null;
        }
        return loader;
    }
    
    public void Login() {
        FXMLLoader loadedLoader = cambiarEscena("LoginView.fxml", 600, 400);

        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof LoginController) {
                LoginController pdbc = (LoginController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de PaginaInicio.fxml no es una instancia de ProductsDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ProductosDBView.fxml, no se puede configurar el controlador.");
        }
    }
    
    public void Dashboard() {
        FXMLLoader loadedLoader = cambiarEscena("DashboardView.fxml", 1000, 600);

        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof DashboardController) {
                DashboardController pdbc = (DashboardController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de PaginaInicio.fxml no es una instancia de ProductsDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ProductosDBView.fxml, no se puede configurar el controlador.");
        }
    }
    
    public void Clientes() {
        FXMLLoader loadedLoader = cambiarEscena("ClientDBView.fxml", 1280, 720);

      
        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof ClientDBController) {
                ClientDBController pdbc = (ClientDBController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de ClienteDBView.fxml no es una instancia de ClientDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ClientDBView.fxml, no se puede configurar el controlador.");
        }
    }
    
    public void Productos() {
        FXMLLoader loadedLoader = cambiarEscena("ProductsDBView.fxml", 1280, 720);

      
        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof ProductsDBController) {
                ProductsDBController pdbc = (ProductsDBController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de PaginaInicio.fxml no es una instancia de ProductsDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ProductosDBView.fxml, no se puede configurar el controlador.");
        }
    }
    
    public void Factura() {
        FXMLLoader loadedLoader = cambiarEscena("FacturaView.fxml", 1280, 720);

      
        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof FacturaDBController) {
                FacturaDBController pdbc = (FacturaDBController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de PaginaInicio.fxml no es una instancia de ProductsDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ProductosDBView.fxml, no se puede configurar el controlador.");
        }
    }
    
    public void Ajustes() {
        FXMLLoader loadedLoader = cambiarEscena("SettingsView.fxml", 600, 400);

      
        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof SettingsController) {
                SettingsController pdbc = (SettingsController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de PaginaInicio.fxml no es una instancia de ProductsDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ProductosDBView.fxml, no se puede configurar el controlador.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        launch(args);
    }
}
