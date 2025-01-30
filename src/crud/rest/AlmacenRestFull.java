/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.rest;

import crud.objetosTransferibles.Almacen;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:AlmacenFacadeREST
 * [almacen]<br>
 * USAGE:
 * <pre>
 *        AlmacenRestFull client = new AlmacenRestFull();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Ser_090
 */
public class AlmacenRestFull {

    private static final Logger LOGGER = Logger.getLogger(AlmacenRestFull.class.getName());

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI
            = ResourceBundle.getBundle("recursos.configCliente")
                    .getString("BASE_URI");

    public AlmacenRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("almacen");
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void edit_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Almacen.class);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findById_XML(GenericType<T> responseType, Long id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("articulo/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Almacen.class);
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(Almacen.class);
    }

    public void remove_Relacion(Object requestEntity) throws WebApplicationException {
        webTarget
                .path("borrar") // Define el endpoint 'borrar'
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML) // Solicita una respuesta en formato XML
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Almacen.class);
    }

    public void createRelacion_XML(Object requestEntity) throws WebApplicationException {
        webTarget
                .path("relacion") // Define el endpoint 'borrar'
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML) // Solicita una respuesta en formato XML
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Almacen.class);
    }

    public void close() {
        client.close();
    }

}
