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
    public Collection<Usuario> getAllUsers() {
        List<Usuario> users = null;
        try {
            users = usuarioCliente.findAll_XML(new GenericType<List<Usuario>>() {
            });
        } catch (Exception e) {
            //Falta implementar las excepciones
        }
        return users;
    }

    @Override
    public Object getInicioSesion(Usuario usuario) {
        Object user = null;
        try {
            user = usuarioCliente.inicioSesion_XML(usuario);
        } catch (Exception e) {
            //Falta implementar las excepciones
        }
        return user;
    }

}
