/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliarMainTest;

import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Categoria;
import crud.objetosTransferibles.Departamento;
import crud.objetosTransferibles.Trabajador;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 * Clase principal de prueba para la visualización de la interfaz de gestión de
 * artículos.
 *
 * <p>
 * Esta clase extiende de {@link Application} y se encarga de lanzar una
 * aplicación JavaFX que carga la pantalla principal de artículos a través de la
 * factoría correspondiente. Se simula una sesión de usuario creando un objeto
 * {@link Trabajador} con parámetros predefinidos.
 * </p>
 *
 * @author Sergio
 */
public class MainTestArticulosPrincipal extends Application {

    /**
     * Método principal que arranca la aplicación JavaFX.
     *
     * <p>
     * Este método invoca a {@code launch(args)}, el cual inicia el ciclo de
     * vida de la aplicación, lo que a su vez llamará al método
     * {@link #start(Stage)}.
     * </p>
     *
     * @param args Argumentos de línea de comandos pasados a la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa y muestra la interfaz principal de artículos.
     *
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li>Obtiene la instancia singleton de la factoría de artículos mediante
     * {@link FactoriaArticulos#getInstance()}.</li>
     * <li>Crea un objeto {@link Trabajador} configurado con parámetros
     * predefinidos para simular la sesión de un usuario.</li>
     * <li>Llama al método {@code cargarArticulosPrincipal} de la factoría de
     * artículos para mostrar la pantalla principal, pasando como parámetros el
     * escenario actual, el objeto trabajador y un valor nulo para el tercer
     * parámetro.</li>
     * </ul>
     *
     * @param stage El escenario principal (ventana) de la aplicación.
     * @throws Exception Si ocurre algún error durante la inicialización o carga
     * de la interfaz.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Obtener la instancia singleton de la factoría de artículos.
        FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();

        // Creación y configuración del objeto Trabajador con parámetros predefinidos.
        Trabajador trabajador = new Trabajador(
                Departamento.ALMACENAMIENTO,
                Categoria.GESTION_INVENTARIO,
                "a@a.es",
                "12345678A",
                "Sergio",
                "Carrer Delagarza, 7, 69º B",
                "Madrid",
                "45879",
                "A12345678",
                true);

        // Cargar la pantalla principal de artículos utilizando la factoría.
        factoriaArticulos.cargarArticulosPrincipal(stage, trabajador, null);
    }
}
