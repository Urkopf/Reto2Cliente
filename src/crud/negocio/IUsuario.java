/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Usuario;
import java.util.Collection;

/**
 *
 * @author Ser_090
 */
public interface IUsuario<T> {

    public Collection<Usuario> getAllUsers() throws Exception;

    public Object getInicioSesion(Usuario usuario) throws Exception;

    public Object getCambiarContrasena(Usuario usuario) throws Exception;

    public Object getRecuperarContrasena(Usuario usuario) throws Exception;

}
