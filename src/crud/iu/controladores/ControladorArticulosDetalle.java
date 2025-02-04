package crud.iu.controladores;

import crud.negocio.FactoriaAlmacen;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Almacen;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de detalle de un Artículo.
 * <p>
 * Permite visualizar y asignar almacenes a un artículo, así como gestionar el
 * resto de campos que describen al artículo. También maneja la navegación y
 * acciones disponibles (guardar cambios, eliminar asignaciones, entre otros).
 */
public class ControladorArticulosDetalle implements Initializable {

    /**
     * Logger para la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosDetalle.class.getName());

    /**
     * Factoría para la gestión de Artículos.
     */
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();

    /**
     * Factoría para la gestión de Almacenes.
     */
    private FactoriaAlmacen factoriaAlmacenes = FactoriaAlmacen.getInstance();

    /**
     * Factoría para la gestión de Usuarios.
     */
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();

    /**
     * Factoría para la gestión de Pedidos.
     */
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();

    /**
     * Referencia al escenario principal (Stage).
     */
    private Stage stage;

    /**
     * Artículo que se está visualizando o editando en esta ventana.
     */
    private Articulo articulo;

    /**
     * Referencia al Trabajador que está usando la aplicación.
     */
    private Trabajador userTrabajador;

    /**
     * Lista observable de almacenes asociados al artículo.
     */
    private ObservableList<Almacen> almacenesPorArticulo;

    /**
     * Lista observable de almacenes disponibles (no asociados todavía).
     */
    private ObservableList<Almacen> almacenesDisponibles;

    /**
     * Lista de almacenes disponibles original antes de cualquier modificación.
     */
    private ObservableList<Almacen> almacenesDisponiblesOriginales;

    /**
     * Lista de almacenes originales del artículo antes de cualquier
     * modificación.
     */
    private ObservableList<Almacen> almacenesDelArticuloOriginal;

    /**
     * Contenedor principal de la vista.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * Botón para asignar un almacén al artículo.
     */
    @FXML
    private Button botonAlmacen;

    /**
     * Botón para guardar los cambios realizados.
     */
    @FXML
    private Button botonGuardar;

    /**
     * Botón para volver a la pantalla anterior.
     */
    @FXML
    private Button botonAtras;

    /**
     * Botón para eliminar un almacén asignado al artículo.
     */
    @FXML
    private Button botonEliminar;

    /**
     * Tabla de almacenes disponibles (no asignados al artículo).
     */
    @FXML
    private TableView<Almacen> tablaAlmacenesDisponibles;

    /**
     * Columna de la tabla de almacenes disponibles que muestra el ID.
     */
    @FXML
    private TableColumn<Almacen, Long> columnaId;

    /**
     * Columna de la tabla de almacenes disponibles que muestra la dirección.
     */
    @FXML
    private TableColumn<Almacen, String> columnaDireccion;

    /**
     * Columna de la tabla de almacenes disponibles que muestra el espacio
     * disponible.
     */
    @FXML
    private TableColumn<Almacen, Double> columnaEspacios;

    /**
     * Tabla de almacenes actualmente asignados al artículo.
     */
    @FXML
    private TableView<Almacen> tablaAlmacenesArticulo;

    /**
     * Columna de la tabla de almacenes del artículo que muestra el ID.
     */
    @FXML
    private TableColumn<Almacen, Long> columnaId2;

    /**
     * Columna de la tabla de almacenes del artículo que muestra la dirección.
     */
    @FXML
    private TableColumn<Almacen, String> columnaDireccion2;

    /**
     * Columna de la tabla de almacenes del artículo que muestra el espacio
     * disponible.
     */
    @FXML
    private TableColumn<Almacen, Double> columnaEspacios2;

    /**
     * Campo de texto para mostrar el ID del artículo.
     */
    @FXML
    private TextField campoId;

    /**
     * Campo de texto para mostrar el nombre del artículo.
     */
    @FXML
    private TextField campoNombre;

    /**
     * Campo de texto para mostrar el precio del artículo.
     */
    @FXML
    private TextField campoPrecio;

    /**
     * Selector de fecha para la fecha de reposición del artículo.
     */
    @FXML
    private DatePicker campoFecha;

