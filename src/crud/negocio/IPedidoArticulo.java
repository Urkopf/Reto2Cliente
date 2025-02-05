/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.PedidoArticulo;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface IPedidoArticulo {

    public Collection<PedidoArticulo> getAllPedidoArticulo() throws Exception;

    public void crearPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;

    public void actualizarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;

    public void borrarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;
}
