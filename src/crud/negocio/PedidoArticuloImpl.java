/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.PedidoArticulo;
import crud.rest.PedidoArticulosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class PedidoArticuloImpl implements IPedidoArticulo {

    private PedidoArticulosRestFull cliente;

    public PedidoArticuloImpl() {
        cliente = new PedidoArticulosRestFull();
    }

    @Override
    public Collection<PedidoArticulo> getAllPedidoArticulo() throws Exception {
        List<PedidoArticulo> pedidoArticulos = null;

        pedidoArticulos = cliente.findAll_XML(new GenericType<List<PedidoArticulo>>() {
        });

        return pedidoArticulos;
    }

    @Override
    public void crearPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {

        cliente.create_XML(pedidoArticulo);

    }

    @Override
    public void actualizarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {

        cliente.edit_XML(pedidoArticulo);

    }

    @Override
    public void borrarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {

        cliente.remove(pedidoArticulo.getId());

    }

}
