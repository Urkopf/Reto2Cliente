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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class ArticuloImpl implements IArticulo {

    private static final Logger LOGGER = Logger.getLogger(ArticuloImpl.class.getName());

    private ArticulosRestFull cliente;

    public ArticuloImpl() {
        cliente = new ArticulosRestFull();
    }

    @Override
    public Collection<Articulo> getAllArticulos() throws Exception {
        List<Articulo> articulos = null;
        articulos = cliente.findAll_XML(new GenericType<List<Articulo>>() {
        });
        return articulos;
    }

    @Override

    public void crearArticulo(Articulo articulo) throws Exception {
        cliente.create_XML(articulo);
    }

    @Override
    public void actualizarArticulo(Articulo articulo) throws Exception {
        cliente.edit_XML(articulo);
    }

    @Override
    public void actualizarArticuloDetalle(Articulo articulo) throws Exception {

        cliente.editDetalle_XML(articulo);
    }

    @Override
    public void borrarArticulo(Articulo articulo) throws Exception {
        cliente.remove(articulo.getId());
    }

}
