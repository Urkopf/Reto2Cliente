/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Trabajador;
import crud.rest.TrabajadorRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link ITrabajador} para la gestión de
 * trabajadores.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link TrabajadorRestFull}) para
 * realizar operaciones CRUD (Crear, Leer, Actualizar y Borrar) sobre objetos de
 * tipo {@link Trabajador}. Las operaciones se realizan mediante el envío y
 * recepción de datos en formato XML.
 * </p>
 *
 * @author Sergio
 */
public class TrabajadorImpl implements ITrabajador {

    /**
     * Cliente RESTful para la comunicación con el servicio de trabajadores.
     */
    private TrabajadorRestFull cliente;

    /**
     * Constructor que inicializa el cliente RESTful para trabajadores.
     */
    public TrabajadorImpl() {
        cliente = new TrabajadorRestFull();
    }

    /**
     * Obtiene una colección de todos los trabajadores disponibles.
     * <p>
     * Realiza una consulta al servicio RESTful para recuperar una lista de
     * objetos {@link Trabajador} en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Trabajador}.
     * @throws Exception Si ocurre algún error al obtener los trabajadores.
     */
    @Override
    public Collection<Trabajador> getAllTrabajadores() throws Exception {
        List<Trabajador> trabajadores = null;
        trabajadores = cliente.findAll_XML(new GenericType<List<Trabajador>>() {
        });
        return trabajadores;
    }

    /**
     * Crea un nuevo trabajador.
     * <p>
     * Envía un objeto {@link Trabajador} al servicio RESTful para crear un
     * nuevo registro.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del
     * trabajador.
     */
    @Override
    public void crearTrabajador(Trabajador trabajador) throws Exception {
        cliente.create_XML(trabajador);
    }

    /**
     * Actualiza la información de un trabajador existente.
     * <p>
     * Envía un objeto {@link Trabajador} al servicio RESTful para actualizar
     * los datos del trabajador.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} con la información
     * actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * trabajador.
     */
    @Override
    public void actualizarTrabajador(Trabajador trabajador) throws Exception {
        cliente.edit_XML(trabajador);
    }

    /**
     * Borra un trabajador.
     * <p>
     * Envía el identificador del objeto {@link Trabajador} al servicio RESTful
     * para eliminar el registro correspondiente.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del
     * trabajador.
     */
    @Override
    public void borrarTrabajador(Trabajador trabajador) throws Exception {
        cliente.remove(trabajador.getId());
    }
}
