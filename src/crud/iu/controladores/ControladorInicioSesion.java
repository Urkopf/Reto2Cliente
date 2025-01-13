package crud.iu.controladores;

import crud.negocio.FactoriaUsuarios;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
//import utilidades.Message;
import crud.objetosTransferibles.Usuario;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import static crud.utilidades.ValidateUtilities.isValid;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Controlador FXML para la vista de inicio de sesión (SignIn). Este controlador
 * gestiona la interacción entre la interfaz de usuario y la lógica de negocio
 * para el inicio de sesión de usuarios existentes.
 *
 * @author Lucian
 */
public class ControladorInicioSesion implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ApplicationClientSignInController.class.getName());
    private Stage stage = new Stage();
    private FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
    private boolean hasError = false;  // Indica si hay errores en el formulario
    private Usuario usuario;  // Usuario que intenta iniciar sesión

    // Elementos de la interfaz FXML
    @FXML
    private Label label;
    @FXML
    private TextField campoEmail;  // Campo de texto para el nombre de usuario
    @FXML
    private PasswordField campoContrasena;  // Campo de contraseña
    @FXML
    private TextField campoContrasenaVista;  // Campo de texto visual para la contraseña
    @FXML
    private Button botonIniciarSesion;  // Botón para iniciar sesión
    @FXML
    private Button botonRegistro;  // Hipervínculo para ir a la página de registro
    @FXML
    private GridPane gridPane;  // Contenedor de todos los campos del formulario
    @FXML
    private ImageView errorCorreo;  // Icono de error para el campo de inicio de sesión
    @FXML
    private ImageView errorContrasena;  // Icono de error para el campo de contraseña
    @FXML
    private Button botonOjo;  // Botón para alternar la visibilidad de la contraseña
    @FXML
    private Button botonActualizar;

    private ContextMenu contextMenu;  // Menú contextual personalizado
    private Boolean actualizar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Crear el menú contextual personalizado
        contextMenu = new ContextMenu();
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

        // Opción "Borrar campos" en el menú contextual
        MenuItem clearFieldsItem = new MenuItem("Borrar campos");
        clearFieldsItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        clearFieldsItem.setOnAction(event -> handleClearFields());

        // Opción "Salir" en el menú contextual
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        exitItem.setOnAction(event -> handleExit());

        // Añadir las opciones personalizadas al menú contextual
        contextMenu.getItems().addAll(clearFieldsItem, exitItem);

        // Asignar el menú personalizado a cada campo de texto y eliminar el menú predeterminado
        assignCustomContextMenu(campoEmail);
        assignCustomContextMenu(campoContrasena);

        // Asignar el menú contextual al GridPane
        gridPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(gridPane, event.getScreenX(), event.getScreenY());
            }
        });
        // Añadir listener a cada TextField o PasswordField en el GridPane
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TextField || node instanceof PasswordField) {
                node.setOnKeyTyped(event -> hideErrorImage(node));  // Ocultar error al escribir
            }
        }
    }

    /**
     * Maneja la acción de salir de la aplicación.
     */
    private void handleExit() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();  // Cierra la ventana
    }

    /**
     * Limpia los campos de inicio de sesión.
     */
    private void handleClearFields() {
        campoEmail.clear();  // Limpiar el campo de nombre de usuario
        campoContrasena.clear();  // Limpiar el campo de contraseña
        label.requestFocus();  // Devuelve el foco al título
    }

    /**
     * Establece el escenario principal.
     *
     * @param stage El escenario de la aplicación.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa el escenario con el contenido de la vista.
     *
     * @param root El nodo raíz de la escena.
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            botonIniciarSesion.setOnAction(null);
            botonIniciarSesion.addEventHandler(ActionEvent.ACTION, this::handleButtonLoginButton);
            botonRegistro.setOnAction(this::handleHyperLinkRegistry);  // Manejar clic en el hipervínculo de registro

            if (!campoEmail.getText().equals("")) {
                campoContrasena.requestFocus();  // Establece el foco en el campo de contraseña
            }

            // Configurar la visibilidad de la contraseña
            botonOjo.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibility();  // Alternar la visibilidad de la contraseña
                }
            });

            botonOjo.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibilityReleased();  // Alternar la visibilidad de la contraseña al soltar
                }
            });

            configurarTeclasMnemotecnicas();  // Configurar teclas mnemotécnicas
            stage.show();  // Mostrar el escenario
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el stage", e);
        }
    }

    /**
     * Configura las teclas de acceso rápido para los botones de iniciar sesión
     * y registrar.
     */
    private void configurarTeclasMnemotecnicas() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.I) {
                botonIniciarSesion.fire();  // Simula el clic en el botón Iniciar sesión
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonRegistro.fire();  // Simula el clic en el hipervínculo Registrar
                event.consume();  // Evita la propagación adicional del evento
            }
        });
    }

    /**
     * Establece el nombre de usuario en el campo de inicio de sesión.
     *
     * @param login El nombre de usuario a establecer.
     */
    public void setLogin(String login) {
        if (login != null && !login.isEmpty()) {
            campoEmail.setText(login);
        }
    }

    /**
     * Asigna el menú contextual personalizado a un campo de texto.
     *
     * @param textField El campo de texto al que se le asignará el menú
     * contextual.
     */
    private void assignCustomContextMenu(TextField textField) {
        textField.setContextMenu(contextMenu);  // Asignar el menú contextual personalizado
    }

    /**
     * Maneja la acción al mostrar la ventana de inicio de sesión.
     *
     * @param event El evento de acción.
     */
    private void handleWindowShowing(javafx.event.Event event) {
        LOGGER.info("Mostrando Ventana de Inicio de Sesión");
    }

    /**
     * Maneja el clic en el hipervínculo para registrar un nuevo usuario.
     *
     * @param event El evento de acción.
     */
    @FXML
    private void handleHyperLinkRegistry(ActionEvent event) {
        factoria.cargarRegistro(stage, null);  // Cargar la ventana de registro
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     *
     * @param event El evento de acción.
     */
    @FXML
    private void handleButtonLoginButton(ActionEvent event) {

        if (event.getSource().equals(botonActualizar)) {
            actualizar = true;
        } else {
            actualizar = false;
        }
        if (actualizar) {
            LOGGER.info("Botón Actualizar Sesion presionado");
        } else {
            LOGGER.info("Botón Iniciar Sesion presionado");
        }

        hasError = false;
        // Verificar si todos los campos están llenos
        if (!comprobarCamposCompletos()) {
            LOGGER.severe("Error: Todos los campos deben ser completados.");
            for (Node node : gridPane.getChildren()) {
                if (node instanceof TextField || node instanceof PasswordField) {
                    if (((TextField) node).getText().isEmpty()) {
                        showErrorImage(node); // Mostrar error y marcar el campo
                        hasError = true;
                    }
                }
            }
        }

        // Validar campos específicos como contraseña y nombre de usuario
        campoContrasena.setText(campoContrasena.getText().trim());
        if (!isValid(campoContrasena.getText(), "pass")) {
            showErrorImage(campoContrasena);
            hasError = true;
        }

        if (!isValid(campoEmail.getText(), "email")) {
            showErrorImage(campoEmail);
            hasError = true;
        }

        // Si hay errores, no continuar
        if (hasError) {
            LOGGER.severe("Hay errores en el formulario.");
            showErrorDialog(AlertType.ERROR, "Error", "Uno o varios campos incorrectos o vacíos. Mantenga el cursor encima de los campos para más información.");
        } else {
            // Si no hay errores, proceder con el formulario
            usuario = new Usuario();  // Crear un nuevo usuario
            usuario.setCorreo(campoEmail.getText());
            usuario.setContrasena(campoContrasena.getText());

//            Message response = factoria.getUsuarioService("inicioSesion", usuario);  // Enviar los datos de inicio de sesión al servidor
//            messageManager(response);  // Manejar la respuesta del servidor
        }
    }

    /**
     * Maneja la respuesta del servidor a la solicitud de inicio de sesión.
     *
     * @param message El mensaje de respuesta del servidor.
     */
    /*  private void messageManager(Message message) {
        switch (message.getType()) {
            case LOGIN_OK:
                botonIniciarSesion.setDisable(true);  // Deshabilitar el botón de inicio de sesión
                if (!actualizar) {

                    factoria.loadMainUserWindow(stage, (Usuario) message.getObject());  // Cargar la ventana principal
                } else {
                    factoria.loadSignUpWindow(stage, (Usuario) message.getObject());  // Cargar el SignUP
                }
                break;
            case SIGNIN_ERROR:
                campoEmail.setStyle("-fx-border-color: red;");
                campoContrasena.setStyle("-fx-border-color: red;");
                errorCorreo.setVisible(true);  // Mostrar icono de error para nombre de usuario
                errorContrasena.setVisible(true);  // Mostrar icono de error para contraseña
                showErrorDialog(Alert.AlertType.ERROR, "Error", "El correo electrónico (login) y/o la contraseña incorrect@/s");
                break;
            case BAD_RESPONSE:
                showErrorDialog(Alert.AlertType.ERROR, "Error", "Error interno de la base de datos, inténtelo de nuevo...");
                break;
            case CONNECTION_ERROR:
                showErrorDialog(Alert.AlertType.ERROR, "Error", "Error de conexión con la base de datos. No hay conexión disponible, inténtelo de nuevo...");
                break;
            case SERVER_ERROR:
                showErrorDialog(Alert.AlertType.ERROR, "Error", "Servidor no encontrado, inténtelo de nuevo...");
                break;
            case NON_ACTIVE:
                showErrorDialog(Alert.AlertType.ERROR, "Error", "El usuario introducido está desactivado, no puede hacer iniciar sesión comuniquese con el departamento de Sistemas.");
                break;
        }
    }

    /**
     * Verifica que todos los campos obligatorios estén llenos.
     *
     * @return true si todos los campos están llenos, false en caso contrario.
     */
    private boolean comprobarCamposCompletos() {
        for (Node node : gridPane.getChildren()) {
            if ((node instanceof TextField || node instanceof PasswordField) && (node != campoContrasenaVista)) {
                if (((TextField) node).getText() == null || ((TextField) node).getText().isEmpty()) {
                    LOGGER.severe("Error: El campo " + ((TextField) node).getPromptText() + " está vacío.");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Muestra el icono de error en un campo que contiene un error de
     * validación.
     *
     * @param node El nodo que representa el campo.
     */
    private void showErrorImage(Node node) {
        node.getStyleClass().add("error-field");  // Añadir clase CSS para marcar el error
        showErrorIcon(node);  // Mostrar icono de error
    }

    /**
     * Oculta el icono de error en un campo cuando se corrige el error.
     *
     * @param node El nodo que representa el campo.
     */
    private void hideErrorImage(Node node) {
        node.getStyleClass().remove("error-field");  // Eliminar clase CSS
        hideErrorIcon(node);  // Ocultar el icono de error
    }

    /**
     * Muestra el icono de error correspondiente al campo indicado.
     *
     * @param node El nodo que representa el campo.
     */
    private void showErrorIcon(Node node) {
        if (node == campoEmail) {
            errorCorreo.setVisible(true);
        } else if (node == campoContrasena) {
            errorContrasena.setVisible(true);
        }
    }

    /**
     * Oculta el icono de error correspondiente al campo indicado.
     *
     * @param node El nodo que representa el campo.
     */
    private void hideErrorIcon(Node node) {
        if (node == campoEmail) {
            errorCorreo.setVisible(false);
        } else if (node == campoContrasena) {
            errorContrasena.setVisible(false);
        }
    }

    /**
     * Muestra el campo de contraseña en texto plano y oculta el PasswordField.
     */
    private void togglePasswordVisibility() {
        campoContrasenaVista.setText(campoContrasena.getText());  // Copiar contenido del PasswordField al TextField
        campoContrasena.setVisible(false);
        campoContrasenaVista.setVisible(true);

        // Cambiar la imagen del botón a "mostrar"
        ImageView imageView = (ImageView) botonOjo.getGraphic();
        imageView.setImage(new Image("resources/iconos/ocultar.png"));
    }

    /**
     * Oculta el campo de texto plano y muestra el PasswordField.
     */
    private void togglePasswordVisibilityReleased() {
        campoContrasena.setText(campoContrasenaVista.getText());  // Copiar contenido del TextField al PasswordField
        campoContrasena.setVisible(true);
        campoContrasenaVista.setVisible(false);

        // Cambiar la imagen del botón a "ocultar"
        ImageView imageView = (ImageView) botonOjo.getGraphic();
        imageView.setImage(new Image("resources/iconos/visualizar.png"));
    }
}
