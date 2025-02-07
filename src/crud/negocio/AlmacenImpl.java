/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Almacen;
import crud.rest.AlmacenRestFull;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link IAlmacen} para la gestión de almacenes.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link AlmacenRestFull}) para realizar
 * operaciones de consulta y modificación sobre los almacenes.
 * </p>
 *
 * @author Sergio
 */
public class AlmacenImpl implements IAlmacen {

    /**
     * Logger para la clase {@code AlmacenImpl}.
     */
    private static final Logger LOGGER = Logger.getLogger(AlmacenImpl.class.getName());

    /**
     * Cliente RESTful para la comunicación con el servicio de almacenes.
     */
    private AlmacenRestFull cliente;

    /**
     * Constructor que inicializa el cliente RESTful para almacenes.
     */
    public AlmacenImpl() {
        cliente = new AlmacenRestFull();
    }

    /**
     * Obtiene todos los almacenes.
     * <p>
     * Realiza una consulta al servicio RESTful para obtener una colección de
     * almacenes en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Almacen}.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<Almacen> getAllAlmacenes() throws Exception {
        List<Almacen> almacenes = null;

        LOGGER.log(Level.INFO, "Estoy en la Implementacion completa");
        almacenes = cliente.findAll_XML(new GenericType<List<Almacen>>() {
        });

        return almacenes;
    }

    /**
     * Obtiene los almacenes asociados a un identificador específico.
     * <p>
     * Realiza una consulta al servicio RESTful para obtener una colección de
     * almacenes filtrados por su identificador.
     * </p>
     *
     * @param id El identificador para filtrar los almacenes.
     * @return Una colección de objetos {@link Almacen} que corresponden al
     * identificador dado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public Collection<Almacen> getAllAlmacenesById(Long id) throws Exception {
        List<Almacen> almacenes;

        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", id);
        almacenes = cliente.findById_XML(new GenericType<List<Almacen>>() {
        }, id);

        return almacenes;
    }

    /**
     * Crea o actualiza la relación de un almacén.
     * <p>
     * Este método envía un objeto {@link Almacen} al servicio RESTful para
     * crear o actualizar su relación.
     * </p>
     *
     * @param almacen El objeto {@link Almacen} que contiene la información a
     * crear o actualizar.
     * @throws Exception Si ocurre algún error durante la operación.
     */
    @Override
    public void CrearActualizarRelacion(Almacen almacen) throws Exception {

        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", almacen.getId());
        cliente.createRelacion_XML(almacen);
    }

    /**
     * Borra la relación de un almacén.
     * <p>
     * Este método envía un objeto {@link Almacen} al servicio RESTful para
     * eliminar la relación existente.
     * </p>
     *
     * @param almacen El objeto {@link Almacen} cuya relación se desea eliminar.
     * @throws Exception Si ocurre algún error durante la operación.
     */
    @Override
    public void BorrarRelacion(Almacen almacen) throws Exception {
        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", almacen.getId());
        cliente.remove_Relacion(almacen);
    }

}
