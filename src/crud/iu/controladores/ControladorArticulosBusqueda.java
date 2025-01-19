package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
    private Spinner<Double> spinnerPrecioMin;
    @FXML
    private Spinner<Double> spinnerPrecioMax;
    @FXML
    private Spinner<Integer> spinnerStock;
    @FXML
    private ComboBox<String> comboBoxNombre;

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
        configurarSpinnerPrecioMin();
        configurarSpinnerPrecioMax();
        configurarSpinnerStock();

    }

    //Eventos
    @FXML
    private void handleReiniciarFiltros(ActionEvent event) {
        spinnerIdDesde.getValueFactory().setValue(0L);
        spinnerIdHasta.getValueFactory().setValue(Long.MAX_VALUE);
        comboBoxNombre.setValue(null);
        datePickerDesde.setValue(null);
        datePickerHasta.setValue(null);
        spinnerPrecioMin.getValueFactory().setValue(0.0);
        spinnerPrecioMax.getValueFactory().setValue(Double.MAX_VALUE);
        spinnerStock.getValueFactory().setValue(0);
        LOGGER.info("Filtros reiniciados.");

    }

    @FXML
    private void handleAtras(ActionEvent event) {
        LOGGER.info("Regresando a ArticulosPrincipal.");
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador);

    }

    @FXML
    private void handleBuscar(ActionEvent event) {

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
                value = Math.min(Long.MAX_VALUE, value + steps);
                setValue(value);
            }
        });
        spinnerIdDesde.getValueFactory().setValue(0L);
    }

    private void configurarSpinnerIdHasta() {
        spinnerIdHasta.setValueFactory(new SpinnerValueFactory<Long>() {
            private long value = Long.MAX_VALUE;

            @Override
            public void decrement(int steps) {
                value = Math.max(0, value - steps);
                setValue(value);
            }

            @Override
            public void increment(int steps) {
                value = Math.min(Long.MAX_VALUE, value + steps);
                setValue(value);
            }
        });
        spinnerIdHasta.getValueFactory().setValue(Long.MAX_VALUE);
    }

    private void configurarSpinnerPrecioMin() {
        spinnerPrecioMin.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 0.01));
    }

    private void configurarSpinnerPrecioMax() {
        spinnerPrecioMax.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, Double.MAX_VALUE, 0.01));
    }

    private void configurarSpinnerStock() {
        spinnerStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
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
