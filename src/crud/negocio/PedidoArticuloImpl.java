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
    public Collection<PedidoArticulo> getAllArticulos() throws LogicaNegocioException {
        List<PedidoArticulo> pedidoArticulos = null;
        try {
            pedidoArticulos = cliente.findAll_XML(new GenericType<List<PedidoArticulo>>() {
            });
        } catch (Exception e) {
            throw new LogicaNegocioException("");
        }
        return pedidoArticulos;
    }

    @Override
    public void crearArticulo(PedidoArticulo pedidoArticulo) throws LogicaNegocioException {
        try {
            cliente.create_XML(pedidoArticulo);
        } catch (Exception e) {
            throw new LogicaNegocioException("");
        }
    }

    @Override
    public void actualizarPPedidoArticulo(PedidoArticulo pedidoArticulo) throws LogicaNegocioException {
        try {
            cliente.edit_XML(pedidoArticulo);
        } catch (Exception e) {
            throw new LogicaNegocioException("");
        }
    }

    @Override
    public void borrarPedidoArticulo(PedidoArticulo pedidoArticulo) throws LogicaNegocioException {
        try {
            cliente.remove(pedidoArticulo.getId());
        } catch (Exception e) {
            throw new LogicaNegocioException("");
        }
    }

}
