/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.PedidoArticulo;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de relaciones entre
 * pedidos y artículos.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener una colección de
 * objetos {@link PedidoArticulo}, así como para crear, actualizar y borrar
 * dichas relaciones.
 * </p>
 *
 * @author Urko
 */
public interface IPedidoArticulo {

    /**
     * Obtiene una colección de todas las relaciones entre pedidos y artículos
     * disponibles.
     *
     * @return Una colección de objetos {@link PedidoArticulo} que representan
     * las relaciones existentes.
     * @throws Exception Si ocurre algún error al obtener los datos.
     */
    public Collection<PedidoArticulo> getAllPedidoArticulo() throws Exception;

    /**
     * Crea una nueva relación entre un pedido y un artículo.
     * <p>
     * Envía un objeto {@link PedidoArticulo} al sistema para que se cree un
     * nuevo registro.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} que se desea
     * crear.
     * @throws Exception Si ocurre algún error durante la creación del registro.
     */
    public void crearPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;

    /**
     * Actualiza la información de una relación existente entre un pedido y un
     * artículo.
     * <p>
     * Envía un objeto {@link PedidoArticulo} al sistema para actualizar los
     * datos de la relación.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} con la información
     * actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * registro.
     */
    public void actualizarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;

    /**
     * Borra una relación entre un pedido y un artículo.
     * <p>
     * Envía un objeto {@link PedidoArticulo} al sistema para eliminar el
     * registro correspondiente.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} que se desea
     * borrar.
     * @throws Exception Si ocurre algún error durante el borrado del registro.
     */
    public void borrarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception;
}
