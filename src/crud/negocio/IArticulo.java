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

    public Collection<Articulo> getAllArticulos() throws Exception;

    public void crearArticulo(Articulo articulo) throws Exception;

    public void actualizarArticulo(Articulo articulo) throws Exception;

    public void actualizarArticuloDetalle(Articulo articulo) throws Exception;

    public void borrarArticulo(Articulo articulo) throws Exception;
}
