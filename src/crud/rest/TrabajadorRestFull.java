package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Trabajador;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Cliente RESTful para el servicio de trabajadores.
 * <p>
 * Esta clase establece la comunicación con el servicio RESTful que gestiona las
 * operaciones CRUD (Crear, Leer, Actualizar y Borrar) sobre objetos de tipo
 * {@link Trabajador}. Los datos se intercambian en formato XML y la URI base se
 * obtiene desde el archivo de configuración mediante {@code ResourceBundle}.
 * </p>
 *
 * @author Sergio
 */
public class TrabajadorRestFull {

    /**
     * Logger para la clase {@code TrabajadorRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(TrabajadorRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio RESTful para
     * trabajadores.
     */
    private WebTarget webTarget;

    /**
     * Cliente JAX-RS utilizado para realizar las peticiones al servicio.
     */
    private Client client;

    /**
     * URI base del servicio RESTful, obtenida del archivo de propiedades.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente JAX-RS y configura el WebTarget
     * para el recurso "trabajador".
     */
    public TrabajadorRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("trabajador");
    }

    /**
     * Obtiene el recuento de trabajadores disponibles.
     *
     * @return Una cadena que representa el número de trabajadores.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Actualiza un trabajador enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * trabajador.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca un trabajador por su identificador y lo retorna en el tipo
     * especificado.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param id El identificador del trabajador a buscar.
     * @return El trabajador encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la búsqueda.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Busca un rango de trabajadores en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param from El identificador de inicio del rango.
     * @param to El identificador de fin del rango.
     * @return El rango de trabajadores encontrado, convertido al tipo
     * especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws Exception {
        WebTarget resource = webTarget.path(from + "/" + to);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea un nuevo trabajador enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo
     * trabajador.
     * @throws Exception Si ocurre algún error durante la creación del
     * trabajador.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todos los trabajadores en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType El GenericType del tipo de objeto de respuesta.
     * @return Una colección de trabajadores convertidos al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina un trabajador identificado por su ID.
     *
     * @param id El identificador del trabajador que se desea eliminar.
     * @throws Exception Si ocurre algún error durante la eliminación.
     */
    public void remove(Long id) throws Exception {
        Response response = webTarget.path(id.toString()).request().delete();
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Cierra el cliente JAX-RS para liberar recursos.
     */
    public void close() {
        client.close();
    }
}
