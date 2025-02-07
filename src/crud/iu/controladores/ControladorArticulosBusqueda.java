package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
import java.net.ConnectException;
import java.net.URL;
import java.time.LocalDate;
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
import javax.ws.rs.ProcessingException;

/**
 * Controlador para la ventana de búsqueda de Artículos.
 * <p>
 * Permite aplicar distintos filtros (ID, nombre, fecha, precio, stock) y
 * mostrar los resultados de la búsqueda en la vista principal de Artículos.
 * Además, maneja la navegación para regresar o reiniciar los filtros.
 *
 * @author Sergio
 */
public class ControladorArticulosBusqueda implements Initializable {

    /**
     * Logger para la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosBusqueda.class.getName());

    /**
     * Factoría encargada de gestionar la lógica de Artículos (acceso a datos,
     * vistas, etc.).
     */
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();

    /**
     * Factoría para la gestión de Usuarios.
     */
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();

    /**
     * Lista de artículos observable para el manejo en la UI (si fuera
     * necesario).
     */
    private ObservableList<Articulo> articulosObservableList;

    /**
     * Escenario principal de la aplicación.
     */
    private Stage stage;

    /**
     * Colección de artículos provenientes de una búsqueda previa (opcional).
     */
    private Collection<Articulo> articuloBusqueda;

    /**
     * Usuario genérico (opcional) que podría interactuar con la aplicación.
     */
    private Usuario usuario;

    /**
     * Botón para retroceder a la vista principal de Artículos.
     */
    @FXML
    private Button botonAtras;

    /**
     * Botón para reiniciar los filtros de búsqueda.
     */
    @FXML
    private Button botonReiniciar;

    /**
     * Botón para ejecutar la búsqueda con los filtros seleccionados.
     */
    @FXML
    private Button botonBuscar;

    /**
     * Spinner (control numérico) para establecer el rango inicial de ID.
     */
    @FXML
    private Spinner<Long> spinnerIdDesde;

    /**
     * Spinner (control numérico) para establecer el rango final de ID.
     */
    @FXML
    private Spinner<Long> spinnerIdHasta;

    /**
     * Selector de fecha inicial para filtrar por fecha de reposición.
     */
    @FXML
    private DatePicker datePickerDesde;

    /**
     * Selector de fecha final para filtrar por fecha de reposición.
     */
    @FXML
    private DatePicker datePickerHasta;

    /**
     * Campo de texto para el precio mínimo a filtrar.
     */
    @FXML
    private TextField textFieldPrecioMin;

    /**
     * Campo de texto para el precio máximo a filtrar.
     */
    @FXML
    private TextField textFieldPrecioMax;

    /**
     * Campo de texto para filtrar según stock exacto.
     */
    @FXML
    private TextField textFieldStock;

    /**
     * ComboBox que permite elegir el nombre del artículo a filtrar.
     */
    @FXML
    private ComboBox<String> comboBoxNombre;

    /**
     * CheckBox para indicar que se desea filtrar por ID.
     */
    @FXML
    private CheckBox checkBoxId;

    /**
     * CheckBox para indicar que se desea filtrar por Nombre.
     */
    @FXML
    private CheckBox checkBoxNombre;

    /**
     * CheckBox para indicar que se desea filtrar por Fecha.
     */
    @FXML
    private CheckBox checkBoxFecha;

    /**
     * CheckBox para indicar que se desea filtrar por Precio.
     */
    @FXML
    private CheckBox checkBoxPrecio;

    /**
     * CheckBox para indicar que se desea filtrar por Stock.
     */
    @FXML
    private CheckBox checkBoxStock;

    /**
     * Lista observable de nombres disponibles para el ComboBox.
     */
    private ObservableList<String> nombresDisponibles;

    /**
     * Trabajador que está usando la aplicación (se asigna si está disponible).
     */
    private Trabajador userTrabajador;

