/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

import crud.excepciones.ExcepcionesUtilidad;
import crud.iu.controladores.ControladorArticulosBusqueda;
import crud.iu.controladores.ControladorArticulosDetalle;
import crud.iu.controladores.ControladorArticulosPrincipal;
import crud.objetosTransferibles.Articulo;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * Factoría para la gestión y carga de vistas relacionadas con los artículos.
 * <p>
 * Esta clase implementa el patrón Singleton para asegurar que exista una única
 * instancia de la factoría. Proporciona métodos para obtener la implementación
 * de la interfaz {@code IArticulo} y para cargar las diferentes ventanas
 * (principal, búsqueda y detalle) de la aplicación.
 * </p>
 *
 * @author Sergio
 */
public class FactoriaArticulos {

    /**
     * Logger para la clase {@code FactoriaArticulos}.
     */
    private static final Logger LOGGER = Logger.getLogger(FactoriaArticulos.class.getName());

    /**
     * Instancia única de la factoría.
     */
    private static FactoriaArticulos instance;

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private FactoriaArticulos() {
    }

    /**
     * Retorna la instancia única de {@code FactoriaArticulos}.
     * <p>
     * Si la instancia aún no ha sido creada, se crea una nueva.
     * </p>
     *
     * @return La instancia única de {@code FactoriaArticulos}.
     */
    public static FactoriaArticulos getInstance() {
        if (instance == null) {
            instance = new FactoriaArticulos();
        }
        return instance;
    }

    /**
     * Proporciona acceso a una implementación de la interfaz {@link IArticulo}.
     * <p>
     * Este método retorna una nueva instancia de {@link ArticuloImpl}.
     * </p>
     *
     * @return Una implementación de {@code IArticulo}.
     */
    public IArticulo acceso() {
        return new ArticuloImpl();
    }

    /**
     * Carga y muestra la ventana principal de artículos.
     * <p>
     * Este método carga la vista FXML para la ventana principal de artículos,
     * configura su controlador con el escenario, el usuario actual y la
     * colección de artículos obtenidos en la búsqueda, y posteriormente
     * inicializa el escenario.
     * </p>
     *
     * @param stage El escenario donde se mostrará la ventana.
     * @param user El usuario actual.
     * @param articuloBusqueda La colección de artículos resultantes de la
     * búsqueda.
     */
    public void cargarArticulosPrincipal(Stage stage, Object user, Collection<Articulo> articuloBusqueda) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/ArticulosPrincipal.fxml"));
            Parent root = cargador.load();
            ControladorArticulosPrincipal controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.setBusqueda(articuloBusqueda);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de ArticulosPrincipal: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana de búsqueda de artículos.
     * <p>
     * Este método carga la vista FXML para la ventana de búsqueda de artículos,
     * configura su controlador con el escenario y el usuario actual, y
     * posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario donde se mostrará la ventana.
     * @param user El usuario actual.
     */
    public void cargarArticulosBusqueda(Stage stage, Object user) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/ArticulosBusqueda.fxml"));
            Parent root = cargador.load();
            ControladorArticulosBusqueda controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de ArticulosBusqueda: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }

    /**
     * Carga y muestra la ventana de detalle de un artículo.
     * <p>
     * Este método carga la vista FXML para la ventana de detalle de un
     * artículo, configura su controlador con el escenario, el usuario actual y
     * el artículo a mostrar, y posteriormente inicializa el escenario.
     * </p>
     *
     * @param stage El escenario donde se mostrará la ventana.
     * @param user El usuario actual.
     * @param articulo El artículo cuyo detalle se desea visualizar.
     */
    public void cargarArticulosDetalle(Stage stage, Object user, Articulo articulo) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/crud/iu/vistas/ArticulosDetalle.fxml"));
            Parent root = cargador.load();
            ControladorArticulosDetalle controlador = cargador.getController();
            controlador.setStage(stage);
            controlador.setUser(user);
            controlador.setArticulo(articulo);
            controlador.initStage(root);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la ventana de Articulos Detalle: {0}", e.getMessage());
            ExcepcionesUtilidad.centralExcepciones(e, e.getMessage());
        }
    }
}
