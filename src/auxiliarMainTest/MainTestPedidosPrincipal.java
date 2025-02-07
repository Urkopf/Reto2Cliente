/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliarMainTest;

import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 * Clase principal de prueba para la carga de la interfaz de gestión de pedidos.
 *
 * <p>
 * Esta clase extiende de {@link Application} y se utiliza para iniciar una
 * aplicación JavaFX que muestra la pantalla principal de pedidos. Se crea una
 * instancia de {@link Trabajador} para simular la sesión de un usuario y se
 * utiliza la factoría {@link FactoriaPedidos} para cargar la vista
 * correspondiente.
 * </p>
 *
 * @author Urko
 */
public class MainTestPedidosPrincipal extends Application {

    /**
     * Inicializa y muestra la pantalla principal de pedidos.
     *
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li>Obtiene las instancias singleton de {@link FactoriaPedidos} y
     * {@link FactoriaPedidoArticulo}.</li>
     * <li>Crea un objeto {@link Trabajador} y le asigna un identificador
     * predefinido.</li>
     * <li>Llama al método {@code cargarPedidosPrincipal} de
     * {@link FactoriaPedidos} para cargar la interfaz de pedidos, pasando como
     * parámetros el escenario actual, el trabajador y un valor nulo para el
     * tercer parámetro.</li>
     * </ul>
     *
     * @param stage El escenario principal (ventana) de la aplicación.
     * @throws Exception Si ocurre algún error durante la inicialización o carga
     * de la interfaz.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
        FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
        FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();

        // Creación y configuración del objeto Trabajador.
        Trabajador trabajador = new Trabajador();
        Long id2 = 3L;
        trabajador.setId(id2);

        // Cargar la pantalla principal de pedidos utilizando la factoría correspondiente.
        factoriaPedidos.cargarPedidosPrincipal(stage, trabajador, null);
    }

    /**
     * Punto de entrada de la aplicación.
     *
     * <p>
     * Este método lanza la aplicación JavaFX, lo que inicia el ciclo de vida de
     * la misma y llama internamente al método {@link #start(Stage)}.
     * </p>
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
