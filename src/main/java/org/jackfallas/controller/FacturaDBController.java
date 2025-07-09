package org.jackfallas.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.jackfallas.model.DetalleCompra;
import org.jackfallas.model.Client;
import org.jackfallas.model.Products;
import org.jackfallas.db.Conexion;
import org.jackfallas.system.Main;

public class FacturaDBController implements Initializable {
    private Main principal;

    @FXML private Button btnNuevo, btnEditar, btnEliminar, btnGuardar, btnCancelar, btnBuscar;
    @FXML private Button btnAnterior, btnSiguiente;
    @FXML private TableView<DetalleCompra> tablaDetalleFactura;
    @FXML private TableColumn<DetalleCompra, Integer> colIDDetalle;
    @FXML private TableColumn<DetalleCompra, Integer> colIDCliente;
    @FXML private TableColumn<DetalleCompra, Integer> colIDProducto;
    @FXML private TableColumn<DetalleCompra, LocalDate> colFecha;
    @FXML private TableColumn<DetalleCompra, Double> colTotal;

    @FXML private TextField txtIDDetalle, txtTotal, txtBuscar;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Client> cbxIDClient;
    @FXML private ComboBox<Products> cbxIDProducts;

    private ObservableList<DetalleCompra> listaDetalleFactura;
    private ObservableList<Client> listaClients;
    private ObservableList<Products> listaProducts;

    private enum EstadoFormulario {
        AGREGAR, ELIMINAR, ACTUALIZAR, NINGUNA
    };
    EstadoFormulario tipoDeAccion = EstadoFormulario.NINGUNA;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnNuevo.setGraphic(new ImageView(new Image("/images/ADD.png")));
        btnEliminar.setGraphic(new ImageView(new Image("/images/delete red.png")));
        btnEditar.setGraphic(new ImageView(new Image("/images/edit.png")));
        btnBuscar.setGraphic(new ImageView(new Image("/images/search.png")));
        btnSiguiente.setGraphic(new ImageView(new Image("/images/next.png")));
        btnAnterior.setGraphic(new ImageView(new Image("/images/back.png")));

        configurarColumnas();
        cargarClients();
        cargarProducts();
        cargarTablaDetalleFactura();

