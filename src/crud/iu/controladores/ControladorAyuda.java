/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.iu.controladores;

import crud.excepciones.ExcepcionesUtilidad;
import crud.negocio.FactoriaUsuarios;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ProcessingException;

/**
 * Controlador para la vista de Ayuda en la gestión de usuarios.
 * <p>
 * Este controlador se encarga de inicializar la ventana de ayuda, cargar la
 * página HTML correspondiente según el tipo especificado y gestionar errores de
 * conexión.
 * </p>
 *
 * @author Sergio
 */
public class ControladorAyuda implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorAyuda.class.getName());

    /**
     * Componente WebView que se utiliza para mostrar la página de ayuda.
     */
    @FXML
    private WebView webView;

    /**
     * Escenario que contiene la ventana de ayuda.
     */
    private Stage stage;

    /**
     * Tipo de ayuda a cargar, utilizado para determinar qué archivo HTML se
     * mostrará.
     */
    private String tipo;

    /**
     * Inicializa y configura el escenario de la ventana de ayuda.
     * <p>
     * Este método configura la escena y el escenario, establece sus
     * propiedades, y define el comportamiento al mostrar la ventana. Si ocurre
     * algún error durante la inicialización, se maneja mediante
     * {@link ExcepcionesUtilidad#centralExcepciones(Exception, String)} y, en
     * caso de problemas de conexión, se redirige a la pantalla de inicio de
     * sesión.
     * </p>
     *
     * @param root El nodo raíz que contiene la estructura de la interfaz de
     * usuario.
     */
    public void initStage(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Ayuda para la Gestion de Usuarios");
            stage.setResizable(false);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setOnShowing(this::handleWindowShowing);
            stage.show();
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
     * Maneja el evento de mostrar la ventana, cargando la página de ayuda en el
     * {@link WebView}.
     * <p>
     * Se utiliza el {@link WebEngine} del WebView para cargar la página HTML
     * correspondiente al tipo de ayuda especificado en el atributo
     * {@code tipo}.
     * </p>
     *
     * @param event Evento generado al mostrar la ventana.
     */
    private void handleWindowShowing(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        // Load help page.
        webEngine.load(getClass()
                .getResource("/recursos/ayuda/ayuda_" + tipo + ".html").toExternalForm());
    }

    /**
     * Método de inicialización de la interfaz, invocado automáticamente después
     * de que se hayan cargado los elementos FXML.
     *
     * @param location La ubicación utilizada para resolver rutas relativas para
     * el objeto raíz, o {@code null} si la ubicación no se conoce.
     * @param resources Los recursos utilizados para la localización, o
     * {@code null} si no se aplican.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Método intencionalmente vacío.
    }

    /**
     * Establece el tipo de ayuda a mostrar.
     * <p>
     * Este método asigna el valor al atributo {@code tipo}, que se utiliza para
     * cargar la página HTML correspondiente en el método
     * {@link #handleWindowShowing(WindowEvent)}.
     * </p>
     *
     * @param tipo Cadena que representa el tipo de ayuda.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
        LOGGER.info("Tipo Ayuda asignado.");
    }
}
