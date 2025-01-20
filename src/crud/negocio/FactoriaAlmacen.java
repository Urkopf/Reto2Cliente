/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import java.util.logging.Logger;

/**
 *
 * @author Ser_090
 */
public class FactoriaAlmacen {

    private static final Logger LOGGER = Logger.getLogger(FactoriaAlmacen.class.getName());

    private static FactoriaAlmacen instance;

    private FactoriaAlmacen() {
    }

    public static FactoriaAlmacen getInstance() {
        if (instance == null) {
            instance = new FactoriaAlmacen();
        }

        return instance;
    }

    public IAlmacen acceso() {
        return new AlmacenImpl();
    }

}
