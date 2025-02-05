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
    public Collection<Cliente> getAllClientes() throws Exception {
        List<Cliente> clientes = null;

        clientes = clienteRest.findAll_XML(new GenericType<List<Cliente>>() {
        });

        return clientes;
    }

    @Override
    public void crearCliente(Cliente cliente) throws Exception {

        clienteRest.create_XML(cliente);

    }

    @Override
    public void actualizarCliente(Cliente cliente) throws Exception {

        clienteRest.edit_XML(cliente);

    }

    @Override
    public void borrarCliente(Cliente cliente) throws Exception {

        clienteRest.remove(cliente.getId());

    }

}
