/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Articulo;
import crud.objetosTransferibles.Trabajador;
import crud.utilidades.AlertUtilities;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 *
 * @author Sergio
 */
public class ControladorArticulosPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosPrincipal.class.getName());
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private Stage stage = new Stage();
    private Trabajador userTrabajador;
    private ObservableList<Articulo> ArticulosObservableList;
    private static final int FILAS_POR_PAGINA = 14;

    @FXML
    private Button botonNuevo;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBusqueda;
    @FXML
    private TableView<Articulo> tablaArticulos;
    @FXML
    private TableColumn<Articulo, Long> columnaId;
    @FXML
    private TableColumn<Articulo, String> columnaNombre;
    @FXML
    private TableColumn<Articulo, Double> columnaPrecio;
    @FXML
    private TableColumn<Articulo, Date> columnaFecha;
    @FXML
    private TableColumn<Articulo, String> columnaDescripcion;
    @FXML
    private TableColumn<Articulo, Integer> columnaStock;
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonDetalles;
    @FXML
    private Button botonGuardar;
    @FXML
    private Pagination paginador;

    // Copia de seguridad de los datos originales
    private ObservableList<Articulo> articulosOriginales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosPrincipal");
        configurarTabla();
        cargarDatosArticulos();
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

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos");
        LOGGER.info("Inicializando la escena principal");
        botonNuevo.addEventHandler(ActionEvent.ACTION, this::handleNuevoArticulo);
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario
    }

    private void configurarTabla() {
        tablaArticulos.setEditable(true);

        //Gestion de Columnas
        //Columna Id (no editable)
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Columna Nombre (Editable)
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        configurarEdicionNombre(columnaNombre);

        //Columna Precio (Editable)
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        configurarEdicionPrecio(columnaPrecio);

        //Columna Fecha Reposicion (Editable)
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReposicion"));
        configurarEdicionFecha(columnaFecha);

        //Columna Descripcion (Editable)
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        configurarEdicionDescripcion(columnaDescripcion);

        //Columna Stock (Editable)
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        configurarEdicionStock(columnaStock);

    }

    private void cargarDatosArticulos() {
        try {
            LOGGER.info("Cargando datos de articulos...");
            Collection<Articulo> articulos = factoriaArticulos.acceso().getAllArticulos();
            if (articulos == null || articulos.isEmpty()) {
                LOGGER.warning("No se encontraron articulos.");
                articulos = new ArrayList<>();
            }
            ArticulosObservableList = FXCollections.observableArrayList(articulos);
            tablaArticulos.setItems(ArticulosObservableList);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar los datos de articulos", e);
            AlertUtilities.showErrorDialog(Alert.AlertType.ERROR, "Error al cargar los articulos", "No se pudieron cargar los articulos. Intente nuevamente más tarde.");
        }

    }

