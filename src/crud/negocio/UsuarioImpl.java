/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Usuario;
import crud.rest.UsuariosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 * Implementación de la interfaz {@link IUsuario} para la gestión de usuarios.
 * <p>
 * Esta clase utiliza un cliente RESTful ({@link UsuariosRestFull}) para
 * realizar operaciones relacionadas con los usuarios, tales como obtener la
 * lista de usuarios, iniciar sesión, cambiar contraseña y recuperar contraseña.
 * Los datos se manejan en formato XML.
 * </p>
 *
 * @author Urko
 */
public class UsuarioImpl implements IUsuario {

    /**
     * Cliente RESTful para la comunicación con el servicio de usuarios.
     */
    private UsuariosRestFull usuarioCliente;

    /**
     * Constructor que inicializa el cliente RESTful para usuarios.
     */
    public UsuarioImpl() {
        usuarioCliente = new UsuariosRestFull();
    }

    /**
     * Obtiene una colección de todos los usuarios disponibles.
     * <p>
     * Realiza una consulta al servicio RESTful para recuperar una lista de
     * objetos {@link Usuario} en formato XML.
     * </p>
     *
     * @return Una colección de objetos {@link Usuario} que representan todos
     * los usuarios.
     * @throws Exception Si ocurre algún error al obtener los usuarios.
     */
    @Override
    public Collection<Usuario> getAllUsers() throws Exception {
        List<Usuario> users = null;
        users = usuarioCliente.findAll_XML(new GenericType<List<Usuario>>() {
        });
        return users;
    }

    /**
     * Gestiona la operación de inicio de sesión para un usuario.
     * <p>
     * Envía el objeto {@link Usuario} con las credenciales necesarias al
     * servicio RESTful, y retorna un objeto que representa el resultado del
     * proceso de inicio de sesión.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} que contiene las credenciales de
     * inicio de sesión.
     * @return Un objeto que representa el resultado del proceso de inicio de
     * sesión.
     * @throws Exception Si ocurre algún error durante el inicio de sesión.
     */
    @Override
    public Object getInicioSesion(Usuario usuario) throws Exception {
        Object user = null;
        user = usuarioCliente.inicioSesion_XML(usuario);
        return user;
    }

    /**
     * Gestiona la operación de cambio de contraseña para un usuario.
     * <p>
     * Envía el objeto {@link Usuario} con la información necesaria al servicio
     * RESTful para cambiar la contraseña, y retorna un objeto que representa el
     * resultado de la operación.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} que contiene la información para
     * cambiar la contraseña.
     * @return Un objeto que representa el resultado del proceso de cambio de
     * contraseña.
     * @throws Exception Si ocurre algún error durante el cambio de contraseña.
     */
    @Override
    public Object getCambiarContrasena(Usuario usuario) throws Exception {
        Object user = null;
        user = usuarioCliente.cambiar_XML(usuario);
        return user;
    }

    /**
     * Gestiona la operación de recuperación de contraseña para un usuario.
     * <p>
     * Envía el objeto {@link Usuario} al servicio RESTful para iniciar el
     * proceso de recuperación de contraseña, y retorna un objeto que representa
     * el resultado de dicha operación.
     * </p>
     *
     * @param usuario El objeto {@link Usuario} para el cual se desea recuperar
     * la contraseña.
     * @return Un objeto que representa el resultado del proceso de recuperación
     * de contraseña.
     * @throws Exception Si ocurre algún error durante la recuperación de la
     * contraseña.
     */
    @Override
    public Object getRecuperarContrasena(Usuario usuario) throws Exception {
        Object user = null;
        user = usuarioCliente.recuperar_XML(usuario);
        return user;
    }
}
