package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Pedido;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource: PedidoFacadeREST [pedido]
 */
public class PedidosRestFull {

    private static final Logger LOGGER = Logger.getLogger(PedidosRestFull.class.getName());

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    public PedidosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("pedido");
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget.path("count");
        try {
            Response response = resource.request().get();
            return ExcepcionesUtilidad.handleResponse(response, String.class);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "No se pudo contar los pedidos.");
            throw e;  // Re-lanzamos para manejo externo si es necesario
        }
    }

    public void edit_XML(Object requestEntity) throws WebApplicationException {
        try {
            Response response = webTarget.request().put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
            ExcepcionesUtilidad.handleResponse(response, Void.class);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al editar el pedido.");
            throw e;
        }
    }

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget.path(id);
        try {
            Response response = resource.request().get();
            return ExcepcionesUtilidad.handleResponse(response, responseType);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al encontrar el pedido con ID: " + id);
            throw e;
        }
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget.path(from + "/" + to);
        try {
            Response response = resource.request().get();
            return ExcepcionesUtilidad.handleResponse(response, responseType);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al encontrar el rango de pedidos: " + from + " a " + to);
            throw e;
        }
    }

    public void create_XML(Object requestEntity) throws WebApplicationException {
        try {
            Response response = webTarget.request().post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
            ExcepcionesUtilidad.handleResponse(response, Void.class);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al crear el pedido.");
            throw e;
        }
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        try {
            Response response = webTarget.request().get();
            return ExcepcionesUtilidad.handleResponse(response, responseType);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al obtener todos los pedidos.");
            throw e;
        }
    }

    public void remove(Long id) throws WebApplicationException {
        try {
            Response response = webTarget.path(id.toString()).request().delete();
            ExcepcionesUtilidad.handleResponse(response, Void.class);
        } catch (Exception e) {
            ExcepcionesUtilidad.clasificadorExcepciones(e, "Error al eliminar el pedido con ID: " + id);
            throw e;
        }
    }

    public void close() {
        client.close();
    }

}
