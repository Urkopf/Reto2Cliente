/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Pedido;
import crud.rest.PedidosRestFull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class PedidoImpl implements IPedido {

    private PedidosRestFull cliente = new PedidosRestFull();

    private Logger LOGGER = Logger.getLogger(PedidoImpl.class.getName());

    @Override
    public Collection<Pedido> getAllPedidos() throws LogicaNegocioException {
        try {
            return cliente.findAll_XML(new GenericType<List<Pedido>>() {
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los pedidos desde el servidor REST", e);
            //throw new LogicaNegocioException("Error");
            return new ArrayList<>(); // Devuelve una lista vacía si falla la conexión

        }

    }

    @Override
    public void crearPedido(Pedido pedido) throws LogicaNegocioException {
        try {
            cliente.create_XML(pedido);
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

    @Override
    public void actualizarPedido(Pedido pedido) throws LogicaNegocioException {
        try {
            cliente.edit_XML(pedido);
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

    @Override
    public void borrarPedido(Pedido pedido) throws LogicaNegocioException {
        try {
            cliente.remove(pedido.getId());
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }
}
