/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class PedidoArticuloFactoria {

    private static final Logger LOGGER = Logger.getLogger(PedidoArticuloFactoria.class.getName());

    private static PedidoArticuloFactoria instance;

    private PedidoArticuloFactoria() {
    }

    public static PedidoArticuloFactoria getInstance() {
        if (instance == null) {
            instance = new PedidoArticuloFactoria();
        }

        return instance;
    }

    public IPedidoArticulo acceso() {
        return new PedidoArticuloImpl();
    }

}
