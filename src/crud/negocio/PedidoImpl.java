/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Pedido;
import crud.rest.PedidosRestFull;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link IPedido} para la gestión de pedidos.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link PedidosRestFull}) para realizar
 * operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre objetos de tipo
 * {@link Pedido}. Las operaciones se llevan a cabo mediante llamadas a métodos
 * que envían o reciben datos en formato XML.
 * </p>
 *
 * @author Urko
 */
public class PedidoImpl implements IPedido {

    /**
     * Cliente RESTful para la comunicación con el servicio de pedidos.
     */
    private PedidosRestFull cliente = new PedidosRestFull();

    /**
     * Logger para registrar información y errores en la clase
     * {@code PedidoImpl}.
     */
    private Logger LOGGER = Logger.getLogger(PedidoImpl.class.getName());

    /**
     * Obtiene una colección de todos los pedidos disponibles.
     * <p>
     * Este método realiza una consulta al servicio RESTful para recuperar una
     * lista de objetos {@link Pedido} en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Pedido} que representan todos los
     * pedidos.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<Pedido> getAllPedidos() throws Exception {
        return cliente.findAll_XML(new GenericType<List<Pedido>>() {
        });
    }

    /**
     * Crea un nuevo pedido.
     * <p>
     * Envía un objeto {@link Pedido} al servicio RESTful para que se cree un
     * nuevo registro.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del pedido.
     */
    @Override
    public void crearPedido(Pedido pedido) throws Exception {
        cliente.create_XML(pedido);
    }

    /**
     * Actualiza la información de un pedido existente.
     * <p>
     * Envía un objeto {@link Pedido} al servicio RESTful para actualizar los
     * datos del pedido.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} con la información actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * pedido.
     */
    @Override
    public void actualizarPedido(Pedido pedido) throws Exception {
        cliente.edit_XML(pedido);
    }

    /**
     * Borra un pedido.
     * <p>
     * Envía el identificador del objeto {@link Pedido} al servicio RESTful para
     * eliminar el registro correspondiente.
     * </p>
     *
     * @param pedido El objeto {@link Pedido} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del pedido.
     */
    @Override
    public void borrarPedido(Pedido pedido) throws Exception {
        cliente.remove(pedido.getId());
    }
}
