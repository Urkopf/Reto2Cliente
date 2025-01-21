package crud.iu.controladores;

import crud.excepciones.LogicaNegocioException;
import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.PedidoArticulo;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;

import static crud.utilidades.AlertUtilities.showErrorDialog;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * <h1>ControladorPedidosPrincipal</h1>
 * Controlador para la ventana principal de Pedidos. Se encarga de la
 * visualización, creación, edición, eliminación y búsqueda de pedidos.
 *
 * @author
 * <a href="mailto:urkoperitz@example.com">Urko Peritz</a>
 */
public class ControladorPedidosPrincipal implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="Constantes y campos estáticos">
    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosPrincipal.class.getName());
    private static final int FILAS_POR_PAGINA = 14;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Campos de negocio y lógica">
    private final FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private final FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();

    private Stage stage;
    private Usuario usuario;
    private Cliente userCliente;
    private Trabajador userTrabajador;

    private ObservableList<Pedido> pedidosObservableList; // Lista de pedidos en la tabla
    private ObservableList<Pedido> pedidosOriginales;     // Copia de seguridad de la lista original
    private Collection<Pedido> listaBusqueda;

    /**
     * Nueva variable para llevar control de si hay cambios (nuevo, edición o
     * borrado) que aún no se hayan guardado.
     */
    private boolean hayCambiosNoGuardados = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Elementos FXML">
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
    // </editor-fold>

    /**
     * Método de inicialización de JavaFX que se ejecuta automáticamente al
     * cargar la vista FXML.
     *
     * @param url URL de referencia
     * @param rb ResourceBundle si se usan recursos de internacionalización
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador PedidosPrincipal");
        configurarTabla();
    }

    /**
     * Inicializa la vista con el {@code Stage} y configura los handlers de
     * eventos. Se encarga de cargar datos y configurar la paginación.
     *
     * @param root Nodo raíz de la escena.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Pedidos");
        LOGGER.info("Inicializando la escena principal");

        configurarHandlers();
        configurarListeners();
        stage.show();

        // Cargar datos y configurar paginador
        cargarDatosPedidos();
        configurarPaginador();
    }

    /**
     * Asigna el objeto {@code Stage} para este controlador.
     *
     * @param stage El {@code Stage} principal de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Asigna el usuario que ha iniciado sesión (puede ser {@code Cliente} o
     * {@code Trabajador}). Tras asignar el usuario, carga los datos de pedidos
     * correspondientes.
     *
     * @param user Usuario activo, instancia de {@code Cliente} o
     * {@code Trabajador}.
     */
    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado (Cliente): " + userCliente.getNombre() + " " + userCliente.getId());
            } else {
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado (Trabajador): " + userTrabajador.getNombre());
            }
            cargarDatosPedidos();
        }
    }

    /**
     * Recibe la lista de pedidos resultado de una búsqueda y la asigna a
     * {@code listaBusqueda}.
     *
     * @param lista Lista con los pedidos filtrados.
     */
    public void setBusqueda(Collection<Pedido> lista) {
        this.listaBusqueda = lista;
        LOGGER.info("Lista Busqueda asignada. Elementos: "
                + ((lista != null) ? lista.size() : "Null"));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Handlers de los botones con verificación de cambios sin guardar">
    /**
     * Handler para el botón "Nuevo". Marca que hay cambios y crea un pedido
     * nuevo (con valores predeterminados).
     */
    @FXML
    private void handleNuevoPedido(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Nuevo Pedido presionado");

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setEstado(Estado.PREPARACION);
        nuevoPedido.setFechaPedido(new Date());
        nuevoPedido.setDireccion("Nueva Dirección"); // Valor predeterminado

        if (userTrabajador != null) {
            // Asignar primer CIF disponible (en caso de no ser un cliente)
            try {
                Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                ObservableList<String> cifsClientes = obtenerCifsClientes(clientes);
                if (!cifsClientes.isEmpty()) {
                    String cifAsignado = cifsClientes.get(0);
                    nuevoPedido.setCifCliente(cifAsignado);
                    Cliente clienteAsignado = clientes.stream()
                            .filter(c -> c.getCif().equals(cifAsignado))
                            .findFirst()
                            .orElse(null);
                    nuevoPedido.setCliente(clienteAsignado);
                } else {
                    nuevoPedido.setCifCliente("");
                    LOGGER.warning("No se encontraron clientes para asignar un CIF.");
                }
            } catch (LogicaNegocioException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        } else if (userCliente != null) {
            // Si el usuario es cliente, usar su propio CIF
            nuevoPedido.setCifCliente(userCliente.getCif());
            nuevoPedido.setCliente(userCliente);
        } else {
            nuevoPedido.setCifCliente("");
        }

        pedidosObservableList.add(nuevoPedido);
        setHayCambiosNoGuardados(true); // <<--- Marcamos que hay cambios

        int indicePagina = (pedidosObservableList.size() - 1) / FILAS_POR_PAGINA;
        paginador.setCurrentPageIndex(indicePagina);
        actualizarPagina(indicePagina);
        tablaPedidos.scrollTo(nuevoPedido);

        LOGGER.info("Nuevo pedido añadido con valores predeterminados.");
    }

    /**
     * Handler para el botón "Reiniciar". Antes de reiniciar, comprueba si hay
     * cambios sin guardar.
     */
    @FXML
    private void handleReiniciarTabla(ActionEvent event) {
        confirmarCambiosSinGuardar(this::handleReiniciarTablaInterno);
    }

    /**
     * Lógica interna original del botón "Reiniciar" (sin la comprobación de
     * cambios).
     */
    private void handleReiniciarTablaInterno() {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Reiniciar Tabla presionado");
        listaBusqueda = null;
        reiniciarTabla();
        // No olvides: tras reiniciar, ya no hay cambios sin guardar
        setHayCambiosNoGuardados(false);
    }

    /**
     * Handler para el botón "Búsqueda". Antes de realizar la búsqueda,
     * comprueba si hay cambios sin guardar.
     */
    @FXML
    private void handleBusqueda(ActionEvent event) {
        confirmarCambiosSinGuardar(this::handleBusquedaInterno);
    }

    /**
     * Lógica interna original del botón "Búsqueda" (sin la comprobación de
     * cambios).
     */
    private void handleBusquedaInterno() {
        LOGGER.info("Botón Búsqueda presionado");
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosBusqueda(stage, userCliente);
        } else {
            factoriaPedidos.cargarPedidosBusqueda(stage, userTrabajador);
        }
    }

    /**
     * Handler para el botón "Eliminar". Marca que hay cambios y elimina los
     * pedidos seleccionados.
     */
    @FXML
    private void handleEliminar(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Eliminar presionado");

        ObservableList<Pedido> seleccionados = tablaPedidos.getSelectionModel().getSelectedItems();
        if (seleccionados.isEmpty()) {
            AlertUtilities.showErrorDialog(Alert.AlertType.WARNING, "Eliminar Pedidos",
                    "Debe seleccionar al menos un pedido para eliminar.");
        } else {
            pedidosObservableList.removeAll(seleccionados);
            setHayCambiosNoGuardados(true); // <<--- Marcamos que hay cambios
            actualizarTablaYPaginador();
            LOGGER.info("Pedidos eliminados de la tabla.");
        }
    }

    /**
     * Handler para el botón "Guardar". Guarda los cambios realizados en la
     * tabla (creación, modificación o eliminación de pedidos).
     */
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        guardarCambiosInterno();
    }

    /**
     * Método interno que guarda cambios y pone la bandera a false si todo va
     * bien.
     */
    private void guardarCambiosInterno() {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Guardar Cambios presionado");

        if (!validarCamposPedidos()) {
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Guardar Cambios",
                    "Hay errores en algunos campos. Corríjalos antes de guardar.");
            return;
        }

        try {
            // Crear o actualizar pedidos
            for (Pedido pedido : pedidosObservableList) {
                Pedido pedidoOriginal = pedidosOriginales.stream()
                        .filter(p -> p.getId() != null && p.getId().equals(pedido.getId()))
                        .findFirst()
                        .orElse(null);

                if (pedido.getId() == null) {
                    // Crear nuevo
                    LOGGER.info("Creando pedido.");
                    factoriaPedidos.acceso().crearPedido(pedido);
                } else if (pedidoOriginal != null && haCambiado(pedidoOriginal, pedido)) {
                    // Actualizar existente
                    LOGGER.info("Actualizando pedido: " + pedido.getId());
                    factoriaPedidos.acceso().actualizarPedido(pedido);
                }
            }
            // Eliminar pedidos
            for (Pedido pedidoOriginal : pedidosOriginales) {
                if (!pedidosObservableList.contains(pedidoOriginal) && pedidoOriginal.getId() != null) {
                    LOGGER.info("Eliminando pedido: " + pedidoOriginal.getId());
                    ObservableList<PedidoArticulo> lista = (ObservableList<PedidoArticulo>) FactoriaPedidoArticulo.getInstance().acceso().getAllPedidoArticulo();
                    lista = FXCollections.observableArrayList(
                            lista.stream()
                                    .filter(p -> p.getPedidoId().equals(pedidoOriginal.getId()))
                                    .collect(Collectors.toList()));
                    if (lista.size() == 0) {
                        factoriaPedidos.acceso().borrarPedido(pedidoOriginal);
                    } else {
                        //preguntar
                        for (PedidoArticulo pa : lista) {
                            FactoriaPedidoArticulo.getInstance().acceso().borrarPedidoArticulo(pa);
                        }
                        factoriaPedidos.acceso().borrarPedido(pedidoOriginal);

                    }

                }
            }
            pedidosOriginales.setAll(pedidosObservableList);

            LOGGER.info("Cambios guardados exitosamente.");
            reiniciarTabla();
            setHayCambiosNoGuardados(false); // <<--- Ya no hay cambios sin guardar
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
            showErrorDialog(Alert.AlertType.ERROR, "Error",
                    "No se pudieron guardar los cambios. Intente nuevamente.");
        }
    }

    /**
     * Handler para el botón "Detalles". Antes de mostrar detalles (y por tanto
     * cambiar de ventana), comprueba si hay cambios sin guardar.
     */
    @FXML
    private void handleDetalles(ActionEvent event
    ) {
        confirmarCambiosSinGuardar(this::handleDetallesInterno);
    }

    /**
     * Lógica interna original del botón "Detalles" (sin la comprobación de
     * cambios).
     */
    private void handleDetallesInterno() {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Detalles presionado");

        Pedido pedidoSeleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado == null) {
            LOGGER.warning("No se ha seleccionado ningún pedido.");
            showErrorDialog(Alert.AlertType.WARNING, "Detalles de Pedido",
                    "Debe seleccionar un pedido para ver los detalles.");
            return;
        }

        try {
            if (userCliente != null) {
                FactoriaPedidoArticulo.getInstance()
                        .cargarPedidosDetalle(stage, userCliente, pedidoSeleccionado);
            } else {
                FactoriaPedidoArticulo.getInstance()
                        .cargarPedidosDetalle(stage, userTrabajador, pedidoSeleccionado);
            }
            LOGGER.info("Cargando detalles del pedido: " + pedidoSeleccionado.getId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar detalles del pedido", e);
            showErrorDialog(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los detalles del pedido. Intente nuevamente.");
        }
    }

    /**
     * Handler para el botón "Atrás". Antes de volver al menú principal,
     * comprueba si hay cambios sin guardar.
     */
    @FXML
    private void handleAtras(ActionEvent event) {
        confirmarCambiosSinGuardar(this::handleAtrasInterno);
    }

    /**
     * Lógica interna original del botón "Atrás" (sin la comprobación de
     * cambios).
     */
    private void handleAtrasInterno() {
        if (userCliente != null) {
            factoriaUsuarios.cargarMenuPrincipal(stage, userCliente);
        } else {
            factoriaUsuarios.cargarMenuPrincipal(stage, userTrabajador);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Métodos de ayuda para comprobar si hay cambios sin guardar">
    /**
     * Llama a este método antes de ejecutar cualquier acción que pueda
     * descartar cambios. Si hay cambios sin guardar, se muestra un Alert con
     * tres opciones:
     * <ul>
     * <li>Guardar y continuar</li>
     * <li>Continuar sin guardar</li>
     * <li>Cancelar</li>
     * </ul>
     *
     * @param accionAEjecutar Acción que se desea ejecutar tras la comprobación.
     */
    private void confirmarCambiosSinGuardar(Runnable accionAEjecutar) {
        if (!hayCambiosNoGuardados) {
            // Si no hay cambios sin guardar, ejecutamos directamente la acción
            accionAEjecutar.run();
            return;
        }

        // Si hay cambios, mostramos un diálogo de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambios sin guardar");
        alert.setHeaderText(null);
        alert.setContentText("Hay cambios sin guardar. ¿Qué desea hacer?");

        ButtonType buttonGuardar = new ButtonType("Guardar");
        ButtonType buttonSinGuardar = new ButtonType("No guardar");
        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonGuardar, buttonSinGuardar, buttonCancelar);

        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent() || result.get() == buttonCancelar) {
            // Opción "Cancelar": no hacemos nad
            return;
        } else if (result.get() == buttonGuardar) {
            // Primero guardamos cambios
            guardarCambiosInterno();
            // Luego ejecutamos la acción (si el guardado no falló)
            if (!hayCambiosNoGuardados) {
                // Importante: solo continuar si realmente se guardó todo bien.
                accionAEjecutar.run();
            }
        } else if (result.get() == buttonSinGuardar) {
            // Descartamos cambios y ejecutamos la acción
            // Para “descartar” en este ejemplo basta con recargar la tabla
            // o seguir la acción. Aquí ejecutamos sin recargar.
            accionAEjecutar.run();
        }
    }

    /**
     * Asigna el valor de la bandera que indica si hay cambios sin guardar.
     *
     * @param hayCambios true si hay cambios no guardados, false si no.
     */
    private void setHayCambiosNoGuardados(boolean hayCambios) {
        this.hayCambiosNoGuardados = hayCambios;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Métodos de configuración inicial">
    /**
     * Configura los handlers de los botones y otros componentes.
     */
    private void configurarHandlers() {
        botonNuevo.setOnAction(this::handleNuevoPedido);
        botonReiniciar.setOnAction(this::handleReiniciarTabla);
        botonDetalles.setOnAction(this::handleDetalles);
        botonBusqueda.setOnAction(this::handleBusqueda);
        botonEliminar.setOnAction(this::handleEliminar);
        botonGuardar.setOnAction(this::handleGuardarCambios);
        botonAtras.setOnAction(this::handleAtras);
    }

    /**
     * Configura los listeners necesarios para la tabla y otros componentes.
     * Incluye la salida del modo edición al hacer clic fuera de la celda
     * editada.
     */
    private void configurarListeners() {
        // Listener para la selección de filas de la tabla
        tablaPedidos.getSelectionModel().getSelectedItems()
                .addListener((ListChangeListener<Pedido>) change -> actualizarEstadoBotones());

        // Salir del modo edición al hacer clic fuera de la celda en edición
        anchorPane.setOnMouseClicked(event -> cancelarEdicionEnTabla());
        tablaPedidos.setOnMouseClicked(event -> cancelarEdicionEnTabla());
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

    /**
     * Configura las columnas de la tabla para su edición y visualización.
     */
    private void configurarTabla() {
        tablaPedidos.setEditable(true);
        tablaPedidos.getColumns().forEach(column -> {
            column.setSortable(false); // Desactiva el sort en cada columna
            column.setResizable(false);
        });

        // Múltiples filas seleccionables
        tablaPedidos.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Columna ID
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Columna Usuario ID (oculta)
        columnaUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
        columnaUsuarioId.setVisible(false);

        // Columna Dirección (editable)
        configurarColumnaDireccion();

        // Columna CIF (editable solo para Trabajador)
        configurarColumnaCif();

        // Columna Fecha (editable con DatePicker)
        configurarColumnaFecha();

        // Columna Estado (editable con ComboBox)
        configurarColumnaEstado();

        // Columna Total (no editable)
        configurarColumnaTotal();

        // Ajustar ancho de columnas de forma proporcional
        ajustarAnchoProporcional(tablaPedidos);
    }

    /**
     * Configura el paginador y establece la página inicial.
     */
    private void configurarPaginador() {
        int numeroPaginas = (int) Math.ceil((double) pedidosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);

        // Cambiar de página al mover el paginador
        paginador.currentPageIndexProperty().addListener((obs, oldIndex, newIndex)
                -> actualizarPagina(newIndex.intValue()));

        actualizarPagina(0);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Carga y reinicio de datos">
    /**
     * Carga los datos de pedidos desde la base de datos o de la lista de
     * búsqueda. Aplica filtro si el usuario es un {@code Cliente}.
     */
    private void cargarDatosPedidos() {
        try {
            LOGGER.info("Cargando datos de pedidos...");
            Collection<Pedido> pedidos;

            if (listaBusqueda != null) {
                // Si se ha hecho una búsqueda, usar esos resultados
                pedidos = listaBusqueda;
            } else {
                // Caso contrario, obtener todos los pedidos
                pedidos = factoriaPedidos.acceso().getAllPedidos();
            }

            if (pedidos == null || pedidos.isEmpty()) {
                pedidos = new ArrayList<>();
            }

            if (userCliente != null) {
                // Filtrar solo los pedidos del cliente
                pedidosObservableList = FXCollections.observableArrayList(
                        pedidos.stream()
                                .filter(p -> p.getCifCliente().equals(userCliente.getCif()))
                                .collect(Collectors.toList()));
            } else {
                // Si es trabajador, mostrar todos
                pedidosObservableList = FXCollections.observableArrayList(pedidos);
            }

            // Crear una copia de seguridad
            pedidosOriginales = FXCollections.observableArrayList(
                    pedidosObservableList.stream().map(Pedido::clone).collect(Collectors.toList()));

            actualizarTablaYPaginador();
            actualizarEstadoBotones();
            setHayCambiosNoGuardados(false); // Al cargar inicial no hay cambios
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los datos de pedidos", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR,
                    "Error al cargar pedidos",
                    "No se pudieron cargar los pedidos. Intente nuevamente más tarde.");
        }
    }

    /**
     * Reinicia la tabla a los datos originales, eliminando el filtro de
     * búsqueda.
     */
    private void reiniciarTabla() {
        cargarDatosPedidos();
        LOGGER.info("Tabla reiniciada a los datos originales.");
    }

    /**
     * Actualiza la tabla y el paginador tras cambios en la lista.
     */
    private void actualizarTablaYPaginador() {
        tablaPedidos.setItems(pedidosObservableList);
        int numeroPaginas = (int) Math.ceil((double) pedidosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);
        actualizarPagina(0);
    }

    /**
     * Actualiza el contenido de la tabla según la página seleccionada.
     *
     * @param indicePagina Índice de la página a mostrar.
     */
    private void actualizarPagina(int indicePagina) {
        int desdeIndice = indicePagina * FILAS_POR_PAGINA;
        int hastaIndice = Math.min(desdeIndice + FILAS_POR_PAGINA, pedidosObservableList.size());

        if (desdeIndice <= hastaIndice) {
            ObservableList<Pedido> pagina = FXCollections.observableArrayList(
                    pedidosObservableList.subList(desdeIndice, hastaIndice));
            tablaPedidos.setItems(pagina);
        }
    }

    /**
     * Habilita o deshabilita los botones según la selección de la tabla. Por
     * ejemplo, el botón "Detalles" solo se habilita si hay exactamente un
     * pedido seleccionado.
     */
    private void actualizarEstadoBotones() {
        int numSeleccionados = tablaPedidos.getSelectionModel().getSelectedItems().size();
        // Detalles habilitado solo con una fila seleccionada
        botonDetalles.setDisable(numSeleccionados != 1);
        // Eliminar habilitado con al menos una fila seleccionada
        botonEliminar.setDisable(numSeleccionados == 0);
    }

    /**
     * Cancela la edición en la tabla si se está editando alguna celda.
     */
    private void cancelarEdicionEnTabla() {
        if (tablaPedidos.getEditingCell() != null) {
            tablaPedidos.edit(-1, null);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Validación y detección de cambios">
    /**
     * Verifica si los campos obligatorios de los pedidos son válidos.
     *
     * @return {@code true} si todos los pedidos son válidos; {@code false} en
     * caso contrario.
     */
    private boolean validarCamposPedidos() {
        boolean todosValidos = true;
        for (Pedido pedido : pedidosObservableList) {
            if (!validarPedido(pedido)) {
                todosValidos = false;
            }
        }
        return todosValidos;
    }

    /**
     * Verifica la validez de un pedido particular. Comprueba campos como CIF,
     * fecha y estado.
     *
     * @param pedido El pedido a validar.
     * @return {@code true} si el pedido es válido; {@code false} en caso
     * contrario.
     */
    private boolean validarPedido(Pedido pedido) {
        boolean valido = true;

        if (pedido.getCifCliente() == null || pedido.getCifCliente().trim().isEmpty()) {
            marcarCeldaInvalida(columnaCif, pedido);
            valido = false;
        }
        if (pedido.getFechaPedido() == null) {
            marcarCeldaInvalida(columnaFecha, pedido);
            valido = false;
        }
        if (pedido.getEstado() == null) {
            marcarCeldaInvalida(columnaEstado, pedido);
            valido = false;
        }
        // Dirección se valida directamente en la edición de la celda

        return valido;
    }

    /**
     * Marca la celda de la columna específica como inválida para un pedido
     * dado.
     *
     * @param <T> Tipo de la columna.
     * @param columna Columna a marcar.
     * @param pedido Pedido que contiene el valor no válido.
     */
    private <T> void marcarCeldaInvalida(TableColumn<Pedido, T> columna, Pedido pedido) {
        columna.setCellFactory(column -> new TableCell<Pedido, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && pedido.equals(getTableView().getItems().get(getIndex()))) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    /**
     * Determina si los datos de un pedido han cambiado en relación a su copia
     * original.
     *
     * @param original Pedido original.
     * @param modificado Pedido modificado.
     * @return {@code true} si hay cambios, {@code false} en caso contrario.
     */
    private boolean haCambiado(Pedido original, Pedido modificado) {
        if (original == null || modificado == null) {
            return false;
        }
        if (!original.getEstado().equals(modificado.getEstado())) {
            return true;
        }
        if (!original.getDireccion().equals(modificado.getDireccion())) {
            return true;
        }
        if (!original.getFechaPedido().equals(modificado.getFechaPedido())) {
            return true;
        }
        if (!original.getCifCliente().equals(modificado.getCifCliente())) {
            return true;
        }
        return Double.compare(original.getTotal(), modificado.getTotal()) != 0;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Utilidades de clientes y columnas">
    /**
     * Retorna una lista de CIFs a partir de una colección de clientes.
     *
     * @param clientes Colección de instancias de {@code Cliente}.
     * @return Lista de CIFs en un {@code ObservableList<String>}.
     */
    private ObservableList<String> obtenerCifsClientes(Collection<Cliente> clientes) {
        try {
            return FXCollections.observableArrayList(
                    clientes.stream()
                            .map(Cliente::getCif)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los CIF de los clientes", e);
            showErrorDialog(Alert.AlertType.ERROR, "Error",
                    "No se pudieron cargar los CIF de los clientes.");
            return FXCollections.observableArrayList();
        }
    }

    /**
     * Configura la columna "Dirección" para ser editable mediante un
     * {@code TextField}.
     */
    private void configurarColumnaDireccion() {
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

                // Validar al perder el foco
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textField.getText());
                    }
                });

                // Validar al pulsar Enter
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
                pedido.setDireccion(newValue); // Actualiza el modelo
                setHayCambiosNoGuardados(true); // Marca que hay cambios sin guardar
                tablaPedidos.refresh(); // Refresca la tabla
            }

            private void validarYCommit(String newValue) {
                if (newValue == null || newValue.trim().isEmpty()) {
                    showErrorDialog(Alert.AlertType.ERROR, "Campo obligatorio",
                            "La dirección no puede estar vacía.");
                    cancelEdit();
                } else {
                    commitEdit(newValue);
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

    }

    /**
     * Configura la columna "CIF" para ser editable mediante un {@code ComboBox}
     * solo para usuarios de tipo {@code Trabajador}.
     */
    private void configurarColumnaCif() {
        columnaCif.setCellValueFactory(new PropertyValueFactory<>("cifCliente"));
        columnaCif.setCellFactory(tc -> new TableCell<Pedido, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            private String valorOriginal;

            @Override
            public void startEdit() {
                // Solo editable para trabajadores
                if (userTrabajador != null) {
                    super.startEdit();
                    Collection<Cliente> clientes;
                    try {
                        clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                        ObservableList<String> cifs = obtenerCifsClientes(clientes);
                        comboBox.setItems(cifs);
                    } catch (LogicaNegocioException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }

                    valorOriginal = getItem();
                    comboBox.setValue((valorOriginal != null) ? valorOriginal : null);

                    setGraphic(comboBox);
                    setText(null);
                    comboBox.requestFocus();

                    // Listener para perder foco
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
                    // Buscar y asignar el cliente correspondiente al nuevo CIF
                    Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
                    Cliente clienteActualizado = clientes.stream()
                            .filter(c -> c.getCif().equals(newValue))
                            .findFirst()
                            .orElse(null);
                    pedido.setCliente(clienteActualizado);
                } catch (LogicaNegocioException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }

                setText(newValue);
                setGraphic(null);
                setHayCambiosNoGuardados(true); // <<--- Marcamos que hay cambios
                tablaPedidos.refresh();
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
    }

    /**
     * Configura la columna "Fecha" para ser editable mediante un
     * {@code DatePicker}.
     */
    private void configurarColumnaFecha() {
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        columnaFecha.setCellFactory(tc -> new TableCell<Pedido, Date>() {
            private final DatePicker datePicker = new DatePicker();
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public void startEdit() {
                super.startEdit();
                Date item = getItem();
                if (item != null) {
                    datePicker.setValue(item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } else {
                    datePicker.setValue(null);
                }
                setGraphic(datePicker);
                setText(null);

                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        cancelEdit();

                    }
                });

                datePicker.setOnAction(event -> {
                    Date newDate = Date.from(datePicker.getValue()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant());

                    // Validación de fecha anterior a hoy
                    if (newDate.before(new Date()) && !newDate.equals(getItem())) {
                        showErrorDialog(Alert.AlertType.ERROR, "Fecha inválida",
                                "No se permiten fechas anteriores al día actual.");
                        cancelEdit();
                    } else {
                        commitEdit(newDate);
                    }
                });
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText((getItem() != null) ? dateFormat.format(getItem()) : null);
                setGraphic(null);
            }

            @Override
            public void commitEdit(Date newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setFechaPedido(newValue);
                setHayCambiosNoGuardados(true); // <<--- Marcamos que hay cambios
                tablaPedidos.refresh();
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
    }

    /**
     * Configura la columna "Estado" para ser editable mediante un
     * {@code ComboBox} de valores del enum {@link Estado}, exclusivamente para
     * trabajadores.
     */
    private void configurarColumnaEstado() {
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setCellFactory(tc -> new TableCell<Pedido, Estado>() {
            private final ComboBox<Estado> comboBox = new ComboBox<>(FXCollections.observableArrayList(Estado.values()));

            @Override
            public void startEdit() {
                // Solo editable si el usuario es trabajador
                if (userTrabajador != null) {
                    super.startEdit();
                    if (getItem() != null) {
                        comboBox.setValue(getItem());
                    }
                    setGraphic(comboBox);
                    setText(null);

                    comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            cancelEdit();

                        }
                    });

                    comboBox.setOnAction(event -> commitEdit(comboBox.getValue()));
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText((getItem() != null) ? getItem().name() : null);
                setGraphic(null);
            }

            @Override
            public void commitEdit(Estado newValue) {
                super.commitEdit(newValue);
                Pedido pedido = getTableView().getItems().get(getIndex());
                pedido.setEstado(newValue);
                setHayCambiosNoGuardados(true); // <<--- Marcamos que hay cambios
                tablaPedidos.refresh();

            }

            @Override
            protected void updateItem(Estado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        comboBox.setValue(item);
                        setGraphic(comboBox);
                        setText(null);
                    } else {
                        setText(item.name());
                        setGraphic(null);
                    }
                }
            }
        });
    }

    /**
     * Configura la columna "Total" para que no sea editable y muestre el valor
     * con formato de moneda.
     */
    private void configurarColumnaTotal() {
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
    }

    /**
     * Ajusta el ancho de las columnas de forma proporcional a la anchura total
     * del TableView.
     *
     * @param tableView El TableView al que se le ajustará el ancho de columnas.
     */
    private void ajustarAnchoProporcional(TableView<?> tableView) {
        double totalWidth = tableView.getPrefWidth();
        tableView.getColumns().forEach(column -> {
            if (column.equals(columnaId)) {
                column.setPrefWidth(totalWidth * 0.05d);
            } else if (column.equals(columnaCif)) {
                column.setPrefWidth(totalWidth * 0.17d);
            } else if (column.equals(columnaDireccion)) {
                column.setPrefWidth(totalWidth * 0.23d);
            } else if (column.equals(columnaEstado)) {
                column.setPrefWidth(totalWidth * 0.2d);
            } else if (column.equals(columnaFecha)) {
                column.setPrefWidth(totalWidth * 0.2d);
            } else if (column.equals(columnaTotal)) {
                column.setPrefWidth((totalWidth * 0.15d) - 20);
            }
        });
    }
    // </editor-fold>
}
