package crud.iu.controladores;

import crud.negocio.FactoriaArticulos;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Clase base para los controladores de ventanas que incluyen un menú.
 * <p>
 * Esta clase proporciona la configuración y el manejo de eventos para los
 * elementos del menú, permitiendo la navegación entre vistas y la gestión de la
 * sesión del usuario.
 * </p>
 *
 * @author Urko
 */
public abstract class ControladorMenuVentanas {

    /**
     * Escenario principal asociado al controlador.
     */
    protected Stage stage;

    /**
     * Elemento de menú para la opción "Ir a Pedidos".
     */
    @FXML
    protected MenuItem opcionIrPedidos;

    /**
     * Elemento de menú para la opción "Ir a Artículos".
     */
    @FXML
    protected MenuItem opcionIrArticulos;

    /**
     * Elemento de menú para la opción "Cerrar Sesión".
     */
    @FXML
    protected MenuItem opcionCerrarSesion;

    /**
     * Elemento de menú para la opción "Salir".
     */
    @FXML
    protected MenuItem opcionSalir;

    /**
     * Elemento de menú para la opción "Volver".
     */
    @FXML
    protected MenuItem opcionVolver;

    /**
     * Asigna el escenario (Stage) actual al controlador.
     *
     * @param stage el escenario actual
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Configura el menú según el contexto de la ventana actual y el tipo de
     * usuario. Se realizan las siguientes configuraciones:
     * <ul>
     * <li>Si el usuario es una instancia de
     * {@link crud.objetosTransferibles.Cliente}, se oculta la opción de
     * artículos.</li>
     * <li>Se deshabilitan las opciones del menú correspondientes a la ventana
     * actualmente activa.</li>
     * </ul>
     *
     * @param ventanaActual el nombre de la ventana actual (por ejemplo,
     * "Pedidos" o "Artículos")
     * @param user el usuario actual
     */
    protected void configurarMenu(String ventanaActual, Object user) {
        if (user instanceof Cliente) {
            // Ocultar opción de artículos si es cliente
            opcionIrArticulos.setVisible(false);
        }

        // Deshabilitar opciones según la ventana actual
        opcionIrPedidos.setDisable("Pedidos".equals(ventanaActual));
        opcionIrArticulos.setDisable("Artículos".equals(ventanaActual));
    }

    /**
     * Maneja el evento del menú para volver a la ventana principal.
     * <p>
     * Llama a la factoría de usuarios para cargar la vista del menú principal,
     * pasando el usuario actual.
     * </p>
     *
     * @param event el evento de acción generado al seleccionar la opción
     * "Volver"
     */
    @FXML
    private void handleMenuVolver(ActionEvent event) {
        FactoriaUsuarios.getInstance().cargarMenuPrincipal(stage, getCurrentUser());
    }

    /**
     * Maneja el evento del menú para cerrar la sesión actual.
     * <p>
     * Llama a la factoría de usuarios para cargar la pantalla de inicio de
     * sesión.
     * </p>
     *
     * @param event el evento de acción generado al seleccionar la opción
     * "Cerrar Sesión"
     */
    @FXML
    private void handleMenuCerrarSesion(ActionEvent event) {
        FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
    }

    /**
     * Maneja el evento del menú para salir de la aplicación.
     * <p>
     * Cierra el escenario actual.
     * </p>
     *
     * @param event el evento de acción generado al seleccionar la opción
     * "Salir"
     */
    @FXML
    private void handleMenuSalir(ActionEvent event) {
        stage.close();
    }

    /**
     * Maneja el evento del menú para ir a la ventana de pedidos.
     * <p>
     * Llama a la factoría de pedidos para cargar la vista principal de pedidos,
     * pasando el usuario actual.
     * </p>
     *
     * @param event el evento de acción generado al seleccionar la opción "Ir a
     * Pedidos"
     */
    @FXML
    private void handleMenuIrPedidos(ActionEvent event) {
        FactoriaPedidos.getInstance().cargarPedidosPrincipal(stage, getCurrentUser(), null);
    }

    /**
     * Maneja el evento del menú para ir a la ventana de artículos.
     * <p>
     * Llama a la factoría de artículos para ejecutar la lógica de acceso a la
     * vista de artículos.
     * </p>
     *
     * @param event el evento de acción generado al seleccionar la opción "Ir a
     * Artículos"
     */
    @FXML
    private void handleMenuIrArticulos(ActionEvent event) {
        FactoriaArticulos.getInstance().acceso(); // Implementar lógica para la vista de artículos
    }

    /**
     * Obtiene el usuario actual.
     * <p>
     * Este método abstracto debe ser implementado por las clases que extienden
     * {@code ControladorMenuVentanas} para retornar el usuario que está
     * utilizando la aplicación.
     * </p>
     *
     * @return el usuario actual
     */
    protected abstract Object getCurrentUser();
}
