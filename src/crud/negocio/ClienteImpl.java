/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Cliente;
import crud.rest.ClienteRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class ClienteImpl implements ICliente {

    private ClienteRestFull clienteRest;

    public ClienteImpl() {
        clienteRest = new ClienteRestFull();
    }

    @Override
    public Collection<Cliente> getAllClientes() throws LogicaNegocioException {
        List<Cliente> clientes = null;
        try {
            clientes = clienteRest.findAll_XML(new GenericType<List<Cliente>>() {
            });
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
        return clientes;
    }

    @Override
    public void crearCliente(Cliente cliente) throws LogicaNegocioException {
        try {
            clienteRest.create_XML(cliente);
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

    @Override
    public void actualizarCliente(Cliente cliente) throws LogicaNegocioException {
        try {
            clienteRest.edit_XML(cliente);
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

    @Override
    public void borrarCliente(Cliente cliente) throws LogicaNegocioException {
        try {
            clienteRest.remove(cliente.getId());
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

}
