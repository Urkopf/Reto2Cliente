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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosPrincipal");
        configurarTabla();
        cargarDatosArticulos();
    }

    private void configurarTabla() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaReposicion"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

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
        LOGGER.info("Inicializando la escena principal");
        botonNuevo.addEventHandler(ActionEvent.ACTION, this::handleNuevoArticulo);
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario
    }

    //Eventos Botones
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


}
