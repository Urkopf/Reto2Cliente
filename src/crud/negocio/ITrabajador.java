/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Trabajador;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface ITrabajador {

    public Collection<Trabajador> getAllClientes() throws LogicaNegocioException;

    public void crearTrabajador(Trabajador trabajador) throws LogicaNegocioException;

    public void actualizarTrabajador(Trabajador trabajador) throws LogicaNegocioException;

    public void borrarTrabajador(Trabajador trabajador) throws LogicaNegocioException;

}
