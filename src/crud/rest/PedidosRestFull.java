package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Pedido;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource: PedidoFacadeREST [pedido]
 * <p>
 * Esta clase establece la comunicación con el servicio RESTful que gestiona los
 * pedidos. Proporciona métodos para realizar operaciones CRUD (Crear, Leer,
 * Actualizar, Borrar) sobre objetos de tipo {@link Pedido} utilizando formato
 * XML para el intercambio de datos.
 *
 * @author Urko
 * </p>
 */
public class PedidosRestFull {

    /**
     * Logger para la clase {@code PedidosRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(PedidosRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio RESTful para
     * pedidos.
     */
    private WebTarget webTarget;

    /**
     * Cliente JAX-RS utilizado para realizar las peticiones al servicio.
     */
    private Client client;

    /**
     * URI base del servicio RESTful, obtenida desde el archivo de propiedades.
     */
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    /**
     * Constructor que inicializa el cliente JAX-RS y configura el WebTarget
     * para el recurso "pedido".
     */
    public PedidosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("pedido");
    }

    /**
     * Obtiene el recuento de pedidos disponibles.
     *
     * @return Una cadena que representa el número de pedidos.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Actualiza un pedido enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * pedido.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request().put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca un pedido por su identificador y lo retorna en el tipo
     * especificado.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param id El identificador del pedido a buscar.
     * @return El pedido encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la búsqueda.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Busca un rango de pedidos en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param from El identificador de inicio del rango.
     * @param to El identificador de fin del rango.
     * @return El rango de pedidos encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws Exception {
        WebTarget resource = webTarget.path(from + "/" + to);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea un nuevo pedido enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo pedido.
     * @throws Exception Si ocurre algún error durante la creación del pedido.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request().post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todos los pedidos en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType El GenericType del tipo de objeto de respuesta.
     * @return Una colección de pedidos convertidos al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina un pedido identificado por su ID.
     *
     * @param id El identificador del pedido que se desea eliminar.
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
