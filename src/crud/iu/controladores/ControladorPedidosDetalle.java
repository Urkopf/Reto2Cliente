package crud.iu.controladores;

import crud.excepciones.LogicaNegocioException;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.PedidoArticulo;
import crud.objetosTransferibles.Trabajador;
import crud.utilidades.AlertUtilities;

import static crud.utilidades.AlertUtilities.showErrorDialog;

import java.net.URL;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * <h1>ControladorPedidosDetalle</h1>
 * Controlador para la ventana de detalles de un pedido. Permite gestionar los
 * artículos asociados a dicho pedido (añadir, eliminar o modificar cantidades),
 * así como editar los datos generales del pedido (dirección, fecha, estado,
 * etc.).
 *
 * <p>
 * También muestra el precio subtotal de cada artículo y el total acumulado del
 * pedido.
 * </p>
 *
 * <p>
 * <strong>Autor:</strong>
 * <a href="mailto:urkoperitz@example.com">Urko Peritz</a>
 * </p>
 */
public class ControladorPedidosDetalle implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="Constantes y Logger">
    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosDetalle.class.getName());
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Factorías de acceso a datos">
    private final FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private final FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private final FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private final FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stage y entidades principales">
    private Stage stage;
    private Pedido pedido;
    private Cliente userCliente;
    private Trabajador userTrabajador;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Listas para la tabla de artículos">
    /**
     * Lista de todos los artículos disponibles en el sistema.
     */
    private ObservableList<Articulo> articulosDisponibles;

    /**
     * Lista de artículos que forman parte del pedido actual.
     */
    private ObservableList<PedidoArticulo> articulosDelPedido;

    /**
     * Copia original de los artículos del pedido, para detectar cambios y
     * borrados.
     */
    private ObservableList<PedidoArticulo> articulosDelPedidoOriginales;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Componentes FXML">
    @FXML
    private AnchorPane anchorPane;

    // Tabla de artículos disponibles
    @FXML
    private TableView<Articulo> tablaArticulosDisponibles;
    @FXML
    private TableColumn<Articulo, Long> columnaId;
    @FXML
    private TableColumn<Articulo, String> columnaNombre;
    @FXML
    private TableColumn<Articulo, Double> columnaPrecio;

    // Tabla de artículos en el pedido
    @FXML
    private TableView<PedidoArticulo> tablaArticulosPedidos;
    @FXML
    private TableColumn<PedidoArticulo, Long> columnaPedidoArticuloId;
    @FXML
    private TableColumn<PedidoArticulo, Long> columnaId2;
    @FXML
    private TableColumn<PedidoArticulo, String> columnaNombre2;
    @FXML
    private TableColumn<PedidoArticulo, Integer> columnaUnidades2;
    @FXML
    private TableColumn<PedidoArticulo, Double> columnaPrecio2;

    // Campos de formulario de pedido
    @FXML
    private TextField campoTotal;
    @FXML
    private Button botonCompra;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonAtras;
    @FXML
    private TextField campoId;
    @FXML
    private ComboBox<String> campoCif;
    @FXML
    private TextField campoDireccion;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private ComboBox<Estado> campoEstado;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Control de cambios no guardados">
    /**
     * Indica si hay cambios sin guardar. Se marca a {@code true} cada vez que
     * se modifica un artículo o cualquier campo del pedido. Se restablece a
     * {@code false} en {@link #handleGuardarCambios(ActionEvent)}.
     */
    private boolean cambiosNoGuardados = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos de ciclo de vida de JavaFX">
    /**
     * Se ejecuta tras cargar el FXML.
     *
     * @param location URL de la ubicación de origen.
     * @param resources Recursos de internacionalización.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTablas();
        cargarArticulosDisponibles();

        articulosDelPedido = FXCollections.observableArrayList();
        articulosDelPedidoOriginales = FXCollections.observableArrayList(
                articulosDelPedido.stream()
                        .map(PedidoArticulo::clone)
                        .collect(Collectors.toList())
        );

        // Llamada inicial para mostrar el total (inicialmente 0)
        actualizarTotal();
    }

    /**
     * Inicializa la escena, asigna el {@code Stage} y configura la ventana.
     * Llamar a este método tras configurar el pedido y el usuario.
     *
     * @param root Nodo raíz (parent) de la escena.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Detalles del pedido");

        LOGGER.info("Inicializando la escena principal");

        configurarHandlers();
        stage.show();

        // Si ya tenemos el pedido, cargamos sus datos en la vista
        if (pedido != null) {
            cargarDatosPedidoEnVista();
            // Cargamos artículos del pedido y establecemos listeners
            cargarArticulosDelPedido();
            actualizarTotal();
            agregarListeners();
        }

        // Si el usuario es un cliente, deshabilitar ciertos campos
        if (userCliente != null) {
            campoEstado.setDisable(true);
            campoCif.setDisable(true);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Setters para Stage, Pedido y Usuario">
    /**
     * Asigna el {@code Stage} principal para este controlador.
     *
     * @param stage Stage de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Asigna el pedido que se está gestionando.
     *
     * @param pedido El {@link Pedido} que se detallará en la vista.
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Asigna el usuario que está visualizando/gestionando el pedido. Puede ser
     * un {@link Cliente} o un {@link Trabajador}.
     *
     * @param user Objeto de tipo {@code Cliente} o {@code Trabajador}.
     */
    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado (Cliente): "
                        + userCliente.getNombre()
                        + " ID=" + userCliente.getId());
            } else {
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado (Trabajador): " + userTrabajador.getNombre());
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Configuración de tablas">
    /**
     * Configura las tablas de artículos disponibles y artículos del pedido.
     * Ajusta las columnas, sus celdas y el modo de edición (Spinner para
     * cantidades).
     */
    private void configurarTablas() {
        // ---------------- Tabla de artículos disponibles ----------------
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Columna Precio (alineación derecha + " €")
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaPrecio.setCellFactory(tc -> new TableCell<Articulo, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        // Evitar reordenación de columnas
        tablaArticulosDisponibles.getColumns().forEach(col -> col.setSortable(false));

        // ---------------- Tabla de artículos en el pedido ----------------
        columnaPedidoArticuloId.setCellValueFactory(new PropertyValueFactory<>("pedidoArticuloId"));
        columnaId2.setCellValueFactory(new PropertyValueFactory<>("articuloId"));

        // Nombre
        columnaNombre2.setSortable(false);
        columnaNombre2.setCellValueFactory(new PropertyValueFactory<>("articuloNombre"));

        // Cantidad (Spinner)
        columnaUnidades2.setSortable(false);
        columnaUnidades2.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaUnidades2.setCellFactory(tc -> new TableCell<PedidoArticulo, Integer>() {

            private Spinner<Integer> spinner;

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                PedidoArticulo pa = (PedidoArticulo) getTableRow().getItem();
                Articulo articulo = buscarArticuloPorId(pa.getArticuloId());

                int maxStock = (articulo != null) ? articulo.getStock() : 1000;
                spinner = new Spinner<>();
                spinner.setEditable(true);

                // Definir el rango de valores
                SpinnerValueFactory<Integer> valueFactory
                        = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxStock, pa.getCantidad());
                spinner.setValueFactory(valueFactory);

                // Listener para actualizar el modelo al cambiar el valor del Spinner
                spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.equals(pa.getCantidad())) {
                        pa.setCantidad(newVal);
                        actualizarTotal();
                        cambiosNoGuardados = true; // Se ha modificado la cantidad
                    }
                });

                // Listener para refrescar la tabla al perder el foco
                spinner.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (!isFocused) {
                        commitEdit(spinner.getValue());
                        getTableView().refresh();
                        // Ordenar si se desea
                        ordenarTablaPorArticuloId();
                    }
                });

                // Manejar edición manual (teclado)
                spinner.getEditor().textProperty().addListener((obs, oldText, newText) -> {
                    try {
                        int value = Integer.parseInt(newText);
                        if (value < 1) {
                            spinner.getValueFactory().setValue(1);
                        } else if (value > maxStock) {
                            spinner.getValueFactory().setValue(maxStock);
                        } else {
                            spinner.getValueFactory().setValue(value);
                        }
                    } catch (NumberFormatException e) {
                        spinner.getEditor().setText(oldText);
                    }
                });

                setGraphic(spinner);
            }
        });

        // Precio (Subtotal)
        columnaPrecio2.setSortable(false);
        columnaPrecio2.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaPrecio2.setCellFactory(tc -> new TableCell<PedidoArticulo, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                PedidoArticulo pa = (PedidoArticulo) getTableRow().getItem();
                Articulo articulo = buscarArticuloPorId(pa.getArticuloId());
                double precioUnitario = (articulo != null ? articulo.getPrecio() : 0);
                double subtotal = precioUnitario * pa.getCantidad();

                setText(String.format("%.2f €", subtotal));
                setStyle("-fx-alignment: CENTER-RIGHT;");
            }
        });

        // Evitar reordenación de columnas en la tabla de pedido
        tablaArticulosPedidos.getColumns().forEach(col -> col.setSortable(false));

        // Ajustar ancho de columnas en ambas tablas
        ajustarAnchoProporcionalTablaArticulosDisponibles(tablaArticulosDisponibles);
        ajustarAnchoProporcionalTablaArticulosComprados(tablaArticulosPedidos);
    }

    /**
     * Ajusta el ancho de las columnas de la tabla de artículos disponibles de
     * forma proporcional.
     *
     * @param tableView La tabla de artículos disponibles.
     */
    private void ajustarAnchoProporcionalTablaArticulosDisponibles(TableView<?> tableView) {
        double totalWidth = tableView.getPrefWidth();
        tableView.getColumns().forEach(column -> {
            if (column.equals(columnaId)) {
                column.setPrefWidth(totalWidth * 0.1d);
            } else if (column.equals(columnaNombre)) {
                column.setPrefWidth(totalWidth * 0.6d);
            } else if (column.equals(columnaPrecio)) {
                column.setPrefWidth((totalWidth * 0.3d) - 20);
            }
        });
    }

    /**
     * Ajusta el ancho de las columnas de la tabla de artículos pedidos de forma
     * proporcional.
     *
     * @param tableView La tabla de artículos asociados al pedido.
     */
    private void ajustarAnchoProporcionalTablaArticulosComprados(TableView<?> tableView) {
        double totalWidth = tableView.getPrefWidth();
        tableView.getColumns().forEach(column -> {
            if (column.equals(columnaId2)) {
                column.setPrefWidth(totalWidth * 0.05d);
            } else if (column.equals(columnaNombre2)) {
                column.setPrefWidth(totalWidth * 0.55d);
            } else if (column.equals(columnaUnidades2)) {
                column.setPrefWidth(totalWidth * 0.2d);
            } else if (column.equals(columnaPrecio2)) {
                column.setPrefWidth((totalWidth * 0.2d) - 20);
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Carga de datos en tablas y campos">
    /**
     * Carga todos los artículos disponibles desde la base de datos y los
     * muestra en la tabla de artículos disponibles.
     */
    private void cargarArticulosDisponibles() {
        try {
            Collection<Articulo> articulos = factoriaArticulos.acceso().getAllArticulos();
            articulosDisponibles = FXCollections.observableArrayList(articulos);

            // Ordenar por ID para mayor claridad
            FXCollections.sort(articulosDisponibles, Comparator.comparing(Articulo::getId));
            tablaArticulosDisponibles.setItems(articulosDisponibles);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los artículos disponibles", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los artículos disponibles.");
        }
    }

    /**
     * Carga los artículos correspondientes al pedido y los muestra en la tabla
     * de artículos del pedido.
     */
    private void cargarArticulosDelPedido() {
        try {
            Collection<PedidoArticulo> articulosPedido = factoriaPedidoArticulo.acceso().getAllPedidoArticulo();

            if (articulosPedido != null && pedido != null && pedido.getId() != null) {
                articulosDelPedido = FXCollections.observableArrayList(
                        articulosPedido.stream()
                                .filter(pa -> pa.getPedidoId() != null
                                && pa.getPedidoId().equals(pedido.getId()))
                                .collect(Collectors.toList())
                );
            } else {
                articulosDelPedido = FXCollections.observableArrayList();
            }

            articulosDelPedidoOriginales = FXCollections.observableArrayList(
                    articulosDelPedido.stream()
                            .map(PedidoArticulo::clone)
                            .collect(Collectors.toList())
            );

            // Asignar la lista a la tabla
            tablaArticulosPedidos.setItems(articulosDelPedido);
            tablaArticulosPedidos.refresh();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los artículos del pedido", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los artículos del pedido.");
        }
    }

    /**
     * Carga los datos del pedido en los campos de la vista (ID, dirección, CIF,
     * estado, fecha).
     */
    private void cargarDatosPedidoEnVista() {
        cargarCifsClientes();
        cargarEstados();

        campoId.setText(String.valueOf(pedido.getId()));
        campoDireccion.setText(pedido.getDireccion());
        campoCif.setValue(pedido.getCifCliente());
        campoEstado.setValue(pedido.getEstado());

        campoFecha.setValue(
                pedido.getFechaPedido().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        );
    }

    /**
     * Carga la lista de CIF de todos los clientes en el {@code ComboBox}
     * campoCif.
     */
    private void cargarCifsClientes() {
        try {
            Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
            ObservableList<String> cifs = FXCollections.observableArrayList(
                    clientes.stream().map(Cliente::getCif).collect(Collectors.toList())
            );
            campoCif.setItems(cifs);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los CIF de los clientes", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los CIF de los clientes.");
        }
    }

    /**
     * Carga todos los valores posibles de {@link Estado} en el ComboBox
     * correspondiente.
     */
    private void cargarEstados() {
        campoEstado.setItems(FXCollections.observableArrayList(Estado.values()));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Configuración de handlers y listeners">
    /**
     * Configura los handlers para los botones principales. (Compra, Eliminar,
     * Guardar, Atrás).
     */
    private void configurarHandlers() {
        botonCompra.setOnAction(this::handleCompra);
        botonEliminar.setOnAction(this::handleEliminar);
        botonGuardar.setOnAction(this::handleGuardarCambios);
        botonAtras.setOnAction(this::handleAtras);
    }

    /**
     * Agrega listeners a los campos de texto y combo boxes para validaciones
     * (dirección, fecha, CIF, estado, total).
     */
    private void agregarListeners() {
        // Dirección
        campoDireccion.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) {
                if (campoDireccion.getText().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación",
                            "La dirección no puede estar vacía.");
                    campoDireccion.setText(pedido.getDireccion());
                } else {
                    pedido.setDireccion(campoDireccion.getText());
                    cambiosNoGuardados = true;
                }
            }
        });

        // CIF
        campoCif.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación",
                        "Debe seleccionar un CIF válido.");
                campoCif.setValue(oldValue);
            } else {
                pedido.setCifCliente(newValue);
                cambiosNoGuardados = true;
            }
        });

        // Estado
        campoEstado.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                pedido.setEstado(newValue);
                cambiosNoGuardados = true;
            }
        });

        // Fecha
        campoFecha.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isBefore(java.time.LocalDate.now()) && !newValue.equals(oldValue)) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación",
                            "La fecha no puede ser anterior al día de hoy.");
                    campoFecha.setValue(oldValue);
                } else {
                    pedido.setFechaPedido(java.sql.Date.valueOf(newValue));
                    cambiosNoGuardados = true;
                }
            }
        });

        // Total (si fuera editable)
        campoTotal.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) {
                try {
                    String valor = campoTotal.getText().replace(" €", "");
                    double total = Double.parseDouble(valor);
                    pedido.setTotal(total);
                    cambiosNoGuardados = true;
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación",
                            "El total debe ser un valor numérico.");
                    campoTotal.setText(String.format("%.2f €", pedido.getTotal()));
                }
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Handlers FXML">
    /**
     * Handler del botón "Compra": agrega el artículo seleccionado de la lista
     * de disponibles al pedido, con cantidad inicial de 1.
     *
     * @param event Evento de la acción.
     */
    @FXML
    private void handleCompra(ActionEvent event) {
        Articulo articuloSeleccionado = tablaArticulosDisponibles.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Agregar Artículo",
                    "Seleccione un artículo para agregar.");
            return;
        }

        // Buscar si el artículo ya existe en el pedido
        PedidoArticulo pedidoArticulo = articulosDelPedido.stream()
                .filter(pa -> pa.getArticuloId().equals(articuloSeleccionado.getId()))
                .findFirst()
                .orElse(null);

        if (pedidoArticulo != null) {
            // Si existe, incrementar la cantidad
            pedidoArticulo.setCantidad(pedidoArticulo.getCantidad() + 1);
        } else {
            // Crear nuevo PedidoArticulo
            pedidoArticulo = new PedidoArticulo();
            pedidoArticulo.setArticulo(articuloSeleccionado);
            pedidoArticulo.setPedido(pedido);
            pedidoArticulo.setCantidad(1);
            pedidoArticulo.setPrecioCompra(articuloSeleccionado.getPrecio());

            articulosDelPedido.add(pedidoArticulo);
        }

        tablaArticulosPedidos.refresh();
        actualizarTotal();
        cambiosNoGuardados = true;
    }

    /**
     * Handler del botón "Eliminar": quita uno en la cantidad del artículo
     * seleccionado o lo elimina por completo si la cantidad es 1.
     *
     * @param event Evento de la acción.
     */
    @FXML
    private void handleEliminar(ActionEvent event) {
        PedidoArticulo articuloSeleccionado = tablaArticulosPedidos.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Eliminar Artículo",
                    "Seleccione un artículo para eliminar.");
            return;
        }

        if (articuloSeleccionado.getCantidad() > 1) {
            articuloSeleccionado.setCantidad(articuloSeleccionado.getCantidad() - 1);
        } else {
            articulosDelPedido.remove(articuloSeleccionado);
        }

        tablaArticulosPedidos.refresh();
        actualizarTotal();
        cambiosNoGuardados = true;
    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        LOGGER.info("Botón Guardar Cambios presionado");
        try {
            // Actualizar el total del pedido
            NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
            String valor = campoTotal.getText().replace(" €", "");
            Number number = nf.parse(valor);
            double total = number.doubleValue();
            pedido.setTotal(total);
            factoriaPedidos.acceso().actualizarPedido(pedido);

            // Actualizar stock y pedido-artículo en la base de datos
            for (PedidoArticulo pedidoArticulo : articulosDelPedido) {
                Articulo articulo = buscarArticuloPorId(pedidoArticulo.getArticuloId());
                PedidoArticulo pedidoArticuloOriginal = articulosDelPedidoOriginales.stream()
                        .filter(p -> p.getId() != null && p.getId().equals(pedidoArticulo.getId()))
                        .findFirst()
                        .orElse(null);

                if (pedidoArticulo.getId() == null) {
                    // Nuevo PedidoArticulo: crear y restar stock
                    LOGGER.info("Creando nuevo PedidoArticulo (ArticuloID=" + pedidoArticulo.getArticuloId() + ")");
                    factoriaPedidoArticulo.acceso().crearPedidoArticulo(pedidoArticulo);
                    actualizarStock(articulo, -pedidoArticulo.getCantidad());
                } else if (pedidoArticuloOriginal != null && haCambiado(pedidoArticuloOriginal, pedidoArticulo)) {
                    // Actualizar PedidoArticulo existente
                    LOGGER.info("Actualizando PedidoArticulo con ID=" + pedidoArticulo.getId());
                    factoriaPedidoArticulo.acceso().actualizarPedidoArticulo(pedidoArticulo);

                    // Ajustar stock según la diferencia de cantidades
                    int diferenciaCantidad = pedidoArticulo.getCantidad() - pedidoArticuloOriginal.getCantidad();
                    actualizarStock(articulo, -diferenciaCantidad);
                }
            }

            // Eliminar PedidoArticulo y devolver stock
            for (PedidoArticulo pedidoArticuloOriginal : articulosDelPedidoOriginales) {
                if (!articulosDelPedido.contains(pedidoArticuloOriginal)) {
                    LOGGER.info("Borrando PedidoArticulo con ID=" + pedidoArticuloOriginal.getId());
                    factoriaPedidoArticulo.acceso().borrarPedidoArticulo(pedidoArticuloOriginal);
                    Articulo articulo = buscarArticuloPorId(pedidoArticuloOriginal.getArticuloId());
                    actualizarStock(articulo, pedidoArticuloOriginal.getCantidad());
                }
            }

            // Actualizar la lista original y marcar como guardados
            articulosDelPedidoOriginales.setAll(articulosDelPedido);
            cambiosNoGuardados = false;
            reiniciar();

            LOGGER.info("Cambios guardados exitosamente.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
            showErrorDialog(Alert.AlertType.ERROR, "Error",
                    "No se pudieron guardar los cambios. Intente nuevamente.");
        }
    }

    /**
     * Actualiza el stock de un artículo en la base de datos.
     *
     * @param articulo El artículo cuyo stock se debe actualizar.
     * @param cantidad La cantidad a ajustar (positiva para aumentar, negativa
     * para reducir).
     */
    private void actualizarStock(Articulo articulo, int cantidad) throws LogicaNegocioException {
        if (articulo != null) {
            int nuevoStock = articulo.getStock() + cantidad;
            if (nuevoStock < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Stock insuficiente",
                        "El artículo " + articulo.getNombre() + " no tiene suficiente stock.");
                throw new IllegalStateException("Stock insuficiente para el artículo ID=" + articulo.getId());
            }
            articulo.setStock(nuevoStock);
            factoriaArticulos.acceso().actualizarArticulo(articulo);
        }
    }

    /**
     * Handler del botón "Atrás": Verifica si hay cambios no guardados y pide
     * confirmación. Ofrece tres opciones: Guardar, No guardar o Cancelar.
     *
     * @param event Evento de la acción.
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
    }

    /**
     * Vuelve a la pantalla principal de pedidos según el tipo de usuario.
     */
    private void irAPantallaPrincipal() {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, null);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, null);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos de apoyo y utilidades">
    /**
     * Reinicia la vista recargando los artículos y actualizando el total.
     */
    private void reiniciar() {
        configurarTablas();
        cargarArticulosDisponibles();
        cargarArticulosDelPedido();
        tablaArticulosDisponibles.refresh();
        tablaArticulosPedidos.refresh();
        actualizarTotal();
    }

    /**
     * Compara dos objetos de tipo {@link PedidoArticulo} para ver si han
     * cambiado en la cantidad (u otros campos si fuera necesario).
     *
     * @param original El objeto antes de los cambios.
     * @param modificado El objeto tras los cambios.
     * @return {@code true} si hay diferencias; {@code false} de lo contrario.
     */
    public boolean haCambiado(PedidoArticulo original, PedidoArticulo modificado) {
        if (original == null || modificado == null) {
            return false;
        }
        // Comparar campos relevantes
        return original.getCantidad() != modificado.getCantidad();
    }

    /**
     * Actualiza el campo "Total" con la suma de las cantidades por precio de
     * compra de todos los artículos en el pedido.
     */
    private void actualizarTotal() {
        double total = articulosDelPedido.stream()
                .mapToDouble(pa -> pa.getCantidad() * pa.getPrecioCompra())
                .sum();
        campoTotal.setText(String.format("%.2f €", total));
    }

    /**
     * Muestra una alerta genérica con el tipo, el título y el mensaje
     * indicados.
     *
     * @param tipo Tipo de alerta (ERROR, WARNING, INFO, etc.).
     * @param titulo Título de la ventana de alerta.
     * @param mensaje Mensaje descriptivo de la alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Ordena la tabla de artículos del pedido según el ID del artículo. Útil
     * tras modificar cantidades para mantener un orden coherente.
     */
    private void ordenarTablaPorArticuloId() {

        tablaArticulosPedidos.refresh();
    }

    /**
     * Busca el objeto {@link Articulo} en la lista de artículos disponibles por
     * su ID.
     *
     * @param articuloId ID del artículo a buscar.
     * @return El artículo correspondiente o {@code null} si no se encuentra.
     */
    private Articulo buscarArticuloPorId(Long articuloId) {
        if (articulosDisponibles == null) {
            return null;
        }
        return articulosDisponibles.stream()
                .filter(a -> a.getId().equals(articuloId))
                .findFirst()
                .orElse(null);
    }
    // </editor-fold>
}
