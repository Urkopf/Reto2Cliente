/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.PedidoArticulo;
import crud.rest.PedidoArticulosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link IPedidoArticulo} para la gestión de las
 * relaciones entre pedidos y artículos.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link PedidoArticulosRestFull}) para
 * realizar operaciones de consulta, creación, actualización y borrado de
 * registros de tipo {@link PedidoArticulo}.
 * </p>
 *
 * @author Urko
 */
public class PedidoArticuloImpl implements IPedidoArticulo {

    /**
     * Cliente RESTful para la comunicación con el servicio de pedido-artículos.
     */
    private PedidoArticulosRestFull cliente;

    /**
     * Constructor que inicializa el cliente RESTful para pedido-artículos.
     */
    public PedidoArticuloImpl() {
        cliente = new PedidoArticulosRestFull();
    }

    /**
     * Obtiene una colección de todas las relaciones entre pedidos y artículos.
     * <p>
     * Este método realiza una consulta al servicio RESTful para recuperar una
     * lista de objetos {@link PedidoArticulo} en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link PedidoArticulo}.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<PedidoArticulo> getAllPedidoArticulo() throws Exception {
        List<PedidoArticulo> pedidoArticulos = null;
        pedidoArticulos = cliente.findAll_XML(new GenericType<List<PedidoArticulo>>() {
        });
        return pedidoArticulos;
    }

    /**
     * Crea una nueva relación entre un pedido y un artículo.
     * <p>
     * Envía un objeto {@link PedidoArticulo} al servicio RESTful para que se
     * cree un nuevo registro.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} que se desea
     * crear.
     * @throws Exception Si ocurre algún error durante la creación.
     */
    @Override
    public void crearPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {
        cliente.create_XML(pedidoArticulo);
    }

    /**
     * Actualiza la información de una relación existente entre un pedido y un
     * artículo.
     * <p>
     * Envía un objeto {@link PedidoArticulo} al servicio RESTful para
     * actualizar los datos del registro correspondiente.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} con la información
     * actualizada.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    @Override
    public void actualizarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {
        cliente.edit_XML(pedidoArticulo);
    }

    /**
     * Borra una relación entre un pedido y un artículo.
     * <p>
     * Envía el identificador del objeto {@link PedidoArticulo} al servicio
     * RESTful para eliminar el registro correspondiente.
     * </p>
     *
     * @param pedidoArticulo El objeto {@link PedidoArticulo} que se desea
     * borrar.
     * @throws Exception Si ocurre algún error durante el borrado.
     */
    @Override
    public void borrarPedidoArticulo(PedidoArticulo pedidoArticulo) throws Exception {
        cliente.remove(pedidoArticulo.getId());
    }
}
