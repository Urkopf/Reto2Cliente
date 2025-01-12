/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.objetosTransferibles.Articulo;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class FactoriaArticulos {

    private static final Logger LOGGER = Logger.getLogger(FactoriaArticulos.class.getName());

    private static FactoriaArticulos instance;

    private FactoriaArticulos() {
    }

    public static FactoriaArticulos getInstance() {
        if (instance == null) {
            instance = new FactoriaArticulos();
        }

        return instance;
    }

    public List<Articulo> getAllArticulos() {
        return (List<Articulo>) new ArticuloImpl().getAllArticulos();
    }

    public void createArticulo(Articulo articulo) {
        new ArticuloImpl().crearArticulo(articulo);
    }

}
