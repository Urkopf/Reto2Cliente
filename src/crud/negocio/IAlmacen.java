/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Almacen;
import java.util.Collection;

/**
 *
 * @author Ser_090
 */
public interface IAlmacen {

    public Collection<Almacen> getAllAlmacenes() throws LogicaNegocioException;

    public Collection<Almacen> getAllAlmacenesById(Long id) throws LogicaNegocioException;

    public void CrearActualizarRelacion(Almacen almacen) throws LogicaNegocioException;

    public void BorrarRelacion(Almacen almacen) throws LogicaNegocioException;
}
