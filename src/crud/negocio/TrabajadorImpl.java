/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Trabajador;
import crud.rest.TrabajadorRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class TrabajadorImpl implements ITrabajador {

    private TrabajadorRestFull cliente;

    public TrabajadorImpl() {
        cliente = new TrabajadorRestFull();
    }

    @Override
    public Collection<Trabajador> getAllClientes() throws LogicaNegocioException {
        List<Trabajador> trabajadores = null;
        try {
            trabajadores = cliente.findAll_XML(new GenericType<List<Trabajador>>() {
            });
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
        return trabajadores;
    }

    @Override
    public void crearTrabajador(Trabajador trabajador) throws LogicaNegocioException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualizarTrabajador(Trabajador trabajador) throws LogicaNegocioException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void borrarTrabajador(Trabajador trabajador) throws LogicaNegocioException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
