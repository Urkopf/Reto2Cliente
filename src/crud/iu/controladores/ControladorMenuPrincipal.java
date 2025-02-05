package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controlador para la ventana del Menú Principal.
 */
public class ControladorMenuPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorMenuPrincipal.class.getName());
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private Stage stage;

    private ContextMenu contextMenu;

    // Elementos FXML
    @FXML
    private Label labelTitulo;
    @FXML
    private Button botonSalir;
    @FXML
    private ImageView botonAyuda;
    @FXML
    private Button botonCerrarSesion;
    @FXML
    private Button botonCambiarContrasena;
    @FXML
    private Button botonPedido;
    @FXML
    private Button botonArticulo;
    @FXML
    private AnchorPane panel;
    private Cliente userCliente;
    private Trabajador userTrabajador;

    /**
     * Inicializa el controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contextMenu = new ContextMenu();

        // Aplicar estilo personalizado al menú contextual
        contextMenu.getStyleClass().add("context-menu");
        contextMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;"
                + "-fx-padding: 10px;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5;");

        // Configurar y añadir opción de cerrar sesión al menú contextual
        MenuItem closeSesion = new MenuItem("Cerrar sesión");
        closeSesion.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        closeSesion.setOnAction(event -> cerrarSesion());

        // Configurar y añadir opción de salir al menú contextual
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        exitItem.setOnAction(event -> salir());

        contextMenu.getItems().addAll(closeSesion, exitItem);

        // Configurar menú contextual para mostrar al hacer clic derecho
        panel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(panel, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

    }

    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú");
            stage.setResizable(false);
            botonPedido.setOnAction(null);
            botonPedido.addEventHandler(ActionEvent.ACTION, this::manejarBotonPedido);
            botonArticulo.setOnAction(null);
            botonArticulo.addEventHandler(ActionEvent.ACTION, this::manejarBotonArticulo);
            botonCambiarContrasena.setOnAction(null);
            botonCambiarContrasena.addEventHandler(ActionEvent.ACTION, this::manejarBotonCambiarContrasena);
            botonCerrarSesion.setOnAction(null);
            botonCerrarSesion.addEventHandler(ActionEvent.ACTION, this::manejarBotonCerrarSesion);
            botonSalir.setOnAction(null);
            botonSalir.addEventHandler(ActionEvent.ACTION, this::manejarBotonSalir);
            botonAyuda.setOnMouseClicked(event -> {
                mostrarAyuda();
            });
            botonAyuda.setOnMouseEntered(event -> {
                botonAyuda.setStyle("-fx-cursor: hand;"); // Cambia el cursor al pasar el ratón
            });

            botonAyuda.setOnMouseExited(event -> {
                botonAyuda.setStyle("-fx-cursor: default;"); // Vuelve al cursor normal al salir
            });
            //configurarTeclasMnemotecnicas();  // Configurar teclas mnemotécnicas
            stage.show();  // Mostrar el escenario

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el stage", e);
        }
    }

    /**
     * Asigna el usuario activo y actualiza el mensaje del título.
     *
     * @param usuario Usuario activo.
     */
    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = new Cliente();
                this.userCliente = (Cliente) user;
                labelTitulo.setText("Bienvenid@, " + userCliente.getNombre() + "!");
                LOGGER.info("Cliente asignado: " + userCliente.getNombre());
                botonArticulo.setVisible(false);

            } else {
                this.userTrabajador = new Trabajador();
                this.userTrabajador = (Trabajador) user;
                labelTitulo.setText("Bienvenid@, " + userTrabajador.getNombre() + "!");
                botonArticulo.setVisible(true);
                LOGGER.info("Trabajador asignado: " + userTrabajador.getNombre());
            }

        }
    }

    /**
     * Asigna el stage principal.
     *
     * @param stage Stage principal de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado al Menú Principal.");
    }

    /**
     * Maneja el evento del botón "Salir".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarBotonSalir(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Salir' presionado.");
            salir();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al salir", e);
            showErrorDialog(AlertType.ERROR, "No se pudo salir de la aplicación", "Inténtelo de nuevo más tarde.");
        }
    }

    /**
     * Maneja el evento del botón "Cerrar Sesión".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarBotonCerrarSesion(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Cerrar Sesión' presionado.");
            // Lógica para cerrar sesión y volver a la pantalla de inicio de sesión
            cerrarSesion();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cerrar sesión", e);
            showErrorDialog(AlertType.ERROR, "No se pudo cerrar la sesión", "Inténtelo de nuevo más tarde.");
        }
    }

    /**
     * Maneja el evento del botón "Cambiar Contraseña".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarBotonCambiarContrasena(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Cambiar Contraseña' presionado.");
            factoriaUsuarios.cargarCambiarContrasena(stage, (userCliente != null) ? userCliente : userTrabajador);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar contraseña", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Gestión de Pedidos".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarBotonPedido(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Gestión de Pedidos' presionado.");

            factoriaPedidos.cargarPedidosPrincipal(stage, (userCliente != null) ? userCliente : userTrabajador, null);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al gestionar pedidos", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Gestión de Artículos".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarBotonArticulo(ActionEvent event) {
        try {

            LOGGER.info("Botón 'Gestión de Artículos' presionado.");
            factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al gestionar artículos", e);
            showErrorDialog(AlertType.ERROR, "No se pudo gestionar los artículos", "Inténtelo de nuevo más tarde.");
        }
    }

    private void cerrarSesion() {
        factoriaUsuarios.cargarInicioSesion(stage, "");
    }

    private void salir() {
        stage.close();
    }

    private void mostrarAyuda() {
        factoriaUsuarios.cargarAyuda("menuPrincipal");
    }
}
