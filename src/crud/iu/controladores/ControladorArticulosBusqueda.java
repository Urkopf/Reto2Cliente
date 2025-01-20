package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorArticulosBusqueda implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosBusqueda.class.getName());
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private ObservableList<Articulo> articulosObservableList;
    private Stage stage;
    private Usuario usuario;

    // Elementos FXML
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBuscar;
    @FXML
    private Spinner<Long> spinnerIdDesde;
    @FXML
    private Spinner<Long> spinnerIdHasta;
    @FXML
    private DatePicker datePickerDesde;
    @FXML
    private DatePicker datePickerHasta;
    @FXML
    private TextField textFieldPrecioMin;
    @FXML
    private TextField textFieldPrecioMax;
    @FXML
    private TextField textFieldStock;
    @FXML
    private ComboBox<String> comboBoxNombre;
    @FXML
    private CheckBox checkBoxId;
    @FXML
    private CheckBox checkBoxNombre;
    @FXML
    private CheckBox checkBoxFecha;
    @FXML
    private CheckBox checkBoxPrecio;
    @FXML
    private CheckBox checkBoxStock;

    private ObservableList<String> nombresDisponibles;
    private Trabajador userTrabajador;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Busqueda");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");

        //botonBuscar.addEventHandler(ActionEvent.ACTION, this::handleBuscar);
        botonReiniciar.addEventHandler(ActionEvent.ACTION, this::handleReiniciarFiltros);
        botonAtras.addEventHandler(ActionEvent.ACTION, this::handleAtras);

        stage.show();  // Mostrar el escenario

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosBusqueda");
        configurarSpinners();
        configurarComboBox();

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

    private void configurarSpinners() {
        configurarSpinnerIdDesde();
        configurarSpinnerIdHasta();

    }

    //Eventos
    @FXML
    private void handleReiniciarFiltros(ActionEvent event) {
        spinnerIdDesde.getValueFactory().setValue(0L);
        spinnerIdHasta.getValueFactory().setValue(0L);
        comboBoxNombre.setValue(null);
        setDatePickerDate(datePickerDesde, new Date());
        setDatePickerDate(datePickerHasta, new Date());
        textFieldPrecioMin.clear();
        textFieldPrecioMax.clear();
        textFieldStock.clear();

        checkBoxId.setSelected(false);
        checkBoxNombre.setSelected(false);
        checkBoxFecha.setSelected(false);
        checkBoxPrecio.setSelected(false);
        checkBoxStock.setSelected(false);

        LOGGER.info("Filtros reiniciados.");

    }

    //Permitir poner en el DatePicker un Date y no un LocalDate
    private void setDatePickerDate(DatePicker datePicker, Date fecha) {
        if (fecha != null) {
            datePicker.setValue(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            datePicker.setValue(null);
        }
    }

    @FXML
    private void handleAtras(ActionEvent event) {
        LOGGER.info("Regresando a ArticulosPrincipal.");
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador);

    }

    @FXML
    private void handleBuscar(ActionEvent event) {

        try {
            //Obtenemos lo puesto por el usuario
            Long idDesde = checkBoxId.isSelected() ? spinnerIdDesde.getValue() : null;
            Long idHasta = checkBoxId.isSelected() ? spinnerIdHasta.getValue() : null;

            if (idDesde != null && idHasta != null && idDesde > idHasta) {
                throw new IllegalArgumentException("El ID desde no puede ser mayor que el ID hasta.");
            }

            String nombre = checkBoxNombre.isSelected() ? comboBoxNombre.getValue() : null;
            //Manejo de Fechas con Date() no con LocalDate()
            Date fechaDesde = checkBoxFecha.isSelected() && datePickerDesde.getValue() != null
                    ? Date.from(datePickerDesde.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : null;

            Date fechaHasta = checkBoxFecha.isSelected() && datePickerHasta.getValue() != null
                    ? Date.from(datePickerHasta.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : null;
            Double precioMin = checkBoxPrecio.isSelected() ? validarMin(parseDouble(textFieldPrecioMin.getText())) : null;
            Double precioMax = checkBoxPrecio.isSelected() ? validarMax(parseDouble(textFieldPrecioMax.getText()), precioMin) : null;
            Integer stock = checkBoxStock.isSelected() ? validarStock(parseInteger(textFieldStock.getText())) : null;

            //Aqui la logica de filtros, ya sea con lista o con Query
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al realizar la búsqueda", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error de Búsqueda", "No se pudo completar la búsqueda. Intente nuevamente.");
        }

    }

    //Configuraciones
    private void configurarSpinnerIdDesde() {
        spinnerIdDesde.setValueFactory(new SpinnerValueFactory<Long>() {
            private long value = 0;

            @Override
            public void decrement(int steps) {
                value = Math.max(0, value - steps);
                setValue(value);
            }

            @Override
            public void increment(int steps) {
                value = Math.min(0, value + steps);
                setValue(value);
            }
        });
        spinnerIdDesde.getValueFactory().setValue(0L);
    }

    private void configurarSpinnerIdHasta() {
        spinnerIdHasta.setValueFactory(new SpinnerValueFactory<Long>() {
            private long value = 0;

            @Override
            public void decrement(int steps) {
                value = Math.max(0, value - steps);
                setValue(value);
            }

            @Override
            public void increment(int steps) {
                value = Math.min(0, value + steps);
                setValue(value);
            }
        });
        spinnerIdHasta.getValueFactory().setValue(0L);
    }

    private Double parseDouble(String valor) {
        try {
            return valor != null && !valor.isEmpty() ? Double.parseDouble(valor) : null;
        } catch (NumberFormatException e) {
            LOGGER.warning("Error al parsear Double: " + valor);
            return null;
        }
    }

    private Double validarMin(Double valor) {
        if (valor != null && valor < 0) {
            throw new IllegalArgumentException("El valor mínimo no puede ser menor que 0.");
        }
        return valor;
    }

    private Double validarMax(Double valorMax, Double valorMin) {
        if (valorMax != null && valorMin != null && valorMax < valorMin) {
            throw new IllegalArgumentException("El valor máximo no puede ser menor que el mínimo.");
        }
        return valorMax;
    }

    private Integer parseInteger(String valor) {
        try {
            return valor != null && !valor.trim().isEmpty() ? Integer.parseInt(valor.trim()) : null;
        } catch (NumberFormatException e) {
            LOGGER.warning("Error al parsear Integer: " + valor + " Asegúrate de que sea un número válido.");
            return null;
        }
    }

    private Integer validarStock(Integer valor) {
        if (valor != null && valor < 0) {
            throw new IllegalArgumentException("El valor debe ser mayor de 0.");
        }
        return valor;
    }

    private void configurarComboBox() {
        String nombre;

        try {
            //Obtenemos los articulos en una lista observable
            Collection<Articulo> articulos = factoriaArticulos.acceso().getAllArticulos();
            if (articulos == null || articulos.isEmpty()) {
                LOGGER.warning("No se encontraron articulos.");
                articulos = new ArrayList<>();
            }

            //Obtenemos los nombres en una lista observable
            nombresDisponibles = FXCollections.observableArrayList();
            for (Articulo articulo : articulos) {
                nombre = articulo.getNombre();
                nombresDisponibles.add(nombre);
            }

            comboBoxNombre.setItems(nombresDisponibles);

            LOGGER.info("ComboBox de nombres configurado correctamente.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar nombres para el ComboBox", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error al cargar los articulos", "No se pudieron cargar los articulos. Intente nuevamente más tarde.");
        }
    }

}
