
package org.jackfallas.controller;

import javafx.fxml.Initializable;
import org.jackfallas.system.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.jackfallas.model.Products;
import org.jackfallas.db.Conexion;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Connection; // Interfaz de coneccion
import java.sql.CallableStatement; // Enunciado de llamada
import java.sql.ResultSet; // Resultado del resuld grid
import java.sql.SQLException;// mi excepcion SQL
import java.time.LocalDate;

import javafx.scene.control.TableView;// Tabla tabla
import javafx.scene.control.TableColumn;// columna de tabla
import javafx.scene.control.DatePicker;// Seleccionador de fecha dp
import javafx.scene.control.TextField;// carga de texto txt
import javafx.scene.control.cell.PropertyValueFactory;//formato de celdas

import javafx.collections.FXCollections;// Arreglo o collecion de javafx
import javafx.collections.ObservableList;// Lista observable para cargar la lista
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class ProductsDBController implements Initializable {
     private Main principal;

    @FXML private Button btnProductos, btnClientes, btnProveedores, btnEmpleados; 
    @FXML private Button btnNuevo, btnEliminar, btnEditar, btnSiguiente, btnRetroceder, btnBuscar;
    @FXML private TextField txtIDProducto, txtNombreProducto, txtCategoria, txtDescripcion, txtPrecio, txtBuscar;

    @FXML private TableView<Products> tablaProductos;
    @FXML private TableColumn colIDProducto, colNombreProducto, colCategoria, colDescripcion, colPrecio;
    private ObservableList<Products> listaProductos;
    private Products modeloProducto;

    private enum EstadoFormulario {
        AGREGAR, ACTUALIZAR, NINGUNA
    };
    private EstadoFormulario tipoDeAccion = EstadoFormulario.NINGUNA;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar los iconos de los botones en el estado NINGUNA
        btnNuevo.setGraphic(new ImageView(new Image("/images/ADD.png"))); 
        btnEliminar.setGraphic(new ImageView(new Image("/images/delete red.png")));
        btnEditar.setGraphic(new ImageView(new Image("/images/edit.png")));
        btnBuscar.setGraphic(new ImageView(new Image("/images/search.png")));
        btnSiguiente.setGraphic(new ImageView(new Image("/images/next.png")));
        btnRetroceder.setGraphic(new ImageView(new Image("/images/back.png")));

        configurarColumnas();
        cargarProductos();
        tablaProductos.setOnMouseClicked(eh -> cargarProductoEnTextFields());

        cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    public void configurarColumnas() {
        colIDProducto.setCellValueFactory(new PropertyValueFactory<Products, Integer>("idProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Products, String>("nombreProducto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Products, String>("categoria"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Products, String>("descripción"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Products, Double>("precio"));

        colIDProducto.setStyle("-fx-alignment: CENTER;");
        colPrecio.setStyle("-fx-alignment: CENTER-RIGHT;"); 
    }

    public void cargarProductos() {
        listaProductos = FXCollections.observableArrayList(listarProductos());
        tablaProductos.setItems(listaProductos);
        if (!listaProductos.isEmpty()) {
            tablaProductos.getSelectionModel().selectFirst();
            cargarProductoEnTextFields();
        } else {
            limpiarTextFields();
        }
    }

    public void cargarProductoEnTextFields() {
        Products productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtIDProducto.setText(String.valueOf(productoSeleccionado.getIdProducto()));
            txtNombreProducto.setText(productoSeleccionado.getNombreProducto());
            txtCategoria.setText(productoSeleccionado.getCategoria());
            txtDescripcion.setText(productoSeleccionado.getDescripción());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
        } else {
            limpiarTextFields();
        }
    }

    public ArrayList<Products> listarProductos() {
        ArrayList<Products> productos = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstancia().getConexion();
            
            String sql = "call sp_ListarProductos();";
            CallableStatement enunciado = conexion.prepareCall(sql);
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                productos.add(new Products(
                                        rs.getInt(1),
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getString(4),
                                        rs.getDouble(5))); 
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }

    public Products obtenerModeloProducto() {
        int idProducto = (txtIDProducto.getText().isEmpty()) ? 0 : Integer.parseInt(txtIDProducto.getText());
        String nombreProducto = txtNombreProducto.getText();
        String categoria = txtCategoria.getText();
        String descripcion = txtDescripcion.getText();
        Double precio = (txtPrecio.getText().isEmpty()) ? 0.0 : Double.parseDouble(txtPrecio.getText());
        return new Products(idProducto, nombreProducto, categoria, descripcion, precio);
    }

    public void agregarProducto() {
        modeloProducto = obtenerModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarProducto(?,?,?,?);"); 
            enunciado.setString(1, modeloProducto.getNombreProducto());
            enunciado.setString(2, modeloProducto.getCategoria());
            enunciado.setString(3, modeloProducto.getDescripción());
            enunciado.setDouble(4, modeloProducto.getPrecio());
            enunciado.execute();
            System.out.println("Producto agregado correctamente.");
            cargarProductos();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error de formato en el precio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarProducto() {
        modeloProducto = obtenerModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarProducto(?,?,?,?,?);"); 
            enunciado.setInt(1, modeloProducto.getIdProducto());
            enunciado.setString(2, modeloProducto.getNombreProducto());
            enunciado.setString(3, modeloProducto.getCategoria());
            enunciado.setString(4, modeloProducto.getDescripción());
            enunciado.setDouble(5, modeloProducto.getPrecio());
            enunciado.execute();
            System.out.println("Producto actualizado correctamente.");
            cargarProductos();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error de formato en el precio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarProducto() {
        Products productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            try {
                CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarProducto(?);");
                enunciado.setInt(1, productoSeleccionado.getIdProducto());
                enunciado.execute();
                System.out.println("Producto eliminado correctamente.");
                cargarProductos();
                limpiarTextFields();
            } catch (SQLException e) {
                System.out.println("Error al eliminar producto: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No hay producto seleccionado para eliminar.");
            
        }
    }

    public void limpiarTextFields() {
        txtIDProducto.clear();
        txtNombreProducto.clear();
        txtCategoria.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
    }

    private void cambiarEstadoFormulario(EstadoFormulario estado) {
        this.tipoDeAccion = estado;

        boolean camposEditables = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtIDProducto.setDisable(true); 
        txtNombreProducto.setDisable(!camposEditables);
        txtCategoria.setDisable(!camposEditables);
        txtDescripcion.setDisable(!camposEditables);
        txtPrecio.setDisable(!camposEditables);

        tablaProductos.setDisable(camposEditables);
        txtBuscar.setDisable(camposEditables);
        btnBuscar.setDisable(camposEditables);

        btnSiguiente.setDisable(camposEditables);
        btnRetroceder.setDisable(camposEditables);


        Image imagenNuevo = new Image("/images/ADD.png"); 
        Image imagenGuardar = new Image("/images/Save.png");
        if (camposEditables) {
            btnNuevo.setGraphic(new ImageView(imagenGuardar));
        } else {
            btnNuevo.setGraphic(new ImageView(imagenNuevo));
        }

        Image imagenEliminar = new Image("/images/delete red.png");
        Image imagenCancelar = new Image("/images/cancel.png");
        if (camposEditables) {
            btnEliminar.setGraphic(new ImageView(imagenCancelar));
            btnEliminar.setDisable(false); 
        } else {
            btnEliminar.setGraphic(new ImageView(imagenEliminar));
            btnEliminar.setDisable(false);
        }

        btnEditar.setDisable(camposEditables);
    }

    @FXML
    public void btnRetrocederAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            int indice = tablaProductos.getSelectionModel().getSelectedIndex();
            if (indice > 0) {
                tablaProductos.getSelectionModel().select(indice - 1);
                cargarProductoEnTextFields();
            }
        }
    }

    @FXML
    public void btnSiguienteAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            int indice = tablaProductos.getSelectionModel().getSelectedIndex();
            if (indice < listaProductos.size() - 1) {
                tablaProductos.getSelectionModel().select(indice + 1);
                cargarProductoEnTextFields();
            }
        }
    }

    @FXML
    public void btnNuevoAction() {
        switch (tipoDeAccion) {
            case NINGUNA:
                System.out.println("Iniciando modo: AGREGAR Producto");
                limpiarTextFields();
                txtNombreProducto.requestFocus();
                cambiarEstadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                System.out.println("Ejecutando: AGREGAR Producto a la BD");
                agregarProducto();
                break;
            case ACTUALIZAR:
                System.out.println("Ejecutando: ACTUALIZAR Producto en la BD");
                actualizarProducto();
                break;
        }
    }

    @FXML
    public void btnEditarAction() {
        if (tablaProductos.getSelectionModel().getSelectedItem() != null && tipoDeAccion == EstadoFormulario.NINGUNA) {
            System.out.println("Iniciando modo: ACTUALIZAR Producto");
            cambiarEstadoFormulario(EstadoFormulario.ACTUALIZAR);
            txtNombreProducto.requestFocus();
        } else if (tablaProductos.getSelectionModel().getSelectedItem() == null) {
            System.out.println("Por favor, selecciona un producto para editar.");
           
        }
    }

    @FXML
    public void btnEliminarAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            System.out.println("Ejecutando: ELIMINAR Producto de la BD");
            eliminarProducto();
        } else {
            System.out.println("Ejecutando: CANCELAR operación actual.");
            cargarProductoEnTextFields();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        }
    }

    @FXML
    public void btnBuscarProducto() {
        ArrayList<Products> resultadoBusqueda = new ArrayList<>();
        String nombre = txtBuscar.getText();
        for (Products producto : listaProductos) {
            if (producto.getNombreProducto().toLowerCase().contains(nombre.toLowerCase())) {
                resultadoBusqueda.add(producto);
            }
        }
        tablaProductos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaProductos.getSelectionModel().selectFirst();
        }
    }

   
//    @FXML
//    public void handleButtonAction(ActionEvent event) {
//        if (event.getSource() == btnClientes) {
//            principal.mostrarVistaClientes();
//        } else if (event.getSource() == btnProveedores) {
//            principal.mostrarVistaProveedores();
//        }

    }

