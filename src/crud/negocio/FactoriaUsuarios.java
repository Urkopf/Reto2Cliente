/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

//import crud.iu.controladores.ControladorInicioSesion;
//import crud.iu.controladores.ControladorRegistro;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class FactoriaUsuarios {

    private static final Logger LOGGER = Logger.getLogger(FactoriaUsuarios.class.getName());
    public static final String CLIENTE = "cliente";
    public static final String TRABAJADOR = "trabajador";
    public static final String INICIO = "inicioSesion";

    private static FactoriaUsuarios instance;

    private FactoriaUsuarios() {
    }

    public static FactoriaUsuarios getInstance() {
        if (instance == null) {
            instance = new FactoriaUsuarios();
        }

        return instance;
    }

    public IUsuario inicioSesion() {
        return new UsuarioImpl();
    }

    public ICliente accesoCliente() {
        return new ClienteImpl();
    }

    public ITrabajador accesoTrabajador() {
        return new TrabajadorImpl();
    }

    /*
    //Ventanas
    public void cargarInicioSesion(Stage stage, String email) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/InicioSesion.fxml"));
            Parent root = cargador.load();
            ControladorInicioSesion controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setLogin(email);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventnaa de InicioSesion: {0}", e.getMessage());
            showErrorDialog(AlertType.ERROR, "Error", "No se puede cargar la ventana de Inicio de sesión");
        }
    }

    public void cargarRegistro(Stage stage, Usuario usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/Registro.fxml"));
            Parent root = cargador.load();
            ControladorRegistro controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(usuario);
            controlador.setModoActualizar(usuario != null);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventnaa de InicioSesion: {0}", e.getMessage());
            showErrorDialog(AlertType.ERROR, "Error", "No se puede cargar la ventana de Inicio de sesión");
        }
    }
     */
}
