package crud.iu.controladores;

import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.AlertUtilities;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;

/**
 * Controlador para la ventana del Menú Principal.
 */
public class ControladorMenuPrincipal implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorMenuPrincipal.class.getName());

    private Stage stage;
    private Usuario usuario;

    // Elementos FXML
    @FXML
    private Label labelTitulo;
    @FXML
    private Button botonSalir;
    @FXML
    private Button botonCerrarSesion;
    @FXML
    private Button botonCambiarContrasena;
    @FXML
    private Button botonPedido;
    @FXML
    private Button botonArticulo;

    /**
     * Inicializa el controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador del Menú Principal");
    }

    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú");
            stage.setResizable(false);
            botonPedido.setOnAction(null);
            botonPedido.addEventHandler(ActionEvent.ACTION, this::handleBotonPedido);
            botonArticulo.setOnAction(null);
            botonArticulo.addEventHandler(ActionEvent.ACTION, this::handleBotonArticulo);
            botonCambiarContrasena.setOnAction(null);
            botonCambiarContrasena.addEventHandler(ActionEvent.ACTION, this::handleBotonCambiarContrasena);
            botonCerrarSesion.setOnAction(null);
            botonCerrarSesion.addEventHandler(ActionEvent.ACTION, this::handleBotonCerrarSesion);
            botonSalir.setOnAction(null);
            botonSalir.addEventHandler(ActionEvent.ACTION, this::handleBotonSalir);

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
                this.usuario = new Cliente();
                this.usuario = (Cliente) user;
            } else {
                this.usuario = new Trabajador();
                this.usuario = (Trabajador) user;
            }
            labelTitulo.setText("Bienvenido, " + usuario.getNombre() + "!");
            LOGGER.info("Usuario asignado: " + usuario.getNombre());
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
    private void handleBotonSalir(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Salir' presionado.");
            stage.close();
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
    private void handleBotonCerrarSesion(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Cerrar Sesión' presionado.");
            // Lógica para cerrar sesión y volver a la pantalla de inicio de sesión
            stage.close();
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
    private void handleBotonCambiarContrasena(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Cambiar Contraseña' presionado.");
            showErrorDialog(AlertType.ERROR, "Funcionalidad en desarrollo", "Próximamente estará disponible.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar contraseña", e);
            showErrorDialog(AlertType.ERROR, "No se pudo cambiar la contraseña", "Inténtelo de nuevo más tarde.");
        }
    }

    /**
     * Maneja el evento del botón "Gestión de Pedidos".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void handleBotonPedido(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Gestión de Pedidos' presionado.");
            showErrorDialog(AlertType.ERROR, "Funcionalidad en desarrollo", "Próximamente estará disponible.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al gestionar pedidos", e);
            showErrorDialog(AlertType.ERROR, "No se pudo gestionar los pedidos", "Inténtelo de nuevo más tarde.");
        }
    }

    /**
     * Maneja el evento del botón "Gestión de Artículos".
     *
     * @param event Evento de acción.
     */
    @FXML
    private void handleBotonArticulo(ActionEvent event) {
        try {
            LOGGER.info("Botón 'Gestión de Artículos' presionado.");
            showErrorDialog(AlertType.ERROR, "Funcionalidad en desarrollo", "Próximamente estará disponible.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al gestionar artículos", e);
            showErrorDialog(AlertType.ERROR, "No se pudo gestionar los artículos", "Inténtelo de nuevo más tarde.");
        }
    }
}
