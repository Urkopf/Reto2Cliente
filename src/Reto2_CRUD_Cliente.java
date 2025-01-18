/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import crud.negocio.FactoriaPedidos;
import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        //FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
        FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
        Cliente cliente = new Cliente();
        cliente.setCif("A12345678");
        Long id = 1L;
        cliente.setId(id);

        Trabajador trabajador = new Trabajador();
        Long id2 = 3L;
        trabajador.setId(id2);

        //factoria.cargarInicioSesion(stage, "");
        //factoriaPedidos.cargarPedidosPrincipal(stage, cliente);
        factoriaPedidos.cargarPedidosPrincipal(stage, trabajador);
    }

}
