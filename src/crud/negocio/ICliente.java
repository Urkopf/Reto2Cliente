/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Cliente;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface ICliente {

    public Collection<Cliente> getAllClientes() throws Exception;

    public void crearCliente(Cliente cliente) throws Exception;

    public void actualizarCliente(Cliente cliente) throws Exception;

    public void borrarCliente(Cliente cliente) throws Exception;

}
