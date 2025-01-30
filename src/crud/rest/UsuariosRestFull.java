/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.rest;

import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Trabajador;
import crud.objetosTransferibles.Usuario;
import crud.utilidades.Utilidades;
import java.io.StringReader;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Jersey REST client generated for REST resource:UsuarioFacadeREST
 * [usuario]<br>
 * USAGE:
 * <pre>
 *        UsuariosRestFull client = new UsuariosRestFull();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author 2dam
 */
public class UsuariosRestFull {

    private Logger LOGGER = Logger.getLogger(UsuariosRestFull.class.getName());
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI
            = ResourceBundle.getBundle("recursos.configCliente")
                    .getString("BASE_URI");

    public UsuariosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("usuario");
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public Usuario inicioSesion_XML(Usuario usuario) throws WebApplicationException {
        WebTarget resource = webTarget.path("sesion");
        LOGGER.info("Enviando solicitud a: " + resource.getUri());

        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        int status = response.getStatus();
        LOGGER.info("Estado de la respuesta: " + status);

        switch (status) {
            case 200:
                // 1) Leer el XML a String
                String xml = response.readEntity(String.class);

                // 2) Decidir manualmente
                if (xml.contains("<cliente")) {
                    Cliente c = unmarshall(xml, Cliente.class);
                    LOGGER.info("Cliente recibido: " + c.getNombre());
                    return c;
                } else {
                    // Asumimos que es Trabajador
                    Trabajador t = unmarshall(xml, Trabajador.class);
                    LOGGER.info("Trabajador recibido: " + t.getNombre());
                    return t;
                }

            case 401:
                throw new WebApplicationException("Credenciales incorrectas", Response.Status.UNAUTHORIZED);
            case 500:
                throw new WebApplicationException("Error en el servidor", Response.Status.INTERNAL_SERVER_ERROR);
            default:
                throw new WebApplicationException("Error desconocido: " + status);
        }
    }

    private <T> T unmarshall(String xml, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(Cliente.class, Trabajador.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
        } catch (JAXBException e) {
            throw new RuntimeException("Error de parseo JAXB: " + e.getMessage(), e);
        }
    }

    public Usuario cambiar_XML(Usuario usuario) throws WebApplicationException {
        Usuario cliente = null;
        WebTarget resource = webTarget.path("cambiar");
        LOGGER.info("Enviando solicitud a: " + resource.getUri());
        // Enviar el objeto Usuario como entidad
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        LOGGER.info("Estado de la respuesta: " + response.getStatus());

        if (response.getStatus() == 200) {
            cliente = response.readEntity(Cliente.class
            );
            System.out.println("Cliente recibido: " + cliente.getNombre());
        } else {
            System.out.println("Error: " + response.getStatus());
        }
        return cliente;

    }

    public Usuario recuperar_XML(Object usuario) throws WebApplicationException {
        Usuario cliente = null;
        WebTarget resource = webTarget.path("recuperar");
        LOGGER.info("Enviando solicitud a: " + resource.getUri());
        // Enviar el objeto Usuario como entidad
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        LOGGER.info("Estado de la respuesta: " + response.getStatus());

        if (response.getStatus() == 200) {
            cliente = response.readEntity(Cliente.class
            );
            System.out.println("Cliente recibido: " + cliente.getNombre());
        } else {
            System.out.println("Error: " + response.getStatus());
        }
        return cliente;

    }

    public void edit_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Usuario.class
                );
    }

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Usuario.class
                );
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(Usuario.class
        );
    }

    public void close() {
        client.close();
    }

}
