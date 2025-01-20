package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.PedidoArticulo;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Estado;
import crud.utilidades.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorPedidosDetalle implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosDetalle.class.getName());

    // Factorías de acceso a datos
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();

    // Escenario y entidades
    private Stage stage;
    private Pedido pedido;
    private Cliente userCliente;
    private Trabajador userTrabajador;

    // Listas para la tabla
    private ObservableList<Articulo> articulosDisponibles;
    private ObservableList<PedidoArticulo> articulosDelPedido;
    private ObservableList<PedidoArticulo> articulosDelPedidoOriginales;

    // Componentes FXML
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Articulo> tablaArticulosDisponibles;
    @FXML
    private TableColumn<Articulo, Long> columnaId;
    @FXML
    private TableColumn<Articulo, String> columnaNombre;
    @FXML
    private TableColumn<Articulo, Double> columnaPrecio;

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

    // -------------------------------------------------------------------------
    //  MÉTODOS DE INICIALIZACIÓN
    // -------------------------------------------------------------------------
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

        // Si no llamamos aquí a cargarArticulosDelPedido(), se llamará en initStage también
        // Pero generalmente lo hacemos una vez que 'pedido' está seteado
        actualizarTotal();

    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Detalles del pedido");

        LOGGER.info("Inicializando la escena principal");

        // Añadir eventos a los botones
        botonCompra.setOnAction(this::handleCompra);
        botonEliminar.setOnAction(this::handleEliminar);
        botonGuardar.setOnAction(this::handleGuardarCambios);
        botonAtras.setOnAction(this::handleAtras);

        stage.show();

        // Si ya tenemos el pedido, cargamos sus datos a pantalla
        if (pedido != null) {
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

            // Ahora sí, cargar los PedidoArticulo
            cargarArticulosDelPedido();
            actualizarTotal();
            agregarListeners();
        }
        if (userCliente != null) {
            campoEstado.setDisable(true);
            campoCif.setDisable(true);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado (Cliente): " + userCliente.getNombre() + " ID=" + userCliente.getId());
            } else {
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado (Trabajador): " + userTrabajador.getNombre());
            }
        }
    }

    // -------------------------------------------------------------------------
    //  CONFIGURACIÓN DE TABLAS
    // -------------------------------------------------------------------------
    private void configurarTablas() {
        // Tabla de artículos disponibles

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Evitamos que el usuario reordene las columnas
        tablaArticulosDisponibles.getColumns().forEach(col -> col.setSortable(false));

        // Tabla de artículos del pedido
        columnaPedidoArticuloId.setCellValueFactory(new PropertyValueFactory<>("pedidoArticuloId"));
        columnaId2.setCellValueFactory(new PropertyValueFactory<>("articuloId"));

        // Nombre (personalizado, no sortable)
        columnaNombre2.setSortable(false);
        columnaNombre2.setCellValueFactory(new PropertyValueFactory<>("articuloNombre"));

        // Cantidad (Spinner), no sortable
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

                // Buscar Articulo y calcular máximo stock
                Articulo articulo = articulosDisponibles.stream()
                        .filter(a -> a.getId().equals(pa.getArticuloId()))
                        .findFirst()
                        .orElse(null);

                int maxStock = (articulo != null) ? articulo.getStock() : 1000;

                // Crear Spinner y setear rango
                spinner = new Spinner<>();
                spinner.setEditable(true);

                SpinnerValueFactory<Integer> valueFactory
                        = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxStock, pa.getCantidad());
                spinner.setValueFactory(valueFactory);

                // Cada vez que cambia el Spinner -> actualizar modelo
                spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.equals(pa.getCantidad())) {
                        pa.setCantidad(newVal);
                        // Recalcular total
                        actualizarTotal();
                        // No llamamos a tableView.refresh() aquí para evitar bucles
                    }
                });

                // Al perder el foco, refrescamos la tabla para que se redibuje Subtotal
                spinner.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                    if (!isFocused) {
                        // commitEdit si la columna está en modo editable
                        commitEdit(spinner.getValue());
                        // Forzar refresco de la tabla
                        getTableView().refresh();
                        // Además, forzamos orden si lo deseas
                        ordenarTablaPorArticuloId();
                    }
                });

                // Manejar entradas manuales en el editor
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

        // Precio (Subtotal), no sortable
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
                Articulo articulo = articulosDisponibles.stream()
                        .filter(a -> a.getId().equals(pa.getArticuloId()))
                        .findFirst()
                        .orElse(null);

                double precioUnitario = (articulo != null ? articulo.getPrecio() : 0);
                double subtotal = precioUnitario * pa.getCantidad();
                setText(String.format("%.2f €", subtotal));
            }
        });

        // También podemos desactivar la reordenación de columnas en la tabla de pedido:
        tablaArticulosPedidos.getColumns().forEach(col -> col.setSortable(false));
    }

    // -------------------------------------------------------------------------
    //  CARGA DE DATOS
    // -------------------------------------------------------------------------
    private void cargarArticulosDisponibles() {
        try {
            Collection<Articulo> articulos = factoriaArticulos.acceso().getAllArticulos();
            articulosDisponibles = FXCollections.observableArrayList(articulos);

            // Ordenamos la lista por ID (si lo deseas)
            FXCollections.sort(articulosDisponibles, Comparator.comparing(Articulo::getId));

            tablaArticulosDisponibles.setItems(articulosDisponibles);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los artículos disponibles", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los artículos disponibles.");
        }
    }

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

            // Ordena la lista por el ID del artículo
            ordenarListaPorArticuloId(articulosDelPedido);

            // Asigna la lista a la tabla
            tablaArticulosPedidos.setItems(articulosDelPedido);

            // Y actualiza la vista
            tablaArticulosPedidos.refresh();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los artículos del pedido", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los artículos del pedido.");
        }
    }

    private void cargarCifsClientes() {
        try {
            Collection<Cliente> clientes = factoriaUsuarios.accesoCliente().getAllClientes();
            ObservableList<String> cifs = FXCollections.observableArrayList(
                    clientes.stream().map(Cliente::getCif).collect(Collectors.toList())
            );
            campoCif.setItems(cifs);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los CIF de los clientes", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los CIF de los clientes.");
        }
    }

    private void cargarEstados() {
        campoEstado.setItems(FXCollections.observableArrayList(Estado.values()));
    }

    // -------------------------------------------------------------------------
    //  EVENTOS DE BOTONES Y LISTENERS DE CAMPOS
    // -------------------------------------------------------------------------
    private void agregarListeners() {
        // Listener para campoDireccion
        campoDireccion.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) { // Se ejecuta cuando pierde el foco
                if (campoDireccion.getText().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La dirección no puede estar vacía.");
                    campoDireccion.setText(pedido.getDireccion()); // Restaurar el valor original
                } else {
                    pedido.setDireccion(campoDireccion.getText());
                }
            }
        });

        // Listener para campoCif
        campoCif.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Debe seleccionar un CIF válido.");
                campoCif.setValue(oldValue); // Restaurar el valor anterior
            } else {
                pedido.setCifCliente(newValue);
            }
        });

        // Listener para campoEstado
        campoEstado.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                pedido.setEstado(newValue);
            }
        });

        // Listener para campoFecha
        campoFecha.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isBefore(java.time.LocalDate.now()) && !newValue.equals(oldValue)) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La fecha no puede ser anterior al día de hoy.");
                    campoFecha.setValue(oldValue); // Restaurar el valor anterior
                } else {
                    pedido.setFechaPedido(java.sql.Date.valueOf(newValue));
                }
            }
        });

        // Listener para campoTotal (solo para validación adicional, si es editable)
        campoTotal.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) { // Se ejecuta cuando pierde el foco
                try {
                    String valor = campoTotal.getText().replace(" €", ""); // Quitar símbolo de moneda
                    double total = Double.parseDouble(valor);
                    pedido.setTotal(total);
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El total debe ser un valor numérico.");
                    campoTotal.setText(String.format("%.2f €", pedido.getTotal())); // Restaurar el valor original
                }
            }
        });
    }

    @FXML
    private void handleCompra(ActionEvent event) {
        Articulo articuloSeleccionado = tablaArticulosDisponibles.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Agregar Artículo", "Seleccione un artículo para agregar.");
            return;
        }

        // Busca si ya existe un PedidoArticulo para ese artículo
        PedidoArticulo pedidoArticulo = articulosDelPedido.stream()
                .filter(pa -> pa.getArticuloId().equals(articuloSeleccionado.getId()))
                .findFirst()
                .orElse(null);

        if (pedidoArticulo != null) {
            // Si ya existe, aumenta la cantidad
            pedidoArticulo.setCantidad(pedidoArticulo.getCantidad() + 1);
        } else {
            pedidoArticulo = new PedidoArticulo();
            pedidoArticulo.setArticulo(new Articulo());
            pedidoArticulo.setPedido(new Pedido());

            // Objetos completos
            pedidoArticulo.setArticulo(articuloSeleccionado);
            pedidoArticulo.setPedido(pedido);

            pedidoArticulo.setCantidad(1);
            pedidoArticulo.setPrecioCompra(articuloSeleccionado.getPrecio());
            articulosDelPedido.add(pedidoArticulo);

        }

        // Ordena la lista por ID de artículo
        ordenarListaPorArticuloId(articulosDelPedido);

        tablaArticulosPedidos.refresh();
        actualizarTotal();
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        PedidoArticulo articuloSeleccionado = tablaArticulosPedidos.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Eliminar Artículo", "Seleccione un artículo para eliminar.");
            return;
        }

        // Si la cantidad > 1, decrementa; si no, lo elimina
        if (articuloSeleccionado.getCantidad() > 1) {
            articuloSeleccionado.setCantidad(articuloSeleccionado.getCantidad() - 1);
        } else {
            articulosDelPedido.remove(articuloSeleccionado);
        }

        ordenarListaPorArticuloId(articulosDelPedido);
        tablaArticulosPedidos.refresh();
        actualizarTotal();
    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        LOGGER.info("Botón Guardar Cambios presionado");

        try {
            // Actualizar el pedido (total, etc.)
            NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
            // o un Locale específico: new Locale("es", "ES")

            String valor = campoTotal.getText().replace(" €", ""); // Quitar el símbolo
            Number number = nf.parse(valor);
            double total = number.doubleValue();
            pedido.setTotal(total);
            factoriaPedidos.acceso().actualizarPedido(pedido);

            // Para cada PedidoArticulo actual en la tabla
            for (PedidoArticulo pedidoArticulo : articulosDelPedido) {
                // Asignar también las ENTIDADES completas
                pedidoArticulo.setPedido(pedido);
                Articulo art = articulosDisponibles.stream()
                        .filter(a -> a.getId().equals(pedidoArticulo.getArticuloId()))
                        .findFirst()
                        .orElse(null);
                pedidoArticulo.setArticulo(art);

                PedidoArticulo pedidoArticuloOriginal = articulosDelPedidoOriginales.stream()
                        .filter(p -> p.getId() != null && p.getId().equals(pedidoArticulo.getId()))
                        .findFirst()
                        .orElse(null);

                // Caso 1: ID es null => no existía, hay que crearlo en BBDD
                if (pedidoArticulo.getId() == null) {
                    LOGGER.info("Creando nuevo PedidoArticulo (ArticuloID=" + pedidoArticulo.getArticuloId() + ")");
                    factoriaPedidoArticulo.acceso().crearPedidoArticulo(pedidoArticulo);

                    // Caso 2: ID existe y se detectan cambios => actualizar
                } else if (pedidoArticuloOriginal != null && haCambiado(pedidoArticuloOriginal, pedidoArticulo)) {
                    LOGGER.info("Actualizando PedidoArticulo con ID=" + pedidoArticulo.getId());
                    factoriaPedidoArticulo.acceso().actualizarPedidoArticulo(pedidoArticulo);
                }
            }

            // Para cada PedidoArticulo original que ya no esté en la lista => borrarlo
            for (PedidoArticulo pedidoArticuloOriginal : articulosDelPedidoOriginales) {
                if (!articulosDelPedido.contains(pedidoArticuloOriginal)) {
                    LOGGER.info("Borrando PedidoArticulo con ID=" + pedidoArticuloOriginal.getId());
                    factoriaPedidoArticulo.acceso().borrarPedidoArticulo(pedidoArticuloOriginal);
                }
            }

            // Refresca la lista de "originales" con el estado actual
            articulosDelPedidoOriginales.setAll(articulosDelPedido);

            // Forzamos recarga (si deseas)
            reiniciar();

            LOGGER.info("Cambios guardados exitosamente.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error", "No se pudieron guardar los cambios. Intente nuevamente.");
        }
    }

    @FXML
    private void handleAtras(ActionEvent event) {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, null);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, null);
        }
    }

    // -------------------------------------------------------------------------
    //  MÉTODOS DE APOYO
    // -------------------------------------------------------------------------
    private void reiniciar() {
        cargarArticulosDisponibles();
        cargarArticulosDelPedido();
        tablaArticulosDisponibles.refresh();
        tablaArticulosPedidos.refresh();
        actualizarTotal();
    }

    public boolean haCambiado(PedidoArticulo original, PedidoArticulo modificado) {
        if (original == null || modificado == null) {
            return false;
        }
        // Podrías comprobar más campos si lo deseas (precioCompra, etc.)
        return original.getCantidad() != modificado.getCantidad();
    }

    private void actualizarTotal() {
        double total = articulosDelPedido.stream()
                .mapToDouble(pa -> pa.getCantidad() * pa.getPrecioCompra())
                .sum();
        campoTotal.setText(String.format("%.2f €", total));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // -------------------------------------------------------------------------
    //  ORDENAR TABLA POR ID DE ARTÍCULO
    // -------------------------------------------------------------------------
    /**
     * Ordena la lista de PedidoArticulo en memoria según el ID del artículo.
     * Luego refresca la tabla.
     */
    private void ordenarListaPorArticuloId(ObservableList<PedidoArticulo> lista) {
        FXCollections.sort(lista, Comparator.comparing(PedidoArticulo::getArticuloId));
    }

    /**
     * Para llamar tras editar cantidades, si deseas reordenar en vivo.
     */
    private void ordenarTablaPorArticuloId() {
        ordenarListaPorArticuloId(articulosDelPedido);
        tablaArticulosPedidos.refresh();
    }
}