    /**
     * Configura la escena principal y muestra la ventana.
     *
     * @param root Nodo raíz de la vista.
     * @throws java.lang.Exception Cualquier excepcion que se genere fuera del
     * try lo tratara la factoria
     */
    public void initStage(Parent root) throws Exception {

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Busqueda");
        LOGGER.info("Inicializando la escena principal");
        try {
            botonBuscar.addEventHandler(ActionEvent.ACTION, this::handleBuscar);
            botonReiniciar.addEventHandler(ActionEvent.ACTION, this::handleReiniciarFiltros);
            botonAtras.addEventHandler(ActionEvent.ACTION, this::handleAtras);

            stage.show();  // Mostrar el escenario
            stage.centerOnScreen();
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
            if (e instanceof ConnectException || e instanceof ProcessingException) {

                FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
            } else {
                throw e;
            }

        }
    }

    /**
     * Método llamado automáticamente al cargar el FXML.
     *
     * @param url Localización del FXML (no se usa en este caso).
     * @param rb Recursos de localización (no se usan en este caso).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosBusqueda");
        configurarSpinners();
        configurarComboBox();
    }

    /**
     * Asigna el usuario (Trabajador) que está utilizando la aplicación.
     *
     * @param user Objeto Trabajador o similar.
     */
    public void setUser(Object user) {
        if (user != null) {
            this.userTrabajador = new Trabajador();
            this.userTrabajador = (Trabajador) user;
            LOGGER.info("Usuario asignado: " + userTrabajador.getNombre());
        }
    }

    /**
     * Asigna la colección de artículos previamente obtenidos en otra búsqueda.
     *
     * @param articuloBusqueda Colección de Articulos.
     */
    public void setBusqueda(Collection<Articulo> articuloBusqueda) {
        this.articuloBusqueda = articuloBusqueda;
    }

    /**
     * Asigna el escenario principal de la aplicación.
     *
     * @param stage Escenario (Stage) en JavaFX.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Configura los spinners de ID (rango desde / hasta).
     */
    private void configurarSpinners() {
        configurarSpinnerIdDesde();
        configurarSpinnerIdHasta();
    }

    /**
     * Reinicia todos los filtros (controles) a sus valores por defecto.
     *
     * @param event Evento de la acción (clic en el botón).
     */
    @FXML
    private void handleReiniciarFiltros(ActionEvent event) {
        spinnerIdDesde.getValueFactory().setValue(0L);
        spinnerIdHasta.getValueFactory().setValue(0L);
        comboBoxNombre.setValue(null);
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

    /**
     * Asigna una fecha (Date) a un DatePicker (LocalDate).
     *
     * @param datePicker DatePicker de la interfaz.
     * @param fecha Fecha tipo Date que se quiere asignar.
     */
    private void setDatePickerDate(DatePicker datePicker, Date fecha) {
        if (fecha != null) {
            datePicker.setValue(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            datePicker.setValue(null);
        }
    }

    /**
     * Retorna a la vista principal de Artículos sin aplicar filtros.
     *
     * @param event Evento de la acción (clic en el botón).
     */
    @FXML
    private void handleAtras(ActionEvent event) {
        LOGGER.info("Regresando a ArticulosPrincipal.");
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, null);
    }

    /**
     * Ejecuta la búsqueda de Artículos aplicando los filtros seleccionados.
     *
     * @param event Evento de la acción (clic en el botón).
     */
    @FXML
    private void handleBuscar(ActionEvent event) {
        try {
            Collection<Articulo> articulosFiltrados = new ArrayList<>(factoriaArticulos.acceso().getAllArticulos());

            // Filtro por ID
            if (checkBoxId.isSelected()) {
                Long idDesde = spinnerIdDesde.getValue();
                Long idHasta = spinnerIdHasta.getValue();
                articulosFiltrados.removeIf(articulo
                        -> articulo.getId().intValue() < idDesde || articulo.getId().intValue() > idHasta);
            }

            // Filtro por Nombre
            if (checkBoxNombre.isSelected()) {
                String nombre = comboBoxNombre.getValue();
                articulosFiltrados.removeIf(articulo -> !articulo.getNombre().equalsIgnoreCase(nombre));
            }

            // Filtro por Fecha
            if (checkBoxFecha.isSelected()) {
                Date fechaDesde = convertToDate(datePickerDesde.getValue());
                Date fechaHasta = convertToDate(datePickerHasta.getValue());
                articulosFiltrados.removeIf(articulo -> articulo.getFechaReposicion().before(fechaDesde)
                        || articulo.getFechaReposicion().after(fechaHasta));
            }

            // Filtro por Precio
            if (checkBoxPrecio.isSelected()) {
                Double precioMin = parseDouble(textFieldPrecioMin.getText());
                Double precioMax = parseDouble(textFieldPrecioMax.getText());
                articulosFiltrados.removeIf(articulo -> articulo.getPrecio() < precioMin || articulo.getPrecio() > precioMax);
            }

            // Filtro por Stock
            if (checkBoxStock.isSelected()) {
                Integer stock = validarStock(parseInteger(textFieldStock.getText()));
                articulosFiltrados.removeIf(articulo -> articulo.getStock() != stock);
            }

            mostrarResultados(articulosFiltrados);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al realizar la búsqueda", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error de Búsqueda",
                    "No se pudo completar la búsqueda. Intente nuevamente.");
        }
    }

