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

public class AlmacenRestFull {

    private static final Logger LOGGER = Logger.getLogger(AlmacenRestFull.class.getName());

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    public AlmacenRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("almacen");
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

    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    public <T> T findById_XML(GenericType<T> responseType, Long id) throws Exception {
        WebTarget resource = webTarget.path("articulo/" + id);
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

    public void remove(String id) throws Exception {
        Response response = webTarget.path(id).request().delete();
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public void remove_Relacion(Object requestEntity) throws Exception {
        Response response = webTarget.path("borrar")
                .request().post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public void createRelacion_XML(Object requestEntity) throws Exception {
        Response response = webTarget.path("relacion")
                .request().post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    public void close() {
        client.close();
    }
}
