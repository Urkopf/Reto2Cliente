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
 *
 * @author 2dam
 */
public class Reto2_CRUD_Cliente extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
        FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
        FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();
        Cliente cliente = new Cliente();
        cliente.setCif("A12345678");
        Long id = 1L;
        cliente.setId(id);

        Trabajador trabajador = new Trabajador();
        Long id2 = 3L;
        trabajador.setId(id2);

        Pedido pedido = new Pedido();
        Long id3 = 1L;
        pedido.setId(id3);
        pedido.setCifCliente("A12345678");
        pedido.setDireccion("Carrer Delagarza, 7, 69º B");
        // Crear la fecha específica
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaPedido = sdf.parse("2024-12-15");
        pedido.setFechaPedido(fechaPedido);
        pedido.setEstado(Estado.PREPARACION);
        pedido.setCliente(cliente);

        factoria.cargarInicioSesion(stage, "");
        //factoriaPedidos.cargarPedidosPrincipal(stage, cliente);
        //factoriaPedidos.cargarPedidosPrincipal(stage, trabajador, null);
        //factoriaPedidoArticulo.cargarPedidosDetalle(stage, cliente, pedido);

    }

}
