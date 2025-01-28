/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Articulo;
import crud.rest.ArticulosRestFull;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class ArticuloImpl implements IArticulo {

    private ArticulosRestFull cliente;

    public ArticuloImpl() {
        cliente = new ArticulosRestFull();
    }

    @Override
    public Collection<Articulo> getAllArticulos() throws LogicaNegocioException {
        List<Articulo> articulos = null;
        try {
            articulos = cliente.findAll_XML(new GenericType<List<Articulo>>() {
            });
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
        return articulos;
    }

    @Override
    public void crearArticulo(Articulo articulo) throws LogicaNegocioException {
        try {
            cliente.create_XML(articulo);
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

    @Override
    public void actualizarArticulo(Articulo articulo) throws LogicaNegocioException {
        try {
            cliente.edit_XML(articulo);
        } catch (Exception e) {
        }
    }

    @Override
    public void actualizarArticuloDetalle(Articulo articulo) throws LogicaNegocioException {
        try {

            cliente.editDetalle_XML(articulo);
        } catch (Exception e) {
        }
    }

    @Override
    public void borrarArticulo(Articulo articulo) throws LogicaNegocioException {
        try {
            cliente.remove(articulo.getId());
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
    }

}
