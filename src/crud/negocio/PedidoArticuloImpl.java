/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

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
    public Collection<PedidoArticulo> getAllArticulos() {
        List<PedidoArticulo> pedidoArticulos = null;
        try {
            pedidoArticulos = cliente.findAll_XML(new GenericType<List<PedidoArticulo>>() {
            });
        } catch (Exception e) {
        }
        return pedidoArticulos;
    }

    @Override
    public void crearArticulo(PedidoArticulo pedidoArticulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
