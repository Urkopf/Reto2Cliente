package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.PedidoArticulo;
import crud.objetosTransferibles.Trabajador;
import crud.utilidades.AlertUtilities;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
import javax.ws.rs.ProcessingException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Controlador principal de la vista de Artículos.
 * <p>
 * Permite la creación, edición y eliminación de artículos, así como la
 * impresión de informes y navegación hacia otras vistas (Menú principal,
 * Búsqueda, Detalles, Pedidos, etc.).
 *
 * @author Sergio
 */
public class ControladorArticulosPrincipal implements Initializable {

    /**
     * Logger para la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosPrincipal.class.getName());

    /**
     * Factoría para la lógica de negocio referente a Artículos.
     */
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();

    /**
     * Factoría para la gestión de Usuarios (ej. carga de menús, ayudas, etc.).
     */
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();

    /**
     * Factoría para la gestión de Pedidos.
     */
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();

    /**
     * Escenario principal donde se mostrará la vista.
     */
    private Stage stage = new Stage();

    /**
     * Trabajador que ha iniciado sesión y está utilizando la aplicación.
     */
    private Trabajador userTrabajador;

    /**
     * Lista observable de Artículos que se muestran en la tabla.
     */
    private ObservableList<Articulo> articulosObservableList;

    /**
     * Número de filas que se mostrarán por página en la tabla de artículos.
     */
    private static final int FILAS_POR_PAGINA = 14;

    /**
     * Lista de artículos resultante de una búsqueda previa, para mostrar en la
     * tabla. Si es nula, se cargarán todos los artículos de la base de datos.
     */
    private Collection<Articulo> listaBusqueda;

    /**
     * Botón para crear un artículo nuevo.
     */
    @FXML
    private Button botonNuevo;

    /**
     * Botón para reiniciar la tabla y recargar los datos originales.
     */
    @FXML
    private Button botonReiniciar;

    /**
     * Botón para acceder a la vista de búsqueda de artículos.
     */
    @FXML
    private Button botonBusqueda;

    /**
     * Tabla principal donde se listan los artículos.
     */
    @FXML
    private TableView<Articulo> tablaArticulos;

    /**
     * Columna que muestra el ID del artículo (no editable).
     */
    @FXML
    private TableColumn<Articulo, Long> columnaId;

    /**
     * Columna que muestra y permite editar el nombre del artículo.
     */
    @FXML
    private TableColumn<Articulo, String> columnaNombre;

    /**
     * Columna que muestra y permite editar el precio del artículo.
     */
    @FXML
    private TableColumn<Articulo, Double> columnaPrecio;

    /**
     * Columna que muestra y permite editar la fecha de reposición del artículo.
     */
    @FXML
    private TableColumn<Articulo, Date> columnaFecha;

    /**
     * Columna que muestra y permite editar la descripción del artículo.
     */
    @FXML
    private TableColumn<Articulo, String> columnaDescripcion;

    /**
     * Columna que muestra y permite editar el stock del artículo.
     */
    @FXML
    private TableColumn<Articulo, Integer> columnaStock;

    /**
     * Botón para regresar al Menú principal.
     */
    @FXML
    private Button botonAtras;

    /**
     * Botón para eliminar uno o varios artículos seleccionados.
     */
    @FXML
    private Button botonEliminar;

    /**
     * Botón para ver el detalle de un artículo seleccionado.
     */
    @FXML
    private Button botonDetalles;

    /**
     * Botón para guardar los cambios realizados en la tabla.
     */
    @FXML
    private Button botonGuardar;

    /**
     * Paginador para dividir los artículos por páginas en la tabla.
     */
    @FXML
    private Pagination paginador;

    /**
     * Contenedor principal (AnchorPane) de la vista.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * Copia de seguridad de los artículos originales para detectar cambios no
     * guardados.
     */
    private ObservableList<Articulo> articulosOriginales;

    /**
     * Bandera que indica si hay cambios sin guardar.
     */
    private boolean hayCambiosNoGuardados = false;

