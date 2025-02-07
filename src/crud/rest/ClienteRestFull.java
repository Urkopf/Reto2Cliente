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
 * Cliente RESTful para el servicio de clientes.
 * <p>
 * Esta clase se encarga de establecer la comunicación con el servicio RESTful
 * que gestiona las operaciones CRUD (Crear, Leer, Actualizar y Borrar) sobre
 * los clientes. Los datos se manejan en formato XML y JSON, según corresponda.
 * La URI base se obtiene del archivo de configuración mediante
 * {@code ResourceBundle}.
 * </p>
 *
 * @author Urko
 */
public class ClienteRestFull {

    /**
     * Logger para la clase {@code ClienteRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(ClienteRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio de clientes.
     */
    private WebTarget webTarget;

    /**
     * Cliente JAX-RS utilizado para realizar las peticiones al servicio
     * RESTful.
     */
    private Client client;

    /**
     * URI base del servicio RESTful, obtenida desde el archivo de propiedades.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente JAX-RS y configura el WebTarget
     * para el recurso "cliente".
     */
    public ClienteRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("cliente");
    }

    /**
     * Obtiene el recuento de clientes disponibles.
     *
     * @return Una cadena que representa el número de clientes.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Actualiza un cliente enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * cliente.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Actualiza un cliente enviando el objeto en formato JSON.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * cliente.
     * @param id El identificador del cliente que se desea actualizar.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_JSON(Object requestEntity, String id) throws Exception {
        Response response = webTarget.path(id).request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_JSON));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca un cliente por su identificador y lo retorna en el tipo
     * especificado.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param id El identificador del cliente a buscar.
     * @return El cliente encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la búsqueda.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Busca un rango de clientes en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param from El identificador de inicio del rango.
     * @param to El identificador de fin del rango.
     * @return El rango de clientes encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws Exception {
        WebTarget resource = webTarget.path(from + "/" + to);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea un nuevo cliente enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo cliente.
     * @throws Exception Si ocurre algún error durante la creación del cliente.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todos los clientes en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType El GenericType del tipo de objeto de respuesta.
     * @return Una colección de clientes convertidos al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina un cliente identificado por su ID.
     *
     * @param id El identificador del cliente que se desea eliminar.
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
