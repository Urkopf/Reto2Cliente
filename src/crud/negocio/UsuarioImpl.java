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
 *
 * @author Ser_090
 */
public class UsuarioImpl implements IUsuario {

    private UsuariosRestFull usuarioCliente;

    public UsuarioImpl() {
        usuarioCliente = new UsuariosRestFull();
    }

    @Override
    public Collection<Usuario> getAllUsers() throws Exception {
        List<Usuario> users = null;

        users = usuarioCliente.findAll_XML(new GenericType<List<Usuario>>() {
        });

        return users;
    }

    @Override
    public Object getInicioSesion(Usuario usuario) throws Exception {
        Object user = null;

        user = usuarioCliente.inicioSesion_XML(usuario);

        return user;
    }

    @Override
    public Object getCambiarContrasena(Usuario usuario) throws Exception {

        Object user = null;

        user = usuarioCliente.cambiar_XML(usuario);

        return user;
    }

    @Override
    public Object getRecuperarContrasena(Usuario usuario) throws Exception {
        Object user = null;

        user = usuarioCliente.recuperar_XML(usuario);

        return user;
    }

}
