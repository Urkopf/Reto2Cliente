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
 * Clase base para controladores con menú.
 */
public abstract class ControladorMenuVentanas {

    protected Stage stage;

    @FXML
    protected MenuItem opcionIrPedidos;
    @FXML
    protected MenuItem opcionIrArticulos;
    @FXML
    protected MenuItem opcionCerrarSesion;
    @FXML
    protected MenuItem opcionSalir;
    @FXML
    protected MenuItem opcionVolver;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Configura el menú basado en el contexto actual.
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

    @FXML
    private void handleMenuVolver(ActionEvent event) {
        FactoriaUsuarios.getInstance().cargarMenuPrincipal(stage, getCurrentUser());
    }

    @FXML
    private void handleMenuCerrarSesion(ActionEvent event) {
        FactoriaUsuarios.getInstance().cargarInicioSesion(stage, "");
    }

    @FXML
    private void handleMenuSalir(ActionEvent event) {
        stage.close();
    }

    @FXML
    private void handleMenuIrPedidos(ActionEvent event) {
        FactoriaPedidos.getInstance().cargarPedidosPrincipal(stage, getCurrentUser(), null);
    }

    @FXML
    private void handleMenuIrArticulos(ActionEvent event) {
        FactoriaArticulos.getInstance().acceso(); // Implementar lógica para la vista de artículos
    }

    protected abstract Object getCurrentUser();
}
