package controlador;

import auxiliarMainTest.MainTestPedidosPrincipal;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;
import java.util.stream.Collectors;

/**
 * Clase de test para ControladorPedidosPrincipal. Se inicializa la aplicación
 * una única vez y se reutiliza la misma ventana para todos los tests. Se
 * localizan nodos por fx:id y se obtiene la celda de la tabla mediante
 * nth-of-type.
 *
 * Se asume que en el FXML: - La TableView tiene fx:id="tablaPedidos" - Las
 * columnas tienen fx:id="columnaId", "columnaCif", "columnaDireccion",
 * "columnaFecha", "columnaEstado" y "columnaTotal" - Los botones tienen
 * fx:id="botonNuevo", "botonReiniciar", "botonBusqueda", "botonEliminar",
 * "botonGuardar", "botonDetalles" y "botonAtras"
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorPedidosPrincipalTest extends ApplicationTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Nodos de la UI
    private TableView<Pedido> tablaPedidos;
    // Si las columnas tienen fx:id en el FXML, se pueden obtener:
    private TableColumn<?, ?> columnaId, columnaCif, columnaDireccion, columnaFecha, columnaEstado, columnaTotal;
    private Button botonNuevo, botonReiniciar, botonBusqueda, botonEliminar, botonGuardar, botonDetalles, botonAtras;

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        // Se registra la Stage principal y se lanza la aplicación una única vez
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(MainTestPedidosPrincipal.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Como ya se lanzó la aplicación en setUpClass, aquí solo esperamos y localizamos los nodos.
        sleep(1000);
        // Localizamos la TableView con fx:id="tablaPedidos"
        tablaPedidos = lookup("#tablaPedidos").queryTableView();

        // Localizamos los botones
        botonNuevo = lookup("#botonNuevo").queryButton();
        botonReiniciar = lookup("#botonReiniciar").queryButton();
        botonBusqueda = lookup("#botonBusqueda").queryButton();
        botonEliminar = lookup("#botonEliminar").queryButton();
        botonGuardar = lookup("#botonGuardar").queryButton();
        botonDetalles = lookup("#botonDetalles").queryButton();
        botonAtras = lookup("#botonAtras").queryButton();
    }

    /**
     * Devuelve el nodo correspondiente a la celda en la fila rowIndex y columna
     * colIndex usando selectores CSS nth-of-type. Se asume que no hay columnas
     * ocultas ni reordenables. Nota: nth-of-type en CSS empieza en 1.
     */
    private Node getCellByRowColumn(int rowIndex, int colIndex) {
        String rowSelector = String.format(".table-row-cell:nth-of-type(%d)", rowIndex + 1);
        String cellSelector = String.format(".table-cell:nth-of-type(%d)", colIndex + 1);
        Node rowNode = lookup(rowSelector).query();
        return from(rowNode).lookup(cellSelector).query();
    }

    /**
     * Selecciona la fila con el índice rowIndex.
     */
    private void selectRow(int rowIndex) {
        String rowSelector = String.format(".table-row-cell:nth-of-type(%d)", rowIndex + 1);
        Node rowNode = lookup(rowSelector).query();
        clickOn(rowNode);
    }

    /**
     * Test A: Estado inicial de la ventana. Verifica que la tabla y sus
     * columnas estén visibles, que los botones tengan el estado correcto, y
     * que, sin selección, botones como "Detalles" y "Eliminar" estén
     * deshabilitados.
     */
    @Test
    public void testA_initialState() {
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());
        verifyThat("#columnaId", NodeMatchers.isVisible());
        verifyThat("#columnaCif", NodeMatchers.isVisible());
        verifyThat("#columnaDireccion", NodeMatchers.isVisible());
        verifyThat("#columnaFecha", NodeMatchers.isVisible());
        verifyThat("#columnaEstado", NodeMatchers.isVisible());
        verifyThat("#columnaTotal", NodeMatchers.isVisible());

        // Sin selección, botones de "Detalles" y "Eliminar" deben estar deshabilitados.
        verifyThat("#botonDetalles", NodeMatchers.isDisabled());
        verifyThat("#botonEliminar", NodeMatchers.isDisabled());

        // Botones "Nuevo", "Reiniciar", "Búsqueda", "Guardar", "Atrás" deberían estar habilitados.
        verifyThat("#botonNuevo", NodeMatchers.isEnabled());
        verifyThat("#botonReiniciar", NodeMatchers.isEnabled());
        verifyThat("#botonBusqueda", NodeMatchers.isEnabled());
        verifyThat("#botonGuardar", NodeMatchers.isEnabled());
        verifyThat("#botonAtras", NodeMatchers.isEnabled());
    }

    /**
     * Test B: Crear un nuevo pedido. Se pulsa "Nuevo", se edita la última fila
     * para configurar: - CIF (ComboBox) en columna 1: seleccionar "B87654321" -
     * Dirección (TextField) en columna 2: "Calle Test, 20, 25º A" - Fecha
     * (DatePicker) en columna 3: "31/12/2025" - Estado (ComboBox) en columna 4:
     * seleccionar "COMPLETADO" Luego se pulsa "Guardar" y se verifica que el
     * pedido se creó.
     */
    @Test
    public void testB_createNewOrder() {
        clickOn(botonNuevo);
        sleep(500);
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No se añadió una nueva fila tras pulsar 'Nuevo'", totalFilas > 0);
        int rowIndex = totalFilas - 1;
        // Ajusta los índices según el orden real de tus columnas:
        int colCIF = 1, colDireccion = 2, colFecha = 3, colEstado = 4;

        // Seleccionamos la última fila para editar
        selectRow(rowIndex);

        // (1) Editar celda CIF (ComboBox)
        Node cellCIF = getCellByRowColumn(rowIndex, colCIF);
        doubleClickOn(cellCIF);
        sleep(300);
        clickOn(".combo-box");
        sleep(300);
        clickOn("B87654321");
        sleep(300);
        type(KeyCode.ENTER);
        sleep(300);

        // (2) Editar celda Dirección (TextField)
        Node cellDireccion = getCellByRowColumn(rowIndex, colDireccion);
        doubleClickOn(cellDireccion);
        eraseText(50);
        write("Calle Test, 20, 25º A");
        type(KeyCode.ENTER);

        // (3) Editar celda Fecha (DatePicker)
        Node cellFecha = getCellByRowColumn(rowIndex, colFecha);
        doubleClickOn(cellFecha);
        eraseText(10);
        write("31/12/2025");
        type(KeyCode.ENTER);

        // (4) Editar celda Estado (ComboBox)
        Node cellEstado = getCellByRowColumn(rowIndex, colEstado);
        doubleClickOn(cellEstado);
        sleep(300);
        clickOn(".combo-box");
        sleep(300);
        clickOn("COMPLETADO");
        sleep(300);
        type(KeyCode.ENTER);
        sleep(300);

        clickOn(botonGuardar);
        sleep(500);

        boolean creado = tablaPedidos.getItems().stream().anyMatch(p
                -> p.getCifCliente().equals("B87654321")
                && p.getDireccion().equals("Calle Test, 20, 25º A")
                && p.getEstado() == Estado.COMPLETADO
                && dateFormat.format(p.getFechaPedido()).equals("31/12/2025")
        );
        assertTrue("El nuevo pedido no se creó correctamente", creado);
    }

    /**
     * Test C: Actualizar la dirección del último pedido con CIF "B87654321".
     */
    @Test
    public void testC_updateOrder() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla", totalFilas > 0);
        // Buscamos el último pedido con CIF "B87654321"
        List<Pedido> matching = tablaPedidos.getItems().stream()
                .filter(p -> "B87654321".equals(p.getCifCliente()))
                .collect(Collectors.toList());
        assertFalse("No se encontró ningún pedido con CIF B87654321", matching.isEmpty());
        Pedido pedidoAEditar = matching.get(matching.size() - 1);
        int rowIndex = tablaPedidos.getItems().indexOf(pedidoAEditar);

        // Se supone que la columna Dirección es la 2.
        selectRow(rowIndex);
        Node cellDireccion = getCellByRowColumn(rowIndex, 2);
        doubleClickOn(cellDireccion);
        eraseText(pedidoAEditar.getDireccion().length());
        write("Calle Modificada, 10");
        type(KeyCode.ENTER);

        clickOn(botonGuardar);
        sleep(500);

        List<Pedido> updatedMatching = tablaPedidos.getItems().stream()
                .filter(p -> "B87654321".equals(p.getCifCliente()))
                .collect(Collectors.toList());
        Pedido pedidoActualizado = updatedMatching.get(updatedMatching.size() - 1);
        assertEquals("La dirección no se actualizó correctamente", "Calle Modificada, 10", pedidoActualizado.getDireccion());
    }

    /**
     * Test D: Eliminar el último pedido con CIF "B87654321".
     */
    @Test
    public void testD_deleteOrder() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla", totalFilas > 0);
        List<Pedido> matching = tablaPedidos.getItems().stream()
                .filter(p -> "B87654321".equals(p.getCifCliente()))
                .collect(Collectors.toList());
        assertFalse("No se encontró ningún pedido con CIF B87654321", matching.isEmpty());
        Pedido pedidoAEliminar = matching.get(matching.size() - 1);
        int rowIndex = tablaPedidos.getItems().indexOf(pedidoAEliminar);
        selectRow(rowIndex);

        int totalAntes = tablaPedidos.getItems().size();
        clickOn(botonEliminar);
        sleep(500);
        // Se asume que el alert de confirmación tiene el botón con texto "OK"
        clickOn("OK");
        sleep(500);
        int totalDespues = tablaPedidos.getItems().size();
        assertEquals("El pedido no se eliminó correctamente", totalAntes - 1, totalDespues);
    }

    /**
     * Test E: Introducir una fecha inválida (anterior a hoy) en la celda Fecha
     * de la última fila y comprobar que aparece el Alert con "Fecha inválida".
     */
    @Test
    public void testE_invalidDateEntry() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla", totalFilas > 0);
        int rowIndex = totalFilas - 1;
        selectRow(rowIndex);
        // Se supone que la columna Fecha es la 3.
        Node cellFecha = getCellByRowColumn(rowIndex, 3);
        doubleClickOn(cellFecha);
        LocalDate fechaInvalida = LocalDate.now().minusDays(1);
        String fechaStr = dateFormat.format(Date.from(fechaInvalida.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eraseText(10);
        write(fechaStr);
        type(KeyCode.ENTER);
        sleep(500);
        verifyThat("Fecha inválida", NodeMatchers.isVisible());
        clickOn("OK");
    }

    /**
     * Test F: Intentar eliminar sin selección. Debe aparecer un Alert indicando
     * que se debe seleccionar al menos un pedido para eliminar.
     */
    @Test
    public void testF_deleteWithoutSelection() {
        tablaPedidos.getSelectionModel().clearSelection();
        clickOn(botonEliminar);
        sleep(500);
        verifyThat("Debe seleccionar al menos un pedido para eliminar.", NodeMatchers.isVisible());
        clickOn("OK");
    }

    /**
     * Test G: Modificar algún campo (por ejemplo, Dirección) sin guardar y
     * pulsar "Atrás". Debe aparecer un Alert preguntando "Hay cambios sin
     * guardar. ¿Qué desea hacer?" y se cancela.
     */
    @Test
    public void testG_unsavedChangesExitConfirmation() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla", totalFilas > 0);
        int rowIndex = totalFilas - 1;
        Pedido pedido = tablaPedidos.getItems().get(rowIndex);
        // Suponemos que la columna Dirección es la 2.
        selectRow(rowIndex);
        Node cellDireccion = getCellByRowColumn(rowIndex, 2);
        doubleClickOn(cellDireccion);
        eraseText(pedido.getDireccion().length());
        write("Direccion con cambio sin guardar");
        type(KeyCode.ENTER);
        clickOn(botonAtras);
        sleep(500);
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());
        clickOn("Cancel");
    }

    /**
     * Test H: Similar al anterior, pero al pulsar "Búsqueda" (o "Reiniciar") se
     * muestra el Alert y se cancela, manteniéndose en la vista.
     */
    @Test
    public void testH_cancelUnsavedChangesDialog() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla", totalFilas > 0);
        int rowIndex = totalFilas - 1;
        Pedido pedido = tablaPedidos.getItems().get(rowIndex);
        // Editamos la columna Dirección (col 2)
        selectRow(rowIndex);
        Node cellDireccion = getCellByRowColumn(rowIndex, 2);
        doubleClickOn(cellDireccion);
        eraseText(pedido.getDireccion().length());
        write("Otro cambio sin guardar");
        type(KeyCode.ENTER);
        clickOn(botonBusqueda);  // o botonReiniciar, según corresponda
        sleep(500);
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());
        clickOn("Cancel");
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());
    }
}
