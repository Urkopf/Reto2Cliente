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
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;

/**
 * Clase de test para ControladorPedidosPrincipal.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorPedidosPrincipalTest extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(ControladorPedidosPrincipalTest.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Nodos de la UI
    private TableView<Pedido> tablaPedidos;
    private Button botonNuevo, botonReiniciar, botonBusqueda, botonEliminar, botonGuardar, botonDetalles, botonAtras;

    public void start(Stage stage) throws Exception {
        new MainTestPedidosPrincipal().start(stage);
        sleep(1000);
        tablaPedidos = lookup("#tablaPedidos").queryTableView();

        botonNuevo = lookup("#botonNuevo").queryButton();
        botonReiniciar = lookup("#botonReiniciar").queryButton();
        botonBusqueda = lookup("#botonBusqueda").queryButton();
        botonEliminar = lookup("#botonEliminar").queryButton();
        botonGuardar = lookup("#botonGuardar").queryButton();
        botonDetalles = lookup("#botonDetalles").queryButton();
        botonAtras = lookup("#botonAtras").queryButton();
    }

    // ---------------------------------------------------------------------
    // EJEMPLO: testA con creación + checks de botones y alert de confirmación
    // ---------------------------------------------------------------------
    //@Ignore
    @Test
    public void testA_createNewOrderAndCheckButtons() {
        // Reiniciamos para partir de la tabla original
        clickOn(botonReiniciar);
        sleep(300);

        // Con 0 filas seleccionadas, Detalles y Eliminar deshabilitados
        tablaPedidos.getSelectionModel().clearSelection();
        sleep(300);
        assertTrue("Detalles debería estar deshabilitado sin selección",
                botonDetalles.isDisabled());
        assertTrue("Eliminar debería estar deshabilitado sin selección",
                botonEliminar.isDisabled());

        // Creamos un nuevo pedido
        clickOn(botonNuevo);
        sleep(500);

        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No se añadió una nueva fila al pulsar 'Nuevo'", totalFilas > 0);

        // Seleccionamos la primera fila (nueva)
        tablaPedidos.getSelectionModel().select(0);
        tablaPedidos.scrollTo(0);

        // Con 1 fila seleccionada, Detalles y Eliminar habilitados
        assertFalse("Detalles debería estar habilitado con 1 fila seleccionada",
                botonDetalles.isDisabled());
        assertFalse("Eliminar debería estar habilitado con 1 fila seleccionada",
                botonEliminar.isDisabled());

        // Obtenemos la primera fila visible
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());
        Node filaSeleccionada = filas.get(0);

        // Ordenamos celdas según posición horizontal
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Celda CIF (ComboBox) -> índice 1
        doubleClickOn(celdas.get(1));
        sleep(200);
        ComboBox<String> comboCif = lookup(".combo-box").query();
        interact(() -> comboCif.getSelectionModel().select("B87654321"));
        push(KeyCode.ENTER);

        // Celda Dirección (TextField) -> índice 2
        doubleClickOn(celdas.get(2));
        TextField direccionField = lookup(".text-field").query();
        direccionField.clear();
        write("Calle Test, 20, 25º A");
        push(KeyCode.ENTER);

        // Celda Fecha (DatePicker) -> índice 3
        doubleClickOn(celdas.get(3));
        DatePicker datePicker = lookup(".date-picker").query();
        TextField editor = datePicker.getEditor();
        editor.clear();
        write("31/12/2025");
        push(KeyCode.ENTER);

        // Celda Estado (ComboBox) -> índice 4
        doubleClickOn(celdas.get(4));
        ComboBox<Estado> comboEstado = lookup(".combo-box").query();
        interact(() -> comboEstado.getSelectionModel().select(Estado.COMPLETADO));
        push(KeyCode.ENTER);

        // Probamos a seleccionar más de 1 fila si existe más filas
        if (totalFilas > 1) {
            // Ctrl+Click en la fila 1
            press(KeyCode.CONTROL).clickOn(filas.get(1)).release(KeyCode.CONTROL);
            sleep(300);

            // Con varias filas, Detalles deshabilitado, Eliminar habilitado
            assertTrue("Detalles debería estar deshabilitado con varias filas",
                    botonDetalles.isDisabled());
            assertFalse("Eliminar debería estar habilitado con varias filas",
                    botonEliminar.isDisabled());
        }

        // Guardamos
        clickOn(botonGuardar);
        sleep(500);

        // Verificamos mensaje de guardado correcto
        verifyThat("Datos Guardados correctamente.", NodeMatchers.isVisible());
        clickOn("Aceptar"); // Cerrar el Alert

    }

    // ---------------------------------------------------------------------
    // TEST B: Leer el pedido recién creado
    // ---------------------------------------------------------------------
    //@Ignore
    @Test
    public void testB_readNewOrder() {

        // Verificamos que el pedido con CIF=B87654321 existe
        boolean existe = tablaPedidos.getItems().stream().anyMatch(p
                -> "B87654321".equals(p.getCifCliente())
                && "Calle Test, 20, 25º A".equals(p.getDireccion())
                && p.getEstado() == Estado.COMPLETADO
                && dateFormat.format(p.getFechaPedido()).equals("31/12/2025")
        );
        assertTrue("No se encontró el pedido recién creado en la tabla", existe);
    }

    // ---------------------------------------------------------------------
    // TEST C: Actualizar la dirección de ese pedido
    // ---------------------------------------------------------------------
    //@Ignore
    @Test
    public void testC_updateOrder() {
        // 1. Obtener el pedido con el ID más grande
        ObservableList<Pedido> items = tablaPedidos.getItems();
        assertFalse("La tabla está vacía", items.isEmpty());

        Pedido ultimoPedido = items.stream()
                .max(Comparator.comparing(Pedido::getId))
                .orElseThrow(() -> new AssertionError("No se encontró ningún pedido en la tabla"));

        // 2. Forzar al TableView a renderizar la fila del último pedido
        interact(() -> {
            tablaPedidos.scrollTo(ultimoPedido);
            tablaPedidos.getSelectionModel().select(ultimoPedido);
        });
        sleep(500);
        // Obtenemos la primera fila visible
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        // 4. Calcular el índice del 'ultimoPedido' en la lista
        int rowIndex = tablaPedidos.getItems().indexOf(ultimoPedido);

        // 5. Seleccionar la fila en base a ese índice
        Node filaSeleccionada = filas.get(rowIndex);

        // Ordenamos celdas según posición horizontal
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // 5. Editar la dirección
        doubleClickOn(celdas.get(2));
        TextField direccionField = lookup(".text-field").query();
        direccionField.clear();
        write("Calle Modificada, 10");
        type(KeyCode.ENTER);

        // 6. Guardar cambios
        clickOn(botonGuardar);
        sleep(500);
        verifyThat("Datos Guardados correctamente.", NodeMatchers.isVisible());
        clickOn("Aceptar");

        boolean existe = tablaPedidos.getItems().stream().anyMatch(p
                -> "B87654321".equals(p.getCifCliente())
                && "Calle Modificada, 10".equals(p.getDireccion())
                && p.getEstado() == Estado.COMPLETADO
                && dateFormat.format(p.getFechaPedido()).equals("31/12/2025")
        );
        assertTrue("El pedido con CIF B87654321 no se ha modificado correctamente", existe);
    }

    // ---------------------------------------------------------------------
    // TEST D: Eliminar el último pedido con CIF "B87654321"
    // ---------------------------------------------------------------------
    //@Ignore
    @Test
    public void testD_deleteOrder() {
        // 1. Obtener el pedido con el ID más grande
        ObservableList<Pedido> items = tablaPedidos.getItems();
        assertFalse("La tabla está vacía", items.isEmpty());

        Pedido ultimoPedido = items.stream()
                .max(Comparator.comparing(Pedido::getId))
                .orElseThrow(() -> new AssertionError("No se encontró ningún pedido en la tabla"));

        // 2. Forzar al TableView a renderizar la fila del último pedido
        interact(() -> {
            tablaPedidos.scrollTo(ultimoPedido);
            tablaPedidos.getSelectionModel().select(ultimoPedido);
        });
        sleep(500);

        // Eliminamos
        clickOn(botonEliminar);
        sleep(500);
        // Si sale algún alerta de confirmación interna (pedido con artículos), se asume "OK"
        // Por si la ventana del controller pregunta "¿Está seguro?"
        if (lookup("¿Está seguro de que desea eliminar el pedido?").tryQuery().isPresent()) {
            clickOn("Aceptar");
        }
        if (lookup("Pedidos borrados de la tabla actual. Si quiere aplicar los cambios en la base de datos no olvide guardar los cambios").tryQuery().isPresent()) {
            clickOn("Aceptar");
        }
        // Tras eliminar de la tabla, hay cambios sin guardar

        // Guardamos para confirmar en BD
        clickOn(botonGuardar);
        sleep(500);
        verifyThat("Datos Guardados correctamente.", NodeMatchers.isVisible());
        clickOn("Aceptar");

        boolean existe = tablaPedidos.getItems().stream().anyMatch(p
                -> "B87654321".equals(p.getCifCliente())
                && "Calle Modificada, 10".equals(p.getDireccion())
                && p.getEstado() == Estado.COMPLETADO
                && dateFormat.format(p.getFechaPedido()).equals("31/12/2025")
        );
        assertFalse("El pedido con CIF B87654321 sigue apareciendo tras eliminar", existe);
    }

    // ---------------------------------------------------------------------
    // TEST E: Introducir una fecha inválida (anterior a hoy)
    // ---------------------------------------------------------------------
    @Test
    public void testE_invalidDateEntry() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas en la tabla para probar fecha inválida", totalFilas > 0);

        // Seleccionamos la última fila (para asegurarnos de que existe)
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaPedidos.scrollTo(rowIndex);
            tablaPedidos.getSelectionModel().clearSelection();
            tablaPedidos.getSelectionModel().select(rowIndex);
        });
        sleep(300);

        // Obtenemos la fila visible
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        // Suponiendo que el orden de las celdas es:
        // Índice 0: ID, 1: CIF, 2: Dirección, 3: Fecha, 4: Estado, 5: Total (si existe)
        // Obtenemos la celda de Fecha (índice 3)
        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Double click en la celda de Fecha para entrar en modo edición
        doubleClickOn(celdas.get(3));
        sleep(300);

        // Consultamos el DatePicker que se ha mostrado
        DatePicker datePicker = lookup(".date-picker").query();
        // Convertimos una fecha inválida: ayer
        LocalDate fechaInvalida = LocalDate.now().minusDays(1);
        // Convertir a String con formato dd/MM/yyyy (por ejemplo, "01/01/2000" o usando fechaInvalida)
        // Aquí usaremos la fecha de ayer en formato dd/MM/yyyy
        String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(Date.from(fechaInvalida.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Actuamos sobre el editor del DatePicker
        TextField editor = datePicker.getEditor();
        interact(() -> {
            editor.clear();
            editor.appendText(fechaStr);
        });
        push(KeyCode.ENTER);
        sleep(500);

        // Verificamos que aparece el Alert con el mensaje "Fecha inválida"
        verifyThat("No se permiten fechas anteriores al día actual.", NodeMatchers.isVisible());
        clickOn("Aceptar");

    }

    // ---------------------------------------------------------------------
    // TEST F: Intentar eliminar sin selección
    // ---------------------------------------------------------------------
    @Test
    public void testF_deleteWithoutSelection() {
        // Aseguramos que no hay ninguna fila seleccionada
        interact(() -> tablaPedidos.getSelectionModel().clearSelection());
        sleep(300);

        // Verificamos que el botón "Eliminar" está deshabilitado
        assertTrue("El botón de eliminar debería estar deshabilitado sin selección", botonEliminar.isDisabled());

    }

    // ---------------------------------------------------------------------
    // TEST G: Cambiar algo y pulsar "Atrás" -> debe saltar el alert de confirmación
    // ---------------------------------------------------------------------
    @Test
    public void testG_unsavedChangesExitConfirmation() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas para probar cambios sin guardar", totalFilas > 0);

        // Seleccionamos la última fila
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaPedidos.scrollTo(rowIndex);
            tablaPedidos.getSelectionModel().clearSelection();
            tablaPedidos.getSelectionModel().select(rowIndex);
        });
        sleep(300);

        // Obtenemos la fila visible y ordenamos las celdas para editar la Dirección (índice 2)
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Editamos la celda de Dirección (índice 2)
        doubleClickOn(celdas.get(2));
        sleep(300);
        TextField direccionField = lookup(".text-field").query();
        interact(() -> {
            direccionField.clear();
            direccionField.appendText("Cambio sin guardar G");
        });
        push(KeyCode.ENTER);
        sleep(300);

        // Pulsamos el botón Atrás
        clickOn(botonAtras);
        sleep(500);

        // Verificamos que aparece el diálogo de cambios sin guardar
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());
        // Cancelamos la acción para continuar en la misma vista
        clickOn("Cancelar");
        sleep(300);

        // Verificamos que seguimos en la vista de la tabla
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());

    }

    // ---------------------------------------------------------------------
    // TEST H: Cambiar algo y pulsar "Búsqueda" o "Reiniciar" -> alert de cambios
    // ---------------------------------------------------------------------
    @Test
    public void testH_cancelUnsavedChangesDialog() {
        int totalFilas = tablaPedidos.getItems().size();
        assertTrue("No hay filas para probar cambios sin guardar", totalFilas > 0);

        // Seleccionamos la última fila
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaPedidos.scrollTo(rowIndex);
            tablaPedidos.getSelectionModel().clearSelection();
            tablaPedidos.getSelectionModel().select(rowIndex);
        });
        sleep(300);

        // Obtenemos la fila visible y ordenamos las celdas para editar la Dirección (índice 2)
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Editamos la celda de Dirección (índice 2)
        doubleClickOn(celdas.get(2));
        sleep(300);
        TextField direccionField = lookup(".text-field").query();
        interact(() -> {
            direccionField.clear();
            direccionField.appendText("Cambio sin guardar H");
        });
        push(KeyCode.ENTER);
        sleep(300);

        // Pulsamos el botón Búsqueda (puedes usar también Reiniciar, aquí ejemplificamos Búsqueda)
        clickOn(botonBusqueda);
        sleep(500);

        // Verificamos que aparece el diálogo de cambios sin guardar
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());
        // Cancelamos la acción para permanecer en la vista actual
        clickOn("Cancelar");
        sleep(300);

        // Confirmamos que seguimos en la vista de la tabla
        verifyThat("#tablaPedidos", NodeMatchers.isVisible());

    }
}
