/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Cliente;
import crud.rest.ClienteRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link ICliente} para la gestión de clientes.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link ClienteRestFull}) para realizar
 * operaciones de consulta, creación, actualización y borrado de objetos
 * {@link Cliente}.
 * </p>
 *
 * @author Urko
 */
public class ClienteImpl implements ICliente {

    /**
     * Cliente RESTful para la comunicación con el servicio de clientes.
     */
    private ClienteRestFull clienteRest;

    /**
     * Constructor que inicializa el cliente RESTful para clientes.
     */
    public ClienteImpl() {
        clienteRest = new ClienteRestFull();
    }

    /**
     * Obtiene todos los clientes disponibles.
     * <p>
     * Realiza una consulta al servicio RESTful para recuperar una colección de
     * clientes en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Cliente}.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<Cliente> getAllClientes() throws Exception {
        List<Cliente> clientes = null;
        clientes = clienteRest.findAll_XML(new GenericType<List<Cliente>>() {
        });
        return clientes;
    }

    /**
     * Crea un nuevo cliente.
     * <p>
     * Envía un objeto {@link Cliente} al servicio RESTful para ser creado.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación.
     */
    @Override
    public void crearCliente(Cliente cliente) throws Exception {
        clienteRest.create_XML(cliente);
    }

    /**
     * Actualiza un cliente existente.
     * <p>
     * Envía un objeto {@link Cliente} al servicio RESTful para actualizar la
     * información del cliente.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} con los datos actualizados.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    @Override
    public void actualizarCliente(Cliente cliente) throws Exception {
        clienteRest.edit_XML(cliente);
    }

    /**
     * Borra un cliente.
     * <p>
     * Envía el identificador del objeto {@link Cliente} al servicio RESTful
     * para eliminar el cliente correspondiente.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado.
     */
    @Override
    public void borrarCliente(Cliente cliente) throws Exception {
        clienteRest.remove(cliente.getId());
    }

}
