/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.rest.UsuariosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Ser_090
 */
public class UsuarioImpl<T> implements IUsuario<T> {

    private UsuariosRestFull usuarioCliente;
    private String tipo;

    public UsuarioImpl(String tipo) {
        this.tipo = tipo;
        usuarioCliente = new UsuariosRestFull(tipo);
    }

    @Override
    public Collection<T> getAllUsers() {
        List<T> users = null;
        try {
            users = usuarioCliente.findAll_XML(new GenericType<List<T>>() {
            });
        } catch (Exception e) {
            //Falta implementar las excepciones
        }
        return users;
    }

    @Override
    public void createUser(T usuario) {
        try {
            usuarioCliente.create_XML(usuario);
        } catch (Exception e) {
            //Falta implementar las excepciones
        }

    }


}
