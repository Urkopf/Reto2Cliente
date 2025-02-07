package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Usuario;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import static crud.utilidades.ValidateUtilities.isValid;
import java.net.ConnectException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.ws.rs.ProcessingException;

/**
 * Controlador para la vista de recuperación de contraseña.
 * <p>
 * Esta clase se encarga de gestionar la interacción del usuario en la interfaz
 * de recuperación de contraseña, incluyendo la validación del correo, el envío
 * de la solicitud de recuperación y la navegación entre ventanas.
 * </p>
 *
 * @author Sergio
 */
public class ControladorRecuperarContrasena implements Initializable {

    /**
     * Logger para registrar eventos y errores del controlador.
     */
    private static final Logger LOGGER = Logger.getLogger(ControladorRecuperarContrasena.class.getName());

    /**
     * Instancia de la factoría de usuarios.
     */
    FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();

    /**
     * Campo de texto para ingresar el correo electrónico.
     */
    @FXML
    private TextField campoCorreo;

    /**
     * Botón para cancelar la operación y volver a la pantalla de inicio de
     * sesión.
     */
    @FXML
    private Button botonCancelar;

    /**
     * Botón para enviar la solicitud de recuperación de contraseña.
     */
    @FXML
    private Button botonRecuperar;

    /**
     * Icono de ayuda que muestra información adicional al usuario.
     */
    @FXML
    private ImageView botonAyuda;

    /**
     * Escenario (Stage) actual de la aplicación.
     */
    private Stage stage;

    /**
     * Método de inicialización del controlador.
     * <p>
     * Se ejecuta automáticamente al cargar la vista y se utiliza para realizar
     * la configuración inicial.
     * </p>
     *
     * @param location La ubicación utilizada para resolver rutas relativas para
     * el objeto raíz, o null si se desconociera.
     * @param resources Los recursos utilizados para la localización, o null si
     * no se especifica ninguno.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Inicializando controlador de recuperación de contraseña.");
    }

    /**
     * Inicializa el Stage con la raíz de la escena y configura los eventos de
     * los controles.
     * <p>
     * Se establece el título, se define que el escenario no es redimensionable,
     * se asignan los manejadores de eventos a los botones y se muestra el
     * escenario.
     * </p>
     *
     * @param root El nodo raíz que contiene la interfaz gráfica.
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Recuperar Contraseña");
            stage.setResizable(false);

            botonCancelar.addEventHandler(ActionEvent.ACTION, this::manejarCancelar);
            botonRecuperar.addEventHandler(ActionEvent.ACTION, this::manejarRecuperar);
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
     * Asigna el Stage actual al controlador.
     *
     * @param stage El escenario actual en el que se mostrará la interfaz.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado a Recuperar contrasena.");
    }

    /**
     * Maneja el evento de clic en el botón Cancelar.
     * <p>
     * Redirige al usuario a la pantalla de inicio de sesión.
     * </p>
     *
     * @param event Evento de acción generado al hacer clic en el botón
     * Cancelar.
     */
    @FXML
    private void manejarCancelar(ActionEvent event) {
        factoria.cargarInicioSesion(stage, "");
    }

    /**
     * Maneja el evento de clic en el botón Recuperar Contraseña.
     * <p>
     * Valida el correo ingresado y, de ser correcto, procede a enviar la
     * solicitud de recuperación.
     * </p>
     *
     * @param event Evento de acción generado al hacer clic en el botón
     * Recuperar.
     */
    @FXML
    private void manejarRecuperar(ActionEvent event) {
        String correo = campoCorreo.getText().trim();

        if (correo.isEmpty()) {
            showErrorDialog(Alert.AlertType.WARNING, "Campo vacío", "Por favor, ingrese un correo electrónico válido.");
            LOGGER.warning("Correo vacío al intentar recuperar contraseña.");
            return;
        }

        if (!isValid(correo, "email")) {
            showErrorDialog(Alert.AlertType.ERROR, "Correo no válido", "El formato del correo electrónico no es correcto.");
            LOGGER.warning("Correo no válido: " + correo);
            return;
        }

        enviarSolicitudRecuperacion(correo);
    }

    /**
     * Maneja el evento de clic en el icono de ayuda.
     * <p>
     * Invoca la factoría de usuarios para cargar la vista de ayuda
     * correspondiente a la recuperación de contraseña.
     * </p>
     */
    private void mostrarAyuda() {
        factoria.cargarAyuda("recuperarContrasena");
    }

    /**
     * Envía la solicitud de recuperación de contraseña.
     * <p>
     * Crea un objeto Usuario con el correo ingresado, llama al método de
     * recuperación de contraseña y muestra una alerta informativa. En caso de
     * éxito, redirige a la pantalla de inicio de sesión.
     * </p>
     *
     * @param correo El correo electrónico al que se enviará la solicitud de
     * recuperación.
     */
    private void enviarSolicitudRecuperacion(String correo) {
        try {
            LOGGER.info("Enviando solicitud de recuperación para el correo: " + correo);
            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            factoria.inicioSesion().getRecuperarContrasena(usuario);
            showErrorDialog(Alert.AlertType.INFORMATION, "Solicitud enviada", "Se ha enviado la solicitud de recuperación al correo " + correo + ".");
            factoria.cargarInicioSesion(stage, "");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al enviar solicitud de recuperación.", e);
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Cierra la ventana actual.
     * <p>
     * Este método redirige al usuario a la pantalla de inicio de sesión,
     * simulando el cierre de la ventana de recuperación.
     * </p>
     */
    private void cerrarVentana() {
        factoria.cargarInicioSesion(stage, "");
    }
}
