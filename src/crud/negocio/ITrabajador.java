/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Trabajador;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para la gestión de trabajadores.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener una colección de
 * trabajadores, así como para crear, actualizar y borrar trabajadores.
 * </p>
 *
 * @author Sergio
 */
public interface ITrabajador {

    /**
     * Obtiene una colección de todos los trabajadores disponibles.
     * <p>
     * Nota: El nombre del método es "getAllTrabajadores" a pesar de que se
     * espera     * que retorne objetos de tipo {@link Trabajador}.
     * </p>
     *
     * @return Una colección de objetos {@link Trabajador} que representan todos
     * los trabajadores.
     * @throws Exception Si ocurre algún error al obtener los trabajadores.
     */
    public Collection<Trabajador> getAllTrabajadores() throws Exception;

    /**
     * Crea un nuevo trabajador.
     * <p>
     * Envía un objeto {@link Trabajador} al sistema para crear un nuevo
     * registro.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del
     * trabajador.
     */
    public void crearTrabajador(Trabajador trabajador) throws Exception;

    /**
     * Actualiza la información de un trabajador existente.
     * <p>
     * Envía un objeto {@link Trabajador} al sistema para actualizar los datos
     * del trabajador.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} con la información
     * actualizada.
     * @throws Exception Si ocurre algún error durante la actualización del
     * trabajador.
     */
    public void actualizarTrabajador(Trabajador trabajador) throws Exception;

    /**
     * Borra un trabajador.
     * <p>
     * Envía un objeto {@link Trabajador} al sistema para eliminar el registro
     * correspondiente.
     * </p>
     *
     * @param trabajador El objeto {@link Trabajador} que se desea borrar.
     * @throws Exception Si ocurre algún error durante el borrado del
     * trabajador.
     */
    public void borrarTrabajador(Trabajador trabajador) throws Exception;

}
