package org.jackfallas.controller;

import javafx.fxml.Initializable;
import org.jackfallas.system.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.jackfallas.model.Client; 
import org.jackfallas.db.Conexion;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClientDBController implements Initializable {
    private Main principal;

    @FXML private Button btnProductos, btnClientes, btnProveedores, btnEmpleados;
    @FXML private Button btnNuevo, btnEliminar, btnEditar, btnSiguiente, btnRetroceder, btnBuscar;
    @FXML private TextField txtIDCliente, txtNombreCliente, txtApellidoCliente, txtNITCliente, txtBuscar;

    @FXML private TableView<Client> tablaClientes;
    @FXML private TableColumn colIDCliente, colNombreCliente, colApellidoCliente, colNITCliente;
    private ObservableList<Client> listaClientes;
    private Client modeloCliente;

    private enum EstadoFormulario {
        AGREGAR, ACTUALIZAR, NINGUNA
    };
    private EstadoFormulario tipoDeAccion = EstadoFormulario.NINGUNA;

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
        btnRetroceder.setGraphic(new ImageView(new Image("/images/back.png")));

        configurarColumnas();
        cargarClientes();
        tablaClientes.setOnMouseClicked(eh -> cargarClienteEnTextFields());

        cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    public void configurarColumnas() {
        colIDCliente.setCellValueFactory(new PropertyValueFactory<Client, Integer>("idCliente"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Client, String>("nombreCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Client, String>("apellidoCliente"));
        colNITCliente.setCellValueFactory(new PropertyValueFactory<Client, String>("NIT"));

        colIDCliente.setStyle("-fx-alignment: CENTER;");
    }

    public void cargarClientes() {
        listaClientes = FXCollections.observableArrayList(listarClientes());
        tablaClientes.setItems(listaClientes);
        if (!listaClientes.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
            cargarClienteEnTextFields();
        } else {
            limpiarTextFields();
        }
    }

    public void cargarClienteEnTextFields() {
        Client clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            txtIDCliente.setText(String.valueOf(clienteSeleccionado.getIdCliente()));
            txtNombreCliente.setText(clienteSeleccionado.getNombreCliente());
            txtApellidoCliente.setText(clienteSeleccionado.getApellidoCliente());
            txtNITCliente.setText(clienteSeleccionado.getNIT());
        } else {
            limpiarTextFields();
        }
    }

    public ArrayList<Client> listarClientes() {
        ArrayList<Client> clientes = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstancia().getConexion();

            String sql = "call sp_ListarClientes();"; 
            CallableStatement enunciado = conexion.prepareCall(sql);
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                clientes.add(new Client(
                                    rs.getInt(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getString(4)));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }

    public Client obtenerModeloCliente() {
        int idCliente = (txtIDCliente.getText().isEmpty()) ? 0 : Integer.parseInt(txtIDCliente.getText());
        String nombreCliente = txtNombreCliente.getText();
        String apellidoCliente = txtApellidoCliente.getText();
        String NIT = txtNITCliente.getText();
        return new Client(idCliente, nombreCliente, apellidoCliente, NIT);
    }

    public void agregarCliente() {
        modeloCliente = obtenerModeloCliente();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarCliente(?,?,?);"); 
            enunciado.setString(1, modeloCliente.getNombreCliente());
            enunciado.setString(2, modeloCliente.getApellidoCliente());
            enunciado.setString(3, modeloCliente.getNIT());
            enunciado.execute();
            System.out.println("Cliente agregado correctamente.");
            cargarClientes();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarCliente() {
        modeloCliente = obtenerModeloCliente();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarCliente(?,?,?,?);"); 
            enunciado.setInt(1, modeloCliente.getIdCliente());
            enunciado.setString(2, modeloCliente.getNombreCliente());
            enunciado.setString(3, modeloCliente.getApellidoCliente());
            enunciado.setString(4, modeloCliente.getNIT());
            enunciado.execute();
            System.out.println("Cliente actualizado correctamente.");
            cargarClientes();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarCliente() {
        Client clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            try {
                CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarCliente(?);"); 
                enunciado.setInt(1, clienteSeleccionado.getIdCliente());
                enunciado.execute();
                System.out.println("Cliente eliminado correctamente.");
                cargarClientes();
                limpiarTextFields();
            } catch (SQLException e) {
                System.out.println("Error al eliminar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No hay cliente seleccionado para eliminar.");
        }
    }

    public void limpiarTextFields() {
        txtIDCliente.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtNITCliente.clear();
    }

    private void cambiarEstadoFormulario(EstadoFormulario estado) {
        this.tipoDeAccion = estado;

        boolean camposEditables = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtIDCliente.setDisable(true);
        txtNombreCliente.setDisable(!camposEditables);
        txtApellidoCliente.setDisable(!camposEditables);
        txtNITCliente.setDisable(!camposEditables);

        tablaClientes.setDisable(camposEditables);
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
            int indice = tablaClientes.getSelectionModel().getSelectedIndex();
            if (indice > 0) {
                tablaClientes.getSelectionModel().select(indice - 1);
                cargarClienteEnTextFields();
            }
        }
    }

    @FXML
    public void btnSiguienteAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            int indice = tablaClientes.getSelectionModel().getSelectedIndex();
            if (indice < listaClientes.size() - 1) {
                tablaClientes.getSelectionModel().select(indice + 1);
                cargarClienteEnTextFields();
            }
        }
    }

    @FXML
    public void btnNuevoAction() {
        switch (tipoDeAccion) {
            case NINGUNA:
                System.out.println("Iniciando modo: AGREGAR Cliente");
                limpiarTextFields();
                txtNombreCliente.requestFocus();
                cambiarEstadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                System.out.println("Ejecutando: AGREGAR Cliente a la BD");
                agregarCliente();
                break;
            case ACTUALIZAR:
                System.out.println("Ejecutando: ACTUALIZAR Cliente en la BD");
                actualizarCliente();
                break;
        }
    }

    @FXML
    public void btnEditarAction() {
        if (tablaClientes.getSelectionModel().getSelectedItem() != null && tipoDeAccion == EstadoFormulario.NINGUNA) {
            System.out.println("Iniciando modo: ACTUALIZAR Cliente");
            cambiarEstadoFormulario(EstadoFormulario.ACTUALIZAR);
            txtNombreCliente.requestFocus();
        } else if (tablaClientes.getSelectionModel().getSelectedItem() == null) {
            System.out.println("Por favor, selecciona un cliente para editar.");
        }
    }

    @FXML
    public void btnEliminarAction() {
        if (tipoDeAccion == EstadoFormulario.NINGUNA) {
            System.out.println("Ejecutando: ELIMINAR Cliente de la BD");
            eliminarCliente();
        } else {
            System.out.println("Ejecutando: CANCELAR operación actual.");
            cargarClienteEnTextFields();
            cambiarEstadoFormulario(EstadoFormulario.NINGUNA);
        }
    }

    @FXML
    public void btnBuscarCliente() {
        ArrayList<Client> resultadoBusqueda = new ArrayList<>();
        String nombre = txtBuscar.getText();
        for (Client cliente : listaClientes) {
            if (cliente.getNombreCliente().toLowerCase().contains(nombre.toLowerCase()) ||
                cliente.getApellidoCliente().toLowerCase().contains(nombre.toLowerCase()) ||
                cliente.getNIT().toLowerCase().contains(nombre.toLowerCase())) { // Puedes buscar también por apellido o NIT
                resultadoBusqueda.add(cliente);
            }
        }
        tablaClientes.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
        }
    }

    // Aquí irían los métodos para cambiar de vista, pero los omitiremos por ahora
    // @FXML
    // public void handleButtonAction(ActionEvent event) {
    //     if (event.getSource() == btnProductos) {
    //         principal.menuProductsDBView(); // Asumiendo que Main tiene este método
    //     } else if (event.getSource() == btnProveedores) {
    //         // principal.menuProveedores();
    //     }
    // }
}