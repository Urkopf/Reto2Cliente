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
import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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

    //Ventanas
    public void cargarInicioSesion(Stage stage, String email) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/InicioSesion.fxml"));
            Parent root = cargador.load();
            ControladorInicioSesion controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setCorreo(email);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

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
        }
    }

    public void cargarMenuPrincipal(Stage stage, Object usuario) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/MenuPrincipal.fxml"));
            Parent root = cargador.load();
            ControladorMenuPrincipal controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(usuario);
            controlador.initStage(root);
        } catch (Exception e) {
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

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
