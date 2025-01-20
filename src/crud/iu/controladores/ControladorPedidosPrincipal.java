package crud.iu.controladores;

import crud.excepciones.LogicaNegocioException;
import crud.negocio.FactoriaPedidoArticulo;
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
import javafx.scene.control.Alert.AlertType;
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

import static crud.utilidades.AlertUtilities.showErrorDialog;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorPedidosPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosPrincipal.class.getName());
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
    private MenuItem opcionIrPedidos;
    @FXML
    private MenuItem opcionIrArticulos;
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
    private TableColumn<Pedido, String> columnaDireccion;
    @FXML
    private TableColumn<Pedido, Estado> columnaEstado;
    @FXML
    private TableColumn<Pedido, Date> columnaFecha;
    @FXML
    private TableColumn<Pedido, Double> columnaTotal;
    @FXML
    private Pagination paginador;
    private Cliente userCliente;
    private Trabajador userTrabajador;

    // Copia de seguridad de los datos originales
    private ObservableList<Pedido> pedidosOriginales;
    private Collection listaBusqueda;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Pedidos");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");

        botonNuevo.addEventHandler(ActionEvent.ACTION, this::handleNuevoPedido);
        botonReiniciar.addEventHandler(ActionEvent.ACTION, this::handleReiniciarTabla);
        botonDetalles.addEventHandler(ActionEvent.ACTION, this::handleDetalles);
        botonBusqueda.addEventHandler(ActionEvent.ACTION, this::handleBusqueda);
        botonEliminar.addEventHandler(ActionEvent.ACTION, this::handleEliminar);
        botonGuardar.addEventHandler(ActionEvent.ACTION, this::handleGuardarCambios);
        botonAtras.addEventHandler(ActionEvent.ACTION, this::handleAtras);

        // Configurar listeners para habilitar/deshabilitar botones
        tablaPedidos.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Pedido>) change -> {
            actualizarEstadoBotones();
        });

        stage.show();  // Mostrar el escenario
        cargarDatosPedidos();
        configurarPaginador();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador PedidosPrincipal");
        configurarTabla();

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

    private void actualizarTablaYPaginador() {
        tablaPedidos.setItems(pedidosObservableList);

        int numeroPaginas = (int) Math.ceil((double) pedidosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);

        // Mostrar la primera página
        actualizarPagina(0);
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
            cargarDatosPedidos();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    public void setBusqueda(Collection<Pedido> lista) {
        this.listaBusqueda = lista;
        if (lista != null) {

        }
        LOGGER.info("Lista Busqueda asignada. Elementos: " + ((lista != null) ? lista.size() : "Null"));
    }

    private void actualizarEstadoBotones() {
        int numSeleccionados = tablaPedidos.getSelectionModel().getSelectedItems().size();

        // Habilitar el botón detalles solo si hay exactamente una fila seleccionada
        botonDetalles.setDisable(numSeleccionados != 1);

        // Habilitar el botón eliminar si hay una o más filas seleccionadas
        botonEliminar.setDisable(numSeleccionados == 0);
    }

    /**
     * Configura las columnas de la tabla de pedidos.
     */
    private void configurarTabla() {
        tablaPedidos.setEditable(true);

        // Desactivar la ordenación en todas las columnas
        tablaPedidos.getColumns().forEach(column -> column.setSortable(false));
        tablaPedidos.getColumns().forEach(column -> column.setResizable(false));
        tablaPedidos.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        // Columna ID (No editable)
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna Usuario ID (Oculta)
        columnaUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        columnaUsuarioId.setVisible(false);

        // Columna Dirección (Editable con TextField)
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaDireccion.setCellFactory(tc -> new TableCell<Pedido, String>() {
            private final TextField textField = new TextField();

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem());
                setGraphic(textField);
                setText(null);
                textField.requestFocus();

                // Listener para perder foco
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textField.getText()); // Validar al perder el foco
                    }
                });

                // Listener para manejar la tecla Enter
                textField.setOnAction(event -> validarYCommit(textField.getText()));
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setDireccion(newValue); // Sincroniza el modelo
            }

            private void validarYCommit(String newValue) {
                if (newValue == null || newValue.trim().isEmpty()) {
                    showErrorDialog(AlertType.ERROR, "Campo obligatorio", "La dirección no puede estar vacía.");
                    cancelEdit(); // Cancela la edición y restaura el valor anterior
                } else {
                    commitEdit(newValue); // Valido, confirma la edición
                }
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

        // Columna CIF (Editable solo para trabajadores, con doble clic)
        columnaCif.setCellValueFactory(new PropertyValueFactory<>("cifCliente"));
        columnaCif.setCellFactory(tc -> new TableCell<Pedido, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            private String valorOriginal;

            @Override
            public void startEdit() {
                if (userTrabajador != null) { // Solo editable para trabajadores
                    super.startEdit();
                    Collection<Cliente> clientes = null;
                    try {
                        clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                    } catch (LogicaNegocioException ex) {
                        Logger.getLogger(ControladorPedidosPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ObservableList<String> cifs = obtenerCifsClientes(clientes);

                    comboBox.setItems(cifs);

                    valorOriginal = getItem();
                    comboBox.setValue(valorOriginal != null ? valorOriginal : (cifs.isEmpty() ? null : cifs.get(0)));

                    setGraphic(comboBox);
                    setText(null);
                    comboBox.requestFocus();

                    // Listener para manejar pérdida de foco
                    comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            if (comboBox.getValue() != null) {
                                commitEdit(comboBox.getValue());
                            } else {
                                cancelEdit();
                            }
                        }
                    });

                    // Listener para selección en ComboBox
                    comboBox.setOnAction(event -> {
                        if (comboBox.getValue() != null) {
                            commitEdit(comboBox.getValue());
                        }
                    });
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(valorOriginal);
                setGraphic(null);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setCifCliente(newValue);

                try {
                    // Buscar y actualizar el cliente correspondiente al nuevo CIF
                    Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                    Cliente clienteActualizado = clientes.stream()
                            .filter(c -> c.getCif().equals(newValue))
                            .findFirst()
                            .orElse(null);
                    pedido.setCliente(clienteActualizado);
                } catch (LogicaNegocioException ex) {
                    Logger.getLogger(ControladorPedidosPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

                setText(newValue);
                setGraphic(null);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setGraphic(null);
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
                        showErrorDialog(Alert.AlertType.ERROR, "Fecha inválida", "No se permiten fechas anteriores al día actual.");
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
                if (userTrabajador != null) { // Solo editable si el usuario es trabajador
                    super.startEdit();
                    if (getItem() != null) {
                        comboBox.setValue(getItem()); // Usamos directamente el valor del enum
                    }
                    setGraphic(comboBox);
                    setText(null);

                    // Listener para perder el foco
                    comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            cancelEdit(); // Salir del modo edición si se pierde el foco
                        }
                    });

                    // Registrar cambios al seleccionar un nuevo valor en el ComboBox
                    comboBox.setOnAction(event -> commitEdit(comboBox.getValue()));
                }
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
                column.setPrefWidth(totalWidth * 0.05d); // 7% del ancho total
            } else if (column.equals(columnaCif)) {
                column.setPrefWidth(totalWidth * 0.17d); // 13% del ancho total
            } else if (column.equals(columnaDireccion)) {
                column.setPrefWidth(totalWidth * 0.23d); // 28% del ancho total
            } else if (column.equals(columnaEstado)) {
                column.setPrefWidth(totalWidth * 0.2d); // 20% del ancho total
            } else if (column.equals(columnaFecha)) {
                column.setPrefWidth(totalWidth * 0.2d); // 20% del ancho total
            } else if (column.equals(columnaTotal)) {
                column.setPrefWidth((totalWidth * 0.15d) - 20); // 15% del ancho total
            }
        });
    }

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
            Collection<Pedido> pedidos;
            if (listaBusqueda != null) {
                pedidos = listaBusqueda;
            } else {
                pedidos = factoriaPedidos.acceso().getAllPedidos();
            }

            if (pedidos == null || pedidos.isEmpty()) {
                pedidos = new ArrayList<>();
            }

            // Filtrar pedidos si el usuario es cliente
            if (userCliente != null) {
                pedidosObservableList = FXCollections.observableArrayList(
                        pedidos.stream()
                                .filter(p -> p.getCifCliente().equals(userCliente.getCif()))
                                .collect(Collectors.toList())
                );
                pedidosOriginales = FXCollections.observableArrayList(
                        pedidosObservableList.stream().map(Pedido::clone).collect(Collectors.toList())
                );
            } else {
                // Si es trabajador, mostrar todos los pedidos
                pedidosObservableList = FXCollections.observableArrayList(pedidos);
                // Crear copia de seguridad de los datos originales
                pedidosOriginales = FXCollections.observableArrayList(
                        pedidos.stream().map(Pedido::clone).collect(Collectors.toList())
                );
            }

            actualizarTablaYPaginador(); // Actualiza la tabla y el paginador
            actualizarEstadoBotones(); // Asegura el estado inicial de los botones
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los datos de pedidos", e);
            AlertUtilities.showErrorDialog(AlertType.ERROR, "Error al cargar pedidos", "No se pudieron cargar los pedidos. Intente nuevamente más tarde.");
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
            actualizarTablaYPaginador();
            LOGGER.info("Pedidos eliminados de la tabla.");
        }
    }

    @FXML
    private void handleReiniciarTabla(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Reiniciar Tabla presionado");
        listaBusqueda = null;
        reiniciar();
        LOGGER.info("Tabla reiniciada a los datos originales.");

    }

    @FXML
    private void handleBusqueda(ActionEvent event) {

        LOGGER.info("Botón Busqueda presionado");
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosBusqueda(stage, userCliente);
        } else {
            factoriaPedidos.cargarPedidosBusqueda(stage, userTrabajador);
        }
    }

    private void reiniciar() {

        cargarDatosPedidos();

    }

    @FXML
    private void handleDetalles(ActionEvent event) {
        cancelarEdicionEnTabla(); // Asegura que no hay edición activa
        LOGGER.info("Botón Detalles presionado");

        // Obtener la fila seleccionada
        Pedido pedidoSeleccionado = tablaPedidos.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado == null) {
            LOGGER.warning("No se ha seleccionado ningún pedido.");
            showErrorDialog(Alert.AlertType.WARNING, "Detalles de Pedido", "Debe seleccionar un pedido para ver los detalles.");
            return;
        }

        try {
            // Llamar a la factoría de PedidoArticulo y cargar el detalle
            if (userCliente != null) {
                FactoriaPedidoArticulo.getInstance().cargarPedidosDetalle(stage, userCliente, pedidoSeleccionado);
            } else {
                FactoriaPedidoArticulo.getInstance().cargarPedidosDetalle(stage, userTrabajador, pedidoSeleccionado);
            }

            LOGGER.info("Cargando detalles del pedido: " + pedidoSeleccionado.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar detalles del pedido", e);
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los detalles del pedido. Intente nuevamente.");
        }
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
        nuevoPedido.setEstado(Estado.PREPARACION);
        nuevoPedido.setFechaPedido(new Date());
        nuevoPedido.setDireccion("Nueva Dirección"); // Dirección predeterminada

        if (userTrabajador != null) {
            try {
                // Si el usuario es un trabajador, asignar el primer CIF disponible
                Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                ObservableList<String> cifsClientes = obtenerCifsClientes(clientes);
                if (!cifsClientes.isEmpty()) {
                    String cifAsignado = cifsClientes.get(0); // Asigna el primer CIF
                    nuevoPedido.setCifCliente(cifAsignado);
                    // Buscar el cliente correspondiente al CIF y asignarlo al pedido
                    Cliente clienteAsignado = clientes.stream()
                            .filter(c -> c.getCif().equals(cifAsignado))
                            .findFirst()
                            .orElse(null);
                    nuevoPedido.setCliente(clienteAsignado);
                } else {
                    nuevoPedido.setCifCliente(""); // Vacío si no hay CIF disponibles
                    LOGGER.warning("No se encontraron clientes para asignar un CIF.");
                }
            } catch (LogicaNegocioException ex) {
                Logger.getLogger(ControladorPedidosPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (userCliente != null) {
            // Si el usuario es un cliente, usar su CIF
            nuevoPedido.setCifCliente(userCliente.getCif());
            nuevoPedido.setCliente(userCliente);
        } else {
            nuevoPedido.setCifCliente(""); // Si no hay cliente ni trabajador, CIF vacío
        }

        pedidosObservableList.add(nuevoPedido);

        // Calcular la página donde está el nuevo pedido
        int indicePagina = (pedidosObservableList.size() - 1) / FILAS_POR_PAGINA;
        paginador.setCurrentPageIndex(indicePagina); // Cambiar a esa página

        actualizarPagina(indicePagina); // Refrescar la tabla
        tablaPedidos.scrollTo(nuevoPedido); // Desplazar a la nueva fila
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
        if (userCliente != null) {
            factoriaUsuarios.cargarMenuPrincipal(stage, userCliente);
        } else {
            factoriaUsuarios.cargarMenuPrincipal(stage, userTrabajador);
        }

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

    private ObservableList<String> obtenerCifsClientes(Collection<Cliente> clientes) {
        try {
            return FXCollections.observableArrayList(
                    clientes.stream().map(Cliente::getCif).collect(Collectors.toList())
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los CIF de los clientes", e);
            showErrorDialog(AlertType.ERROR, "Error", "No se pudieron cargar los CIF de los clientes.");
            return FXCollections.observableArrayList();
        }
    }

}
