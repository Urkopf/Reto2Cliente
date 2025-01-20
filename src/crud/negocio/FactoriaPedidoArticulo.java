/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.iu.controladores.ControladorPedidosDetalle;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Pedido;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author 2dam
 */
public class FactoriaPedidoArticulo {

    private static final Logger LOGGER = Logger.getLogger(FactoriaPedidoArticulo.class.getName());

    private static FactoriaPedidoArticulo instance;

    private FactoriaPedidoArticulo() {
    }

    public static FactoriaPedidoArticulo getInstance() {
        if (instance == null) {
            instance = new FactoriaPedidoArticulo();
        }

        return instance;
    }

    public IPedidoArticulo acceso() {
        return new PedidoArticuloImpl();
    }

    //Ventanas
    public void cargarPedidosDetalle(Stage stage, Object user, Pedido pedido) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/PedidosDetalle.fxml"));
            Parent root = cargador.load();
            ControladorPedidosDetalle controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            //controlador.setPedido(pedido);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventnaa de PedidosDetalle: {0}", e.getMessage());
            showErrorDialog(Alert.AlertType.ERROR, "Error", "No se puede cargar la ventana de Pedidos Detalle.");
        }
    }

}
