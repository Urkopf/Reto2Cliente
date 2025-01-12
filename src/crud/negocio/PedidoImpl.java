/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Pedido;
import crud.rest.PedidosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class PedidoImpl implements IPedido {

    private PedidosRestFull cliente;

    @Override
    public Collection<Pedido> getAllPedidos() {
        List<Pedido> pedidos = null;
        try {
            pedidos = cliente.findAll_XML(new GenericType<List<Pedido>>() {
            });
        } catch (Exception e) {
        }
        return pedidos;
    }

    @Override
    public void crearPedido(Pedido pedido) {
        try {
            cliente.create_XML(pedido);

        } catch (Exception e) {
        }
    }

}
