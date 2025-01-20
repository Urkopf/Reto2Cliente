/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.iu.controladores.ControladorArticulosBusqueda;
import crud.iu.controladores.ControladorArticulosPrincipal;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Sergio
 */
public class FactoriaArticulos {

    private static final Logger LOGGER = Logger.getLogger(FactoriaArticulos.class.getName());

    private static FactoriaArticulos instance;

    private FactoriaArticulos() {
    }

    public static FactoriaArticulos getInstance() {
        if (instance == null) {
            instance = new FactoriaArticulos();
        }

        return instance;
    }

    public IArticulo acceso() {
        return new ArticuloImpl();
    }

    //Ventanas
    public void cargarArticulosPrincipal(Stage stage, Object user) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/ArticulosPrincipal.fxml"));
            Parent root = cargador.load();
            ControladorArticulosPrincipal controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de ArticulosPrincipal: {0}", e.getMessage());
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se puede cargar la ventana de Articulos principal.");
        }
    }

    public void cargarArticulosBusqueda(Stage stage, Object user) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/ArticulosBusqueda.fxml"));
            Parent root = cargador.load();
            ControladorArticulosBusqueda controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de ArticulosBusqueda: {0}", e.getMessage());
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se puede cargar la ventana de Articulos busqueda.");
        }
    }
}
