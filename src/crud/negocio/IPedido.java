/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Pedido;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de pedidos.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener una colección de
 * pedidos, así como para crear, actualizar y borrar pedidos.
 * </p>
 *
 * @author Urko
 */
public interface IPedido {

    /**
     * Obtiene una colección de todos los pedidos disponibles.
     *
     * @return Una colección de objetos {@link Pedido} que representan todos los
     * pedidos.
     * @throws Exception Si ocurre algún error al obtener los pedidos.
     */
    public Collection<Pedido> getAllPedidos() throws Exception;

    /**
     * Crea un nuevo pedido.
     * <p>
     * Envía un objeto {@link Pedido} al sistema para crear un nuevo registro.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del pedido.
     */
    public void crearPedido(Pedido pedido) throws Exception;

    /**
     * Actualiza la información de un pedido existente.
     * <p>
     * Envía un objeto {@link Pedido} al sistema para actualizar los datos del
     * pedido.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} con la información actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * pedido.
     */
    public void actualizarPedido(Pedido pedido) throws Exception;

    /**
     * Borra un pedido.
     * <p>
     * Envía un objeto {@link Pedido} al sistema para eliminar el registro
     * correspondiente.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del pedido.
     */
    public void borrarPedido(Pedido pedido) throws Exception;
}
