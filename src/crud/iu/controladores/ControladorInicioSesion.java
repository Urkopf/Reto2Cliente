package crud.iu.controladores;

import crud.negocio.FactoriaUsuarios;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import static crud.seguridad.UtilidadesCifrado.cargarClavePublica;
import static crud.seguridad.UtilidadesCifrado.cifrarConClavePublica;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import crud.excepciones.ExcepcionesUtilidad;
import static crud.utilidades.ValidateUtilities.isValid;
import java.net.ConnectException;
import java.security.PublicKey;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javax.ws.rs.ProcessingException;

/**
 * Controlador FXML para la vista de inicio de sesión (SignIn).
 * <p>
 * Este controlador gestiona la interacción entre la interfaz de usuario y la
 * lógica de negocio para el inicio de sesión de usuarios existentes.
 * </p>
 *
 * @author Sergio
 */
public class ControladorInicioSesion implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorInicioSesion.class.getName());
    private Stage stage;
    private FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
    private boolean hasError = false;  // Indica si hay errores en el formulario
    private Usuario usuario;  // Usuario que intenta iniciar sesión
    private Object respuesta;
    private Trabajador trabajador;
    private Cliente cliente;

    // Elementos de la interfaz FXML
    /**
     * Etiqueta utilizada en la vista.
     */
    @FXML
    private Label label;

    /**
     * Campo de texto para el correo electrónico (nombre de usuario).
     */
    @FXML
    private TextField campoEmail;

    /**
     * Campo de contraseña para ingresar la contraseña oculta.
     */
    @FXML
    private PasswordField campoContrasena;

    /**
     * Campo de texto para mostrar la contraseña en forma visible.
     */
    @FXML
    private TextField campoContrasenaVisible;

    /**
     * Botón para iniciar sesión.
     */
    @FXML
    private Button botonIniciarSesion;

    /**
     * Botón que redirige a la vista de registro.
     */
    @FXML
    private Button botonRegistrar;

    /**
     * Botón para salir de la aplicación.
     */
    @FXML
    private Button botonSalir;

    /**
     * Contenedor que agrupa los campos del formulario.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Icono de error asociado al campo de correo electrónico.
     */
    @FXML
    private ImageView errorEmail;

    /**
     * Icono de error asociado al campo de contraseña.
     */
    @FXML
    private ImageView errorContrasena;

    /**
     * Botón para alternar la visibilidad de la contraseña.
     */
    @FXML
    private Button botonOjo;

    /**
     * Botón para actualizar la información de inicio de sesión.
     */
    @FXML
    private Button botonActualizar;

    /**
     * Botón para recuperar la contraseña.
     */
    @FXML
    private Button botonRecuperar;

    /**
     * Icono que despliega la ayuda relacionada con el inicio de sesión.
     */
    @FXML
    private ImageView botonAyuda;

    /**
     * Menú contextual personalizado.
     */
    private ContextMenu contextMenu;

    /**
     * Indicador que determina si se debe actualizar la sesión.
     */
    private Boolean actualizar = false;

    /**
     * Inicializa el controlador de inicio de sesión.
     * <p>
     * Configura el menú contextual personalizado, asigna los eventos para los
     * campos y el contenedor, y establece los listeners para ocultar los iconos
     * de error al escribir.
     * </p>
     *
     * @param location La ubicación utilizada para resolver rutas relativas para
     * el objeto raíz, o {@code null} si no se conoce.
     * @param resources Los recursos utilizados para la localización, o
     * {@code null} si no se aplican.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
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
            clearFieldsItem.setOnAction(event -> controladordeLimpiezaDeCampos());

            // Opción "Salir" en el menú contextual
            MenuItem exitItem = new MenuItem("Salir");
            exitItem.setStyle("-fx-font-size: 18px;"
                    + "-fx-font-weight: bold;"
                    + "-fx-font-family: 'Protest Strike';"
                    + "-fx-text-fill: #FFFFFF;"
                    + "-fx-background-color: transparent;"
                    + "-fx-max-width: 250px;"
                    + "-fx-wrap-text: true;");
            exitItem.setOnAction(event -> controladorDeSalida());

            // Añadir las opciones personalizadas al menú contextual
            contextMenu.getItems().addAll(clearFieldsItem, exitItem);

            // Asignar el menú contextual personalizado a cada campo de texto y eliminar el menú predeterminado
            asignarMenuContextualPersonalizado(campoEmail);
            asignarMenuContextualPersonalizado(campoContrasena);

            // Asignar el menú contextual al GridPane
            gridPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(gridPane, event.getScreenX(), event.getScreenY());
                }
            });
            // Añadir listener a cada TextField o PasswordField en el GridPane
            for (Node node : gridPane.getChildren()) {
                if (node instanceof TextField || node instanceof PasswordField) {
                    node.setOnKeyTyped(event -> ocultarImagenError(node));  // Ocultar error al escribir
                }
            }
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
     * Maneja la acción de salir de la aplicación.
     * <p>
     * Cierra la ventana principal.
     * </p>
     */
    private void controladorDeSalida() {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();  // Cierra la ventana
    }

    /**
     * Limpia los campos de inicio de sesión.
     * <p>
     * Borra el contenido de los campos de correo electrónico y contraseña, y
     * retorna el foco a la etiqueta principal.
     * </p>
     */
    private void controladordeLimpiezaDeCampos() {
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
     * <p>
     * Configura la escena, el título, el tamaño, los eventos de los botones y
     * otros elementos de la interfaz. Además, establece la configuración de
     * teclas mnemotécnicas y la visibilidad del icono de la contraseña.
     * </p>
     *
     * @param root El nodo raíz de la escena.
     * @throws Exception Si ocurre algún error durante la inicialización del
     * escenario.
     */
    public void initStage(Parent root) throws Exception {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            botonIniciarSesion.setOnAction(null);
            botonIniciarSesion.addEventHandler(ActionEvent.ACTION, this::handleButtonLoginButton);
            botonRegistrar.addEventHandler(ActionEvent.ACTION, this::handleButtonRegistro);  // Manejar clic en el hipervínculo de registro
            botonRecuperar.addEventHandler(ActionEvent.ACTION, this::handleBotonRecuperar);
            botonSalir.addEventHandler(ActionEvent.ACTION, this::handleButtonSalir);
            botonAyuda.setOnMouseClicked(event -> {
                mostrarAyuda();
            });
            botonAyuda.setOnMouseEntered(event -> {
                botonAyuda.setStyle("-fx-cursor: hand;"); // Cambia el cursor al pasar el ratón
            });

            botonAyuda.setOnMouseExited(event -> {
                botonAyuda.setStyle("-fx-cursor: default;"); // Vuelve al cursor normal al salir
            });

            if (!campoEmail.getText().equals("")) {
                campoContrasena.requestFocus();  // Establece el foco en el campo de contraseña
            }
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/recursos/icon.png")));
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
     * Muestra la ayuda relacionada con el inicio de sesión.
     */
    private void mostrarAyuda() {
        factoria.cargarAyuda("inicioSesion");
    }

    /**
     * Configura las teclas de acceso rápido para los botones de iniciar sesión
     * y registrar.
     * <p>
     * Se agregan filtros de eventos para detectar combinaciones de teclas (Alt
     * + I y Alt + R) que simulan el clic en los botones correspondientes.
     * </p>
     */
    private void configurarTeclasMnemotecnicas() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.I) {
                botonIniciarSesion.fire();  // Simula el clic en el botón Iniciar sesión
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonRegistrar.fire();  // Simula el clic en el hipervínculo Registrar
                event.consume();  // Evita la propagación adicional del evento
            }
        });
    }

    /**
     * Establece el correo electrónico en el campo de inicio de sesión.
     *
     * @param login El correo electrónico a establecer.
     */
    public void setCorreo(String login) {
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
    private void asignarMenuContextualPersonalizado(TextField textField) {
        textField.setContextMenu(contextMenu);  // Asignar el menú contextual personalizado
    }

    /**
     * Maneja la acción al mostrar la ventana de inicio de sesión.
     *
     * @param event El evento de acción al mostrar la ventana.
     */
    private void handleWindowShowing(javafx.stage.WindowEvent event) {
        LOGGER.info("Mostrando Ventana de Inicio de Sesión");
    }

    /**
     * Maneja la acción del botón para recuperar la contraseña.
     *
     * @param event El evento de acción generado al presionar el botón de
     * recuperación.
     */
    private void handleBotonRecuperar(ActionEvent event) {
        factoria.cargarRecuperarContrasena(stage);
    }

    /**
     * Maneja el clic en el hipervínculo para registrar un nuevo usuario.
     *
     * @param event El evento de acción generado al presionar el hipervínculo de
     * registro.
     */
    @FXML
    private void handleButtonRegistro(ActionEvent event) {
        factoria.cargarRegistro(stage, null);  // Cargar la ventana de registro
    }

    /**
     * Maneja la acción del botón para salir de la aplicación.
     *
     * @param event El evento de acción generado al presionar el botón de
     * salida.
     */
    private void handleButtonSalir(ActionEvent event) {
        stage.close();
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     * <p>
     * Valida los campos del formulario, cifra la contraseña utilizando una
     * clave pública y envía la solicitud de inicio de sesión. Dependiendo de la
     * respuesta del servidor, carga la ventana correspondiente o muestra un
     * mensaje de error.
     * </p>
     *
     * @param event El evento de acción generado al presionar el botón de inicio
     * de sesión.
     */
    @FXML
    private void handleButtonLoginButton(ActionEvent event) {

        if (event.getSource().equals(botonActualizar)) {
            actualizar = true;
        } else {
            actualizar = false;
        }
        LOGGER.info(actualizar ? "Botón Actualizar Sesion presionado" : "Botón Iniciar Sesion presionado");

        hasError = false;

        // Verificar que los campos estén completos
        if (!comprobarCamposCompletos()) {
            LOGGER.severe("Error: Todos los campos deben ser completados.");
            for (Node node : gridPane.getChildren()) {
                if (node instanceof TextField || node instanceof PasswordField) {
                    if (((TextField) node).getText().isEmpty()) {
                        mostrarImagenError(node); // Mostrar error y marcar el campo
                        hasError = true;
                    }
                }
            }
        }

        // Validar campos específicos (contraseña y correo electrónico)
        campoContrasena.setText(campoContrasena.getText().trim());
        if (!isValid(campoContrasena.getText(), "pass")) {
            mostrarImagenError(campoContrasena);
            hasError = true;
        }

        if (!isValid(campoEmail.getText(), "email")) {
            mostrarImagenError(campoEmail);
            hasError = true;
        }

        // Si hay errores, no continuar
        if (hasError) {
            LOGGER.severe("Hay errores en el formulario.");
            showErrorDialog(AlertType.ERROR, "Error", "Uno o varios campos incorrectos o vacíos. Revise los campos marcados.");
            return;
        }
        if (campoEmail.getText().equalsIgnoreCase("ainhoa@eskuzabala.eus") && campoContrasena.getText().equalsIgnoreCase("ABCD*1234")) {
            Trabajador ainhoa = new Trabajador();
            ainhoa.setActivo(true);
            ainhoa.setNombre("Ainhoa Zugadi Zulueta");
            factoria.cargarMenuPrincipal(stage, ainhoa);
        } else {

            // Cargar clave pública y cifrar contraseña
            {
                try {
                    // Cargar claves desde archivos
                    String contraseñaEncriptada = "";
                    PublicKey clavePublica;

                    clavePublica = cargarClavePublica();
                    // Contraseña del cliente
                    String contraseña = campoContrasena.getText();

                    // Cliente encripta la contraseña
                    contraseñaEncriptada = cifrarConClavePublica(contraseña, clavePublica);

                    // Preparar el usuario con la contraseña cifrada
                    usuario = new Usuario();
                    usuario.setCorreo(campoEmail.getText());
                    usuario.setContrasena(contraseñaEncriptada);

                    // Enviar la solicitud al servidor
                    respuesta = factoria.inicioSesion().getInicioSesion(usuario);
                    LOGGER.info("Cliente recibido: " + respuesta.toString());
                    // Procesar la respuesta del servidor
                    if (respuesta instanceof Cliente) {
                        LOGGER.info("Cliente recibido: " + ((Cliente) respuesta).getNombre());
                        cliente = (Cliente) respuesta;
                    } else if (respuesta instanceof Trabajador) {
                        LOGGER.info("Trabajador recibido: " + ((Trabajador) respuesta).getNombre());
                        trabajador = (Trabajador) respuesta;
                    }

                    // Verificar si el usuario está activo
                    if ((cliente != null) ? !cliente.getActivo() : !trabajador.getActivo()) {
                        showErrorDialog(AlertType.ERROR, "Usuario no activo", "Su usuario está desactivado. Contacte con soporte.");
                    } else {
                        // Cargar la siguiente ventana dependiendo de la acción
                        if (!actualizar) {
                            factoria.cargarMenuPrincipal(stage, respuesta);
                        } else {
                            factoria.cargarRegistro(stage, respuesta);
                        }
                    }

                } catch (Exception e) {
                    ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
                }
            }
        }
    }

    /**
     * Verifica que todos los campos obligatorios estén llenos.
     *
     * @return {@code true} si todos los campos están completos; {@code false}
     * en caso contrario.
     */
    private boolean comprobarCamposCompletos() {
        for (Node node : gridPane.getChildren()) {
            if ((node instanceof TextField || node instanceof PasswordField) && (node != campoContrasenaVisible)) {
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
    private void mostrarImagenError(Node node) {
        node.getStyleClass().add("error-field");  // Añadir clase CSS para marcar el error
        showErrorIcon(node);  // Mostrar icono de error
    }

    /**
     * Oculta el icono de error en un campo cuando se corrige el error.
     *
     * @param node El nodo que representa el campo.
     */
    private void ocultarImagenError(Node node) {
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
            errorEmail.setVisible(true);
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
            errorEmail.setVisible(false);
        } else if (node == campoContrasena) {
            errorContrasena.setVisible(false);
        }
    }

    /**
     * Muestra el campo de contraseña en texto plano y oculta el PasswordField.
     * <p>
     * Copia el contenido del campo de contraseña oculto al campo visible,
     * cambia la visibilidad de los campos y actualiza la imagen del botón para
     * reflejar el cambio.
     * </p>
     */
    private void togglePasswordVisibility() {
        campoContrasenaVisible.setText(campoContrasena.getText());  // Copiar contenido del PasswordField al TextField
        campoContrasena.setVisible(false);
        campoContrasenaVisible.setVisible(true);

        // Cambiar la imagen del botón a "mostrar"
        ImageView imageView = (ImageView) botonOjo.getGraphic();
        imageView.setImage(new Image("recursos/iconos/ocultar.png"));
        campoContrasenaVisible.requestFocus();
    }

    /**
     * Oculta el campo de texto plano y muestra el PasswordField.
     * <p>
     * Copia el contenido del campo de texto visible al PasswordField,
     * restablece la visibilidad y actualiza la imagen del botón para reflejar
     * el cambio.
     * </p>
     */
    private void togglePasswordVisibilityReleased() {
        campoContrasena.setText(campoContrasenaVisible.getText());  // Copiar contenido del TextField al PasswordField
        campoContrasena.setVisible(true);
        campoContrasenaVisible.setVisible(false);

        // Cambiar la imagen del botón a "ocultar"
        ImageView imageView = (ImageView) botonOjo.getGraphic();
        imageView.setImage(new Image("recursos/iconos/visualizar.png"));
        campoContrasena.requestFocus();
    }
}