//    private void configurarTabla() {
//        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
//        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
//        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
//        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReposicion"));
//        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
//        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
//
//    }
    //Eventos
    @FXML
    private void handleNuevoArticulo(ActionEvent event) {

    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {

    }

    @FXML
    private void handleEliminarArticulo(ActionEvent event) {

    }

    @FXML
    private void handleRecargarTabla(ActionEvent event) {

    }

    @FXML
    private void handleAtras(ActionEvent event) {
        factoriaUsuarios.cargarMenuPrincipal(stage, userTrabajador);
    }

    @FXML
    private void handleBusqueda(ActionEvent event) {

    }

    //Editables de la tabla
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

                // Listener para perder foco
                textFieldNombre.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textFieldNombre.getText());
                    }
                });

                // Listener para manejar la tecla Enter
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
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Campo Obligatorio", "El artículo no puede estar vacío");
                    cancelEdit(); // Cancela la edición y restaura el valor anterior
                } else {
                    commitEdit(newValue); // Valido, confirma la edición
                }
            }
        });
    }

    private void configurarEdicionPrecio(TableColumn<Articulo, Double> columnaPrecio) {
        columnaPrecio.setCellFactory(tc -> new TableCell<Articulo, Double>() {
            private final Spinner<Double> spinnerPrecio = new Spinner<>();

            @Override
            public void startEdit() {
                super.startEdit();

                // Configurar el Spinner con un rango adecuado y un valor inicial
                SpinnerValueFactory<Double> valueFactory
                        = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10000.0, getItem(), 0.01);
                spinnerPrecio.setValueFactory(valueFactory);

                // Formatear el valor del Spinner al iniciar la edición
                spinnerPrecio.getValueFactory().setValue(getItem());
                setGraphic(spinnerPrecio);
                setText(null);
                spinnerPrecio.requestFocus();

                // Listener para manejar la pérdida de foco
                spinnerPrecio.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(spinnerPrecio.getValue());
                    }
                });

                // Listener para manejar Enter (confirma edición)
                spinnerPrecio.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        validarYCommit(spinnerPrecio.getValue());
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
                articulo.setPrecio(newValue); // Actualiza el modelo
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
                        setText(formatPrecio(item)); // Muestra el precio con el formato "€"
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(Double newValue) {
                if (newValue == null || newValue < 0) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido", "El precio no puede ser negativo.");
                    cancelEdit(); // Cancela la edición y restaura el valor anterior
                } else {
                    commitEdit(newValue); // Valido, confirma la edición
                }
            }

            private String formatPrecio(Double precio) {
                return String.format("%.2f €", precio); // Formato con 2 decimales y símbolo del euro
            }
        });
    }

    private void configurarEdicionFecha(TableColumn<Articulo, Date> columnaFecha) {
        columnaFecha.setCellFactory(tc -> new TableCell< Articulo, Date>() {
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
                Articulo articulo = getTableView().getItems().get(getIndex());
                articulo.setFechaReposicion(newValue); // Sincroniza el modelo
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

                // Listener para perder foco
                textFieldDescripcion.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(textFieldDescripcion.getText());
                    }
                });

                // Listener para manejar la tecla Enter
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
                articulo.setDescripcion(newValue); // Actualiza la descripción del artículo
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
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Campo Obligatorio", "La descripción no puede estar vacía");
                    cancelEdit(); // Cancela la edición y restaura el valor anterior
                } else {
                    commitEdit(newValue); // Valido, confirma la edición
                }
            }
        });
    }

    private void configurarEdicionStock(TableColumn<Articulo, Integer> columnaStock) {
        columnaStock.setCellFactory(tc -> new TableCell<Articulo, Integer>() {
            private final Spinner<Integer> spinnerStock = new Spinner<>();

            @Override
            public void startEdit() {
                super.startEdit();

                // Configurar el Spinner con un rango adecuado y un valor inicial
                SpinnerValueFactory<Integer> valueFactory
                        = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, getItem());
                spinnerStock.setValueFactory(valueFactory);

                // Inicializar el valor actual en el Spinner
                spinnerStock.getValueFactory().setValue(getItem());
                setGraphic(spinnerStock);
                setText(null);
                spinnerStock.requestFocus();

                // Listener para manejar la pérdida de foco
                spinnerStock.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        validarYCommit(spinnerStock.getValue());
                    }
                });

                // Listener para manejar Enter (confirma edición)
                spinnerStock.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        validarYCommit(spinnerStock.getValue());
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
                articulo.setStock(newValue); // Actualiza el modelo
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
                        setText(formatStock(item)); // Muestra el stock con el formato "X unid"
                        setGraphic(null);
                    }
                }
            }

            private void validarYCommit(Integer newValue) {
                if (newValue == null || newValue < 0) {
                    AlertUtilities.showErrorDialog(AlertType.ERROR, "Valor inválido", "El stock no puede ser negativo.");
                    cancelEdit(); // Cancela la edición y restaura el valor anterior
                } else {
                    commitEdit(newValue); // Valido, confirma la edición
                }
            }

            private String formatStock(Integer stock) {
                return stock + " unid"; // Agrega el sufijo "unid"
            }
        });
    }

}
