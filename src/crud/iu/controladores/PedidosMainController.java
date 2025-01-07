/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.iu.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author Urko
 */
public class PedidosMainController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(PedidosMainController.class.getName());
    private Stage stage = new Stage();
    //FACTORIA
    private Usuario usuario;

    @FXML
    private Button botonNuevo;
    @FXML
    private Button botonReiniciar;
    @FXML
    private Button botonBusqueda;
    @FXML
    private TableView tablaPedidos;
    @FXML
    private Button botonAtras;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonDetalles;
    @FXML
    private Button botonGuardar;
    @FXML
    private MenuItem opcionIrPedidos;

    @FXML
    private void handleButtonAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setUser(Usuario usuario) {
        this.usuario = usuario;
    }

    public void initStage(Parent root) {

    }

}
