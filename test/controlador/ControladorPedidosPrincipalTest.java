package controlador;

import auxiliarMainTest.MainTestPedidosPrincipal;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.junit.Ignore;
import org.junit.runner.RunWith;

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

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosPrincipalTest.class.getName());
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
     * Test B: Crear un nuevo pedido. Se pulsa "Nuevo", se edita la última fila
     * para configurar: - CIF (ComboBox) en columna 1: seleccionar "B87654321" -
     * Dirección (TextField) en columna 2: "Calle Test, 20, 25º A" - Fecha
     * (DatePicker) en columna 3: "31/12/2025" - Estado (ComboBox) en columna 4:
     * seleccionar "COMPLETADO" Luego se pulsa "Guardar" y se verifica que el
     * pedido se creó.
     */
    @Test
    public void testA_createNewOrder() {
        clickOn(botonNuevo);
        sleep(500); // Esperar a que se añada la nueva fila

        // Verificar que se añadió una nueva fila
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No se añadió una nueva fila tras pulsar 'Nuevo'", totalFilas > 0);

        // Seleccionar la primera fila
        tablaPedidos.getSelectionModel().select(0);
        tablaPedidos.scrollTo(0);

        // Obtener todas las filas visibles
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        // La fila recién creada estará en la primera posición
        Node filaSeleccionada = filas.get(0);

        // Obtener todas las celdas de la fila y ordenarlas según la posición horizontal
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // (1) Editar celda CIF (ComboBox)
        doubleClickOn(celdas.get(1));
        clickOn(celdas.get(1));
        sleep(200); // Esperar a que el ComboBox esté listo
        ComboBox<String> combo = lookup(".combo-box").query();
        interact(() -> {
            combo.getSelectionModel().select("B87654321");
        });

        // (2) Editar celda Dirección (TextField)
        doubleClickOn(celdas.get(2));
        TextField nombreField = lookup(".text-field").query();
        nombreField.clear();
        write("Calle Test, 20, 25º A");
        sleep(200); // Esperar a que el texto se escriba
        push(KeyCode.ENTER);
        sleep(200); // Esperar a que se confirme la edición

        // (3) Editar celda Fecha (DatePicker)
        tablaPedidos.scrollTo(0);
        doubleClickOn(celdas.get(3));
        sleep(200); // Esperar a que el DatePicker esté listo
        DatePicker datePicker = lookup(".date-picker").query();
        LocalDate nuevaFecha = LocalDate.of(2025, 12, 31); // 31 de diciembre de 2025
        datePicker.setValue(nuevaFecha);
        push(KeyCode.ENTER);
        sleep(200); // Esperar a que la fecha se aplique

        // (4) Editar celda Estado (ComboBox)
        doubleClickOn(celdas.get(4));
        sleep(200); // Esperar a que el ComboBox esté listo
        ComboBox<Estado> combo2 = lookup(".combo-box").query();
        combo2.getSelectionModel().select(Estado.COMPLETADO);
        push(KeyCode.ENTER);
        sleep(200); // Esperar a que la selección se aplique

        // Verificar que el pedido se creó correctamente
        boolean creado = tablaPedidos.getItems().stream().anyMatch(p
                -> p.getCifCliente().equals("B87654321")
                && p.getDireccion().equals("Calle Test, 20, 25º A")
                && p.getEstado() == Estado.COMPLETADO
                && dateFormat.format(p.getFechaPedido()).equals("31/12/2025")
        );
        assertTrue("El nuevo pedido no se creó correctamente", creado);
    }

    @Ignore
    @Test
    public void testB_readNewOrder() {

        clickOn(botonReiniciar);
        sleep(200);

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
    @Ignore
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
    @Ignore
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
    @Ignore
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
    @Ignore
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
    @Ignore
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
    @Ignore
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
