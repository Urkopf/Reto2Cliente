/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

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
    public Collection<Articulo> getAllArticulos() {
        List<Articulo> articulos = null;
        try {
            articulos = cliente.findAll_XML(new GenericType<List<Articulo>>() {
            });
        } catch (Exception e) {
        }
        return articulos;
    }

    @Override
    public void crearArticulo(Articulo articulo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
