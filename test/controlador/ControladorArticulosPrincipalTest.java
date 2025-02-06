/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import auxiliarMainTest.MainTestArticulosPrincipal;
import crud.objetosTransferibles.Articulo;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

/**
 * Clase de prueba que verifica el comportamiento de la ventana principal de
 * Artículos. Se utiliza TestFX para la automatización de UI y JUnit para
 * aserciones.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorArticulosPrincipalTest extends ApplicationTest {

    // Logger para la clase actual
    private static final Logger LOGGER = Logger.getLogger(ControladorArticulosPrincipalTest.class.getName());
    // Formato de fecha para comparar y asignar fechas (dd/MM/yyyy)
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Componentes de la UI que se referencian en los tests
    private TableView<Articulo> tablaArticulos;
    private Button botonNuevo, botonReiniciar, botonBusqueda, botonEliminar, botonGuardar, botonDetalles, botonAtras;

    /**
     * Método que inicia la aplicación de prueba. Se sobreescribe para poder
     * interactuar con la ventana y obtener referencias de los componentes.
     *
     * @param stage Stage principal de JavaFX.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        new MainTestArticulosPrincipal().start(stage);
        // Espera un segundo para que se renderice la UI antes de interactuar
        sleep(1000);

        // Asignación de componentes de la UI
        tablaArticulos = lookup("#tablaArticulos").queryTableView();
        botonNuevo = lookup("#botonNuevo").queryButton();
        botonReiniciar = lookup("#botonReiniciar").queryButton();
        botonBusqueda = lookup("#botonBusqueda").queryButton();
        botonEliminar = lookup("#botonEliminar").queryButton();
        botonGuardar = lookup("#botonGuardar").queryButton();
        botonDetalles = lookup("#botonDetalles").queryButton();
        botonAtras = lookup("#botonAtras").queryButton();
    }

    /**
     * Constructor vacío de la clase de prueba.
     */
    public ControladorArticulosPrincipalTest() {
    }

    /**
     * Test que crea un artículo nuevo y verifica que sea añadido correctamente
     * a la tabla.
     */
    @Test
    //@Ignore
    public void test_A_CrearArticulo() {

        // Clic en el botón "Reiniciar" para asegurarnos de un estado inicial conocido
        clickOn(botonReiniciar);

        // Si no hay filas seleccionadas, los botones Detalles y Eliminar deben estar deshabilitados
        tablaArticulos.getSelectionModel().clearSelection();
        sleep(300);
        assertTrue("Detalles debería estar deshabilitado sin selección",
                botonDetalles.isDisabled());
        assertTrue("Eliminar debería estar deshabilitado sin selección",
                botonEliminar.isDisabled());

        // Obtenemos el conteo inicial de filas en la tabla
        int RowCount = tablaArticulos.getItems().size();

        // Hacer clic en el botón "Nuevo Artículo"
        clickOn(botonNuevo);

        // El artículo nuevo se inserta al inicio; se selecciona la fila 0
        tablaArticulos.getSelectionModel().select(0);
        tablaArticulos.scrollTo(0);

        // Con 1 fila seleccionada, los botones Detalles y Eliminar deben habilitarse
        assertFalse("Detalles debería estar habilitado con 1 fila seleccionada",
                botonDetalles.isDisabled());
        assertFalse("Eliminar debería estar habilitado con 1 fila seleccionada",
                botonEliminar.isDisabled());

        // Obtenemos todas las filas visibles de la tabla
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

        /*
         * Se asume que el orden de columnas es:
         *   1: Nombre
         *   2: Precio
         *   3: Fecha (reposicion)
         *   4: Descripción
         *   5: Stock
         */
        // Celda 1: Nombre - asignar un nombre único para el artículo
        String nombreArticulo = "Test de prueba";
        doubleClickOn(celdas.get(1)); // Activa el modo edición en la celda
        // Se espera que aparezca un TextField en la celda
        TextField nombreField = lookup(".text-field").query();
        nombreField.clear();
        write(nombreArticulo);
        push(KeyCode.ENTER);

        // Celda 2: Precio
        doubleClickOn(celdas.get(2)); // activa edición
        Spinner<?> spinnerPrecio = lookup(".spinner").query(); // obtención del Spinner
        TextField precioField = spinnerPrecio.getEditor();     // editor del Spinner
        precioField.clear();
        write("60");  // nuevo precio
        push(KeyCode.ENTER);

        // Celda 3: Fecha (reposicion)
        doubleClickOn(celdas.get(3)); // activa el modo edición en la celda de la fecha
        DatePicker datePicker = lookup(".date-picker").query();
        TextField dateEditor = datePicker.getEditor();
        dateEditor.clear();
        // Escribe la fecha en formato "dd/MM/yyyy"
        write("10/10/2025");
        push(KeyCode.ENTER);

        // Celda 4: Descripción
        clickOn(celdas.get(4));
        write("Descripción de prueba");
        push(KeyCode.ENTER);

        // Celda 5: Stock
        clickOn(celdas.get(5));
        write("10");
        push(KeyCode.ENTER);

        // Si la tabla tiene más de 1 fila, probamos a seleccionar más de 1 fila (Ctrl+Click)
        if (RowCount > 1) {
            press(KeyCode.CONTROL).clickOn(filas.get(1)).release(KeyCode.CONTROL);
            sleep(300);

            // Con selección múltiple, Detalles se deshabilita y Eliminar se habilita
            assertTrue("Detalles debería estar deshabilitado con varias filas",
                    botonDetalles.isDisabled());
            assertFalse("Eliminar debería estar habilitado con varias filas",
                    botonEliminar.isDisabled());
        }

        // Guardamos
        clickOn(botonGuardar);
    }

    /**
     * Test para comprobar que el artículo recién creado existe con los valores
     * correctos.
     */
    @Test
    //@Ignore
    public void test_B_ComprobarNuevoArticulo() {
        boolean existe;
        existe = tablaArticulos.getItems().stream().anyMatch(p
                -> "Test de prueba".equals(p.getNombre())
                && Double.compare(60, p.getPrecio()) == 0
                && dateFormat.format(p.getFechaReposicion()).equals("10/10/2025")
                && "Descripción de prueba".equals(p.getDescripcion())
                && p.getStock() == 101);
        // Verifica que el artículo con esos datos exista
        assertTrue("No se encontró el pedido recién creado en la tabla", existe);
    }

    /**
     * Test para editar un artículo en la tabla y comprobar que se guarda
     * correctamente.
     */
    @Test
    //@Ignore
    public void test_C_EditarArticulo() {
        // Verifica que haya al menos un artículo para editar
        assertFalse("No hay artículos para editar", tablaArticulos.getItems().isEmpty());

        // Selecciona la primera fila y asegura que sea visible
        tablaArticulos.getSelectionModel().select(0);
        tablaArticulos.scrollTo(0);

        // Obtiene las filas visibles
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles en la tabla", filas.isEmpty());

        // La fila a editar es la primera fila visible
        Node filaSeleccionada = filas.get(0);

        // Obtiene y ordena las celdas por posición horizontal
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        /*
         * Orden de columnas:
         *   0: ID (no editable)
         *   1: Nombre
         *   2: Precio
         *   3: Fecha
         *   4: Descripción
         *   5: Stock
         */
        // Edición de Nombre (columna 1)
        String nuevoNombre = "ArticuloModificado";
        doubleClickOn(celdas.get(1)); // activa edición
        TextField nombreField = lookup(".text-field").query();
        nombreField.clear();
        write(nuevoNombre);
        push(KeyCode.ENTER);

        // Clic en botón Guardar
        clickOn(botonGuardar);

        // Verifica que el artículo se modificó correctamente
        boolean existe;
        existe = tablaArticulos.getItems().stream().anyMatch(p
                -> "ArticuloModificado".equals(p.getNombre())
                && Double.compare(750, p.getPrecio()) == 0
                && dateFormat.format(p.getFechaReposicion()).equals("10/01/2024")
                && "Ordenador portátil gama media".equals(p.getDescripcion())
                && p.getStock() == 50);

        assertTrue("El articulo no se modifico correctamente", existe);
    }

    /**
     * Test para eliminar una fila seleccionada de la tabla.
     */
    @Test
    //@Ignore
    public void test_D_BorrarFila() {
        // Conteo inicial de filas
        int totalFilasInicial = tablaArticulos.getItems().size();
        assertTrue("La tabla debe tener al menos una fila para esta prueba.", totalFilasInicial > 0);

        // Seleccionar la primera fila
        Node fila = lookup(".table-row-cell").nth(0).query();
        clickOn(fila);

        // Clic en el botón Eliminar
        clickOn(botonEliminar);

        //Manejar la alerta
        // Verificamos que aparece la alerta por fecha inválida
        verifyThat("No puede borrar el articulo, porque esta en un pedido.", NodeMatchers.isVisible());
        clickOn("Aceptar");

        // 1. Obtener el pedido con el ID más grande
        ObservableList<Articulo> items = tablaArticulos.getItems();
        assertFalse("La tabla está vacía", items.isEmpty());

        Articulo ultimoPedido = items.stream()
                .max(Comparator.comparing(Articulo::getId))
                .orElseThrow(() -> new AssertionError("No se encontró ningún articulo en la tabla"));

        // 2. Forzar al TableView a renderizar la fila del último pedido
        interact(() -> {
            tablaArticulos.scrollTo(ultimoPedido);
            tablaArticulos.getSelectionModel().select(ultimoPedido);
        });
        sleep(500);
        // Obtenemos la primera fila visible
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        // 4. Calcular el índice del 'ultimoPedido' en la lista
        int rowIndex = tablaArticulos.getItems().indexOf(ultimoPedido);

        // 5. Seleccionar la fila en base a ese índice
        Node filaSeleccionada = filas.get(rowIndex);

        clickOn(filaSeleccionada);

        // Clic en el botón Eliminar
        clickOn(botonEliminar);

        // Guardar cambios
        clickOn(botonGuardar);

        // Comprueba que el artículo anteriormente editado ya no está
        boolean existe;
        existe = tablaArticulos.getItems().stream().anyMatch(p
                -> "Test de prueba".equals(p.getNombre())
                && Double.compare(60, p.getPrecio()) == 0
                && dateFormat.format(p.getFechaReposicion()).equals("10/10/2025")
                && "Descripción de prueba".equals(p.getDescripcion())
                && p.getStock() == 101);

        // Debe ser false, ya que el artículo se ha eliminado
        assertFalse("El articulo no se elimino correctamente", existe);
    }

    /**
     * Test para ingresar una fecha inválida (anterior a la actual) y comprobar
     * que aparece la alerta.
     */
    @Test
    //@Ignore
    public void test_E_FechaInvalida() {
        // Verifica que haya filas en la tabla
        int totalFilas = tablaArticulos.getItems().size();
        assertTrue("No hay filas en la tabla para probar fecha inválida", totalFilas > 0);

        // Seleccionamos la última fila
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaArticulos.scrollTo(rowIndex);
            tablaArticulos.getSelectionModel().clearSelection();
            tablaArticulos.getSelectionModel().select(rowIndex);
        });

        // Obtenemos la fila visible
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        // Tomamos la fila seleccionada, que debería ser la última
        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Double click en la celda de la fecha (columna 3 en este ejemplo)
        doubleClickOn(celdas.get(3));

        // Se obtiene el DatePicker que se mostró
        DatePicker datePicker = lookup(".date-picker").query();

        // Se construye una fecha inválida (ayer)
        LocalDate fechaInvalida = LocalDate.now().minusDays(1);

        // Convertimos la fecha inválida a String con formato dd/MM/yyyy
        String fechaStr = new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(Date.from(fechaInvalida.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Accedemos al editor del DatePicker
        TextField editor = datePicker.getEditor();
        interact(() -> {
            editor.clear();
            editor.appendText(fechaStr);
        });
        push(KeyCode.ENTER);

        // Verificamos que aparece la alerta por fecha inválida
        verifyThat("No se permiten fechas anteriores al día actual.", NodeMatchers.isVisible());
        clickOn("Aceptar");
    }

    /**
     * Test para comprobar que el botón "Eliminar" permanece deshabilitado
     * cuando no hay selección.
     */
    @Test
    //@Ignore
    public void test_F_BorrarSinSeleccionarFila() {
        // Limpiar la selección
        interact(() -> tablaArticulos.getSelectionModel().clearSelection());

        // Verificamos que el botón "Eliminar" está deshabilitado
        assertTrue("El botón de eliminar debería estar deshabilitado sin selección", botonEliminar.isDisabled());
    }

    /**
     * Test para comprobar el diálogo de alerta de cambios sin guardar al pulsar
     * "Atrás".
     */
    @Test
    //@Ignore
    public void test_G_AlertaCambiosSinGuardar() {
        int totalFilas = tablaArticulos.getItems().size();
        assertTrue("No hay filas para probar cambios sin guardar", totalFilas > 0);

        // Seleccionamos la última fila
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaArticulos.scrollTo(rowIndex);
            tablaArticulos.getSelectionModel().clearSelection();
            tablaArticulos.getSelectionModel().select(rowIndex);
        });

        // Obtenemos la fila visible y ordenamos sus celdas
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Editamos la celda 2 (por ejemplo, "Nombre" o algún campo)
        doubleClickOn(celdas.get(1));
        TextField nombreField = lookup(".text-field").query();
        interact(() -> {
            nombreField.clear();
            nombreField.appendText("Cambio sin guardar G");
        });
        push(KeyCode.ENTER);

        // Pulsamos el botón Atrás
        clickOn(botonAtras);

        // Verificamos la alerta de cambios sin guardar
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());

        // Elegimos "No Guardar" para descartar cambios
        clickOn("Cancelar");

        // Confirmamos que seguimos en la vista de la tabla
        verifyThat("#tablaArticulos", NodeMatchers.isVisible());
    }

    /**
     * Test para comprobar el diálogo de cambios sin guardar al pulsar
     * "Búsqueda" (similar a Reiniciar).
     */
    @Test
    //@Ignore
    public void test_H_CheckDeAlertasSinGuardar() {
        int totalFilas = tablaArticulos.getItems().size();
        assertTrue("No hay filas para probar cambios sin guardar", totalFilas > 0);

        // Seleccionamos la última fila
        int rowIndex = totalFilas - 1;
        interact(() -> {
            tablaArticulos.scrollTo(rowIndex);
            tablaArticulos.getSelectionModel().clearSelection();
            tablaArticulos.getSelectionModel().select(rowIndex);
        });

        // Obtenemos la fila visible y ordenamos celdas
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles", filas.isEmpty());

        Node filaSeleccionada = filas.get(Math.min(rowIndex, filas.size() - 1));
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        // Editamos una celda (índice 2, por ejemplo)
        doubleClickOn(celdas.get(1));
        TextField campoNombre = lookup(".text-field").query();
        interact(() -> {
            campoNombre.clear();
            campoNombre.appendText("Nombre test Comprobacion");
        });
        push(KeyCode.ENTER);

        // Pulsamos el botón Búsqueda
        clickOn(botonBusqueda);

        // Verificamos la alerta de cambios sin guardar
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", NodeMatchers.isVisible());

        // Seleccionamos "No Guardar" para quedarnos en la vista
        clickOn("Cancelar");

        // Verificamos que seguimos en la vista de la tabla
        verifyThat("#tablaArticulos", NodeMatchers.isVisible());
    }

}
