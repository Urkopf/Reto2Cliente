/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.ExcepcionesUtilidad;
import crud.iu.controladores.ControladorAyuda;
import crud.iu.controladores.ControladorCambiarContrasena;
import crud.iu.controladores.ControladorInicioSesion;
import crud.iu.controladores.ControladorMenuPrincipal;
import crud.iu.controladores.ControladorRecuperarContrasena;
import crud.iu.controladores.ControladorRegistro;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Factoría para la gestión de usuarios y la carga de las vistas asociadas a la
 * autenticación y gestión de usuarios.
 * <p>
 * Esta clase sigue el patrón Singleton para asegurar que exista una única
 * instancia de la factoría durante la ejecución de la aplicación. Además,
 * proporciona métodos para acceder a las implementaciones de las interfaces de
 * usuario y para cargar las diferentes ventanas (inicio de sesión, registro,
 * menú principal, cambiar contraseña, recuperar contraseña y ayuda) de la
 * aplicación.
 * </p>
 *
 * @author Urko
 */
public class FactoriaUsuarios {

    /**
     * Logger para la clase {@code FactoriaUsuarios}.
     */
    private static final Logger LOGGER = Logger.getLogger(FactoriaUsuarios.class.getName());

    /**
     * Constante para identificar al usuario de tipo cliente.
     */
    public static final String CLIENTE = "cliente";

    /**
     * Constante para identificar al usuario de tipo trabajador.
     */
    public static final String TRABAJADOR = "trabajador";

    /**
     * Constante para identificar la pantalla de inicio de sesión.
     */
    public static final String INICIO = "inicioSesion";

    /**
     * Instancia única de la factoría.
     */
    private static FactoriaUsuarios instance;

    /**
     * Constructor privado para evitar la instanciación externa.
     */
    private FactoriaUsuarios() {
    }

    /**
     * Retorna la instancia única de {@code FactoriaUsuarios}.
     * <p>
     * Si la instancia aún no ha sido creada, se crea una nueva.
     * </p>
     *
     * @return La instancia única de {@code FactoriaUsuarios}.
     */
    public static FactoriaUsuarios getInstance() {
        if (instance == null) {
            instance = new FactoriaUsuarios();
        }
        return instance;
    }

    /**
     * Proporciona acceso a una implementación de la interfaz {@link IUsuario}
     * para la autenticación de usuarios.
     *
     * @return Una implementación de {@code IUsuario}.
     */
    public IUsuario inicioSesion() {
        return new UsuarioImpl();
    }

    /**
     * Proporciona acceso a una implementación de la interfaz {@link ICliente}
     * para la gestión de clientes.
     *
     * @return Una implementación de {@code ICliente}.
     */
    public ICliente accesoCliente() {
        return new ClienteImpl();
    }

    /**
     * Proporciona acceso a una implementación de la interfaz
     * {@link ITrabajador} para la gestión de trabajadores.
     *
     * @return Una implementación de {@code ITrabajador}.
     */
    public ITrabajador accesoTrabajador() {
        return new TrabajadorImpl();
    }

    // Ventanas
    /**
     * Carga y muestra la ventana de inicio de sesión.
     * <p>
     * Este método carga la vista FXML para la pantalla de inicio de sesión,
     * configura el controlador con el escenario y el correo electrónico, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param email El correo electrónico del usuario, que puede ser usado para
     * autocompletar el campo.
     */
    public void cargarInicioSesion(Stage stage, String email) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/InicioSesion.fxml"));
            Parent root = cargador.load();
            ControladorInicioSesion controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setCorreo(email);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Inicio de sesion {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana de registro.
     * <p>
     * Este método carga la vista FXML para la pantalla de registro, configura
     * el controlador con el escenario y el usuario actual, establece el modo de
     * actualización en función de si se ha proporcionado un usuario, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param usuario El objeto usuario, que puede ser nulo para un registro
     * nuevo o no nulo para actualización.
     */
    public void cargarRegistro(Stage stage, Object usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/Registro.fxml"));
            Parent root = cargador.load();
            ControladorRegistro controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(usuario);
            controlador.setModoActualizar(usuario != null);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Registro: {0}", e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana del menú principal.
     * <p>
     * Este método carga la vista FXML para la pantalla del menú principal,
     * configura el controlador con el escenario y el usuario actual, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param usuario El usuario actual.
     */
    public void cargarMenuPrincipal(Stage stage, Object usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/MenuPrincipal.fxml"));
            Parent root = cargador.load();
            ControladorMenuPrincipal controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(usuario);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Menu principal: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana para cambiar la contraseña.
     * <p>
     * Este método carga la vista FXML para la pantalla de cambio de contraseña,
     * configura el controlador con el escenario y el usuario actual, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param usuario El usuario actual.
     */
    public void cargarCambiarContrasena(Stage stage, Object usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/CambiarContrasena.fxml"));
            Parent root = cargador.load();
            ControladorCambiarContrasena controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(usuario);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana para recuperar la contraseña.
     * <p>
     * Este método carga la vista FXML para la pantalla de recuperación de
     * contraseña, configura el controlador con el escenario, y posteriormente
     * inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     */
    public void cargarRecuperarContrasena(Stage stage) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/RecuperarContrasena.fxml"));
            Parent root = cargador.load();
            ControladorRecuperarContrasena controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana de ayuda.
     * <p>
     * Este método carga la vista FXML para la pantalla de ayuda, configura el
     * controlador con el tipo de ayuda (título) a mostrar, y posteriormente
     * inicializa el escenario.
     * </p>
     *
     * @param titulo El título o tipo de ayuda a mostrar.
     */
    public void cargarAyuda(String titulo) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/Ayuda_inicioSesion.fxml"));
            Parent root = cargador.load();
            ControladorAyuda controlador = cargador.getController();
            controlador.setTipo(titulo);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

}
