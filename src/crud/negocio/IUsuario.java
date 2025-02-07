/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Usuario;
import java.util.Collection;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de usuarios.
 * <p>
 * Esta interfaz establece los métodos necesarios para obtener la lista de
 * usuarios y para gestionar acciones de inicio de sesión, cambio de contraseña
 * y recuperación de contraseña.
 * </p>
 *
 * @param <T> Parámetro de tipo genérico (si se requiere para futuras
 * implementaciones).
 * @author Urko
 */
public interface IUsuario<T> {

    /**
     * Obtiene una colección con todos los usuarios disponibles.
     *
     * @return Una colección de objetos {@link Usuario} que representan todos
     * los usuarios.
     * @throws Exception Si ocurre algún error al obtener la lista de usuarios.
     */
    public Collection<Usuario> getAllUsers() throws Exception;

    /**
     * Gestiona la operación de inicio de sesión para un usuario.
     * <p>
     * Este método recibe un objeto {@link Usuario} con las credenciales
     * necesarias y retorna un objeto que representa el resultado de la
     * operación de inicio de sesión.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} que contiene las credenciales de
     * inicio de sesión.
     * @return Un objeto que representa el resultado del proceso de inicio de
     * sesión.
     * @throws Exception Si ocurre algún error durante el inicio de sesión.
     */
    public Object getInicioSesion(Usuario usuario) throws Exception;

    /**
     * Gestiona la operación de cambio de contraseña para un usuario.
     * <p>
     * Este método recibe un objeto {@link Usuario} con la información necesaria
     * y retorna un objeto que representa el resultado del proceso de cambio de
     * contraseña.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} que contiene la información para
     * cambiar la contraseña.
     * @return Un objeto que representa el resultado del proceso de cambio de
     * contraseña.
     * @throws Exception Si ocurre algún error durante el cambio de contraseña.
     */
    public Object getCambiarContrasena(Usuario usuario) throws Exception;

    /**
     * Gestiona la operación de recuperación de contraseña para un usuario.
     * <p>
     * Este método recibe un objeto {@link Usuario} con la información necesaria
     * y retorna un objeto que representa el resultado del proceso de
     * recuperación de contraseña.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} para el cual se desea recuperar
     * la contraseña.
     * @return Un objeto que representa el resultado del proceso de recuperación
     * de contraseña.
     * @throws Exception Si ocurre algún error durante la recuperación de la
     * contraseña.
     */
    public Object getRecuperarContrasena(Usuario usuario) throws Exception;
}
