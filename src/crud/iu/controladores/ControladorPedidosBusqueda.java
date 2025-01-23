package crud.iu.controladores;

import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import static crud.utilidades.ExcepcionesUtilidad.clasificadorExcepciones;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
import java.util.stream.Collectors;

/**
 * <h1>ControladorPedidosBusqueda</h1>
 * Controlador para la vista <strong>PedidosBusqueda.fxml</strong>. Gestiona la
 * lógica para filtrar pedidos según varios criterios (ID, CIF, dirección,
 * fecha, estado y precio).
 *
 * <p>
 * <strong>Autor:</strong>
 * <a href="mailto:urkoperitz@example.com">Urko Peritz</a>
 * </p>
 */
public class ControladorPedidosBusqueda implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="Constantes y Logger">
    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosBusqueda.class.getName());
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Factorías de acceso a datos">
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stage y entidades principales">
    private Stage stage;
    private Cliente userCliente;
    private Trabajador userTrabajador;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Componentes FXML">
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox hBoxCif;
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
    private ComboBox<Estado> comboBoxEstado;
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
    // </editor-fold>

    /**
     * Se ejecuta tras cargar el FXML. Configura los {@code Spinner}, los
     * handlers de los botones y otros ajustes de inicialización.
     *
     * @param url URL de la ubicación de origen.
     * @param resourceBundle Recursos de internacionalización.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        factoriaPedidos = FactoriaPedidos.getInstance();

        // Configurar valores iniciales para los spinners
        spinnerIdDesde.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerIdHasta.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        spinnerPrecioDesde.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0));
        spinnerPrecioHasta.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0));

        // Configurar acciones de botones
        botonBuscar.setOnAction(this::handleBuscar);
        botonReiniciarCampos.setOnAction(this::handleReiniciarCampos);
        botonAtras.setOnAction(this::handleAtras);
    }

    /**
     * Inicializa la escena, asigna el {@code Stage} y realiza configuraciones
     * adicionales (spinner, comboBox, estado inicial).
     *
     * @param root Nodo raíz (parent) de la escena.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Búsqueda avanzada");
        LOGGER.info("Inicializando la escena principal");
        stage.show();

        // Configurar spinner con validaciones
        configurarSpinnerId(spinnerIdDesde);
        configurarSpinnerId(spinnerIdHasta);
        configurarSpinnerPrecio(spinnerPrecioDesde);
        configurarSpinnerPrecio(spinnerPrecioHasta);

        // Asocia CheckBoxes con sus respectivos campos
        asociarCampoConCheckBox(spinnerIdDesde.getEditor(), checkBoxIdPedido);
        asociarCampoConCheckBox(spinnerIdHasta.getEditor(), checkBoxIdPedido);
        asociarCampoConCheckBox(comboBoxCIF, checkBoxCIF);
        asociarCampoConCheckBox(textFieldDireccion, checkBoxDireccion);
        asociarCampoConCheckBox(datePickerDesde, checkBoxFecha);
        asociarCampoConCheckBox(datePickerHasta, checkBoxFecha);
        asociarCampoConCheckBox(comboBoxEstado, checkBoxEstado);
        asociarCampoConCheckBox(spinnerPrecioDesde.getEditor(), checkBoxPrecio);
        asociarCampoConCheckBox(spinnerPrecioHasta.getEditor(), checkBoxPrecio);

        // Cargar valores iniciales
        cargarComboBoxCIF();
        cargarComboBoxEstado();

        // Configurar estado inicial de la vista
        configurarEstadoInicial();
    }

    /**
     * Asigna el {@code Stage} principal para este controlador.
     *
     * @param stage El {@code Stage} de la ventana de búsqueda.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Asigna el usuario que está realizando la búsqueda (puede ser un
     * {@link Cliente} o un {@link Trabajador}).
     *
     * @param user Instancia de {@code Cliente} o {@code Trabajador}.
     */
    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = (Cliente) user;
                LOGGER.info("Usuario asignado (Cliente): " + userCliente.getId());
            } else {
                this.userTrabajador = (Trabajador) user;
                LOGGER.info("Usuario asignado (Trabajador): " + userTrabajador.getId());
            }
        }
    }

    /**
     * Asocia un {@code Control} (TextField, Spinner, ComboBox, DatePicker) con
     * un {@code CheckBox}. Cuando el usuario ingresa información en el control,
     * automáticamente se selecciona el CheckBox.
     *
     * @param campo Control a monitorear.
     * @param checkBox CheckBox que se activará si el campo no está vacío.
     */
    private void asociarCampoConCheckBox(Control campo, CheckBox checkBox) {
        if (campo instanceof TextField) {
            ((TextField) campo).textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.trim().isEmpty()) {
                    checkBox.setSelected(true);
                }
            });
        } else if (campo instanceof Spinner) {
            ((Spinner<?>) campo).getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.trim().isEmpty()) {
                    checkBox.setSelected(true);
                }
            });
        } else if (campo instanceof ComboBox) {
            ((ComboBox<?>) campo).valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    checkBox.setSelected(true);
                }
            });
        } else if (campo instanceof DatePicker) {
            ((DatePicker) campo).valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    checkBox.setSelected(true);
                }
            });
        }
    }

    /**
     * Configura un Spinner para permitir únicamente valores enteros positivos.
     *
     * @param spinner El Spinner a configurar.
     */
    private void configurarSpinnerId(Spinner<Integer> spinner) {
        SpinnerValueFactory<Integer> valueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                spinner.getEditor().setText(oldValue);
            } else if (!newValue.isEmpty()) {
                int value = Integer.parseInt(newValue);
                if (value < 1) {
                    spinner.getEditor().setText(oldValue);
                } else {
                    spinner.getValueFactory().setValue(value);
                }
            }
        });
    }

    /**
     * Configura un Spinner para valores decimales (precio). Permite "," o "."
     * como separador, formateando el resultado al perder el foco.
     *
     * @param spinner El Spinner a configurar.
     */
    private void configurarSpinnerPrecio(Spinner<Double> spinner) {
        SpinnerValueFactory<Double> valueFactory
                = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.01);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        TextField editor = spinner.getEditor();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            // Eliminar símbolo de moneda para validación
            String valueWithoutSymbol = newValue.replace("€", "").trim();

            // Validar entrada (digitos, punto o coma)
            if (valueWithoutSymbol.matches("\\d*(\\.\\d*)?|\\d*(,\\d*)?")) {
                if (!valueWithoutSymbol.isEmpty()) {
                    try {
                        // Sustituir coma por punto para parseo
                        String standardizedValue = valueWithoutSymbol.replace(",", ".");
                        double value = Double.parseDouble(standardizedValue);
                        spinner.getValueFactory().setValue(value);
                    } catch (NumberFormatException e) {
                        // Ignorar entradas temporales inválidas
                    }
                }
            } else {
                // Restaura el texto si la entrada es completamente inválida
                editor.setText(oldValue);
            }
        });

        // Al perder el foco, formatear con " €"
        editor.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                double value = spinner.getValue();
                editor.setText(String.format("%.2f €", value));
            }
        });

        // Inicializar el texto con el símbolo
        double initialValue = spinner.getValueFactory().getValue();
        editor.setText(String.format("%.2f €", initialValue));
    }

    /**
     * Maneja la acción del botón "Buscar". Aplica los filtros seleccionados
     * (ID, CIF, dirección, fecha, estado, precio) para filtrar los pedidos.
     *
     * @param event Evento de la acción.
     */
    private void handleBuscar(ActionEvent event) {
        try {
            Collection<Pedido> pedidosFiltrados = new ArrayList<>(factoriaPedidos.acceso().getAllPedidos());

            // Filtro por ID de Pedido
            if (checkBoxIdPedido.isSelected()) {
                int idDesde = spinnerIdDesde.getValue();
                int idHasta = spinnerIdHasta.getValue();
                pedidosFiltrados.removeIf(pedido
                        -> pedido.getId().intValue() < idDesde || pedido.getId().intValue() > idHasta);
            }

            // Filtro por CIF
            if (checkBoxCIF.isSelected()) {
                String cifSeleccionado = comboBoxCIF.getValue();
                pedidosFiltrados.removeIf(pedido
                        -> !pedido.getCifCliente().equalsIgnoreCase(cifSeleccionado));
            }

            // Filtro por dirección
            if (checkBoxDireccion.isSelected()) {
                String direccion = textFieldDireccion.getText().toLowerCase();
                pedidosFiltrados.removeIf(pedido
                        -> !pedido.getDireccion().toLowerCase().contains(direccion));
            }

            // Filtro por fecha
            if (checkBoxFecha.isSelected()) {
                Date fechaDesde = convertToDate(datePickerDesde.getValue());
                Date fechaHasta = convertToDate(datePickerHasta.getValue());
                pedidosFiltrados.removeIf(pedido
                        -> pedido.getFechaPedido().before(fechaDesde)
                        || pedido.getFechaPedido().after(fechaHasta));
            }

            // Filtro por estado
            if (checkBoxEstado.isSelected()) {
                Estado estadoSeleccionado = comboBoxEstado.getValue();
                pedidosFiltrados.removeIf(pedido
                        -> !pedido.getEstado().equals(estadoSeleccionado));
            }

            // Filtro por precio total
            if (checkBoxPrecio.isSelected()) {
                double precioMin = spinnerPrecioDesde.getValue();
                double precioMax = spinnerPrecioHasta.getValue();
                pedidosFiltrados.removeIf(pedido
                        -> pedido.getTotal() < precioMin || pedido.getTotal() > precioMax);
            }

            mostrarResultados(pedidosFiltrados);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al filtrar los pedidos", e);
            clasificadorExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja la acción del botón "Reiniciar Campos". Limpia todos los campos y
     * desmarca sus respectivos CheckBoxes.
     *
     * @param event Evento de la acción.
     */
    private void handleReiniciarCampos(ActionEvent event) {
        // Desactivar CheckBoxes
        checkBoxIdPedido.setSelected(false);
        checkBoxCIF.setSelected(false);
        checkBoxDireccion.setSelected(false);
        checkBoxFecha.setSelected(false);
        checkBoxEstado.setSelected(false);
        checkBoxPrecio.setSelected(false);

        // Restaurar valores iniciales de Spinners
        spinnerIdDesde.getValueFactory().setValue(1);
        spinnerIdHasta.getValueFactory().setValue(100);
        spinnerPrecioDesde.getValueFactory().setValue(0.0);
        spinnerPrecioHasta.getValueFactory().setValue(100.0);

        // Limpiar ComboBoxes
        comboBoxCIF.getSelectionModel().clearSelection();
        comboBoxEstado.getSelectionModel().clearSelection();

        // Limpiar campos de texto y fecha
        textFieldDireccion.clear();
        datePickerDesde.setValue(null);
        datePickerHasta.setValue(null);
    }

    /**
     * Maneja la acción del botón "Atrás". Regresa a la ventana principal de
     * pedidos, conservando la lista completa o el usuario actual.
     *
     * @param event Evento de la acción.
     */
    @FXML
    private void handleAtras(ActionEvent event) {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, null);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, null);
        }
    }

    /**
     * Convierte un {@code LocalDate} a un {@code Date}.
     *
     * @param localDate Fecha en formato {@code LocalDate}.
     * @return Fecha convertida a {@code Date}, o {@code null} si es inválida.
     */
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Muestra los resultados de la búsqueda (pedidos filtrados) en la ventana
     * principal de pedidos.
     *
     * @param pedidosFiltrados Colección de pedidos que cumplen los filtros.
     */
    private void mostrarResultados(Collection<Pedido> pedidosFiltrados) {
        if (userCliente != null) {
            factoriaPedidos.cargarPedidosPrincipal(stage, userCliente, pedidosFiltrados);
        } else {
            factoriaPedidos.cargarPedidosPrincipal(stage, userTrabajador, pedidosFiltrados);
        }
    }

    /**
     * Muestra un cuadro de diálogo de error con el título y mensaje
     * especificados.
     *
     * @param titulo Título del cuadro de diálogo.
     * @param mensaje Mensaje descriptivo del error.
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Carga la lista de CIF de todos los clientes y la asigna al
     * {@code comboBoxCIF}.
     */
    private void cargarComboBoxCIF() {
        try {
            List<String> cifsClientes = factoriaUsuarios.accesoCliente()
                    .getAllClientes()
                    .stream()
                    .map(Cliente::getCif)
                    .collect(Collectors.toList());
            comboBoxCIF.getItems().setAll(cifsClientes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los CIF de los clientes", e);
            clasificadorExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga todas las opciones de estado disponibles en {@link Estado} y las
     * asigna al {@code comboBoxEstado}.
     */
    private void cargarComboBoxEstado() {
        try {
            comboBoxEstado.getItems().setAll(Estado.values());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los estados", e);
            clasificadorExcepciones(e, e.getMessage());
        }
    }

    /**
     * Configura el estado inicial del formulario en función de si el usuario es
     * un cliente o un trabajador.
     */
    private void configurarEstadoInicial() {
        if (userCliente != null) {
            // Ocultar la selección de CIF y fijar el valor del cliente
            checkBoxCIF.setSelected(true);
            comboBoxCIF.setValue(userCliente.getCif());
            hBoxCif.setVisible(false);  // Oculta el contenedor del ComboBox de CIF
            LOGGER.info("Configurado estado inicial para cliente con CIF: " + userCliente.getCif());
        } else {
            LOGGER.info("Configurado estado inicial para trabajador.");
        }
    }

}
