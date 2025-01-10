/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public class UsuarioRestClient {

    private final WebTarget webTarget;
    private final Client cliente;
    private static final String BASE_URI = "la url";

    public UsuarioRestClient() {
        cliente = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = cliente.target(BASE_URI).path("cliente");
    }

    public <T> void create_XML(T requestEntity) throws WebApplicationException {
        //Hace la peticion
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(
                        requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T find_XML(Class<T> responseType, Long id) throws WebApplicationException {
        WebTarget recurso = webTarget;
        //Setea el path para la peticion
        recurso = recurso.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        //Hace la peticion y devuelve la informacion de la respuesta
        return recurso.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws WebApplicationException {
        WebTarget recurso = webTarget;
        //Hace la peticion y devuelve la informacion de la respuesta
        return recurso.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

}
