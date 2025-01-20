package crud.iu.controladores;

import crud.negocio.FactoriaAlmacen;
import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Almacen;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorArticulosDetalle implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosDetalle.class.getName());
    private FactoriaArticulos factoriaPedidos = FactoriaArticulos.getInstance();
    private FactoriaAlmacen factoriaAlmacenes = FactoriaAlmacen.getInstance();
    private Stage stage;
    private Usuario usuario;
    private ObservableList<Articulo> articulosObservableList;
    private ObservableList<Almacen> almacenesObservableList;
    private static final int FILAS_POR_PAGINA = 14;

    // Elementos FXML
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button botonNuevo;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBusqueda;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonDetalles;
    @FXML
    private Button botonAtras;
    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, Long> columnaId;
    @FXML
    private TableColumn<Pedido, Long> columnaUsuarioId;
    @FXML
    private TableColumn<Pedido, String> columnaCif;
    @FXML
    private TableColumn<Pedido, Estado> columnaEstado;
    @FXML
    private TableColumn<Pedido, Date> columnaFecha;
    @FXML
    private TableColumn<Pedido, Double> columnaTotal;
    @FXML
    private Pagination paginador;
    @FXML
    private MenuItem opcionIrPedidos;
    private Trabajador userTrabajador;

    // Copia de seguridad de los datos originales
    private ObservableList<Articulo> articulosOriginales;
    private ObservableList<Almacen> almacenesOriginales;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Detalle");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosDetalle");

    }

    public void setUser(Object user) {
        if (user != null) {
            this.userTrabajador = new Trabajador();
            this.userTrabajador = (Trabajador) user;
            LOGGER.info("Usuario asignado: " + userTrabajador.getNombre());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

}
