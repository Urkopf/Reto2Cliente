/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.ExcepcionesUtilidad;
import crud.iu.controladores.ControladorPedidosDetalle;
import crud.objetosTransferibles.Pedido;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Factoría para la gestión de pedidos y artículos.
 * <p>
 * Esta clase implementa el patrón Singleton para asegurar que solo exista una
 * única instancia de la factoría durante la ejecución de la aplicación.
 * Proporciona métodos para obtener una implementación de la interfaz
 * {@link IPedidoArticulo} y para cargar la ventana de detalle de pedidos.
 * </p>
 *
 * @author Urko
 */
public class FactoriaPedidoArticulo {

    /**
     * Logger para la clase {@code FactoriaPedidoArticulo}.
     */
    private static final Logger LOGGER = Logger.getLogger(FactoriaPedidoArticulo.class.getName());

    /**
     * Instancia única de la factoría.
     */
    private static FactoriaPedidoArticulo instance;

    /**
     * Constructor privado para evitar la instanciación externa.
     */
    private FactoriaPedidoArticulo() {
    }

    /**
     * Retorna la instancia única de {@code FactoriaPedidoArticulo}.
     * <p>
     * Si la instancia aún no ha sido creada, se crea una nueva.
     * </p>
     *
     * @return La instancia única de {@code FactoriaPedidoArticulo}.
     */
    public static FactoriaPedidoArticulo getInstance() {
        if (instance == null) {
            instance = new FactoriaPedidoArticulo();
        }
        return instance;
    }

    /**
     * Proporciona acceso a una implementación de la interfaz
     * {@link IPedidoArticulo}.
     * <p>
     * Este método retorna una nueva instancia de {@link PedidoArticuloImpl}.
     * </p>
     *
     * @return Una implementación de {@code IPedidoArticulo}.
     */
    public IPedidoArticulo acceso() {
        return new PedidoArticuloImpl();
    }

    /**
     * Carga y muestra la ventana de detalle de un pedido.
     * <p>
     * Este método carga la vista FXML para la ventana de detalle de pedidos,
     * configura el controlador con el escenario, el usuario actual y el pedido
     * a visualizar, y posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param user El usuario actual.
     * @param pedido El pedido cuyo detalle se desea visualizar.
     */
    public void cargarPedidosDetalle(Stage stage, Object user, Pedido pedido) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/PedidosDetalle.fxml"));
            Parent root = cargador.load();
            ControladorPedidosDetalle controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.setPedido(pedido);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Pedidos Detalle: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }
}
