package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Cliente RESTful para el servicio de almacén.
 * <p>
 * Esta clase se encarga de establecer la comunicación con el servicio RESTful
 * que gestiona las operaciones sobre los almacenes, tales como contar, buscar,
 * crear, editar y borrar. Los datos se manejan en formato XML.
 * </p>
 *
 * @author Sergio
 *
 * La URI base se obtiene desde el archivo de configuración a través de
 * {@code ResourceBundle}.
 */
public class AlmacenRestFull {

    /**
     * Logger para la clase {@code AlmacenRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(AlmacenRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio RESTful.
     */
    private WebTarget webTarget;

    /**
     * Cliente JAX-RS utilizado para realizar las peticiones.
     */
    private Client client;

    /**
     * URI base del servicio RESTful, obtenida del archivo de propiedades.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente JAX-RS y configura el WebTarget con
     * la URI base.
     */
    public AlmacenRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("almacen");
    }

    /**
     * Obtiene el recuento de recursos disponibles en el servicio.
     *
     * @return Un {@code String} que representa el recuento de almacenes.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Actualiza un recurso en formato XML.
     *
     * @param requestEntity El objeto que se utilizará para actualizar el
     * recurso.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca un recurso por su identificador y lo retorna en el tipo
     * especificado.
     *
     * @param <T> El tipo de respuesta esperado.
     * @param responseType La clase del tipo de respuesta.
     * @param id El identificador del recurso.
     * @return El recurso encontrado convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Busca un recurso basado en un identificador y lo retorna utilizando un
     * GenericType.
     *
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El GenericType del tipo de respuesta.
     * @param id El identificador del recurso.
     * @return El recurso encontrado convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findById_XML(GenericType<T> responseType, Long id) throws Exception {
        WebTarget resource = webTarget.path("articulo/" + id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea un nuevo recurso en formato XML.
     *
     * @param requestEntity El objeto que se desea crear.
     * @throws Exception Si ocurre algún error durante la creación del recurso.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todos los recursos en formato XML utilizando un GenericType.
     *
     * @param <T> El tipo de respuesta esperado.
     * @param responseType El GenericType del tipo de respuesta.
     * @return Una colección de recursos convertidos al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina un recurso identificado por su ID.
     *
     * @param id El identificador del recurso que se desea eliminar.
     * @throws Exception Si ocurre algún error durante la eliminación.
     */
    public void remove(String id) throws Exception {
        Response response = webTarget.path(id).request().delete();
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Elimina una relación enviando un objeto en formato XML.
     *
     * @param requestEntity El objeto que representa la relación a borrar.
     * @throws Exception Si ocurre algún error durante la eliminación.
     */
    public void remove_Relacion(Object requestEntity) throws Exception {
        Response response = webTarget.path("borrar")
                .request().post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Crea una nueva relación enviando un objeto en formato XML.
     *
     * @param requestEntity El objeto que representa la relación a crear.
     * @throws Exception Si ocurre algún error durante la creación.
     */
    public void createRelacion_XML(Object requestEntity) throws Exception {
        Response response = webTarget.path("relacion")
                .request().post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Cierra el cliente JAX-RS para liberar recursos.
     */
    public void close() {
        client.close();
    }
}
