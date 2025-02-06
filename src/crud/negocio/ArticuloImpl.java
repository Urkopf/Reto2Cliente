/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Articulo;
import crud.rest.ArticulosRestFull;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link IArticulo} para la gestión de artículos.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link ArticulosRestFull}) para
 * realizar operaciones de consulta, creación, actualización y borrado de
 * artículos.
 * </p>
 *
 * @author Sergio
 */
public class ArticuloImpl implements IArticulo {

    /**
     * Logger para la clase {@code ArticuloImpl}.
     */
    private static final Logger LOGGER = Logger.getLogger(ArticuloImpl.class.getName());

    /**
     * Cliente RESTful para la comunicación con el servicio de artículos.
     */
    private ArticulosRestFull cliente;

    /**
     * Constructor que inicializa el cliente RESTful para artículos.
     */
    public ArticuloImpl() {
        cliente = new ArticulosRestFull();
    }

    /**
     * Obtiene todos los artículos disponibles.
     * <p>
     * Este método realiza una consulta al servicio RESTful para recuperar una
     * colección de artículos en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Articulo}.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<Articulo> getAllArticulos() throws Exception {
        List<Articulo> articulos = null;
        articulos = cliente.findAll_XML(new GenericType<List<Articulo>>() {
        });
        return articulos;
    }

    /**
     * Crea un nuevo artículo.
     * <p>
     * Este método envía un objeto {@link Articulo} al servicio RESTful para ser
     * creado.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación.
     */
    @Override
    public void crearArticulo(Articulo articulo) throws Exception {
        cliente.create_XML(articulo);
    }

    /**
     * Actualiza un artículo existente.
     * <p>
     * Este método envía un objeto {@link Articulo} al servicio RESTful para
     * actualizar la información del artículo.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} con los datos actualizados.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    @Override
    public void actualizarArticulo(Articulo articulo) throws Exception {
        cliente.edit_XML(articulo);
    }

    /**
     * Actualiza el detalle de un artículo.
     * <p>
     * Este método envía un objeto {@link Articulo} al servicio RESTful para
     * actualizar únicamente los detalles del artículo.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} con los detalles actualizados.
     * @throws Exception Si ocurre algún error durante la actualización del
     * detalle.
     */
    @Override
    public void actualizarArticuloDetalle(Articulo articulo) throws Exception {
        cliente.editDetalle_XML(articulo);
    }

    /**
     * Borra un artículo.
     * <p>
     * Este método envía el identificador del objeto {@link Articulo} al
     * servicio RESTful para eliminar el artículo correspondiente.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado.
     */
    @Override
    public void borrarArticulo(Articulo articulo) throws Exception {
        cliente.remove(articulo.getId());
    }

}
