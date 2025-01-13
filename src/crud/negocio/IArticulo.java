/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Articulo;
import java.util.Collection;

/**
 *
 * @author 2dam
 */
public interface IArticulo {

    public Collection<Articulo> getAllArticulos() throws LogicaNegocioException;

    public void crearArticulo(Articulo articulo) throws LogicaNegocioException;

    public void actualizarArticulo(Articulo articulo) throws LogicaNegocioException;

    public void borrarArticulo(Articulo articulo) throws LogicaNegocioException;
}
