/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import crud.negocio.FactoriaPedidoArticulo;
import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import crud.objetosTransferibles.Trabajador;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal que lanza la aplicación JavaFX para el CRUD de clientes.
 *
 * <p>
 * Esta aplicación configura las instancias necesarias para gestionar usuarios,
 * pedidos y detalles de pedidos. Se crean objetos de tipo
 * {@link Cliente}, {@link Trabajador} y {@link Pedido} con valores
 * predefinidos. Posteriormente, se carga la pantalla de inicio de sesión a
 * través de la fábrica de usuarios.
 * </p>
 *
 * @author Urko
 */
public class Reto2_CRUD_Cliente extends Application {

    /**
     * Punto de entrada principal de la aplicación.
     *
     * <p>
     * Este método inicia la aplicación JavaFX llamando a {@code launch(args)},
     * lo que a su vez invoca el método {@link #start(Stage)}.
     * </p>
     *
     * @param args Argumentos de línea de comandos para la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa y muestra la interfaz de usuario principal.
     *
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li>Obtiene las instancias singleton de las fábricas de usuarios, pedidos
     * y detalles de pedidos.</li>
     * <li>Crea y configura un objeto {@link Cliente} asignándole un CIF y un
     * identificador.</li>
     * <li>Crea y configura un objeto {@link Trabajador} asignándole un
     * identificador.</li>
     * <li>Crea y configura un objeto {@link Pedido} con:
     * <ul>
     * <li>Identificador.</li>
     * <li>CIF del cliente.</li>
     * <li>Dirección de entrega.</li>
     * <li>Fecha del pedido, establecida a partir de una cadena en formato
     * {@code yyyy-MM-dd}.</li>
     * <li>Estado del pedido, en este caso {@link Estado#PREPARACION}.</li>
     * <li>Vinculación del pedido con el cliente creado.</li>
     * </ul>
     * </li>
     * <li>Carga la pantalla de inicio de sesión a través de la fábrica de
     * usuarios.</li>
     * </ul>
     * Nota: Las llamadas para cargar otras vistas (pedidos principales y
     * detalles de pedidos) están comentadas.
     * </p>
     *
     * @param stage El escenario principal (ventana) de la aplicación.
     * @throws Exception Si ocurre algún error durante la inicialización o carga
     * de la aplicación.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Obtener instancias singleton de las fábricas necesarias.
        FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
        FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
        FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();

        // Creación y configuración del objeto Cliente.
        Cliente cliente = new Cliente();
        cliente.setCif("A12345678");
        Long id = 1L;
        cliente.setId(id);

        // Creación y configuración del objeto Trabajador.
        Trabajador trabajador = new Trabajador();
        Long id2 = 3L;
        trabajador.setId(id2);

        // Creación y configuración del objeto Pedido.
        Pedido pedido = new Pedido();
        Long id3 = 1L;
        pedido.setId(id3);
        pedido.setCifCliente("A12345678");
        pedido.setDireccion("Carrer Delagarza, 7, 69º B");

        // Crear la fecha específica del pedido utilizando el formato "yyyy-MM-dd".
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaPedido = sdf.parse("2024-12-15");
        pedido.setFechaPedido(fechaPedido);
        pedido.setEstado(Estado.PREPARACION);
        pedido.setCliente(cliente);

        // Carga de la pantalla de inicio de sesión.
        factoria.cargarInicioSesion(stage, "");

    }
}
