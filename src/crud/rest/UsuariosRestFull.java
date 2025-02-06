package crud.rest;

import crud.excepciones.ExcepcionesUtilidad;
import crud.objetosTransferibles.Usuario;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Cliente RESTful para el servicio de usuarios.
 * <p>
 * Esta clase establece la comunicación con el servicio RESTful que gestiona las
 * operaciones sobre usuarios, incluyendo el inicio de sesión, el cambio de
 * contraseña y la recuperación de contraseña. Los datos se manejan en formato
 * XML. La URI base se obtiene desde el archivo de propiedades utilizando
 * {@code ResourceBundle}.
 * </p>
 *
 * @author Urko
 */
public class UsuariosRestFull {

    /**
     * Logger para la clase {@code UsuariosRestFull}.
     */
    private static final Logger LOGGER = Logger.getLogger(UsuariosRestFull.class.getName());

    /**
     * WebTarget que representa el recurso base del servicio RESTful para
     * usuarios.
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
     * para el recurso "usuario".
     */
    public UsuariosRestFull() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("usuario");
    }

    /**
     * Obtiene el recuento de usuarios disponibles.
     *
     * @return Una cadena que representa el número de usuarios.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public String countREST() throws Exception {
        WebTarget resource = webTarget.path("count");
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, String.class);
    }

    /**
     * Realiza el inicio de sesión de un usuario enviando el objeto en formato
     * XML.
     *
     * @param usuario El objeto {@link Usuario} que contiene las credenciales
     * para iniciar sesión.
     * @return El objeto {@link Usuario} autenticado, según lo devuelto por el
     * servicio.
     * @throws Exception Si ocurre algún error durante el proceso de inicio de
     * sesión.
     */
    public Usuario inicioSesion_XML(Usuario usuario) throws Exception {
        WebTarget resource = webTarget.path("sesion");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
    }

    /**
     * Realiza la operación de cambio de contraseña para un usuario enviando el
     * objeto en formato XML.
     *
     * @param usuario El objeto {@link Usuario} que contiene la información para
     * cambiar la contraseña.
     * @return El objeto {@link Usuario} resultante del proceso de cambio de
     * contraseña.
     * @throws Exception Si ocurre algún error durante el cambio de contraseña.
     */
    public Usuario cambiar_XML(Usuario usuario) throws Exception {
        WebTarget resource = webTarget.path("cambiar");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
    }

    /**
     * Realiza la operación de recuperación de contraseña para un usuario
     * enviando el objeto en formato XML.
     *
     * @param usuario El objeto que contiene la información necesaria para
     * recuperar la contraseña.
     * @return El objeto {@link Usuario} resultante del proceso de recuperación
     * de contraseña.
     * @throws Exception Si ocurre algún error durante la recuperación de la
     * contraseña.
     */
    public Usuario recuperar_XML(Object usuario) throws Exception {
        WebTarget resource = webTarget.path("recuperar");
        Response response = resource.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(usuario, MediaType.APPLICATION_XML));
        return ExcepcionesUtilidad.handleResponse(response, Usuario.class);
    }

    /**
     * Actualiza un usuario enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos actualizados del
     * usuario.
     * @throws Exception Si ocurre algún error durante la actualización.
     */
    public void edit_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Busca un usuario por su identificador y lo retorna en el tipo
     * especificado.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType La clase del tipo de objeto de respuesta.
     * @param id El identificador del usuario a buscar.
     * @return El usuario encontrado, convertido al tipo especificado.
     * @throws Exception Si ocurre algún error durante la búsqueda.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws Exception {
        WebTarget resource = webTarget.path(id);
        Response response = resource.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Crea un nuevo usuario enviando el objeto en formato XML.
     *
     * @param requestEntity El objeto que contiene los datos del nuevo usuario.
     * @throws Exception Si ocurre algún error durante la creación del usuario.
     */
    public void create_XML(Object requestEntity) throws Exception {
        Response response = webTarget.request()
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Recupera todos los usuarios en formato XML.
     *
     * @param <T> El tipo de objeto de respuesta esperado.
     * @param responseType El GenericType del tipo de objeto de respuesta.
     * @return Una colección de usuarios convertidos al tipo especificado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    public <T> T findAll_XML(GenericType<T> responseType) throws Exception {
        Response response = webTarget.request().get();
        return ExcepcionesUtilidad.handleResponse(response, responseType);
    }

    /**
     * Elimina un usuario identificado por su ID.
     *
     * @param id El identificador del usuario que se desea eliminar.
     * @throws Exception Si ocurre algún error durante la eliminación.
     */
    public void remove(String id) throws Exception {
        Response response = webTarget.path(id).request().delete();
        ExcepcionesUtilidad.handleResponse(response, Void.class);
    }

    /**
     * Cierra el cliente JAX-RS para liberar recursos.
     */
    public void close() {
        client.close();
    }
}
