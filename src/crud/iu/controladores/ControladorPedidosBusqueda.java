package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador para la vista PedidosBusqueda.fxml. Gestiona la lógica para
 * filtrar pedidos según los criterios seleccionados.
 */
public class ControladorPedidosBusqueda implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosBusqueda.class.getName());
    // Factorías de acceso a datos
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();

    // Escenario y entidades
    private Stage stage;

    @FXML
    private CheckBox checkBoxIdPedido;

    @FXML
    private Spinner<Integer> spinnerIdDesde;

    @FXML
    private Spinner<Integer> spinnerIdHasta;

    @FXML
    private CheckBox checkBoxCIF;

    @FXML
    private ComboBox<String> comboBoxCIF;

    @FXML
    private CheckBox checkBoxDireccion;

    @FXML
    private TextField textFieldDireccion;

    @FXML
    private CheckBox checkBoxFecha;

    @FXML
    private DatePicker datePickerDesde;

    @FXML
    private DatePicker datePickerHasta;

    @FXML
    private CheckBox checkBoxEstado;

    @FXML
    private ComboBox<String> comboBoxEstado;

    @FXML
    private CheckBox checkBoxPrecio;

    @FXML
    private Spinner<Double> spinnerPrecioDesde;

    @FXML
    private Spinner<Double> spinnerPrecioHasta;

    @FXML
    private Button botonBuscar;

    @FXML
    private Button botonReiniciarCampos;
    @FXML
    private Button botonAtras;

    private Cliente userCliente;
    private Trabajador userTrabajador;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        factoriaPedidos = FactoriaPedidos.getInstance();

        // Configurar valores iniciales para los spinners
        spinnerIdDesde.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerIdHasta.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 100));
        spinnerPrecioDesde.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0));
        spinnerPrecioHasta.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 100.0));

        botonBuscar.setOnAction(this::handleBuscar);
        botonReiniciarCampos.setOnAction(this::handleReiniciarCampos);
        botonAtras.setOnAction(this::handleAtras);
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Búsqueda avanzada");

        LOGGER.info("Inicializando la escena principal");

        stage.show();

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado (Cliente): " + userCliente.getId());
            } else {
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado (Trabajador): " + userCliente.getId());
            }
        }
    }

    /**
     * Maneja la acción del botón Buscar, aplicando los filtros seleccionados.
     */
    private void handleBuscar(ActionEvent event) {
        try {
            Collection<Pedido> pedidosFiltrados = new ArrayList<>(factoriaPedidos.acceso().getAllPedidos());

            if (checkBoxIdPedido.isSelected()) {
                int idDesde = spinnerIdDesde.getValue();
                int idHasta = spinnerIdHasta.getValue();
                pedidosFiltrados.removeIf(pedido -> pedido.getId().intValue() < idDesde || pedido.getId().intValue() > idHasta);
            }

            if (checkBoxCIF.isSelected()) {
                String cifSeleccionado = comboBoxCIF.getValue();
                pedidosFiltrados.removeIf(pedido -> !pedido.getCifCliente().equalsIgnoreCase(cifSeleccionado));
            }

            if (checkBoxDireccion.isSelected()) {
                String direccion = textFieldDireccion.getText().toLowerCase();
                pedidosFiltrados.removeIf(pedido -> !pedido.getDireccion().toLowerCase().contains(direccion));
            }

            if (checkBoxFecha.isSelected()) {
                Date fechaDesde = convertToDate(datePickerDesde.getValue());
                Date fechaHasta = convertToDate(datePickerHasta.getValue());
                pedidosFiltrados.removeIf(pedido -> pedido.getFechaPedido().before(fechaDesde) || pedido.getFechaPedido().after(fechaHasta));
            }

            if (checkBoxEstado.isSelected()) {
                String estadoSeleccionado = comboBoxEstado.getValue();
                pedidosFiltrados.removeIf(pedido -> !pedido.getEstado().equals(estadoSeleccionado));
            }

            if (checkBoxPrecio.isSelected()) {
                double precioDesde = spinnerPrecioDesde.getValue();
                double precioHasta = spinnerPrecioHasta.getValue();
                pedidosFiltrados.removeIf(pedido -> pedido.getTotal() < precioDesde || pedido.getTotal() > precioHasta);
            }

            mostrarResultados(pedidosFiltrados);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar los pedidos", e);
            mostrarError("Error al filtrar los pedidos", e.getMessage());
        }
    }

    /**
     * Convierte un LocalDate a Date.
     *
     * @param localDate la fecha en formato LocalDate
     * @return la fecha convertida en formato Date
     */
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Reinicia todos los campos del formulario.
     */
    private void handleReiniciarCampos(ActionEvent event) {
        checkBoxIdPedido.setSelected(false);
        checkBoxCIF.setSelected(false);
        checkBoxDireccion.setSelected(false);
        checkBoxFecha.setSelected(false);
        checkBoxEstado.setSelected(false);
        checkBoxPrecio.setSelected(false);

        spinnerIdDesde.getValueFactory().setValue(1);
        spinnerIdHasta.getValueFactory().setValue(100);
        spinnerPrecioDesde.getValueFactory().setValue(0.0);
        spinnerPrecioHasta.getValueFactory().setValue(100.0);

        comboBoxCIF.getSelectionModel().clearSelection();
        comboBoxEstado.getSelectionModel().clearSelection();

        textFieldDireccion.clear();
        datePickerDesde.setValue(null);
        datePickerHasta.setValue(null);
    }

    /**
     * Muestra los resultados filtrados.
     */
    private void mostrarResultados(Collection<Pedido> pedidosFiltrados) {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, pedidosFiltrados);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, pedidosFiltrados);
        }
    }

    /**
     * Muestra un error en un cuadro de diálogo.
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleAtras(ActionEvent event) {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, null);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, null);
        }
    }
}
