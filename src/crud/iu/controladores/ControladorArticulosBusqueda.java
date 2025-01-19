package crud.iu.controladores;

import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador para la ventana principal de Pedidos.
 */
public class ControladorArticulosBusqueda implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosBusqueda.class.getName());
    private Stage stage;
    private Usuario usuario;

    // Elementos FXML
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBuscar;

    private Trabajador userTrabajador;

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestión de Articulos Busqueda");
        // Configurar la escena y mostrar la ventana
        LOGGER.info("Inicializando la escena principal");
        stage.show();  // Mostrar el escenario

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando controlador ArticulosBusqueda");

    }

    public void setUser(Object user) {
        if (user != null) {
            this.userTrabajador = new Trabajador();
            this.userTrabajador = (Trabajador) user;
            LOGGER.info("Usuario asignado: " + userTrabajador.getNombre());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        LOGGER.info("Stage asignado.");
    }

}
