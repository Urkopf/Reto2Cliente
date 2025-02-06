/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Cliente;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de clientes.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener una colección de
 * clientes, así como para crear, actualizar y borrar clientes.
 * </p>
 *
 * @author Urko
 */
public interface ICliente {

    /**
     * Obtiene una colección de todos los clientes disponibles.
     *
     * @return Una colección de objetos {@link Cliente} que representan todos
     * los clientes.
     * @throws Exception Si ocurre algún error al obtener los clientes.
     */
    public Collection<Cliente> getAllClientes() throws Exception;

    /**
     * Crea un nuevo cliente.
     * <p>
     * Envía un objeto {@link Cliente} al sistema para crear un nuevo registro.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del cliente.
     */
    public void crearCliente(Cliente cliente) throws Exception;

    /**
     * Actualiza la información de un cliente existente.
     * <p>
     * Envía un objeto {@link Cliente} al sistema para actualizar los datos del
     * cliente.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} con la información actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * cliente.
     */
    public void actualizarCliente(Cliente cliente) throws Exception;

    /**
     * Borra un cliente.
     * <p>
     * Envía un objeto {@link Cliente} al sistema para eliminar el registro
     * correspondiente.
     * </p>
     *
     * @param cliente El objeto {@link Cliente} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del cliente.
     */
    public void borrarCliente(Cliente cliente) throws Exception;

}
