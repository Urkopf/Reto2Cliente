package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.iu.controladores.ControladorRegistro;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import static crud.seguridad.UtilidadesCifrado.cargarClavePublica;
import static crud.seguridad.UtilidadesCifrado.cifrarConClavePublica;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import static crud.utilidades.ValidateUtilities.isValid;
import java.net.ConnectException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.security.PublicKey;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.ws.rs.ProcessingException;

/**
 * Controlador para la vista de cambio de contraseña.
 * <p>
 * Este controlador gestiona la interfaz para cambiar la contraseña del usuario,
 * incluyendo la validación de los campos, la alternancia de la visibilidad de
 * las contraseñas, y el envío de la solicitud de cambio de contraseña
 * utilizando mecanismos de cifrado.
 * </p>
 *
 * @author 2dam
 */
public class ControladorCambiarContrasena implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorCambiarContrasena.class.getName());

    /**
     * Instancia de la factoría de usuarios para manejar la lógica de negocio.
     */
    FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();

    /**
     * Campo de entrada para la contraseña antigua (oculto).
     */
    @FXML
    private PasswordField campoContrasenaVieja;

    /**
     * Campo de entrada para la nueva contraseña (oculto).
     */
    @FXML
    private PasswordField campoContrasena;

    /**
     * Campo de entrada para repetir la nueva contraseña (oculto).
     */
    @FXML
    private PasswordField campoRepiteContrasena;

    /**
     * Campo de texto para visualizar la contraseña antigua.
     */
    @FXML
    private TextField campoContrasenaViejaVista;

    /**
     * Campo de texto para visualizar la nueva contraseña.
     */
    @FXML
    private TextField campoContrasenaVista;

    /**
     * Campo de texto para visualizar la repetición de la nueva contraseña.
     */
    @FXML
    private TextField campoRepiteContrasenaVista;

    /**
     * Botón para cancelar la acción de cambio de contraseña.
     */
    @FXML
    private Button botonCancelar;

    /**
     * Botón para guardar la nueva contraseña.
     */
    @FXML
    private Button botonGuardar;

    /**
     * Icono que permite alternar la visibilidad de la contraseña antigua.
     */
    @FXML
    private ImageView botonOjoVieja;

    /**
     * Icono que permite alternar la visibilidad de la nueva contraseña.
     */
    @FXML
    private ImageView botonOjoNueva;

    /**
     * Icono que permite alternar la visibilidad de la repetición de la nueva
     * contraseña.
     */
    @FXML
    private ImageView botonOjoRepite;

    /**
     * Botón para acceder a la ayuda sobre el cambio de contraseña.
     */
    @FXML
    private ImageView botonAyuda;

    /**
     * Objeto de tipo Cliente para el usuario actual (si es cliente).
     */
    private Cliente userCliente;

    /**
     * Objeto de tipo Trabajador para el usuario actual (si es trabajador).
     */
    private Trabajador userTrabajador;

    /**
     * Escenario (ventana) actual donde se muestra la vista.
     */
    private Stage stage;

    /**
     * Inicializa el controlador de cambio de contraseña.
     * <p>
     * Se configuran los eventos para alternar la visibilidad de las contraseñas
     * utilizando los íconos correspondientes. En caso de error, se maneja la
     * excepción y se redirige al inicio de sesión si corresponde.
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
            LOGGER.info("Inicializando controlador de cambio de contraseña.");

            botonOjoVieja.setOnMousePressed(event -> togglePasswordVisibility(campoContrasenaVieja, campoContrasenaViejaVista));
            botonOjoVieja.setOnMouseReleased(event -> togglePasswordVisibility(campoContrasenaVieja, campoContrasenaViejaVista));

            botonOjoNueva.setOnMousePressed(event -> togglePasswordVisibility(campoContrasena, campoContrasenaVista));
            botonOjoNueva.setOnMouseReleased(event -> togglePasswordVisibility(campoContrasena, campoContrasenaVista));

            botonOjoRepite.setOnMousePressed(event -> togglePasswordVisibility(campoRepiteContrasena, campoRepiteContrasenaVista));
            botonOjoRepite.setOnMouseReleased(event -> togglePasswordVisibility(campoRepiteContrasena, campoRepiteContrasenaVista));
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
     * Maneja el evento de clic en el botón Cancelar.
     * <p>
     * Al presionar el botón Cancelar se regresa a la ventana principal del
     * usuario.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón.
     */
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        LOGGER.info("Botón Cancelar presionado.");
        ventanaPadre();
    }

    /**
     * Redirige al usuario a la ventana principal.
     * <p>
     * Dependiendo del tipo de usuario (Cliente o Trabajador) se carga la
     * ventana principal correspondiente.
     * </p>
     */
    private void ventanaPadre() {
        factoria.cargarMenuPrincipal(stage, (userCliente != null) ? userCliente : userTrabajador);
    }

    /**
     * Asigna el escenario (Stage) en el que se muestra la vista de cambio de
     * contraseña.
     *
     * @param stage El escenario a asignar.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado Cambio de Contraseña.");
    }

    /**
     * Inicializa y configura el escenario (Stage) para la vista de cambio de
     * contraseña.
     * <p>
     * Se establece la escena, título, dimensiones y comportamiento de los
     * botones y elementos de la interfaz. En caso de error durante la
     * inicialización, se maneja la excepción y se redirige al inicio de sesión
     * si es necesario.
     * </p>
     *
     * @param root El nodo raíz que contiene la estructura de la interfaz de
     * usuario.
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Cambiar Contraseña");
            stage.setResizable(false);

            //configurarTeclasMnemotecnicas();  // Configurar teclas mnemotécnicas
            stage.show();  // Mostrar el escenario
            botonCancelar.addEventHandler(ActionEvent.ACTION, this::handleButtonCancel);
            botonGuardar.addEventHandler(ActionEvent.ACTION, this::handleButtonRegister);
            botonAyuda.setOnMouseClicked(event -> {
                mostrarAyuda();
            });
            botonAyuda.setOnMouseEntered(event -> {
                botonAyuda.setStyle("-fx-cursor: hand;"); // Cambia el cursor al pasar el ratón
            });

            botonAyuda.setOnMouseExited(event -> {
                botonAyuda.setStyle("-fx-cursor: default;"); // Vuelve al cursor normal al salir
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
     * Maneja el evento de clic en el botón Guardar cambios.
     * <p>
     * Realiza la validación de los campos de contraseña, verifica que no estén
     * vacíos, que la nueva contraseña cumpla los requisitos y que ambas
     * contraseñas nuevas coincidan. Si la validación es correcta, procede a
     * enviar la solicitud de cambio de contraseña.
     * </p>
     *
     * @param event Evento de acción generado al presionar el botón Guardar.
     */
    @FXML
    private void handleButtonRegister(ActionEvent event) {
        String contrasenaVieja = campoContrasenaVieja.getText().trim();
        String contrasenaNueva = campoContrasena.getText().trim();
        String repiteContrasena = campoRepiteContrasena.getText().trim();

        if (contrasenaVieja.isEmpty() || contrasenaNueva.isEmpty() || repiteContrasena.isEmpty()) {
            showErrorDialog(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
            LOGGER.warning("Campos vacíos al intentar cambiar contraseña.");
            return;
        }

        if (!isValid(contrasenaNueva, "pass")) {
            showErrorDialog(Alert.AlertType.ERROR, "Contraseña no válida", "La nueva contraseña no cumple con los requisitos mínimos.");
            LOGGER.warning("Contraseña no válida.");
            return;
        }

        if (!contrasenaNueva.equals(repiteContrasena)) {
            showErrorDialog(Alert.AlertType.ERROR, "Contraseñas no coinciden", "La nueva contraseña y su repetición no coinciden.");
            LOGGER.warning("Contraseñas no coinciden.");
            return;
        }

        enviarCambioContrasena(contrasenaVieja, contrasenaNueva);
    }

    /**
     * Maneja el evento de clic en el icono de ayuda.
     * <p>
     * Muestra un diálogo informativo con instrucciones para cambiar la
     * contraseña.
     * </p>
     *
     * @param event Evento de clic del mouse.
     */
    @FXML
    private void handleHelpIcon(MouseEvent event) {
        showErrorDialog(Alert.AlertType.INFORMATION, "Ayuda", "Ingrese su contraseña actual y la nueva contraseña para realizar el cambio.");
    }

    /**
     * Alterna la visibilidad de los campos de contraseña.
     * <p>
     * Este método permite mostrar u ocultar el contenido de un
     * {@link PasswordField} mediante un {@link TextField} que refleja su
     * contenido.
     * </p>
     *
     * @param passwordField Campo de contraseña que se oculta o muestra.
     * @param textField Campo de texto que muestra o oculta el contenido de la
     * contraseña.
     */
    private void togglePasswordVisibility(PasswordField passwordField, TextField textField) {
        boolean isPasswordFieldVisible = passwordField.isVisible();
        if (isPasswordFieldVisible) {
            textField.setText(passwordField.getText());
            textField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(textField.getText());
            passwordField.setVisible(true);
            textField.setVisible(false);
        }
    }

    /**
     * Asigna el usuario que solicita el cambio de contraseña.
     * <p>
     * Se determina si el usuario es de tipo {@link Cliente} o
     * {@link Trabajador} y se asigna en consecuencia.
     * </p>
     *
     * @param user Objeto que representa al usuario.
     */
    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userCliente = new Cliente();
                this.userCliente = (Cliente) user;
            } else {
                this.userTrabajador = new Trabajador();
                this.userTrabajador = (Trabajador) user;
            }
        }
    }

    /**
     * Simula el envío de la solicitud de cambio de contraseña.
     * <p>
     * El método prepara el objeto {@link Usuario} con la información necesaria,
     * cifra la contraseña utilizando la clave pública, y envía la solicitud de
     * cambio a través de la factoría de usuarios. Si la operación es exitosa,
     * se muestra un mensaje de confirmación y se redirige al menú principal.
     * </p>
     *
     * @param contrasenaVieja La contraseña actual.
     * @param contrasenaNueva La nueva contraseña a asignar.
     */
    private void enviarCambioContrasena(String contrasenaVieja, String contrasenaNueva) {
        try {

            Usuario usuario = new Usuario();
            if (userCliente != null) {
                usuario.setCorreo(userCliente.getCorreo());
            } else {
                usuario.setCorreo(userTrabajador.getCorreo());
            }
            LOGGER.info("Enviando solicitud de cambio de contraseña.");
            String[] contrasenas = new String[2];

            // Cargar claves desde archivos
            PublicKey clavePublica;
            try {
                clavePublica = cargarClavePublica();

                // Contraseña del cliente
                usuario.setContrasena(cifrarConClavePublica(contrasenaVieja, clavePublica));
                usuario.setCalle(cifrarConClavePublica(contrasenaNueva, clavePublica));
            } catch (Exception ex) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
            }

            factoria.inicioSesion().getCambiarContrasena(usuario);
            showErrorDialog(Alert.AlertType.INFORMATION, "Cambio exitoso", "La contraseña ha sido cambiada correctamente.");
            ventanaPadre();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar la contraseña.", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Muestra la ayuda relacionada con el cambio de contraseña.
     * <p>
     * Se invoca el método de la factoría de usuarios para cargar la vista de
     * ayuda específica.
     * </p>
     */
    private void mostrarAyuda() {
        factoria.cargarAyuda("cambiarContrasena");
    }
}
