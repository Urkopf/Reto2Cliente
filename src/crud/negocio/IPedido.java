/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Pedido;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface IPedido {

    public Collection<Pedido> getAllPedidos() throws LogicaNegocioException;

    public void crearPedido(Pedido pedido) throws LogicaNegocioException;

    public void actualizarPedido(Pedido pedido) throws LogicaNegocioException;

    public void borrarPedido(Pedido pedido) throws LogicaNegocioException;
}
