package controlador;

import auxiliarMainTest.MainTestPedidosPrincipal;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Pedido;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 * Test class for ControladorPedidosPrincipal using TestFX.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorPedidosPrincipalTest extends ApplicationTest {

    private TableView<?> tablaPedidos;

    @Override
    public void start(Stage stage) throws Exception {
        new MainTestPedidosPrincipal().start(stage);
        // Localiza los componentes por su ID
        // Busca la tabla por su ID
        tablaPedidos = lookup("#tablaPedidos").queryTableView();
    }

//    @BeforeClass
//    public static void setUpClass() throws TimeoutException {
//        registerPrimaryStage();
//        setupApplication(MainClient.class);
//    }
    public ControladorPedidosPrincipalTest() {
    }

    /**
     * Verifica que la tabla se muestra correctamente al inicio.
     */
    @Test
    public void test_A_InitialState() {
        // Comprobamos que la tabla está visible
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());
        // Verificamos que la tabla no está vacía
        assertFalse("La tabla no debería estar vacía.", tablaPedidos.getItems().isEmpty());
    }

    @Test
    public void test_B_EditDireccionColumn() {
        // Seleccionamos la primera fila de la tabla
        Node fila = lookup(".table-row-cell").nth(0).query();
        assertNotNull("La fila no existe en la tabla.", fila);

        // Seleccionamos la columna de dirección (cambia el índice según corresponda)
        TableColumn<?, ?> columnaDireccion = tablaPedidos.getColumns().get(3); // Índice de la columna "Dirección"
        assertTrue("La columna no es editable.", columnaDireccion.isEditable());

        // Navegamos a la celda y la editamos
        clickOn(fila);
        type(KeyCode.TAB, 3); // Movernos a la columna de índice 3
        write("Nueva Dirección2");
        press(KeyCode.ENTER).release(KeyCode.ENTER); // Confirmamos la edición

        // Verificamos que el modelo se ha actualizado correctamente
        Pedido pedidoEditado = (Pedido) tablaPedidos.getSelectionModel().getSelectedItem();
        assertEquals("Nueva Dirección2", pedidoEditado.getDireccion());
    }

    @Test
    public void test_C_DeleteRow() {
        // Obtener el conteo inicial de filas
        int totalFilasInicial = tablaPedidos.getItems().size();
        assertTrue("La tabla debe tener al menos una fila para esta prueba.", totalFilasInicial > 0);

        // Seleccionar la primera fila
        Node fila = lookup(".table-row-cell").nth(0).query();
        clickOn(fila);

        // Presionar el botón de eliminar
        clickOn("#botonEliminar");

        // Verificar que aparece una alerta de confirmación
        verifyThat("¿Desea eliminar la fila seleccionada?", NodeMatchers.isVisible());
        clickOn("Aceptar");

        // Verificar que el conteo de filas ha disminuido
        int totalFilasFinal = tablaPedidos.getItems().size();
        assertEquals("La fila no fue eliminada correctamente.", totalFilasInicial - 1, totalFilasFinal);
    }

    @Test
    public void test_D_AddRow() {
        // Obtener el conteo inicial de filas
        int totalFilasInicial = tablaPedidos.getItems().size();

        // Presionar el botón de nuevo pedido
        clickOn("#botonNuevo");

        // Verificar que el conteo de filas ha aumentado
        int totalFilasFinal = tablaPedidos.getItems().size();
        assertEquals("No se ha añadido correctamente una nueva fila.", totalFilasInicial + 1, totalFilasFinal);
    }

    @Test
    public void test_E_NavigateToDetails() {
        // Seleccionamos una fila
        Node fila = lookup(".table-row-cell").nth(0).query();
        clickOn(fila);

        // Presionar el botón de detalles
        clickOn("#botonDetalles");

        // Verificar que se ha cambiado a la vista de detalles
        verifyThat("#vistaDetalles", NodeMatchers.isVisible());
    }

    @Test
    public void test_F_CancelChanges() {
        // Realizamos un cambio en la dirección
        test_B_EditDireccionColumn();

        // Intentamos salir presionando el botón atrás
        clickOn("#botonAtras");

        // Verificar que aparece una alerta de confirmación
        verifyThat("¿Hay cambios sin guardar?", NodeMatchers.isVisible());
        clickOn("Cancelar");

        // Verificar que seguimos en la misma vista
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());
    }

    @Test
    public void test_G_SaveChanges() {
        // Realizamos un cambio en la dirección
        test_B_EditDireccionColumn();

        // Guardamos los cambios
        clickOn("#botonGuardar");

        // Verificar que no hay cambios pendientes
        verifyThat("Cambios guardados exitosamente.", NodeMatchers.isVisible());
    }
}
