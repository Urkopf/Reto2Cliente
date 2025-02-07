/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.ExcepcionesUtilidad;
import crud.iu.controladores.ControladorPedidosBusqueda;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Pedido;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Factoría para la gestión de pedidos.
 * <p>
 * Esta clase implementa el patrón Singleton para asegurar que exista una única
 * instancia de la factoría durante la ejecución de la aplicación. Proporciona
 * métodos para obtener una implementación de la interfaz {@link IPedido} y para
 * cargar las ventanas relacionadas con la gestión de pedidos, como la ventana
 * principal y la de búsqueda.
 * </p>
 *
 * @author Urko
 */
public class FactoriaPedidos {

    /**
     * Logger para la clase {@code FactoriaPedidos}.
     */
    private static final Logger LOGGER = Logger.getLogger(FactoriaPedidos.class.getName());

    /**
     * Instancia única de la factoría.
     */
    private static FactoriaPedidos instance;

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private FactoriaPedidos() {
    }

    /**
     * Retorna la instancia única de {@code FactoriaPedidos}.
     * <p>
     * Si la instancia aún no ha sido creada, se crea una nueva.
     * </p>
     *
     * @return La instancia única de {@code FactoriaPedidos}.
     */
    public static FactoriaPedidos getInstance() {
        if (instance == null) {
            instance = new FactoriaPedidos();
        }
        return instance;
    }

    /**
     * Proporciona acceso a una implementación de la interfaz {@link IPedido}.
     * <p>
     * Este método retorna una nueva instancia de {@link PedidoImpl}.
     * </p>
     *
     * @return Una implementación de {@code IPedido}.
     */
    public IPedido acceso() {
        return new PedidoImpl();
    }

    /**
     * Carga y muestra la ventana principal de pedidos.
     * <p>
     * Este método carga la vista FXML para la ventana principal de pedidos,
     * configura el controlador con el escenario, el usuario actual y la
     * colección de pedidos obtenidos en la búsqueda, y posteriormente
     * inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param user El usuario actual.
     * @param pedidoBusqueda La colección de pedidos resultantes de la búsqueda.
     */
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
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Pedidos Principal: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana de búsqueda de pedidos.
     * <p>
     * Este método carga la vista FXML para la ventana de búsqueda de pedidos,
     * configura el controlador con el escenario y el usuario actual, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario en el cual se mostrará la ventana.
     * @param user El usuario actual.
     */
    public void cargarPedidosBusqueda(Stage stage, Object user) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/PedidosBusqueda.fxml"));
            Parent root = cargador.load();
            ControladorPedidosBusqueda controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Pedidos Busqueda: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }
}