    /**
     * Método llamado al inicializar la vista de JavaFX.
     *
     * @param url No se usa en este caso.
     * @param rb Recursos de localización (no se usan).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosPrincipal");
        configurarTabla();
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
     * @param stage El escenario de JavaFX donde se mostrará esta vista.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

    /**
     * Asigna la lista de artículos resultante de una búsqueda (opcional). Si es
     * nula, se cargarán todos los artículos disponibles.
     *
     * @param lista Colección de Articulos proveniente de una búsqueda.
     */
    public void setBusqueda(Collection<Articulo> lista) {
        this.listaBusqueda = lista;
        LOGGER.info("Lista Busqueda asignada. Elementos: "
                + ((lista != null) ? lista.size() : "Null"));
    }

    /**
     * Inicializa la escena (Stage) con la vista especificada, configura los
     * menús y botones, y finalmente muestra la ventana.
     *
     * @param root Nodo raíz (Parent) cargado desde el FXML.
     * @throws java.lang.Exception Cualquier excepcion que se genere fuera del
     * try lo tratara la factoria
     */
    public void initStage(Parent root) throws Exception {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos");
        LOGGER.info("Inicializando la escena principal");
        try {
            configurarMenu();

            botonNuevo.addEventHandler(ActionEvent.ACTION, this::handleNuevoArticulo);
            botonGuardar.addEventHandler(ActionEvent.ACTION, this::handleGuardarCambios);
            botonAtras.addEventHandler(ActionEvent.ACTION, this::handleAtras);
            botonReiniciar.addEventHandler(ActionEvent.ACTION, this::handleRecargarTabla);
            botonEliminar.addEventHandler(ActionEvent.ACTION, this::handleEliminarArticulo);
            botonBusqueda.addEventHandler(ActionEvent.ACTION, this::handleBusqueda);
            botonDetalles.addEventHandler(ActionEvent.ACTION, this::handleDetalle);

            // Listener para habilitar o deshabilitar botones según la selección en la tabla
            tablaArticulos.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Articulo>) change -> {
                actualizarEstadoBotones();
            });
            configureMnemotecnicKeys();
            cargarDatosArticulos();
            configurarPaginador();
            stage.show();
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
     * Configura las teclas de acceso rápido para los botones de iniciar sesión
     * y registrar.
     */
    private void configureMnemotecnicKeys() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.N) {
                botonNuevo.fire();  // Simula el clic en el botón Nuevo
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonReiniciar.fire();  // Simula el clic en el boton reiniciar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.B) {
                botonBusqueda.fire();  // Simula el clic botom busqueda
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.A) {
                botonAtras.fire();  // Simula el clic en el boton atras
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.E) {
                botonEliminar.fire();  // Simula el clic en el boton eliminar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.D) {
                botonDetalles.fire();  // Simula el clic en el boton detalles
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.G) {
                botonGuardar.fire();  // Simula el clic en el boton guardar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.LEFT) {
                int currentPage = paginador.getCurrentPageIndex();
                if (currentPage > 0) {
                    paginador.setCurrentPageIndex(currentPage - 1);
                    event.consume();
                }
            } else if (event.isAltDown() && event.getCode() == KeyCode.RIGHT) {
                int currentPage = paginador.getCurrentPageIndex();
                if (currentPage < paginador.getPageCount() - 1) {
                    paginador.setCurrentPageIndex(currentPage + 1);
                    event.consume();
                }
            }

        });
    }

    /**
     * Configura el paginador con base en el tamaño de la lista de artículos y
     * establece la página inicial.
     */
    private void configurarPaginador() {
        int numeroPaginas = (int) Math.ceil((double) articulosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);

        paginador.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            actualizarPagina(newIndex.intValue());
        });

        actualizarPagina(0);
    }

    /**
     * Actualiza la tabla para mostrar los artículos correspondientes a la
     * página indicada.
     *
     * @param indicePagina Índice 0-based de la página a mostrar.
     */
    private void actualizarPagina(int indicePagina) {
        int desdeIndice = indicePagina * FILAS_POR_PAGINA;
        int hastaIndice = Math.min(desdeIndice + FILAS_POR_PAGINA, articulosObservableList.size());

        if (desdeIndice <= hastaIndice) {
            ObservableList<Articulo> pagina = FXCollections.observableArrayList(
                    articulosObservableList.subList(desdeIndice, hastaIndice));
            tablaArticulos.setItems(pagina);
        }
    }

    /**
     * Refresca la tabla y recalcula el paginador para reflejar la lista actual.
     */
    private void actualizarTablaYPaginador() {
        tablaArticulos.setItems(articulosObservableList);
        int numeroPaginas = (int) Math.ceil((double) articulosObservableList.size() / FILAS_POR_PAGINA);
        paginador.setPageCount(numeroPaginas);
        paginador.setCurrentPageIndex(0);
        actualizarPagina(0);
    }

    /**
     * Configura la barra de menú superior, asignando acciones a cada ítem de
     * menú (Imprimir, Cerrar sesión, Salir, etc.).
     */
    public void configurarMenu() {
        BorderPane borderPane = (BorderPane) anchorPane.getChildrenUnmodifiable().get(0);
        MenuBar menuBar = (MenuBar) borderPane.getTop();

        Menu menuPrincipal = menuBar.getMenus().get(0);
        Menu menuIr = menuBar.getMenus().get(1);
        Menu menuAyuda = menuBar.getMenus().get(2);

        MenuItem opcionImprimir = menuPrincipal.getItems().get(0);
        opcionImprimir.setOnAction(event -> imprimirInforme());

        MenuItem opcionCerrarSesion = menuPrincipal.getItems().get(1);
        opcionCerrarSesion.setOnAction(event -> cerrarSesion());

        MenuItem opcionSalir = menuPrincipal.getItems().get(2);
        opcionSalir.setOnAction(event -> salirPrograma());

        MenuItem opcionVolver = menuPrincipal.getItems().get(3);
        opcionVolver.setOnAction(event -> volverAlMenuPrincipal());

        MenuItem opcionIrPedidos = menuIr.getItems().get(0);
        opcionIrPedidos.setVisible(true);
        opcionIrPedidos.setOnAction(event -> irVistaPedidos());

        MenuItem opcionIrArticulos = menuIr.getItems().get(1);
        opcionIrArticulos.setVisible(false);

        MenuItem botonAyuda = menuAyuda.getItems().get(0);
        botonAyuda.setOnAction(event -> {
            mostrarAyuda();
        });
    }

    /**
     * Lógica para imprimir informe de artículos (usa JasperReports).
     */
    private void imprimirInforme() {
        System.out.println("Imprimiendo informe...");
        crearInforme();
    }

    /**
     * Cierra la sesión actual (cierra la ventana).
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
     * Crea e imprime un informe (reporte) de los artículos que se están
     * mostrando en la tabla, utilizando JasperReports.
     */
    public void crearInforme() {
        try {
            LOGGER.info("Beginning printing action...");
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/crud/iu/reportes/ArticulosReport.jrxml"));

            // Cargar la imagen desde el classpath
            InputStream logoStream = getClass().getResourceAsStream("/recursos/logoFullrecortado.jpg");
            if (logoStream == null) {
                throw new RuntimeException("No se pudo cargar la imagen del logo desde el JAR.");
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_PATH", logoStream);

            // Crear los datos del informe
            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<Articulo>) this.tablaArticulos.getItems());

            // Llenar el informe
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            // Mostrar el informe en una ventana
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            LOGGER.log(Level.SEVERE,
                    "UI GestionUsuariosController: Error printing report: {0}",
                    ex.getMessage());
            ExcepcionesUtilidad.centralExcepciones(ex, ex.getMessage());

        }
    }

    /**
     * Configura la tabla de artículos (desactiva ordenación, resizable, etc.) y
     * asigna el modo de selección múltiple. Configura también cada columna para
     * que sea editable cuando corresponda.
     */
    private void configurarTabla() {
        tablaArticulos.setEditable(true);

        tablaArticulos.getColumns().forEach(column -> column.setSortable(false));
        tablaArticulos.getColumns().forEach(column -> column.setResizable(false));
        tablaArticulos.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarEdicionNombre(columnaNombre);

        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        configurarEdicionPrecio(columnaPrecio);

        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReposicion"));
        configurarEdicionFecha(columnaFecha);

        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        configurarEdicionDescripcion(columnaDescripcion);

        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        configurarEdicionStock(columnaStock);
    }

    /**
     * Actualiza el estado de los botones "Detalles" y "Eliminar" según la
     * cantidad de elementos seleccionados en la tabla.
     */
    private void actualizarEstadoBotones() {
        int numSeleccionados = tablaArticulos.getSelectionModel().getSelectedItems().size();
        botonDetalles.setDisable(numSeleccionados != 1);
        botonEliminar.setDisable(numSeleccionados == 0);
    }

    /**
     * Cancela la edición en caso de que haya una celda en modo edición.
     */
    private void cancelarEdicionEnTabla() {
        if (tablaArticulos.getEditingCell() != null) {
            tablaArticulos.edit(-1, null);
        }
    }

    /**
     * Carga los datos de artículos desde la factoría. Si existe una lista de
     * búsqueda previa, se usa esa lista en lugar de la consulta general.
     */
    private void cargarDatosArticulos() throws Exception {
        Collection<Articulo> articulos = null;

        LOGGER.info("Cargando datos de articulos...");
        if (listaBusqueda == null) {
            articulos = factoriaArticulos.acceso().getAllArticulos();
        } else {
            articulos = listaBusqueda;
        }

        if (articulos == null || articulos.isEmpty()) {
            LOGGER.warning("No se encontraron articulos.");
            articulos = new ArrayList<>();
        }
        articulosObservableList = FXCollections.observableArrayList(articulos);
        tablaArticulos.getItems().clear();
        tablaArticulos.setItems(articulosObservableList);

        articulosOriginales = FXCollections.observableArrayList(
                articulos.stream().map(Articulo::clone).collect(Collectors.toList()));

        actualizarTablaYPaginador();
        actualizarEstadoBotones();
        setHayCambiosNoGuardados(false);
        tablaArticulos.refresh();

    }

    /**
     * Maneja la acción de crear un nuevo artículo con valores por defecto
     * (precio=0, fecha actual, stock=0).
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleNuevoArticulo(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Nuevo Articulo presionado");
        Articulo nuevoArticulo = new Articulo();
        nuevoArticulo.setNombre("Aqui el nombre");
        nuevoArticulo.setDescripcion("Aqui descripcion");
        nuevoArticulo.setPrecio(0);
        nuevoArticulo.setFechaReposicion(new Date());
        nuevoArticulo.setStock(1);

        articulosObservableList.add(nuevoArticulo);
        setHayCambiosNoGuardados(true);

        int indicePagina = (articulosObservableList.size() - 1) / FILAS_POR_PAGINA;
        paginador.setCurrentPageIndex(indicePagina);

        actualizarPagina(indicePagina);
        tablaArticulos.scrollTo(nuevoArticulo);
        LOGGER.info("Nuevo articulo añadido con valores predeterminados.");
    }

    /**
     * Maneja la acción de guardar los cambios realizados en la tabla.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        guardarCambios();
        recargarTabla();
    }

    /**
     * Procesa el guardado de cambios: crea nuevos artículos, actualiza los
     * existentes y elimina los que hayan sido removidos de la tabla.
     */
    private void guardarCambios() {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Guardar Cambios presionado");

        boolean hayErrores = false;
        for (Articulo articulo : articulosObservableList) {
            if (!validarArticulo(articulo)) {
                hayErrores = true;
            }
        }

        if (hayErrores) {
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Guardar Cambios",
                    "Hay errores en algunos campos. Corríjalos antes de guardar.");
            return;
        }

        try {
            for (Articulo articulo : articulosObservableList) {
                LOGGER.info("Revisando articulos" + articulo.getId());
                Articulo articuloOriginal = articulosOriginales.stream()
                        .filter(p -> p.getId() != null && p.getId().equals(articulo.getId()))
                        .findFirst()
                        .orElse(null);

                if (articulo.getId() == null) {
                    LOGGER.info("Creando articulo.");
                    factoriaArticulos.acceso().crearArticulo(articulo);
                } else if (articuloOriginal != null && haCambiado(articuloOriginal, articulo)) {
                    LOGGER.info("Actualizando articulo: " + articulo);
                    factoriaArticulos.acceso().actualizarArticulo(articulo);
                }
            }

            for (Articulo articuloOriginal : articulosOriginales) {
                if (!articulosObservableList.contains(articuloOriginal)) {
                    LOGGER.info("Eliminando articulo.");
                    factoriaArticulos.acceso().borrarArticulo(articuloOriginal);
                }
            }

            articulosOriginales.setAll(articulosObservableList);
            LOGGER.info("Cambios guardados exitosamente.");
            reiniciar();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar cambios", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error",
                    "No se pudieron guardar los cambios. Intentelo de nuevo.");
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Determina si hay cambios en un artículo comparado con la versión
     * original.
     *
     * @param original Artículo original (antes de modificaciones).
     * @param modificado Artículo después de las modificaciones.
     * @return true si existe alguna diferencia, false en caso contrario.
     */
    public boolean haCambiado(Articulo original, Articulo modificado) {
        if (original == null || modificado == null) {
            return false;
        }
        if (!original.getNombre().equals(modificado.getNombre())) {
            return true;
        }
        if (original.getPrecio() != modificado.getPrecio()) {
            return true;
        }
        if (!original.getFechaReposicion().equals(modificado.getFechaReposicion())) {
            return true;
        }
        if (!original.getDescripcion().equals(modificado.getDescripcion())) {
            return true;
        }
        return original.getStock() != modificado.getStock();
    }

    /**
     * Valida los campos de un artículo (nombre, precio, fecha, descripción,
     * stock).
     *
     * @param articulo Artículo a validar.
     * @return true si todos los campos son válidos, false en caso contrario.
     */
    private boolean validarArticulo(Articulo articulo) {
        boolean valido = true;

        if (articulo.getNombre() == null || articulo.getNombre().isEmpty()) {
            pintarCeldaInvalida(columnaNombre, articulo);
            valido = false;
        }
        if (articulo.getPrecio() <= 0) {
            pintarCeldaInvalida(columnaPrecio, articulo);
            valido = false;
        }
        if (articulo.getFechaReposicion() == null) {
            pintarCeldaInvalida(columnaFecha, articulo);
            valido = false;
        }
        if (articulo.getDescripcion() == null || articulo.getDescripcion().isEmpty()) {
            pintarCeldaInvalida(columnaDescripcion, articulo);
            valido = false;
        }
        if (articulo.getStock() <= 0) {
            pintarCeldaInvalida(columnaStock, articulo);
            valido = false;
        }

        return valido;
    }

    /**
     * Pinta la celda con fondo rojo para indicar un valor inválido.
     *
     * @param <T> Tipo de la columna.
     * @param columna Columna en la que se ha detectado un error.
     * @param articulo Artículo asociado a la fila con error.
     */
    private <T> void pintarCeldaInvalida(TableColumn<Articulo, T> columna, Articulo articulo) {
        columna.setCellFactory(column -> {
            return new TableCell<Articulo, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && articulo.equals(getTableView().getItems().get(getIndex()))) {
                        setStyle("-fx-background-color: red;");
                    } else {
                        setStyle("");
                    }
                }
            };
        });
    }

    /**
     * Maneja la acción de eliminar uno o varios artículos seleccionados en la
     * tabla.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleEliminarArticulo(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Eliminar presionado");
        ObservableList<Articulo> seleccionados = tablaArticulos.getSelectionModel().getSelectedItems();
        if (seleccionados.isEmpty()) {
            AlertUtilities.showErrorDialog(Alert.AlertType.WARNING, "Eliminar Articulos",
                    "Debe seleccionar al menos un articulo para eliminar.");
        } else {
            try {
                for (Articulo articulo : seleccionados) {
                    Collection<PedidoArticulo> coleccion = FactoriaPedidoArticulo.getInstance().acceso().getAllPedidoArticulo();
                    ObservableList<PedidoArticulo> lista = FXCollections.observableArrayList(coleccion);
                    lista = FXCollections.observableArrayList(lista.stream()
                            .filter(p -> p.getPedidoId().equals(articulo.getId()))
                            .collect(Collectors.toList()));

                    if (lista.size() == 0) {
                        articulosObservableList.removeAll(seleccionados);
                        setHayCambiosNoGuardados(true);
                        LOGGER.info("Articulos eliminados de la tabla.");

                    } else {
                        AlertUtilities.showErrorDialog(Alert.AlertType.WARNING, "Detalles de Articulo",
                                "No puede borrar el articulo, porque esta en un pedido.");
                    }
                }

                actualizarTablaYPaginador();

            } catch (Exception ex) {
                ExcepcionesUtilidad.centralExcepciones(ex, ex.getMessage());
            }

        }
    }

    /**
     * Maneja la acción de recargar la tabla. Si hay cambios sin guardar,
     * confirma con el usuario si desea guardarlos o no.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleRecargarTabla(ActionEvent event) {
        if (!hayCambiosNoGuardados) {
            recargarTabla();
        } else {
            confirmarCambiosSinGuardar(this::recargarTabla);
        }
    }

    /**
     * Lógica interna para recargar la tabla, descartando la lista de búsqueda y
     * volviendo a cargar los datos originales.
     */
    private void recargarTabla() {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Reiniciar Tabla presionado");
        reiniciar();
        LOGGER.info("Tabla reiniciada a los datos originales.");
    }

    /**
     * Restablece la tabla a su estado original, descartando cualquier búsqueda
     * y recargando los datos desde la base.
     */
    private void reiniciar() {
        try {
            listaBusqueda = null;

            cargarDatosArticulos();
            setHayCambiosNoGuardados(false);
        } catch (Exception e) {
            if (e instanceof ConnectException || e instanceof ProcessingException) {

                FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
            }
        }

    }

    /**
     * Maneja la acción de volver al menú principal. Si hay cambios sin guardar,
     * se confirmará previamente con el usuario.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleAtras(ActionEvent event) {
        if (!hayCambiosNoGuardados) {
            atras();
        } else {
            confirmarCambiosSinGuardar(this::atras);
        }
    }

    /**
     * Lógica para volver al menú principal de la aplicación.
     */
    private void atras() {
        LOGGER.info("Regresando al menu principal.");
        factoriaUsuarios.cargarMenuPrincipal(stage, userTrabajador);
    }

    /**
     * Maneja la acción de abrir la vista de Búsqueda de artículos. Si hay
     * cambios sin guardar, se confirmará previamente con el usuario.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleBusqueda(ActionEvent event) {
        if (!hayCambiosNoGuardados) {
            busqueda();
        } else {
            confirmarCambiosSinGuardar(this::busqueda);
        }
    }

    /**
     * Lógica interna para cargar la vista de Búsqueda de artículos.
     */
    private void busqueda() {
        LOGGER.info("Vamos a la Busqueda...");
        factoriaArticulos.cargarArticulosBusqueda(stage, userTrabajador);
    }

    /**
     * Maneja la acción de ver los detalles de un artículo seleccionado. Si no
     * hay ninguno seleccionado, muestra un mensaje de advertencia.
     *
     * @param event Evento que dispara la acción.
     */
    @FXML
    private void handleDetalle(ActionEvent event) {
        cancelarEdicionEnTabla();
        LOGGER.info("Botón Detalles presionado");

        Articulo articuloSeleccionado = tablaArticulos.getSelectionModel().getSelectedItem();
        if (articuloSeleccionado == null) {
            LOGGER.warning("No se ha seleccionado ningún articulo.");
            AlertUtilities.showErrorDialog(Alert.AlertType.WARNING, "Detalles de Articulo",
                    "Debe seleccionar un articulo para ver los detalles.");
            return;
        }

        try {
            factoriaArticulos.cargarArticulosDetalle(stage, userTrabajador, articuloSeleccionado);
            LOGGER.info("Cargando detalles del articulo: " + articuloSeleccionado.getId());
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Muestra un diálogo de confirmación en caso de haber cambios sin guardar,
     * permitiendo guardar, descartar o cancelar la operación.
     *
     * @param accionAEjecutar Acción que se ejecutará luego de la elección (ej.
     * recargar tabla, ir atrás, etc.).
     */
    private void confirmarCambiosSinGuardar(Runnable accionAEjecutar) {
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
            return;
        } else if (result.get() == buttonGuardar) {
            guardarCambios();
            if (!hayCambiosNoGuardados) {
                accionAEjecutar.run();
            }
        } else if (result.get() == buttonSinGuardar) {
            accionAEjecutar.run();
        }
    }

    /**
     * Marca si hay cambios sin guardar en la tabla.
     *
     * @param hayCambios true si existen cambios, false en caso contrario.
     */
    private void setHayCambiosNoGuardados(boolean hayCambios) {
        this.hayCambiosNoGuardados = hayCambios;
    }

    /**
     * Configura la edición de la columna "Nombre" en la tabla.
     *
     * @param columnaNombre Columna que se hará editable.
     */
    private void configurarEdicionNombre(TableColumn<Articulo, String> columnaNombre) {
        columnaNombre.setCellFactory(tc -> new TableCell<Articulo, String>() {
            private final TextField textFieldNombre = new TextField();

            @Override
            public void startEdit() {
                super.startEdit();
                textFieldNombre.setText(getItem());
                setGraphic(textFieldNombre);
                setText(null);
                textFieldNombre.requestFocus();

                textFieldNombre.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textFieldNombre.getText());
                    }
                });

                textFieldNombre.setOnAction(event -> validarYCommit(textFieldNombre.getText()));
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
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setNombre(newValue);
                setHayCambiosNoGuardados(true);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        textFieldNombre.setText(item);
                        setGraphic(textFieldNombre);
                        setText(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(String newValue) {
                if (newValue == null || newValue.trim().isEmpty()) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Campo Obligatorio",
                            "El artículo no puede estar vacío");
                    cancelEdit();
                } else {
                    commitEdit(newValue);
                }
            }
        });
    }

    /**
     * Configura la edición de la columna "Precio" en la tabla.
     *
     * @param columnaPrecio Columna que se hará editable.
     */
    private void configurarEdicionPrecio(TableColumn<Articulo, Double> columnaPrecio) {
        columnaPrecio.setCellFactory(tc -> new TableCell<Articulo, Double>() {
            private final Spinner<Double> spinnerPrecio = new Spinner<>();

            @Override
            public void startEdit() {
                super.startEdit();
                SpinnerValueFactory<Double> valueFactory
                        = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10000.0, getItem(), 0.01);
                spinnerPrecio.setValueFactory(valueFactory);
                spinnerPrecio.setEditable(true);

                spinnerPrecio.getValueFactory().setValue(getItem());
                setGraphic(spinnerPrecio);
                setText(null);
                spinnerPrecio.requestFocus();

                spinnerPrecio.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(obtenerValorSpinner(spinnerPrecio));
                    }
                });

                spinnerPrecio.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        validarYCommit(obtenerValorSpinner(spinnerPrecio));
                    }
                });
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(formatPrecio(getItem()));
                setGraphic(null);
            }

            @Override
            public void commitEdit(Double newValue) {
                super.commitEdit(newValue);
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setPrecio(newValue);
                setHayCambiosNoGuardados(true);
            }

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        spinnerPrecio.getValueFactory().setValue(item);
                        setGraphic(spinnerPrecio);
                        setText(null);
                    } else {
                        setText(formatPrecio(item));
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(Double newValue) {
                if (newValue == null || newValue < 0) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido",
                            "El precio no puede ser negativo.");
                    cancelEdit();
                } else {
                    commitEdit(newValue);
                }
            }

            private Double obtenerValorSpinner(Spinner<Double> spinner) {
                try {
                    String input = spinner.getEditor().getText();
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido",
                            "Debe ingresar un número válido.");
                    return spinner.getValue();
                }
            }

            private String formatPrecio(Double precio) {
                return String.format("%.2f €", precio);
            }
        });
    }

    /**
     * Configura la edición de la columna "Fecha de Reposición" en la tabla.
     *
     * @param columnaFecha Columna que se hará editable.
     */
    private void configurarEdicionFecha(TableColumn<Articulo, Date> columnaFecha) {
        columnaFecha.setCellFactory(tc -> new TableCell<Articulo, Date>() {
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

                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        cancelEdit();
                    }
                });

                datePicker.setOnAction(event -> {
                    Date newDate = Date.from(datePicker.getValue()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant());

                    if (newDate.before(new Date()) && !newDate.equals(getItem())) {
                        AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Fecha inválida",
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
                setText(getItem() != null ? dateFormat.format(getItem()) : null);
                setGraphic(null);
            }

            @Override
            public void commitEdit(Date newValue) {
                super.commitEdit(newValue);
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setFechaReposicion(newValue);
                setHayCambiosNoGuardados(true);
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
     * Configura la edición de la columna "Descripción" en la tabla.
     *
     * @param columnaDescripcion Columna que se hará editable.
     */
    private void configurarEdicionDescripcion(TableColumn<Articulo, String> columnaDescripcion) {
        columnaDescripcion.setCellFactory(tc -> new TableCell<Articulo, String>() {
            private final TextField textFieldDescripcion = new TextField();

            @Override
            public void startEdit() {
                super.startEdit();
                textFieldDescripcion.setText(getItem());
                setGraphic(textFieldDescripcion);
                setText(null);
                textFieldDescripcion.requestFocus();

                textFieldDescripcion.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textFieldDescripcion.getText());
                    }
                });

                textFieldDescripcion.setOnAction(event -> validarYCommit(textFieldDescripcion.getText()));
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
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setDescripcion(newValue);
                setHayCambiosNoGuardados(true);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        textFieldDescripcion.setText(item);
                        setGraphic(textFieldDescripcion);
                        setText(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(String newValue) {
                if (newValue == null || newValue.trim().isEmpty()) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Campo Obligatorio",
                            "La descripción no puede estar vacía");
                    cancelEdit();
                } else {
                    commitEdit(newValue);
                }
            }
        });
    }

    /**
     * Configura la edición de la columna "Stock" en la tabla.
     *
     * @param columnaStock Columna que se hará editable.
     */
    private void configurarEdicionStock(TableColumn<Articulo, Integer> columnaStock) {
        columnaStock.setCellFactory(tc -> new TableCell<Articulo, Integer>() {
            private final Spinner<Integer> spinnerStock = new Spinner<>();

            @Override
            public void startEdit() {
                super.startEdit();
                SpinnerValueFactory<Integer> valueFactory
                        = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, getItem());
                spinnerStock.setValueFactory(valueFactory);
                spinnerStock.setEditable(true);

                spinnerStock.getValueFactory().setValue(getItem());
                setGraphic(spinnerStock);
                setText(null);
                spinnerStock.requestFocus();

                spinnerStock.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(obtenerValorSpinner(spinnerStock));
                    }
                });

                spinnerStock.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        validarYCommit(obtenerValorSpinner(spinnerStock));
                    }
                });
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(formatStock(getItem()));
                setGraphic(null);
            }

            @Override
            public void commitEdit(Integer newValue) {
                super.commitEdit(newValue);
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setStock(newValue);
                setHayCambiosNoGuardados(true);
            }

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        spinnerStock.getValueFactory().setValue(item);
                        setGraphic(spinnerStock);
                        setText(null);
                    } else {
                        setText(formatStock(item));
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(Integer newValue) {
                if (newValue == null || newValue < 0) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido",
                            "El stock no puede ser negativo.");
                    cancelEdit();
                } else {
                    commitEdit(newValue);
                }
            }

            private Integer obtenerValorSpinner(Spinner<Integer> spinner) {
                try {
                    String input = spinner.getEditor().getText();
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido",
                            "Debe ingresar un número entero.");
                    return spinner.getValue();
                }
            }

            private String formatStock(Integer stock) {
                return stock + " unid";
            }
        });
    }

    /**
     * Muestra la ayuda correspondiente a la vista de Artículos.
     */
    private void mostrarAyuda() {
        factoriaUsuarios.cargarAyuda("articulosPrincipal");
    }

}
