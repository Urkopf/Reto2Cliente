/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Trabajador;
import java.util.Collection;

/**
 *
 * @author Ser_090
 */
public interface ITrabajador {

    public Collection<Trabajador> getAllUsers();

    public void createUser(Trabajador trabajador);

}
