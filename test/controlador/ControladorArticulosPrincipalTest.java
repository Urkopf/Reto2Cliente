/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import auxiliarMainTest.MainTestArticulosPrincipal;
import crud.objetosTransferibles.Articulo;
import java.util.ArrayList;
import java.util.Comparator;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

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
    public void testCreateArticuloAtBeginning() {
        // Contar filas iniciales
        int initialRowCount = tablaArticulos.getItems().size();

        // Hacer clic en el botón "Nuevo Artículo"
        clickOn("#botonNuevo");

        // Como el nuevo artículo se inserta al inicio, seleccionamos la fila 0
        tablaArticulos.getSelectionModel().select(0);
        tablaArticulos.scrollTo(0);

        // Obtener todas las filas visibles (usando Java 8)
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
        String nombreArticulo = "ArticuloTest";
        doubleClickOn(celdas.get(1)); // Activa el modo edición en la celda
        // Se espera que aparezca un TextField en la celda
        TextField nombreField = lookup(".text-field").query();
        nombreField.clear();
        write(nombreArticulo);
        push(KeyCode.ENTER);

        // Celda 2: Precio
        doubleClickOn(celdas.get(2)); // Activa el modo edición en la celda del precio
        // La celda de precio usa un Spinner; obtenemos el spinner (se asume que es el único en la escena)
        Spinner<?> spinnerPrecio = lookup(".spinner").query();
        // El Spinner tiene un editor que es un TextField:
        TextField precioField = spinnerPrecio.getEditor();
        precioField.clear();
        write("50.0");
        push(KeyCode.ENTER);

        // Celda 3: Fecha - usamos el formato dd/MM/yyyy para la fecha actual
        doubleClickOn(celdas.get(3)); // Activa el modo edición en la celda de la fecha
        DatePicker datePicker = lookup(".date-picker").query();
        TextField dateEditor = datePicker.getEditor();
        dateEditor.clear();

        // Escribe la fecha en el formato esperado por la vista (ejemplo: "dd/MM/yyyy")
        write("02/02/2025");
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

}
