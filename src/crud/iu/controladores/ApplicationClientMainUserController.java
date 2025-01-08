package view;

import business.ApplicationClientFactory;
import business.Client;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utilidades.User;

/**
 * Controlador para la vista principal de la aplicación para usuarios,
 * implementado en FXML. Este controlador gestiona las interacciones de usuario
 * en la pantalla principal, permitiendo acciones como cerrar sesión y salir de
 * la aplicación.
 *
 * También incluye un menú contextual personalizado y asignación de teclas de
 * acceso rápido.
 *
 * @author Lucian
 */
public class ApplicationClientMainUserController implements Initializable {

    @FXML
    private MenuItem menuCerrarSesion;   // Opción de menú para cerrar sesión
    @FXML
    private MenuItem menuSalir;          // Opción de menú para salir de la aplicación
    @FXML
    private Label welcomeLabel;          // Etiqueta para mostrar mensaje de bienvenida al usuario
    @FXML
    private AnchorPane main;             // Panel principal de la vista
    @FXML
    private Button logoutButton;         // Botón para cerrar sesión
    @FXML
    private Menu menuApp;

    private ContextMenu contextMenu;     // Menú contextual personalizado
    private Stage stage;                 // Ventana principal de la aplicación
    private Client client;               // Cliente asociado a la sesión actual
    private User user;                   // Usuario que inició sesión
    private ApplicationClientFactory factory; // Factoría para crear instancias de clientes

    /**
     * Método de inicialización de la vista. Configura el menú contextual, los
     * estilos de la interfaz y las acciones de los componentes.
     *
     * @param url URL de ubicación del recurso FXML
     * @param rb ResourceBundle para los recursos específicos de la vista
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
        closeSesion.setOnAction(event -> logOut());

        // Configurar y añadir opción de salir al menú contextual
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        exitItem.setOnAction(event -> applicationExit());

        contextMenu.getItems().addAll(closeSesion, exitItem);

        // Configurar menú contextual para mostrar al hacer clic derecho
        main.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(main, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    /**
     * Establece el cliente asociado a esta sesión.
     *
     * @param client el cliente actual
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Establece el usuario que ha iniciado sesión y muestra el mensaje de
     * bienvenida.
     *
     * @param user el usuario actual
     */
    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText("Bienvenid@ " + user.getName());
    }

    /**
     * Establece la ventana principal de la aplicación.
     *
     * @param stage la ventana principal de la aplicación
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa la escena principal de la aplicación, configurando el menú,
     * los estilos, y los manejadores de eventos.
     *
     * @param root el nodo raíz de la escena principal
     */
    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Programa principal");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);

            factory = ApplicationClientFactory.getInstance();

            // Filtro para ocultar el menú contextual al hacer clic fuera
            scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (contextMenu.isShowing() && event.getButton() != MouseButton.SECONDARY) {
                    contextMenu.hide();
                }
            });

            // Configurar acciones para los botones y elementos de menú
            logoutButton.setOnAction(event -> logOut());
            menuCerrarSesion.setOnAction(event -> logOut());
            menuSalir.setOnAction(event -> applicationExit());

            configureMnemotecnicKeys(); // Configurar teclas de acceso rápido
            stage.show();
        } catch (Exception e) {
            // Capturar excepciones en la inicialización de la escena
        }
    }

    /**
     * Configura teclas de acceso rápido para los botones de la vista.
     */
    private void configureMnemotecnicKeys() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.C) {
                logoutButton.fire();
                event.consume();
            }
        });
    }

    /**
     * Maneja el evento al mostrar la ventana.
     *
     * @param event el evento de mostrar ventana
     */
    private void handleWindowShowing(javafx.event.Event event) {
        // Implementación personalizada al mostrar la ventana
    }

    /**
     * Cierra la sesión del usuario mostrando una confirmación.
     */
    private void logOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas cerrar sesión?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            factory.loadSignInWindow(stage, "");
        }
    }

    /**
     * Cierra la aplicación mostrando una confirmación.
     */
    private void applicationExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas salir de la aplicación?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage.close();
        }
    }
}