        tablaDetalleFactura.setOnMouseClicked(event -> cargarDetalleFacturaEnTextField());
    }

    public void configurarColumnas() {
        colIDDetalle.setCellValueFactory(new PropertyValueFactory<>("idDetalle"));
        colIDCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colIDProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
    }

    public void cargarTablaDetalleFactura() {
        listaDetalleFactura = FXCollections.observableArrayList(listarDetalleFactura());
        tablaDetalleFactura.setItems(listaDetalleFactura);
        if (!listaDetalleFactura.isEmpty()) {
            tablaDetalleFactura.getSelectionModel().selectFirst();
            cargarDetalleFacturaEnTextField();
        } else {
            limpiarTextField();
        }
    }

    public ArrayList<DetalleCompra> listarDetalleFactura() {
        ArrayList<DetalleCompra> detalles = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarDetalleFactura();");
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                detalles.add(new DetalleCompra(
                        rs.getInt("id_detalle"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_producto"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar Detalles de Factura: " + e.getMessage());
            e.printStackTrace();
        }
        return detalles;
    }

    public void cargarDetalleFacturaEnTextField() {
        DetalleCompra detalle = tablaDetalleFactura.getSelectionModel().getSelectedItem();
        if (detalle != null) {
            txtIDDetalle.setText(String.valueOf(detalle.getIdDetalle()));
            dpFecha.setValue(detalle.getFecha());
            txtTotal.setText(String.valueOf(detalle.getTotal()));

            if (cbxIDClient.getItems() != null && !cbxIDClient.getItems().isEmpty()) {
                for (Client c : cbxIDClient.getItems()) {
                    if (c.getIdCliente() == detalle.getIdCliente()) {
                        cbxIDClient.setValue(c);
                        break;
                    }
                }
            } else {
                 cbxIDClient.getSelectionModel().clearSelection();
            }

            if (cbxIDProducts.getItems() != null && !cbxIDProducts.getItems().isEmpty()) {
                for (Products p : cbxIDProducts.getItems()) {
                    if (p.getIdProducto() == detalle.getIdProducto()) {
                        cbxIDProducts.setValue(p);
                        break;
                    }
                }
            } else {
                cbxIDProducts.getSelectionModel().clearSelection();
            }
        } else {
            limpiarTextField();
        }
    }

    public void cargarClients() {
        ArrayList<Client> clients = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarClientes();");
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("idcliente"),
                        rs.getString("nombre_cliente"),
                        rs.getString("apellido_cliente"),
                        rs.getString("NIT")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar Clients en DetalleFacturaController: " + e.getMessage());
            e.printStackTrace();
        }
        listaClients = FXCollections.observableArrayList(clients);
        cbxIDClient.setItems(listaClients);
        if (!listaClients.isEmpty()) {
            cbxIDClient.getSelectionModel().selectFirst();
        }
    }

    public void cargarProducts() {
        ArrayList<Products> products = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarProductos();");
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                products.add(new Products(
                        rs.getInt("idproducto"),
                        rs.getString("nombre_producto"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar Products en DetalleFacturaController: " + e.getMessage());
            e.printStackTrace();
        }
        listaProducts = FXCollections.observableArrayList(products);
        cbxIDProducts.setItems(listaProducts);
        if (!listaProducts.isEmpty()) {
            cbxIDProducts.getSelectionModel().selectFirst();
        }
    }

    public DetalleCompra cargarModeloDetalleFactura() {
        int idDetalle = txtIDDetalle.getText() == null || txtIDDetalle.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtIDDetalle.getText().trim());
        LocalDate fecha = dpFecha.getValue();
        double total = txtTotal.getText() == null || txtTotal.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txtTotal.getText().trim());

        Client clientSeleccionado = cbxIDClient.getSelectionModel().getSelectedItem();
        Products productSeleccionado = cbxIDProducts.getSelectionModel().getSelectedItem();

        int idCliente = (clientSeleccionado != null) ? clientSeleccionado.getIdCliente() : 0;
        int idProducto = (productSeleccionado != null) ? productSeleccionado.getIdProducto() : 0;

        return new DetalleCompra(idDetalle, idCliente, idProducto, fecha, total);
    }

    public void agregarDetalleFactura() {
        DetalleCompra nuevoDetalle = cargarModeloDetalleFactura();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarDetalleFactura(?,?,?,?);");
            enunciado.setInt(1, nuevoDetalle.getIdCliente());
            enunciado.setInt(2, nuevoDetalle.getIdProducto());
            enunciado.setDate(3, Date.valueOf(nuevoDetalle.getFecha()));
            enunciado.setDouble(4, nuevoDetalle.getTotal());
            int registrosAgregados = enunciado.executeUpdate();
            if (registrosAgregados > 0) {
                System.out.println("Detalle de Factura agregado correctamente.");
                cargarTablaDetalleFactura();
                cambiarEstado(EstadoFormulario.NINGUNA);
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar Detalle de Factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarDetalleFactura() {
        DetalleCompra detalleActualizado = cargarModeloDetalleFactura();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarDetalleFactura(?,?,?,?,?);");
            enunciado.setInt(1, detalleActualizado.getIdDetalle());
            enunciado.setInt(2, detalleActualizado.getIdCliente());
            enunciado.setInt(3, detalleActualizado.getIdProducto());
            enunciado.setDate(4, Date.valueOf(detalleActualizado.getFecha()));
            enunciado.setDouble(5, detalleActualizado.getTotal());
            enunciado.executeUpdate();
            System.out.println("Detalle de Factura actualizado correctamente.");
            cargarTablaDetalleFactura();
            cambiarEstado(EstadoFormulario.NINGUNA);
        } catch (SQLException e) {
            System.out.println("Error al actualizar Detalle de Factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarDetalleFactura() {
        DetalleCompra detalleEliminar = tablaDetalleFactura.getSelectionModel().getSelectedItem();
        if (detalleEliminar != null) {
            try {
                CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarDetalleFactura(?);");
                enunciado.setInt(1, detalleEliminar.getIdDetalle());
                enunciado.execute();
                System.out.println("Detalle de Factura eliminado correctamente.");
                cargarTablaDetalleFactura();
                limpiarTextField();
            } catch (SQLException e) {
                System.out.println("Error al eliminar Detalle de Factura: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Selecciona un detalle de factura para eliminar.");
        }
    }

    public void limpiarTextField() {
        txtIDDetalle.clear();
        dpFecha.setValue(null);
        txtTotal.clear();
        cbxIDClient.getSelectionModel().clearSelection();
        cbxIDProducts.getSelectionModel().clearSelection();
    }

    private void cambiarEstado(EstadoFormulario estado) {
        tipoDeAccion = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtIDDetalle.setDisable(true);
        dpFecha.setDisable(!activo);
        txtTotal.setDisable(!activo);
        cbxIDClient.setDisable(!activo);
        cbxIDProducts.setDisable(!activo);

        tablaDetalleFactura.setDisable(activo);
        txtBuscar.setDisable(activo);
        btnBuscar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);

        Image imagenNuevo = new Image("/images/ADD.png");
        Image imagenGuardar = new Image("/images/Save.png");
        if (activo) {
            btnNuevo.setGraphic(new ImageView(imagenGuardar));
        } else {
            btnNuevo.setGraphic(new ImageView(imagenNuevo));
        }

        Image imagenEliminar = new Image("/images/delete red.png");
        Image imagenCancelar = new Image("/images/cancel.png");
        if (activo) {
            btnEliminar.setGraphic(new ImageView(imagenCancelar));
        } else {
            btnEliminar.setGraphic(new ImageView(imagenEliminar));
        }
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnNuevoAction() {
        switch (tipoDeAccion) {
            case NINGUNA:
                System.out.println("Preparando para agregar nuevo detalle de factura.");
                limpiarTextField();
                cambiarEstado(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                System.out.println("Guardando nuevo detalle de factura.");
                agregarDetalleFactura();
                break;
            case ACTUALIZAR:
                System.out.println("Guardando cambios de edición.");
                actualizarDetalleFactura();
                break;
        }
    }

    @FXML
    private void btnEditarAction() {
        if (tablaDetalleFactura.getSelectionModel().getSelectedItem() != null) {
            cambiarEstado(EstadoFormulario.ACTUALIZAR);
            System.out.println("Editando detalle de factura.");
        } else {
            System.out.println("Seleccione un detalle de factura para editar.");
        }
    }

    @FXML
    private void btnEliminarAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            System.out.println("Eliminando detalle de factura.");
            eliminarDetalleFactura();
        } else {
            System.out.println("Cancelando operación.");
            cambiarEstado(EstadoFormulario.NINGUNA);
            cargarDetalleFacturaEnTextField();
        }
    }

    @FXML
    private void btnBuscarAction() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();

        listaDetalleFactura = FXCollections.observableArrayList(listarDetalleFactura());

        if (textoBusqueda.isEmpty()) {
            tablaDetalleFactura.setItems(listaDetalleFactura);
            if (!listaDetalleFactura.isEmpty()) {
                tablaDetalleFactura.getSelectionModel().selectFirst();
                cargarDetalleFacturaEnTextField();
            } else {
                limpiarTextField();
            }
            return;
        }

        ArrayList<DetalleCompra> resultadoBusqueda = new ArrayList<>();
        for (DetalleCompra dc : listaDetalleFactura) {
            if (String.valueOf(dc.getIdDetalle()).toLowerCase().contains(textoBusqueda)) {
                resultadoBusqueda.add(dc);
            }
        }

        tablaDetalleFactura.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaDetalleFactura.getSelectionModel().selectFirst();
            cargarDetalleFacturaEnTextField();
        } else {
            System.out.println("No se encontró ningún detalle de factura con el ID: " + textoBusqueda);
            limpiarTextField();
        }
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaDetalleFactura.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaDetalleFactura.getSelectionModel().select(indice - 1);
            cargarDetalleFacturaEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaDetalleFactura.getSelectionModel().getSelectedIndex();
        if (indice < tablaDetalleFactura.getItems().size() - 1) {
            tablaDetalleFactura.getSelectionModel().select(indice + 1);
            cargarDetalleFacturaEnTextField();
        }
    }

    @FXML
    public void ButtonsActionsEvents(ActionEvent evento){
    }
}