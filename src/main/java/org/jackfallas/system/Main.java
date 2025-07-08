package org.jackfallas.system;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author informatica
 */
public class Main extends Application{

    public static void main(String[] args) {
        System.out.println("Hello World!");
        launch(args);
    }
    
    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/view/ProductsDBView.fxml"));
        Parent raiz = cargador.load();
        Scene escena = new Scene(raiz);
        escenario.setScene(escena);
        escenario.show();
    }
    
}
