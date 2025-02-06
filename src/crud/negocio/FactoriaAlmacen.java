/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import java.util.logging.Logger;

/**
 * Factoría para la creación de instancias de {@link IAlmacen}.
 * <p>
 * Esta clase sigue el patrón Singleton para garantizar que exista una única
 * instancia de la factoría durante la ejecución de la aplicación. Proporciona
 * un método de acceso para obtener una implementación de la interfaz
 * {@code IAlmacen}.
 * </p>
 *
 * @author Sergio
 */
public class FactoriaAlmacen {

    /**
     * Logger para la clase {@code FactoriaAlmacen}.
     */
    private static final Logger LOGGER = Logger.getLogger(FactoriaAlmacen.class.getName());

    /**
     * Instancia única de la factoría.
     */
    private static FactoriaAlmacen instance;

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private FactoriaAlmacen() {
    }

    /**
     * Retorna la instancia única de {@code FactoriaAlmacen}.
     * <p>
     * Si la instancia aún no ha sido creada, se crea una nueva.
     * </p>
     *
     * @return La instancia única de {@code FactoriaAlmacen}.
     */
    public static FactoriaAlmacen getInstance() {
        if (instance == null) {
            instance = new FactoriaAlmacen();
        }
        return instance;
    }

    /**
     * Proporciona acceso a una implementación de la interfaz {@link IAlmacen}.
     * <p>
     * Este método crea y retorna una nueva instancia de {@link AlmacenImpl}.
     * </p>
     *
     * @return Una instancia de {@link IAlmacen} para la gestión de almacenes.
     */
    public IAlmacen acceso() {
        return new AlmacenImpl();
    }

}
