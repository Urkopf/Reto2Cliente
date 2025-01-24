package crud.iu.controladores;

import crud.iu.controladores.ControladorRegistro;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import static crud.seguridad.UtilidadesCifrado.cargarClavePublica;
import static crud.seguridad.UtilidadesCifrado.encriptarConClavePublica;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import static crud.utilidades.ExcepcionesUtilidad.clasificadorExcepciones;
import static crud.utilidades.ValidateUtilities.isValid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.net.URL;
import java.security.PublicKey;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador para la vista de cambio de contraseña.
 */
public class ControladorCambiarContrasena implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorCambiarContrasena.class.getName());
    FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();

    @FXML
    private PasswordField campoContrasenaVieja;
    @FXML
    private PasswordField campoContrasena;
    @FXML
    private PasswordField campoRepiteContrasena;
    @FXML
    private TextField campoContrasenaViejaVista;
    @FXML
    private TextField campoContrasenaVista;
    @FXML
    private TextField campoRepiteContrasenaVista;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonGuardar;
    @FXML
    private ImageView botonOjoVieja;
    @FXML
    private ImageView botonOjoNueva;
    @FXML
    private ImageView botonOjoRepite;
    @FXML
    private ImageView botonAyuda;
    private Cliente userCliente;
    private Trabajador userTrabajador;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Inicializando controlador de cambio de contraseña.");

        botonOjoVieja.setOnMousePressed(event -> togglePasswordVisibility(campoContrasenaVieja, campoContrasenaViejaVista));
        botonOjoVieja.setOnMouseReleased(event -> togglePasswordVisibility(campoContrasenaVieja, campoContrasenaViejaVista));

        botonOjoNueva.setOnMousePressed(event -> togglePasswordVisibility(campoContrasena, campoContrasenaVista));
        botonOjoNueva.setOnMouseReleased(event -> togglePasswordVisibility(campoContrasena, campoContrasenaVista));

        botonOjoRepite.setOnMousePressed(event -> togglePasswordVisibility(campoRepiteContrasena, campoRepiteContrasenaVista));
        botonOjoRepite.setOnMouseReleased(event -> togglePasswordVisibility(campoRepiteContrasena, campoRepiteContrasenaVista));
    }

    /**
     * Maneja el evento de clic en el botón Cancelar.
     *
     * @param event Evento de acción.
     */
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        LOGGER.info("Botón Cancelar presionado.");
        ventanaPadre();

    }

    private void ventanaPadre() {

        factoria.cargarMenuPrincipal(stage, (userCliente != null) ? userCliente : userTrabajador);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado Cambio de Contraseña.");
    }

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
        } catch (Exception e) {
            clasificadorExcepciones(e, e.getMessage());
        }
    }

    /**
     * Maneja el evento de clic en el botón Guardar cambios.
     *
     * @param event Evento de acción.
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
     *
     * @param event Evento de clic del mouse.
     */
    @FXML
    private void handleHelpIcon(MouseEvent event) {
        showErrorDialog(Alert.AlertType.INFORMATION, "Ayuda", "Ingrese su contraseña actual y la nueva contraseña para realizar el cambio.");
    }

    /**
     * Alterna la visibilidad de los campos de contraseña.
     *
     * @param passwordField Campo de contraseña oculta.
     * @param textField Campo de texto visible.
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
     *
     * @param contrasenaVieja Contraseña actual.
     * @param contrasenaNueva Nueva contraseña.
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
                usuario.setContrasena(encriptarConClavePublica(contrasenaVieja, clavePublica));
                usuario.setCalle(encriptarConClavePublica(contrasenaNueva, clavePublica));
            } catch (Exception ex) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
            }

            factoria.inicioSesion().getCambiarContrasena(usuario);
            showErrorDialog(Alert.AlertType.INFORMATION, "Cambio exitoso", "La contraseña ha sido cambiada correctamente.");
            ventanaPadre();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cambiar la contraseña.", e);
            clasificadorExcepciones(e, e.getMessage());
        }
    }

}
