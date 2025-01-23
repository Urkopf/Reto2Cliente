/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.iu.controladores.ControladorPedidosBusqueda;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Usuario;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author 2dam
 */
public class FactoriaPedidos {

    private static final Logger LOGGER = Logger.getLogger(FactoriaPedidos.class.getName());

    private static FactoriaPedidos instance;

    private FactoriaPedidos() {
    }

    public static FactoriaPedidos getInstance() {
        if (instance == null) {
            instance = new FactoriaPedidos();
        }

        return instance;
    }

    public IPedido acceso() {
        return new PedidoImpl();
    }

    //Ventanas
    public void cargarPedidosPrincipal(Stage stage, Object user, Collection<Pedido> pedidoBusqueda) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/PedidosPrincipal.fxml"));
            Parent root = cargador.load();
            ControladorPedidosPrincipal controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.setBusqueda(pedidoBusqueda);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventnaa de PedidosPrincipal: {0}", e.getMessage());
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se puede cargar la ventana de Pedidos principal.");
        }
    }

    public void cargarPedidosBusqueda(Stage stage, Object user) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/PedidosBusqueda.fxml"));
            Parent root = cargador.load();
            ControladorPedidosBusqueda controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventnaa de Busqueda: {0}", e.getMessage());
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se puede cargar la ventana de Busqueda.");
        }
    }

}
