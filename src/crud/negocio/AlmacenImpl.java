/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.LogicaNegocioException;
import crud.objetosTransferibles.Almacen;
import crud.rest.AlmacenRestFull;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Ser_090
 */
public class AlmacenImpl implements IAlmacen {

    private static final Logger LOGGER = Logger.getLogger(AlmacenImpl.class.getName());

    private AlmacenRestFull cliente;

    public AlmacenImpl() {
        cliente = new AlmacenRestFull();
    }

    @Override
    public Collection<Almacen> getAllAlmacenes() throws LogicaNegocioException {
        List<Almacen> almacenes = null;
        try {
            almacenes = cliente.findAll_XML(new GenericType<List<Almacen>>() {
            });
        } catch (Exception e) {
            throw new LogicaNegocioException("Error");
        }
        return almacenes;
    }

    @Override
    public Collection<Almacen> getAllAlmacenesById(Long id) throws LogicaNegocioException {
        List<Almacen> almacenes;
        try {
            LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", id);
            almacenes = cliente.findById_XML(new GenericType<List<Almacen>>() {
            }, id);
        } catch (Exception e) {
            throw new LogicaNegocioException(e.getMessage());
        }
        return almacenes;
    }

}
