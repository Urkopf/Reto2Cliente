/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import auxiliarMainTest.MainTestArticulosPrincipal;
import crud.objetosTransferibles.Articulo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Ser_090
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorArticulosPrincipalTest extends ApplicationTest {

    private TableView<Articulo> tablaArticulos;

    @Override
    public void start(Stage stage) throws Exception {
        new MainTestArticulosPrincipal().start(stage);
        tablaArticulos = lookup("#tablaArticulos").queryTableView();
    }

    public ControladorArticulosPrincipalTest() {
    }

    //Los test Aqui
    @Test
    @Ignore
    public void test_A_CrearArticulo() {
        // Contar filas iniciales
        int initialRowCount = tablaArticulos.getItems().size();

        // Hacer clic en el botón "Nuevo Artículo"
        clickOn("#botonNuevo");

        // Como el nuevo artículo se inserta al inicio, seleccionamos la fila 0
        tablaArticulos.getSelectionModel().select(0);
        tablaArticulos.scrollTo(0);

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
        doubleClickOn(celdas.get(2)); // activar edición
        // Esperar a que aparezca el Spinner para que no falle el lookup
        Spinner<?> spinnerPrecio = lookup(".spinner").query();
        TextField precioField = spinnerPrecio.getEditor();
        precioField.clear();
        write("60");  // tu nuevo precio
        push(KeyCode.ENTER);

        // Celda 3: Fecha
        doubleClickOn(celdas.get(3)); // Activa el modo edición en la celda de la fecha
        DatePicker datePicker = lookup(".date-picker").query();
        TextField dateEditor = datePicker.getEditor();
        dateEditor.clear();

        // Escribe la fecha en el formato esperado por la vista (ejemplo: "dd/MM/yyyy")
        String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        write(fechaHoy);
        push(KeyCode.ENTER);

        // Celda 3: Descripción
        clickOn(celdas.get(4));
        write("Descripción de prueba");
        push(KeyCode.ENTER);

        // Celda 4: Stock
        clickOn(celdas.get(5));
        write("10");
        push(KeyCode.ENTER);

        // Hacer clic en el botón "Guardar"
        clickOn("#botonGuardar");

        // 4. Verificar que el diálogo de confirmación aparece con el texto esperado
        //    (Ajusta la cadena a exactamente lo que muestres en tu Alert).
        verifyThat("Hay cambios sin guardar. ¿Qué desea hacer?", isVisible());

        // 5. Ahora sí, hacer clic en el botón "Guardar" del diálogo
        clickOn("Guardar");

        // Verificar que el artículo se ha añadido: la cantidad de filas debe incrementarse en 1
        int newRowCount = tablaArticulos.getItems().size();
        assertThat("El artículo debe añadirse a la tabla", newRowCount, is(initialRowCount + 1));

        // Verificar que el artículo creado existe en la tabla (usando el nombre asignado)
        @SuppressWarnings("unchecked")
        List<Articulo> articulos = (List<Articulo>) tablaArticulos.getItems();
        long coincidencias = articulos.stream()
                .filter(a -> a.getNombre() != null && a.getNombre().equals("ArticuloTest"))
                .count();
        assertThat("El artículo creado debe estar en la tabla", coincidencias, is(1L));
    }

    @Test
    //@Ignore
    public void test_B_EditarArticulo() {
        // Comprobar que existe al menos un artículo. Si no hay filas, no se puede editar nada.
        assertFalse("No hay artículos para editar", tablaArticulos.getItems().isEmpty());

        // Seleccionar la primera fila (índice 0) y asegurarnos de que se vea
        tablaArticulos.getSelectionModel().select(0);
        tablaArticulos.scrollTo(0);

        // Obtener las filas visibles
        List<Node> filas = lookup(".table-row-cell").queryAll().stream()
                .filter(Node::isVisible)
                .collect(Collectors.toList());
        assertFalse("No se encontraron filas visibles en la tabla", filas.isEmpty());

        // La fila a editar es la primera (posición 0 en la lista de filas visibles)
        Node filaSeleccionada = filas.get(0);

        // Obtener todas las celdas de la fila seleccionada y ordenarlas por su posición horizontal
        Set<Node> celdasSet = filaSeleccionada.lookupAll(".table-cell");
        List<Node> celdas = new ArrayList<>(celdasSet);
        celdas.sort(Comparator.comparingDouble(Node::getLayoutX));

        /*
     * Se asume el orden de columnas:
     *   0: ID (no editable)
     *   1: Nombre
     *   2: Precio
     *   3: Fecha
     *   4: Descripción
     *   5: Stock
         */
        // ----- Edición de Nombre (columna 1) -----
        String nuevoNombre = "ArticuloModificado";
        doubleClickOn(celdas.get(1)); // Activa edición en la celda de Nombre
        TextField nombreField = lookup(".text-field").query();
        nombreField.clear();
        write(nuevoNombre);
        push(KeyCode.ENTER);

        // ----- Edición de Precio (columna 2) -----
        doubleClickOn(celdas.get(2)); // Activa edición en la celda de Precio
        // Dado que la celda usa un Spinner, lo obtenemos:
        Spinner<?> spinnerPrecio = lookup(".spinner").query();
        TextField precioField = spinnerPrecio.getEditor();
        precioField.clear();
        write("99.99");
        push(KeyCode.ENTER);

        // ----- Edición de Fecha (columna 3) -----
        doubleClickOn(celdas.get(3)); // Activa edición en la celda de Fecha
        DatePicker datePicker = lookup(".date-picker").query();
        TextField dateEditor = datePicker.getEditor();
        dateEditor.clear();
        //Uso una fecha lejana para que no falle
        String fecha = "10/10/2025";
        write(fecha);
        push(KeyCode.ENTER);

        // ----- Edición de Descripción (columna 4) -----
        doubleClickOn(celdas.get(4)); // Activa edición en la celda de Descripción
        TextField descField = lookup(".text-field").query();
        descField.clear();
        write("Descripcion Editada");
        push(KeyCode.ENTER);

        // ----- Edición de Stock (columna 5) -----
        doubleClickOn(celdas.get(5)); // Activa edición en la celda de Stock
        // Dependiendo de tu implementación, Stock también podría ser un Spinner.
        // Aquí asumimos un TextField tras activarse la edición.
        TextField stockField = lookup(".text-field").query();
        stockField.clear();
        write("50");
        push(KeyCode.ENTER);

        // ----- Guardar cambios -----
        clickOn("#botonGuardar");

        // Ahora verificamos que los cambios estén reflejados en el modelo
        @SuppressWarnings("unchecked")
        List<Articulo> articulos = (List<Articulo>) tablaArticulos.getItems();
        // El artículo que hemos editado sigue siendo el primero en la tabla
        Articulo articuloEditado = articulos.get(0);

        // Verificamos cada campo editado (mirar Bien los errores)
        assertThat("El nombre debe ser actualizado", articuloEditado.getNombre(), is(nuevoNombre));
        assertThat("El precio debe ser 99.99", articuloEditado.getPrecio(), is(99.99));
        String fechaArticulo = new SimpleDateFormat("dd/MM/yyyy").format(articuloEditado.getFechaReposicion());
        assertThat("La fecha debe coincidir con la fecha de hoy", fechaArticulo, is(fecha));
        assertThat("La descripción debe haberse actualizado", articuloEditado.getDescripcion(), is("Descripcion Editada"));
        assertThat("El stock debe ser 50", articuloEditado.getStock(), is(50));
    }

    @Test
    @Ignore
    public void test_C_DeleteRow() {
        // Obtener el conteo inicial de filas
        int totalFilasInicial = tablaArticulos.getItems().size();
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
        int totalFilasFinal = tablaArticulos.getItems().size();
        assertEquals("La fila no fue eliminada correctamente.", totalFilasInicial - 1, totalFilasFinal);
    }

}
