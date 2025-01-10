/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Pedido;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface IPedido {

    public Collection<Pedido> getAllArticulos();

    public void crearArticulo(Pedido pedido);
}
