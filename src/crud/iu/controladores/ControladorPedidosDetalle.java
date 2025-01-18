package crud.iu.controladores;

import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.negocio.IPedido;
import crud.negocio.PedidoImpl;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorPedidosDetalle implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosDetalle.class.getName());
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private Stage stage;
    private Usuario usuario;
    private ObservableList<Pedido> pedidosObservableList;
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
    private Cliente userCliente;
    private Trabajador userTrabajador;

    // Copia de seguridad de los datos originales
    private ObservableList<Pedido> pedidosOriginales;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Pedidos");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");

        botonNuevo.addEventHandler(ActionEvent.ACTION, this::handleNuevoPedido);
        botonReiniciar.addEventHandler(ActionEvent.ACTION, this::handleReiniciarTabla);
        //botonBusqueda
        botonEliminar.addEventHandler(ActionEvent.ACTION, this::handleEliminar);
        botonGuardar.addEventHandler(ActionEvent.ACTION, this::handleGuardarCambios);
        botonAtras.addEventHandler(ActionEvent.ACTION, this::handleAtras);

        stage.show();  // Mostrar el escenario

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador PedidosPrincipal");
        configurarTabla();
        cargarDatosPedidos();
        configurarPaginador();
    }

    private void configurarPaginador() {
        int numeroPaginas = (int) Math.ceil((double) pedidosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);

        // Listener para cambiar de página
        paginador.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            actualizarPagina(newIndex.intValue());
        });

        // Mostrar la primera página por defecto
        actualizarPagina(0);
    }

    private void actualizarPagina(int indicePagina) {
        int desdeIndice = indicePagina * FILAS_POR_PAGINA;
        int hastaIndice = Math.min(desdeIndice + FILAS_POR_PAGINA, pedidosObservableList.size());

        if (desdeIndice <= hastaIndice) {
            ObservableList<Pedido> pagina = FXCollections.observableArrayList(pedidosObservableList.subList(desdeIndice, hastaIndice));
            tablaPedidos.setItems(pagina);
        }
    }

    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = new Cliente();
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado: " + userCliente.getNombre() + userCliente.getId());

            } else {
                this.userTrabajador = new Trabajador();
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado: " + userTrabajador.getNombre());
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Configura las columnas de la tabla de pedidos.
     */
    private void configurarTabla() {
        tablaPedidos.setEditable(true);

        // Desactivar la ordenación en todas las columnas
        tablaPedidos.getColumns().forEach(column -> column.setSortable(false));
        tablaPedidos.getColumns().forEach(column -> column.setResizable(false));

        // Columna ID (No editable)
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna Usuario ID (Oculta)
        columnaUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        columnaUsuarioId.setVisible(false);

        // Columna CIF (Editable solo para trabajadores, con doble clic)
        columnaCif.setCellValueFactory(new PropertyValueFactory<>("cifCliente"));
        columnaCif.setCellFactory(tc -> new TableCell<Pedido, String>() {
            private final TextField textField = new TextField();

            @Override
            public void startEdit() {
                super.startEdit();
                if (userTrabajador != null) {
                    textField.setText(getItem());
                    setGraphic(textField);
                    setText(null);
                    textField.requestFocus();
                    // Listener para perder foco
                    textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            commitEdit(textField.getText()); // Commit cuando se pierde el foco
                        }
                    });
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        textField.setText(item);
                        setGraphic(textField);
                        setText(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }
        });

        // Columna Fecha (Editable con DatePicker al hacer doble clic y formato dd/MM/yyyy)
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        columnaFecha.setCellFactory(tc -> new TableCell<Pedido, Date>() {
            private final DatePicker datePicker = new DatePicker();
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public void startEdit() {
                super.startEdit();
                Date item = getItem();
                if (item != null) {
                    datePicker.setValue(item.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                } else {
                    datePicker.setValue(null);
                }
                setGraphic(datePicker);
                setText(null);

                // Listener para perder el foco
                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        cancelEdit(); // Salir del modo edición si se pierde el foco
                    }
                });

                datePicker.setOnAction(event -> {
                    Date newDate = Date.from(datePicker.getValue()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant());

                    // Verificar si la fecha seleccionada es anterior a hoy
                    if (newDate.before(new Date()) && !newDate.equals(getItem())) {
                        // Mostrar error si la fecha es anterior y no coincide con la original
                        AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Fecha inválida", "No se permiten fechas anteriores al día actual.");
                        cancelEdit(); // Cancela la edición
                    } else {
                        commitEdit(newDate); // Fecha válida
                    }
                });

            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? dateFormat.format(getItem()) : null);
                setGraphic(null);
            }

            @Override
            public void commitEdit(Date newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setFechaPedido(newValue); // Sincroniza el modelo
            }

            @Override
            public void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        setGraphic(datePicker);
                        setText(null);
                    } else {
                        setText(dateFormat.format(item));
                        setGraphic(null);
                    }
                }
            }
        });

        //Combo ESTADO
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setCellFactory(tc -> new TableCell<Pedido, Estado>() {
            private final ComboBox<Estado> comboBox = new ComboBox<>(FXCollections.observableArrayList(Estado.values()));

            @Override
            public void startEdit() {
                super.startEdit();
                if (getItem() != null) {
                    comboBox.setValue(getItem()); // Usamos directamente el valor del enum
                }
                setGraphic(comboBox);
                setText(null);

                // Registrar cambios al seleccionar un nuevo valor en el ComboBox
                comboBox.setOnAction(event -> commitEdit(comboBox.getValue()));
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? getItem().name() : null); // Mostrar el nombre del enum
                setGraphic(null);
            }

            @Override
            public void commitEdit(Estado newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setEstado(newValue); // Actualizar el valor del enum en el modelo
            }

            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        comboBox.setValue(item); // Establecer el valor actual en el ComboBox
                        setGraphic(comboBox);
                        setText(null);
                    } else {
                        setText(item.name()); // Mostrar el nombre del enum en la celda
                        setGraphic(null);
                    }
                }
            }
        });

        // Columna Total (No editable, con símbolo €)
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        columnaTotal.setCellFactory(tc -> new TableCell<Pedido, Double>() {
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
        ajustarAnchoProporcional(tablaPedidos);
    }

    private void ajustarAnchoProporcional(TableView<?> tableView) {
        double totalWidth = tableView.getPrefWidth(); // Obtiene el ancho total de la tabla
        tableView.getColumns().forEach(column -> {
            // Define las proporciones para cada columna
            if (column.equals(columnaId)) {
                column.setPrefWidth(totalWidth * 0.1d); // 10% del ancho total
            } else if (column.equals(columnaCif)) {
                column.setPrefWidth(totalWidth * 0.2d); // 20% del ancho total
            } else if (column.equals(columnaEstado)) {
                column.setPrefWidth(totalWidth * 0.2d); // 20% del ancho total
            } else if (column.equals(columnaFecha)) {
                column.setPrefWidth(totalWidth * 0.2d); // 20% del ancho total
            } else if (column.equals(columnaTotal)) {
                column.setPrefWidth((totalWidth * 0.3d) - 20); // 30% del ancho total
            }
        });
    }

    /**
     * Carga los datos de pedidos en la tabla.
     *
     * private void cargarDatosPedidos() { try { LOGGER.info("Cargando datos de
     * pedidos..."); Collection<Pedido> pedidos =
     * factoriaPedidos.acceso().getAllPedidos(); if (pedidos == null ||
     * pedidos.isEmpty()) { LOGGER.warning("No se encontraron pedidos.");
     * pedidos = new ArrayList<>(); } pedidosObservableList =
     * FXCollections.observableArrayList(pedidos);
     * tablaPedidos.setItems(pedidosObservableList); } catch (Exception e) {
     * LOGGER.log(Level.SEVERE, "Error al cargar los datos de pedidos", e);
     * AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error al cargar
     * pedidos", "No se pudieron cargar los pedidos. Intente nuevamente más
     * tarde."); } }
     */
    /**
     * Establece el usuario activo.
     *
     * @param usuario Usuario activo.
     */
    /**
     * Inicializa la ventana principal.
     *
     * @param root Nodo raíz de la escena.
     */
    private void cargarDatosPedidos() {
        try {
            LOGGER.info("Cargando datos de pedidos...");
            Collection<Pedido> pedidos = factoriaPedidos.acceso().getAllPedidos();
            if (pedidos == null || pedidos.isEmpty()) {
                pedidos = new ArrayList<>();
            }

            // Crear copias independientes de los pedidos
            pedidosObservableList = FXCollections.observableArrayList(pedidos);
            pedidosOriginales = FXCollections.observableArrayList(
                    pedidos.stream()
                            .map(Pedido::clone)
                            .collect(Collectors.toList())
            );

            tablaPedidos.setItems(pedidosObservableList);
            configurarSalidaEdicion(); // Configurar el evento para salir del modo edición
            configurarPaginador();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los datos de pedidos", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error al cargar pedidos", "No se pudieron cargar los pedidos. Intente nuevamente más tarde.");
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Eliminar presionado");
        ObservableList<Pedido> seleccionados = tablaPedidos.getSelectionModel().getSelectedItems();
        if (seleccionados.isEmpty()) {
            AlertUtilities.showErrorDialog(Alert.AlertType.WARNING, "Eliminar Pedidos", "Debe seleccionar al menos un pedido para eliminar.");
        } else {
            pedidosObservableList.removeAll(seleccionados);
            LOGGER.info("Pedidos eliminados de la tabla.");
        }
    }

    @FXML
    private void handleReiniciarTabla(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Reiniciar Tabla presionado");
        reiniciar();
        LOGGER.info("Tabla reiniciada a los datos originales.");

    }

    private void reiniciar() {

        cargarDatosPedidos();

    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Guardar Cambios presionado");

        boolean hayErrores = false;
        for (Pedido pedido : pedidosObservableList) {
            if (!validarPedido(pedido)) {
                hayErrores = true;
            }
        }

        if (hayErrores) {
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Guardar Cambios", "Hay errores en algunos campos. Corríjalos antes de guardar.");
            return;
        }

        try {
            for (Pedido pedido : pedidosObservableList) {
                LOGGER.info("Revisando pedidos." + pedido.getId());
                Pedido pedidoOriginal = pedidosOriginales.stream()
                        .filter(p -> p.getId().equals(pedido.getId()))
                        .findFirst()
                        .orElse(null);

                if (pedido.getId() == null) {
                    LOGGER.info("Creando pedido.");
                    factoriaPedidos.acceso().crearPedido(pedido); // Crear nuevo
                } else if (pedidoOriginal != null && haCambiado(pedidoOriginal, pedido)) {
                    LOGGER.info("Actualizando pedido: " + pedido);
                    factoriaPedidos.acceso().actualizarPedido(pedido); // Actualizar existente
                }
            }

            for (Pedido pedidoOriginal : pedidosOriginales) {
                if (!pedidosObservableList.contains(pedidoOriginal)) {
                    LOGGER.info("Eliminando pedido.");
                    factoriaPedidos.acceso().borrarPedido(pedidoOriginal); // Eliminar
                }
            }

            pedidosOriginales.setAll(pedidosObservableList);
            LOGGER.info("Cambios guardados exitosamente.");
            reiniciar();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error", "No se pudieron guardar los cambios. Intente nuevamente.");
        }
    }

    public boolean haCambiado(Pedido original, Pedido modificado) {
        if (original == null || modificado == null) {
            return false;
        }
        return !original.getEstado().equals(modificado.getEstado())
                || !original.getFechaPedido().equals(modificado.getFechaPedido())
                || !original.getCifCliente().equals(modificado.getCifCliente())
                || Double.compare(original.getTotal(), modificado.getTotal()) != 0;
    }

    @FXML
    private void handleNuevoPedido(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Nuevo Pedido presionado");

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCifCliente(userCliente.getCif());
        nuevoPedido.setEstado(Estado.PREPARACION);
        nuevoPedido.setFechaPedido(new Date());

        if (userCliente != null) {
            nuevoPedido.setCliente(userCliente);
        } else if (userTrabajador != null) {
            // Buscar ID del cliente asociado al CIF (simulación)
            //nuevoPedido.setUsuarioId(factoriaUsuarios.accesoCliente().getIdPorCIF(nuevoPedido.getCifCliente()));
        }

        pedidosObservableList.add(nuevoPedido);
        tablaPedidos.scrollTo(nuevoPedido);
        LOGGER.info("Nuevo pedido añadido con valores predeterminados.");

    }

    private boolean validarPedido(Pedido pedido) {
        boolean valido = true;

        if (pedido.getCifCliente() == null || pedido.getCifCliente().isEmpty()) {
            pintarCeldaInvalida(columnaCif, pedido);
            valido = false;
        }
        if (pedido.getFechaPedido() == null) {
            pintarCeldaInvalida(columnaFecha, pedido);
            valido = false;
        }
        if (pedido.getEstado() == null) {
            pintarCeldaInvalida(columnaEstado, pedido);
            valido = false;
        }

        return valido;
    }

    private <T> void pintarCeldaInvalida(TableColumn<Pedido, T> columna, Pedido pedido) {
        columna.setCellFactory(column -> {
            return new TableCell<Pedido, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && pedido.equals(getTableView().getItems().get(getIndex()))) {
                        setStyle("-fx-background-color: red;");
                    } else {
                        setStyle("");
                    }
                }
            };
        });
    }

    @FXML
    private void handleAtras(ActionEvent event) {
        factoriaUsuarios.cargarMenuPrincipal(stage, userCliente);
    }

    private void configurarSalidaEdicion() {
        // Salir del modo edición al hacer clic en el AnchorPane
        anchorPane.setOnMouseClicked(event -> cancelarEdicionEnTabla());

        // Listener para clics en la tabla
        tablaPedidos.setOnMouseClicked(event -> cancelarEdicionEnTabla());

        // Listener para teclas en la tabla
        tablaPedidos.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    cancelarEdicionEnTabla();
                    break;
                default:
                    break;
            }
        });
    }

    private void cancelarEdicionEnTabla() {
        if (tablaPedidos.getEditingCell() != null) {
            tablaPedidos.edit(-1, null); // Salir del modo edición
        }
    }

}
