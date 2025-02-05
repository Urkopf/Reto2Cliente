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
    public Collection<Almacen> getAllAlmacenes() throws Exception {
        List<Almacen> almacenes = null;

        LOGGER.log(Level.INFO, "Estoy en la Implementacion completa");
        almacenes = cliente.findAll_XML(new GenericType<List<Almacen>>() {
        });

        return almacenes;
    }

    @Override
    public Collection<Almacen> getAllAlmacenesById(Long id) throws Exception {
        List<Almacen> almacenes;

        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", id);
        almacenes = cliente.findById_XML(new GenericType<List<Almacen>>() {
        }, id);

        return almacenes;
    }

    @Override
    public void CrearActualizarRelacion(Almacen almacen) throws Exception {

        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", almacen.getId());
        cliente.createRelacion_XML(almacen);
    }

    @Override
    public void BorrarRelacion(Almacen almacen) throws Exception {
        LOGGER.log(Level.INFO, "Estoy en la Implementacion {0}", almacen.getId());
        cliente.remove_Relacion(almacen);
    }

}
