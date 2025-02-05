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
 *
 * @author 2dam
 */
public class MainTestPedidosPrincipal extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
        FactoriaPedidos factoriaPedidos = FactoriaPedidos.getInstance();
        FactoriaPedidoArticulo factoriaPedidoArticulo = FactoriaPedidoArticulo.getInstance();

        Trabajador trabajador = new Trabajador();
        Long id2 = 3L;
        trabajador.setId(id2);

        factoriaPedidos.cargarPedidosPrincipal(stage, trabajador, null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
