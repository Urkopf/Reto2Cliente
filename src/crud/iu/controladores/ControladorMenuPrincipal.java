package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.ConnectException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.ws.rs.ProcessingException;

/**
 * Controlador para la ventana del Menú Principal.
 * <p>
 * Este controlador gestiona la interacción con la ventana principal de la
 * aplicación, permitiendo acceder a las distintas funcionalidades (gestión de
 * pedidos, artículos, cambio de contraseña, cerrar sesión, etc.) según el tipo
 * de usuario (Cliente o Trabajador). Además, configura un menú contextual
 * personalizado para opciones adicionales.
 * </p>
 *
 * @author Urko
 */
public class ControladorMenuPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorMenuPrincipal.class.getName());
    private FactoriaUsuarios factoriaUsuarios = FactoriaUsuarios.getInstance();
    private FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
    private FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();
    private Stage stage;

    private ContextMenu contextMenu;

    // Elementos FXML
    /**
     * Etiqueta que muestra el título y el mensaje de bienvenida.
     */
    @FXML
    private Label labelTitulo;

    /**
     * Botón para salir de la aplicación.
     */
    @FXML
    private Button botonSalir;

    /**
     * Icono que permite acceder a la ayuda.
     */
    @FXML
    private ImageView botonAyuda;

    /**
     * Botón para cerrar la sesión del usuario actual.
     */
    @FXML
    private Button botonCerrarSesion;

    /**
     * Botón para acceder a la funcionalidad de cambio de contraseña.
     */
    @FXML
    private Button botonCambiarContrasena;

    /**
     * Botón para gestionar pedidos.
     */
    @FXML
    private Button botonPedido;

    /**
     * Botón para gestionar artículos.
     */
    @FXML
    private Button botonArticulo;

    /**
     * Panel contenedor principal de la vista.
     */
    @FXML
    private AnchorPane panel;

    /**
     * Usuario de tipo Cliente activo.
     */
    private Cliente userCliente;

    /**
     * Usuario de tipo Trabajador activo.
     */
    private Trabajador userTrabajador;

    /**
     * Inicializa el controlador.
     * <p>
     * Configura el menú contextual personalizado, sus estilos y las acciones
     * asociadas a cada opción. Además, asigna el menú contextual al panel para
     * que se muestre al hacer clic derecho.
     * </p>
     *
     * @param url La URL utilizada para resolver rutas relativas para el objeto
     * raíz, o {@code null} si no se conoce.
     * @param rb Los recursos utilizados para la localización, o {@code null} si
     * no se aplican.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
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
     * Inicializa el escenario (Stage) con el contenido de la vista.
     * <p>
     * Configura la escena, el título, la redimensión y asigna los eventos a los
     * botones, como la gestión de pedidos, artículos, cambio de contraseña,
     * cerrar sesión y salir.
     * </p>
     *
     * @param root El nodo raíz de la escena.
     */
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
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
            if (e instanceof ConnectException || e instanceof ProcessingException) {

                FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
            } else {
                throw e;
            }
        }
    }

    /**
     * Asigna el usuario activo y actualiza el mensaje del título.
     * <p>
     * Dependiendo del tipo de usuario (Cliente o Trabajador), se actualiza la
     * etiqueta de bienvenida y se configuran las vistas de los botones (por
     * ejemplo, se oculta el botón de artículos para clientes).
     * </p>
     *
     * @param user Objeto que representa el usuario activo.
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
     * Asigna el escenario principal.
     *
     * @param stage Stage principal de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado al Menú Principal.");
    }

    /**
     * Maneja el evento del botón "Salir".
     * <p>
     * Al presionar este botón se invoca el método {@link #salir()} para cerrar
     * el escenario.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón "Salir".
     */
    @FXML
    private void manejarBotonSalir(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Salir' presionado.");
            salir();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al salir", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Cerrar Sesión".
     * <p>
     * Al presionar este botón se cierra la sesión del usuario actual y se
     * redirige a la pantalla de inicio de sesión.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón "Cerrar
     * Sesión".
     */
    @FXML
    private void manejarBotonCerrarSesion(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Cerrar Sesión' presionado.");
            // Lógica para cerrar sesión y volver a la pantalla de inicio de sesión
            cerrarSesion();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cerrar sesión", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón "Cambiar Contraseña".
     * <p>
     * Invoca la funcionalidad para cambiar la contraseña del usuario activo.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón "Cambiar
     * Contraseña".
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
     * <p>
     * Invoca la funcionalidad para gestionar los pedidos del usuario activo.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón "Gestión de
     * Pedidos".
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
     * <p>
     * Invoca la funcionalidad para gestionar los artículos, accesible
     * únicamente para usuarios de tipo Trabajador.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón "Gestión de
     * Artículos".
     */
    @FXML
    private void manejarBotonArticulo(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Gestión de Artículos' presionado.");
            factoriaArticulos.cargarArticulosPrincipal(stage, userTrabajador, null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al gestionar artículos", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Cierra la sesión del usuario actual y redirige a la pantalla de inicio de
     * sesión.
     */
    private void cerrarSesion() {
        factoriaUsuarios.cargarInicioSesion(stage, "");
    }

    /**
     * Cierra el escenario actual.
     */
    private void salir() {
        stage.close();
    }

    /**
     * Muestra la ayuda relacionada con el Menú Principal.
     */
    private void mostrarAyuda() {
        factoriaUsuarios.cargarAyuda("menuPrincipal");
    }
}
