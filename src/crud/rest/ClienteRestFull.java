package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Cliente;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClienteRestFull {

    private static final Logger LOGGER = Logger.getLogger(ClienteRestFull.class.getName());

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    public ClienteRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("cliente");
    }

    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public void edit_JSON(Object requestEntity, String id) throws Exception {
        Response response = webTarget.path(id).request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_JSON));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws Exception {
        WebTarget resource = webTarget.path(from + "/" + to);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    public void remove(Long id) throws Exception {
        Response response = webTarget.path(id.toString()).request().delete();
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public void close() {
        client.close();
    }
}