    /**
     * Campo de texto para la descripción del artículo.
     */
    @FXML
    private TextField campoDescripcion;

    /**
     * Campo de texto para el stock del artículo.
     */
    @FXML
    private TextField campoStock;

    /**
     * Indicador de cambios no guardados.
     */
    private boolean cambiosNoGuardados = false;

    /**
     * Lista auxiliar de almacenes a modificar (agregar o eliminar).
     */
    List<Almacen> almacenesCambiar = new ArrayList<>();

    /**
     * Inicializa la escena y configura el menú, handlers, etc.
     *
     * @param root Nodo raíz de la vista.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Detalle");
        LOGGER.info("Inicializando la escena principal");
        configurarMenu();
        configurarHandlers();
        stage.show();  // Mostrar el escenario

        if (articulo != null) {
            cargarArticuloEnFormulario();
            cargarAlmacenesDelArticulo();
            cargarAlmacenesDisponibles();
            configureMnemotecnicKeys();
        }
    }

    /**
     * Configura las teclas de acceso rápido para los botones de iniciar sesión
     * y registrar.
     */
    private void configureMnemotecnicKeys() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonAlmacen.fire();  // Simula el clic en el botón Nuevo
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonEliminar.fire();  // Simula el clic en el boton reiniciar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.A) {
                botonAtras.fire();  // Simula el clic botom busqueda
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.G) {
                botonGuardar.fire();  // Simula el clic en el boton atras
                event.consume();  // Evita la propagación adicional del evento
            }

        });
    }

    /**
     * Método de inicialización del controlador cuando se carga el FXML.
     *
     * @param url Localización del archivo FXML (no se usa en este caso).
     * @param rb Recursos de internacionalización (no se usan en este caso).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosDetalle");
        configurarTablas();

        almacenesPorArticulo = FXCollections.observableArrayList();
        almacenesDelArticuloOriginal = FXCollections.observableArrayList(
                almacenesPorArticulo.stream()
                        .map(Almacen::clone)
                        .collect(Collectors.toList()));
    }

    /**
     * Asigna el usuario (Trabajador) que está utilizando la aplicación.
     *
     * @param user Objeto que representa al Trabajador.
     */
    public void setUser(Object user) {
        if (user != null) {
            this.userTrabajador = new Trabajador();
            this.userTrabajador = (Trabajador) user;
            LOGGER.info("Usuario asignado: " + userTrabajador.getNombre());
        }
    }

    /**
     * Asigna el escenario principal (Stage).
     *
     * @param stage Escenario de JavaFX.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Asigna el artículo con el que se va a trabajar en esta vista.
     *
     * @param articulo Objeto Articulo a editar o visualizar.
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
        LOGGER.info("Articulo asignado.");
    }

    /**
     * Carga en los campos del formulario los datos del artículo.
     */
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

    /**
     * Configura los menús y sus acciones dentro del BorderPane.
     */
    public void configurarMenu() {
        // Obtiene el BorderPane de la raíz
        BorderPane borderPane = (BorderPane) anchorPane.getChildrenUnmodifiable().get(0);

        // Obtiene el menú incluido desde el BorderPane (posición superior)
        MenuBar menuBar = (MenuBar) borderPane.getTop();

        // Accede a los menús dentro del menú incluido
        Menu menuPrincipal = menuBar.getMenus().get(0); // Primer menú ("Menú")
        Menu menuIr = menuBar.getMenus().get(1);
        Menu menuAyuda = menuBar.getMenus().get(2);

        // Configura un listener para cada opción dentro del menú "Menú"
        MenuItem opcionImprimir = menuPrincipal.getItems().get(0); // "Imprimir informe"
        opcionImprimir.setVisible(false);

        MenuItem opcionCerrarSesion = menuPrincipal.getItems().get(1); // "Cerrar sesión"
        opcionCerrarSesion.setOnAction(event -> cerrarSesion());

        MenuItem opcionSalir = menuPrincipal.getItems().get(2); // "Salir del programa"
        opcionSalir.setOnAction(event -> salirPrograma());

        MenuItem opcionVolver = menuPrincipal.getItems().get(3); // "Volver al Menú principal"
        opcionVolver.setOnAction(event -> volverAlMenuPrincipal());

        // Configura un listener para las opciones del menú "Ir a"
        MenuItem opcionIrPedidos = menuIr.getItems().get(0); // "Vista Pedido"
        opcionIrPedidos.setVisible(true);
        opcionIrPedidos.setOnAction(event -> irVistaPedidos());

        MenuItem opcionIrArticulos = menuIr.getItems().get(1); // "Vista Artículo"
        opcionIrArticulos.setVisible(false);

        MenuItem botonAyuda = menuAyuda.getItems().get(0);
        botonAyuda.setOnAction(event -> {
            mostrarAyuda();
        });
    }

    /**
     * Cierra la sesión actual.
     */
    private void cerrarSesion() {
        System.out.println("Cerrando sesión...");
        stage.close();
    }

    /**
     * Sale completamente de la aplicación.
     */
    private void salirPrograma() {
        System.out.println("Saliendo del programa...");
        System.exit(0);
    }

    /**
     * Vuelve al menú principal de la aplicación.
     */
    private void volverAlMenuPrincipal() {
        factoriaUsuarios.cargarMenuPrincipal(stage, userTrabajador);
    }

    /**
     * Navega a la vista de Pedidos.
     */
    private void irVistaPedidos() {
        factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, null);
    }

    /**
     * Configura las tablas (almacenes disponibles y almacenes del artículo)
     * asignándoles columnas y formateadores de celdas.
     */
    private void configurarTablas() {

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        columnaEspacios.setCellValueFactory(new PropertyValueFactory<>("espacio"));
        columnaEspacios.setCellFactory(tc -> new TableCell<Almacen, Double>() {

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        tablaAlmacenesDisponibles.getColumns().forEach(col -> col.setSortable(false));

        //TablaAlmacenesArticulo
        columnaId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaDireccion2.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaEspacios2.setCellValueFactory(new PropertyValueFactory<>("espacio"));
        columnaEspacios2.setCellFactory(tc -> new TableCell<Almacen, Double>() {

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        tablaAlmacenesArticulo.getColumns().forEach(col -> col.setSortable(false));
    }

    /**
     * Carga la lista de almacenes disponibles (que no están asignados al
     * artículo).
     */
    private void cargarAlmacenesDisponibles() {
        try {
            Collection<Almacen> almacenes = factoriaAlmacenes.acceso().getAllAlmacenes();

            Set<Long> idsAlmacenes = almacenesPorArticulo.stream()
                    .map(Almacen::getId)
                    .collect(Collectors.toSet());

            almacenes.removeIf(almacen -> idsAlmacenes.contains(almacen.getId()));

            almacenesDisponibles = FXCollections.observableArrayList(almacenes);

            // Ordenar por ID para mayor claridad
            FXCollections.sort(almacenesDisponibles, Comparator.comparing(Almacen::getId));
            tablaAlmacenesDisponibles.setItems(almacenesDisponibles);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los Almacenes disponibles", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los Almacenes disponibles.");
        }
    }

    /**
     * Carga la lista de almacenes asignados al artículo.
     */
    private void cargarAlmacenesDelArticulo() {
        try {
            Collection<Almacen> almacenes = factoriaAlmacenes.acceso().getAllAlmacenesById(articulo.getId());

            almacenesDelArticuloOriginal = FXCollections.observableArrayList(
                    almacenes.stream()
                            .map(Almacen::clone)
                            .collect(Collectors.toList()));

            almacenesPorArticulo = FXCollections.observableArrayList(almacenes);

            tablaAlmacenesArticulo.setItems(almacenesPorArticulo);
            tablaAlmacenesArticulo.refresh();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los almacenes del articulo", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los almacenes del articulo.");
        }

    }

    /**
     * Configura los manejadores (handlers) de los botones de la interfaz.
     */
    private void configurarHandlers() {
        botonAlmacen.setOnAction(this::handleAsignarAlmacen);
        botonEliminar.setOnAction(this::handleEliminar);
        botonGuardar.setOnAction(this::handleGuardarCambios);
        botonAtras.setOnAction(this::handleAtras);
    }

    /**
     * Asigna el almacén seleccionado de la tabla de disponibles al artículo.
     *
     * @param event Acción que dispara el método.
     */
    @FXML
    private void handleAsignarAlmacen(ActionEvent event) {
        Almacen almacenSeleccionado = tablaAlmacenesDisponibles.getSelectionModel().getSelectedItem();
        if (almacenSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Agregar almacen",
                    "Seleccione un almacen para agregar.");
            return;
        }

        almacenesPorArticulo.add(almacenSeleccionado);
        almacenesCambiar.add(almacenSeleccionado);
        tablaAlmacenesArticulo.refresh();
        cargarAlmacenesDisponibles();
        cambiosNoGuardados = true;
    }

    /**
     * Guarda los cambios realizados en la asignación de almacenes para el
     * artículo.
     *
     * @param event Acción que dispara el método.
     */
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        LOGGER.info("Botón Guardar Cambios presionado");
        List<Almacen> almacenes = tablaAlmacenesArticulo.getItems();
        try {
            articulo.setAlmacenTrump(almacenes);
            factoriaArticulos.acceso().actualizarArticulo(articulo);

            cambiosNoGuardados = false;
            reiniciar();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
        }
    }

    /**
     * Refresca la vista tras guardar cambios o revertirlos.
     */
    private void reiniciar() {
        configurarTablas();
        cargarAlmacenesDelArticulo();
        cargarAlmacenesDisponibles();
        tablaAlmacenesDisponibles.refresh();
        tablaAlmacenesArticulo.refresh();
    }

    /**
     * Elimina el almacén seleccionado de la lista de almacenes asignados al
     * artículo.
     *
     * @param event Acción que dispara el método.
     */
    @FXML
    private void handleEliminar(ActionEvent event) {
        Almacen almacenSeleccionado = tablaAlmacenesArticulo.getSelectionModel().getSelectedItem();
        if (almacenSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Eliminar almacen",
                    "Seleccione un almacen para eliminar.");
            return;
        }

        almacenesPorArticulo.remove(almacenSeleccionado);
        almacenesDisponibles.add(almacenSeleccionado);
        tablaAlmacenesArticulo.refresh();
        cargarAlmacenesDisponibles();
        cambiosNoGuardados = true;
    }

    /**
     * Maneja el evento de volver atrás. Si hay cambios sin guardar, pide
     * confirmación.
     *
     * @param event Acción que dispara el método.
     */
    @FXML
    private void handleAtras(ActionEvent event) {
        if (cambiosNoGuardados) {
            // Preguntar confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cambios sin guardar");
            alert.setHeaderText(null);
            alert.setContentText("Hay cambios sin guardar. ¿Qué desea hacer?");

            // Botones Guardar, No guardar, Cancelar
            ButtonType guardarButton = new ButtonType("Guardar");
            ButtonType noGuardarButton = new ButtonType("No guardar");
            ButtonType cancelarButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(guardarButton, noGuardarButton, cancelarButton);

            // Mostrar y esperar la respuesta del usuario
            alert.showAndWait().ifPresent(response -> {
                if (response == guardarButton) {
                    // Guardar cambios y salir
                    handleGuardarCambios(event);
                    irAPantallaPrincipal();
                } else if (response == noGuardarButton) {
                    // Salir sin guardar
                    irAPantallaPrincipal();
                }
                // Si es Cancelar, no hacemos nada
            });
        } else {
            // Si no hay cambios, volver directamente
            irAPantallaPrincipal();
        }

        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, null);
    }

    /**
     * Navega a la pantalla principal de artículos.
     */
    private void irAPantallaPrincipal() {
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, null);
    }

    /**
     * Muestra una alerta de JavaFX.
     *
     * @param tipo Tipo de alerta (ERROR, WARNING, INFORMATION, etc.).
     * @param titulo Título de la ventana de alerta.
     * @param mensaje Mensaje de la alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra la ayuda (vista de ayuda o manual).
     */
    private void mostrarAyuda() {
        factoriaUsuarios.cargarAyuda("articulosDetalle");
    }

}