    /**
     * Muestra los resultados filtrados regresando a la vista principal.
     *
     * @param articulosFiltrados Colección de Articulos que cumplen el filtro.
     */
    private void mostrarResultados(Collection<Articulo> articulosFiltrados) {
        factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, articulosFiltrados);
    }

    /**
     * Convierte un LocalDate en Date.
     *
     * @param localDate Objeto LocalDate a convertir.
     * @return Fecha en formato Date o null si localDate es null.
     */
    private Date convertToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Configura el Spinner para el ID "desde".
     */
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

    /**
     * Configura el Spinner para el ID "hasta".
     */
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

    /**
     * Convierte la cadena de texto a Double.
     *
     * @param valor Cadena a convertir.
     * @return Valor Double o null si no se puede parsear.
     */
    private Double parseDouble(String valor) {
        try {
            return valor != null && !valor.isEmpty() ? Double.parseDouble(valor) : null;
        } catch (NumberFormatException e) {
            LOGGER.warning("Error al parsear Double: " + valor);
            return null;
        }
    }

    /**
     * Convierte la cadena de texto a Integer.
     *
     * @param valor Cadena a convertir.
     * @return Valor Integer o null si no se puede parsear.
     */
    private Integer parseInteger(String valor) {
        try {
            return valor != null && !valor.trim().isEmpty() ? Integer.parseInt(valor.trim()) : null;
        } catch (NumberFormatException e) {
            LOGGER.warning("Error al parsear Integer: " + valor + " Asegúrate de que sea un número válido.");
            return null;
        }
    }

    /**
     * Valida que el stock sea mayor o igual a 0.
     *
     * @param valor Valor de stock a validar.
     * @return El mismo valor si es válido.
     * @throws IllegalArgumentException si el valor es menor que 0.
     */
    private Integer validarStock(Integer valor) {
        if (valor != null && valor < 0) {
            throw new IllegalArgumentException("El valor debe ser mayor de 0.");
        }
        return valor;
    }

    /**
     * Configura el ComboBox para mostrar los distintos nombres de Artículos.
     */
    private void configurarComboBox() {
        String nombre;
        try {
            // Obtenemos los artículos desde la factoría
            Collection<Articulo> articulos = factoriaArticulos.acceso().getAllArticulos();
            if (articulos == null || articulos.isEmpty()) {
                LOGGER.warning("No se encontraron articulos.");
                articulos = new ArrayList<>();
            }

            // Obtenemos los nombres en una lista observable
            nombresDisponibles = FXCollections.observableArrayList();
            for (Articulo articulo : articulos) {
                nombre = articulo.getNombre();
                nombresDisponibles.add(nombre);
            }

            comboBoxNombre.setItems(nombresDisponibles);
            LOGGER.info("ComboBox de nombres configurado correctamente.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar nombres para el ComboBox", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR,
                    "Error al cargar los articulos",
                    "No se pudieron cargar los articulos. Intente nuevamente más tarde.");
        }
    }

    private void mostrarAyuda() {
        factoriaUsuarios.cargarAyuda("articulosBusqueda");
    }

}
