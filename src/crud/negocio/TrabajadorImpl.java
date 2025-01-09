/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Trabajador;
import crud.rest.TrabajadorRestClient;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Ser_090
 */
public class TrabajadorImpl implements ITrabajador {

    private TrabajadorRestClient trabajadorCliente;

    public TrabajadorImpl() {
        trabajadorCliente = new TrabajadorRestClient();
    }

    @Override
    public Collection<Trabajador> getAllUsers() {
        List<Trabajador> trabajadores = null;
        try {
            trabajadores = trabajadorCliente.findAll_XML(new GenericType<List<Trabajador>>() {
            });
        } catch (Exception ex) {
            //Falta manejar la excepcion
        }

        return trabajadores;
    }

    @Override
    public void createUser(Trabajador trabajador) {
        try {
            trabajadorCliente.create_XML(trabajador);
        } catch (Exception ex) {
            //Falta manejar la excepcion
        }
    }

}
