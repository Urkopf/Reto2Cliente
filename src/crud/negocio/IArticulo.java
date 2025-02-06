/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Articulo;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de artículos.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener una colección de
 * artículos, así como para crear, actualizar y borrar artículos o sus detalles.
 * </p>
 *
 * @author Sergio
 */
public interface IArticulo {

    /**
     * Obtiene una colección con todos los artículos disponibles.
     *
     * @return Una colección de objetos {@link Articulo} que representan todos
     * los artículos.
     * @throws Exception Si ocurre algún error al obtener los artículos.
     */
    public Collection<Articulo> getAllArticulos() throws Exception;

    /**
     * Crea un nuevo artículo.
     * <p>
     * Envía un objeto {@link Articulo} al sistema para que se cree un nuevo
     * registro.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del artículo.
     */
    public void crearArticulo(Articulo articulo) throws Exception;

    /**
     * Actualiza la información de un artículo existente.
     * <p>
     * Envía un objeto {@link Articulo} al sistema para actualizar los datos del
     * artículo.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} con la información
     * actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * artículo.
     */
    public void actualizarArticulo(Articulo articulo) throws Exception;

    /**
     * Actualiza únicamente los detalles específicos de un artículo.
     * <p>
     * Envía un objeto {@link Articulo} al sistema para actualizar solo los
     * detalles del artículo.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} con los detalles a actualizar.
     * @throws Exception Si ocurre algún error durante la actualización de los
     * detalles del artículo.
     */
    public void actualizarArticuloDetalle(Articulo articulo) throws Exception;

    /**
     * Borra un artículo.
     * <p>
     * Envía un objeto {@link Articulo} al sistema para eliminar el registro
     * correspondiente.
     * </p>
     *
     * @param articulo El objeto {@link Articulo} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del artículo.
     */
    public void borrarArticulo(Articulo articulo) throws Exception;
}
