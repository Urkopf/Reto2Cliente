/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Pedido;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class FactoriaPedidos {

    private static final Logger LOGGER = Logger.getLogger(FactoriaPedidos.class.getName());

    private static FactoriaPedidos instance;

    private FactoriaPedidos() {
    }

    public static FactoriaPedidos getInstance() {
        if (instance == null) {
            instance = new FactoriaPedidos();
        }

        return instance;
    }

    public List<Pedido> getAllPedidos() {
        return (List<Pedido>) new PedidoImpl().getAllPedidos();
    }

    public void crearPedido(Pedido pedido) {
        new PedidoImpl().crearPedido(pedido);
    }

}
