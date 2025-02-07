/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Almacen;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de almacenes.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener colecciones de
 * almacenes, así como para crear, actualizar y borrar relaciones asociadas a
 * los almacenes.
 * </p>
 *
 * @author Sergio
 */
public interface IAlmacen {

    /**
     * Obtiene una colección con todos los almacenes disponibles.
     *
     * @return Una colección de objetos {@link Almacen} que representan todos
     * los almacenes.
     * @throws Exception Si ocurre algún error al obtener los almacenes.
     */
    public Collection<Almacen> getAllAlmacenes() throws Exception;

    /**
     * Obtiene una colección de almacenes filtrados por un identificador
     * específico.
     *
     * @param id El identificador utilizado para filtrar los almacenes.
     * @return Una colección de objetos {@link Almacen} que corresponden al
     * identificador proporcionado.
     * @throws Exception Si ocurre algún error al obtener los almacenes.
     */
    public Collection<Almacen> getAllAlmacenesById(Long id) throws Exception;

    /**
     * Crea o actualiza la relación asociada a un almacén.
     * <p>
     * Este método envía el objeto {@link Almacen} al sistema para que se cree o
     * actualice la relación correspondiente.
     * </p>
     *
     * @param almacen El objeto {@link Almacen} que contiene la información de
     * la relación.
     * @throws Exception Si ocurre algún error durante la operación de creación
     * o actualización.
     */
    public void CrearActualizarRelacion(Almacen almacen) throws Exception;

    /**
     * Borra la relación asociada a un almacén.
     * <p>
     * Este método elimina la relación que está asociada al objeto
     * {@link Almacen} proporcionado.
     * </p>
     *
     * @param almacen El objeto {@link Almacen} cuya relación se desea borrar.
     * @throws Exception Si ocurre algún error durante la operación de borrado.
     */
    public void BorrarRelacion(Almacen almacen) throws Exception;
}
