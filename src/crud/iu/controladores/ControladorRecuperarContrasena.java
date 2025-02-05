package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Usuario;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import crud.utilidades.Utilidades;
import static crud.utilidades.ValidateUtilities.isValid;
import java.net.ConnectException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
 */
public class ControladorRecuperarContrasena implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorRecuperarContrasena.class.getName());
    FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();

    @FXML
    private TextField campoCorreo;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonRecuperar;
    @FXML
    private ImageView botonAyuda;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Inicializando controlador de recuperación de contraseña.");
    }

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

        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
            if (e instanceof ConnectException || e instanceof ProcessingException) {

                FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
            } else {
                throw e;
            }

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado a Recuperar contrasena.");
    }

    /**
     * Maneja el evento de clic en el botón Cancelar.
     *
     * @param event Evento de acción.
     */
    @FXML
    private void manejarCancelar(ActionEvent event) {
        factoria.cargarInicioSesion(stage, "");
    }

    /**
     * Maneja el evento de clic en el botón Recuperar Contraseña.
     *
     * @param event Evento de acción.
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
     *
     * @param event Evento de clic del mouse.
     */
    private void mostrarAyuda() {
        factoria.cargarAyuda("recuperarContrasena");
    }

    /**
     * Verifica si el formato del correo es válido.
     *
     * @param correo El correo a validar.
     * @return true si es válido, false en caso contrario.
     */
    /**
     * Simula el envío de la solicitud de recuperación de contraseña.
     *
     * @param correo El correo al que se enviará la contraseña.
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
     * Muestra una alerta en la interfaz.
     *
     * @param tipo Tipo de alerta.
     * @param titulo Título de la alerta.
     * @param mensaje Mensaje de la alerta.
     */
    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        factoria.cargarInicioSesion(stage, "");
    }
}
