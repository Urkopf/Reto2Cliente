package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Cliente RESTful para el servicio de pedido-artículos.
 * <p>
 * Esta clase se encarga de establecer la comunicación con el servicio RESTful
 * que gestiona las operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre las
 * relaciones entre pedidos y artículos. Los datos se manejan en formato XML. La
 * URI base se obtiene desde el archivo de configuración utilizando
 * {@code ResourceBundle}.
 *
 * @author Urko
 * </p>
 */
public class PedidoArticulosRestFull {

    /**
     * Logger para la clase {@code PedidoArticulosRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(PedidoArticulosRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio RESTful para
     * pedido-artículos.
     */
    private WebTarget webTarget;

    /**
     * Cliente JAX-RS utilizado para realizar las peticiones al servicio.
     */
    private Client client;

    /**
     * URI base del servicio RESTful, obtenida desde el archivo de propiedades.
     */
    private static final String BASE_URI
            = ResourceBundle.getBundle("recursos.configCliente")
                    .getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente JAX-RS y configura el WebTarget
     * para el recurso "pedidoarticulo".
     */
    public PedidoArticulosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("pedidoarticulo");
    }

    /**
     * Obtiene el recuento de relaciones entre pedidos y artículos disponibles.
     *
     * @return Una cadena que representa el número de relaciones.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Actualiza una relación entre pedido y artículo enviando el objeto en
     * formato XML.
     *
     * @param requestEntity El objeto que contiene los datos actualizados de la
     * relación.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca una relación entre pedido y artículo por su identificador y la
     * retorna en el tipo especificado.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param id El identificador de la relación a buscar.
     * @return La relación encontrada, convertida al tipo especificado.
     * @throws Exception Si ocurre algún error durante la búsqueda.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Busca un rango de relaciones entre pedidos y artículos en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param from El identificador de inicio del rango.
     * @param to El identificador de fin del rango.
     * @return El rango de relaciones encontrado, convertido al tipo
     * especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws Exception {
        WebTarget resource = webTarget.path(from + "/" + to);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea una nueva relación entre pedido y artículo enviando el objeto en
     * formato XML.
     *
     * @param requestEntity El objeto que contiene los datos de la nueva
     * relación.
     * @throws Exception Si ocurre algún error durante la creación.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todas las relaciones entre pedidos y artículos en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType El GenericType del tipo de objeto de respuesta.
     * @return Una colección de relaciones convertidas al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina una relación entre pedido y artículo identificada por su ID.
     *
     * @param id El identificador de la relación que se desea eliminar.
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
