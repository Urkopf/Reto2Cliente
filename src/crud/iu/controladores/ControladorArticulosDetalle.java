package crud.iu.controladores;

import crud.negocio.FactoriaAlmacen;
import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Almacen;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import java.net.URL;
import java.time.ZoneId;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorArticulosDetalle implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosDetalle.class.getName());
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private FactoriaAlmacen factoriaAlmacenes = FactoriaAlmacen.getInstance();
    private Stage stage;
    private Articulo articulo;
    private Trabajador userTrabajador;
    private ObservableList<Almacen> almacenesPorArticulo;
    private ObservableList<Almacen> almacenesDisponibles;

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
    private TableView<Almacen> tablaAlmacenesDisponibles;
    @FXML
    private TableColumn<Almacen, Long> columnaId;
    @FXML
    private TableColumn<Almacen, String> columnaDireccion;
    @FXML
    private TableColumn<Almacen, String> columnaEspacios;

    @FXML
    private TableView<Almacen> tablaAlmacenesArticulo;
    @FXML
    private TableColumn<Almacen, Long> columnaId2;
    @FXML
    private TableColumn<Almacen, String> columnaDireccion2;
    @FXML
    private TableColumn<Almacen, String> columnaEspacios2;

    @FXML
    private TextField campoId;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoPrecio;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private TextField campoDescripcion;
    @FXML
    private TextField campoStock;

    // Copia de seguridad de los datos originales
    private ObservableList<Articulo> articulosOriginales;
    private ObservableList<Almacen> almacenesOriginales;
    private ObservableList<Almacen> almacenesDelArticulo;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Detalle");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario

        if (articulo != null) {
            cargarArticuloEnFormulario();
            cargarAlmacenesDelArticulo();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosDetalle");
        configurarTablas();
        cargarAlmacenesDisponibles();

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

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
        LOGGER.info("Articulo asignado.");
    }

    private void cargarArticuloEnFormulario() {
        if (articulo != null) {
            campoId.setText(String.valueOf(articulo.getId()));
            campoNombre.setText(articulo.getNombre());
            campoPrecio.setText(articulo.getPrecio() + "€");
            campoDescripcion.setText(articulo.getDescripcion());
            campoStock.setText(articulo.getStock() + "unid.");

            campoFecha.setValue(
                    articulo.getFechaReposicion().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
            );

        }
    }

    private void configurarTablas() {

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaEspacios.setCellValueFactory(new PropertyValueFactory<>("espacio"));

        tablaAlmacenesDisponibles.getColumns().forEach(col -> col.setSortable(false));

        columnaId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaDireccion2.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaEspacios2.setCellValueFactory(new PropertyValueFactory<>("espacio"));

        tablaAlmacenesArticulo.getColumns().forEach(col -> col.setSortable(false));
    }

    private void cargarAlmacenesDisponibles() {
        try {
            Collection<Almacen> almacenes = factoriaAlmacenes.acceso().getAllAlmacenes();
            almacenesDisponibles = FXCollections.observableArrayList(almacenes);

            tablaAlmacenesDisponibles.setItems(almacenesDisponibles);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los alamcenes disponibles", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los almacenes disponibles.");

        }
    }

    private void cargarAlmacenesDelArticulo() {
        try {
            LOGGER.log(Level.INFO, "Articulo: {0}", articulo.getId());
            Collection<Almacen> almacenes = factoriaAlmacenes.acceso().getAllAlmacenesById(articulo.getId());
            if (almacenes != null) {
                LOGGER.log(Level.INFO, "Estoy lleno");
            }
            if (almacenes != null && articulo != null && articulo.getId() != null) {
                almacenesDelArticulo = FXCollections.observableArrayList(
                        almacenes.stream()
                                .filter(pa -> pa.getId() != null
                                && pa.getId().equals(articulo.getId()))
                                .collect(Collectors.toList())
                );
            } else {
                almacenesDelArticulo = FXCollections.observableArrayList();
            }

            tablaAlmacenesArticulo.setItems(almacenesDelArticulo);
            tablaAlmacenesArticulo.refresh();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los almacenes del articulo", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los almacenes del articulo.");
        }

    }

    @FXML
    private void handleAsignarAlmacen(ActionEvent event) {

    }

    @FXML
    private void handleEliminar(ActionEvent event) {

    }

    @FXML
    private void handleAtras(ActionEvent event) {
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador);
    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        LOGGER.info("Botón Guardar Cambios presionado");

    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
