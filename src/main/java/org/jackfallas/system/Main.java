package org.jackfallas.system;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jackfallas.controller.ProductsDBController;
import java.net.URL; 
import java.io.IOException;
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
        factura();
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

    public void productos() {
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
    public void clientes() {
        FXMLLoader loadedLoader = cambiarEscena("ClientDBView.fxml", 1280, 720);

      
        if (loadedLoader != null) {
            Object controller = loadedLoader.getController();
            if (controller instanceof ProductsDBController) {
                ProductsDBController pdbc = (ProductsDBController) controller;
                pdbc.setPrincipal(this);
            } else {
                System.err.println("Error: El controlador de ClienteDBView.fxml no es una instancia de ClientDBController o es nulo.");
              
            }
        } else {
            System.err.println("No se pudo cargar ClientDBView.fxml, no se puede configurar el controlador.");
        }
    }
    public void factura() {
        FXMLLoader loadedLoader = cambiarEscena("FacturaView.fxml", 1280, 720);

      
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

    public static void main(String[] args) {
        System.out.println("Hello World!");
        launch(args);
    }
}
