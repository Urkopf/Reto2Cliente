package crud.iu.controladores;

import crud.negocio.FactoriaPedidos;
import crud.negocio.IPedido;
import crud.negocio.PedidoImpl;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorPedidosPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosPrincipal.class.getName());
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private Stage stage;
    private Usuario usuario;
    private ObservableList<Pedido> pedidosObservableList;

    // Elementos FXML
    @FXML
    private Button botonNuevo;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBusqueda;
    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, Long> columnaId;
    @FXML
    private TableColumn<Pedido, Long> columnaUsuarioId;
    @FXML
    private TableColumn<Pedido, String> columnaCif;
    @FXML
    private TableColumn<Pedido, String> columnaEstado;
    @FXML
    private TableColumn<Pedido, Double> columnaTotal;
    @FXML
    private Pagination paginador;
    @FXML
    private MenuItem opcionIrPedidos;

    /**
     * Inicializa el controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador PedidosPrincipal");
        configurarTabla();
        cargarDatosPedidos();
    }

    /**
     * Configura las columnas de la tabla de pedidos.
     */
    private void configurarTabla() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        columnaCif.setCellValueFactory(new PropertyValueFactory<>("cifCliente"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    /**
     * Carga los datos de pedidos en la tabla.
     */
    private void cargarDatosPedidos() {
        try {
            LOGGER.info("Cargando datos de pedidos...");
            Collection<Pedido> pedidos = factoriaPedidos.acceso().getAllPedidos();
            if (pedidos == null || pedidos.isEmpty()) {
                LOGGER.warning("No se encontraron pedidos.");
                pedidos = new ArrayList<>();
            }
            pedidosObservableList = FXCollections.observableArrayList(pedidos);
            tablaPedidos.setItems(pedidosObservableList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los datos de pedidos", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error al cargar pedidos", "No se pudieron cargar los pedidos. Intente nuevamente más tarde.");
        }
    }

    /**
     * Establece el usuario activo.
     *
     * @param usuario Usuario activo.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        LOGGER.info("Usuario asignado: " + usuario.getNombre());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Inicializa la ventana principal.
     *
     * @param root Nodo raíz de la escena.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario
    }

    /**
     * Acción para crear un nuevo pedido.
     */
    @FXML
    private void handleNuevoPedido(ActionEvent event) {
        LOGGER.info("Botón Nuevo presionado");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nuevo Pedido");
        alert.setHeaderText(null);
        alert.setContentText("Funcionalidad para crear un nuevo pedido.");
        alert.showAndWait();
    }

    /**
     * Acción para reiniciar la tabla.
     */
    @FXML
    private void handleReiniciarTabla(ActionEvent event) {
        LOGGER.info("Botón Reiniciar Tabla presionado");
        cargarDatosPedidos();
    }
}
