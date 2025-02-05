package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import java.io.StringReader;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UsuariosRestFull {

    private static final Logger LOGGER = Logger.getLogger(UsuariosRestFull.class.getName());
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = ResourceBundle.getBundle("recursos.configCliente").getString("BASE_URI");

    public UsuariosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("usuario");
    }

    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    public Usuario inicioSesion_XML(Usuario usuario) throws Exception {
        WebTarget resource = webTarget.path("sesion");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
    }

    public Usuario cambiar_XML(Usuario usuario) throws Exception {
        WebTarget resource = webTarget.path("cambiar");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
    }

    public Usuario recuperar_XML(Object usuario) throws Exception {
        WebTarget resource = webTarget.path("recuperar");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
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

    public void close() {
        client.close();
    }
}
