package crud.excepciones;

import static crud.utilidades.AlertUtilities.showErrorDialog;
import java.net.ConnectException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert.AlertType;
import javax.ws.rs.ProcessingException;
import net.sf.jasperreports.engine.JRException;

/**
 * La clase {@code ExcepcionesUtilidad} centraliza el manejo de excepciones
 * generadas en las operaciones del CRUD y en las respuestas obtenidas desde
 * servicios web.
 * <p>
 * Esta clase proporciona métodos para mostrar diálogos de error con mensajes
 * específicos según el tipo de excepción y para traducir las respuestas HTTP a
 * excepciones de JAX-RS.
 * </p>
 *
 * @author
 */
public class ExcepcionesUtilidad {

    private static final Logger LOGGER = Logger.getLogger(ExcepcionesUtilidad.class.getName());

    /**
     * Centraliza el manejo de excepciones mostrando un diálogo de error
     * adecuado según el tipo de excepción.
     * <p>
     * Se muestra un mensaje específico dependiendo de la instancia de la
     * excepción. En caso de no coincidir con ningún tipo predefinido, se
     * registra la excepción y se muestra un mensaje de error genérico.
     * </p>
     *
     * @param exception La excepción capturada.
     * @param defaultMessage Mensaje de error por defecto a mostrar si la
     * excepción no coincide con ninguna condición específica.
     */
    public static void centralExcepciones(Exception exception, String defaultMessage) {
        if (exception instanceof BadRequestException) {
            showErrorDialog(AlertType.ERROR, "Error de solicitud", "La solicitud es inválida. Verifique los datos enviados.");
        } else if (exception instanceof NotAuthorizedException) {
            showErrorDialog(AlertType.ERROR, "No autorizado", "Credenciales incorrectas o no proporcionadas.");
        } else if (exception instanceof ForbiddenException) {
            showErrorDialog(AlertType.ERROR, "Acceso prohibido", "No tiene permisos para realizar esta acción.");
        } else if (exception instanceof NotFoundException) {
            showErrorDialog(AlertType.ERROR, "No encontrado", "El recurso solicitado no existe.");
        } else if (exception instanceof RedirectionException) {
            showErrorDialog(AlertType.INFORMATION, "Redirección", "El recurso ha sido movido a otra ubicación.");
        } else if (exception instanceof InternalServerErrorException) {
            showErrorDialog(AlertType.ERROR, "Error del servidor", "Se produjo un error interno en el servidor. Inténtelo más tarde.");
        } else if (exception instanceof ServiceUnavailableException) {
            showErrorDialog(AlertType.ERROR, "Servicio no disponible", "El servidor no está disponible actualmente. Inténtelo más tarde.");
        } else if (exception instanceof ServerErrorException) {
            showErrorDialog(AlertType.ERROR, "Error del servidor", "Se produjo un error en el servidor. Inténtelo nuevamente.");
        } else if (exception instanceof ClientErrorException) {
            showErrorDialog(AlertType.ERROR, "Error", "Hubo un problema con la solicitud enviada.");
        } else if (exception instanceof ProcessingException) {
            showErrorDialog(AlertType.ERROR, "Error", "Conexion con servidor fallida. Por seguridad se procede a cerrar la sesión");
        } else if (exception instanceof ConnectException) {
            showErrorDialog(AlertType.ERROR, "Error", "Conexion con servidor fallida. Por seguridad se procede a cerrar la sesión. Intenteo");
        } else if (exception instanceof JRException) {
            showErrorDialog(AlertType.ERROR, "Error", "Hubo un problema generando el informe.");
        } else if (exception instanceof NullPointerException) {
            showErrorDialog(AlertType.ERROR, "Error", "No se han recibido datos.");

        } else {
            LOGGER.log(Level.SEVERE, "Excepción desconocida: {0}", exception.getMessage());
            showErrorDialog(AlertType.ERROR, "Error desconocido", defaultMessage);
        }
    }

    /**
     * Procesa la respuesta HTTP obtenida y la transforma en una instancia del
     * tipo especificado.
     * <p>
     * Si la respuesta pertenece a la familia de éxitos, se retorna la entidad
     * convertida al tipo indicado. En caso contrario, se traduce la respuesta a
     * una excepción adecuada.
     * </p>
     *
     * @param <T> El tipo de la entidad a obtener de la respuesta.
     * @param response La respuesta HTTP obtenida.
     * @param responseType La clase del tipo de entidad esperado.
     * @return Una instancia del tipo T o {@code null} si se espera
     * {@code Void.class}.
     * @throws WebApplicationException Si el estado de la respuesta no es
     * exitoso.
     */
    public static <T> T handleResponse(Response response, Class<T> responseType) throws WebApplicationException {

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return responseType == Void.class ? null : response.readEntity(responseType);
        } else {
            throw translateResponseToException(response);
        }
    }

    /**
     * Procesa la respuesta HTTP obtenida y la transforma en una instancia del
     * tipo especificado utilizando {@code GenericType}.
     * <p>
     * Si la respuesta pertenece a la familia de éxitos, se retorna la entidad
     * convertida al tipo indicado. En caso contrario, se traduce la respuesta a
     * una excepción adecuada.
     * </p>
     *
     * @param <T> El tipo de la entidad a obtener de la respuesta.
     * @param response La respuesta HTTP obtenida.
     * @param responseType El tipo genérico de la entidad esperado.
     * @return Una instancia del tipo T.
     * @throws WebApplicationException Si el estado de la respuesta no es
     * exitoso.
     */
    public static <T> T handleResponse(Response response, GenericType<T> responseType) throws WebApplicationException {

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return response.readEntity(responseType);
        } else {
            throw translateResponseToException(response);
        }
    }

    /**
     * Traduce una respuesta HTTP fallida a una excepción de JAX-RS adecuada.
     * <p>
     * Dependiendo del código de estado HTTP, se retorna una instancia
     * específica de excepción con un mensaje que incluye la entidad leída de la
     * respuesta.
     * </p>
     *
     * @param response La respuesta HTTP que contiene un error.
     * @return Una instancia de {@code WebApplicationException} correspondiente
     * al error HTTP.
     */
    public static WebApplicationException translateResponseToException(Response response) {
        int status = response.getStatus();
        String message = response.readEntity(String.class);

        switch (status) {
            case 400:
                return new BadRequestException("Solicitud inválida: " + message);
            case 401:
                return new NotAuthorizedException("No autorizado: " + message);
            case 403:
                return new ForbiddenException("Acceso prohibido: " + message);
            case 404:
                return new NotFoundException("Recurso no encontrado: " + message);
            case 500:
                return new InternalServerErrorException("Error interno del servidor: " + message);
            case 503:
                return new ServiceUnavailableException("Servicio no disponible: " + message);
            default:
                LOGGER.log(Level.SEVERE, "Error desconocido ({0}): {1}", new Object[]{status, message});
                return new WebApplicationException("Error desconocido: " + message, status);
        }
    }
}
