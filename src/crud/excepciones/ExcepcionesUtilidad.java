package crud.excepciones;

import static crud.utilidades.AlertUtilities.showErrorDialog;
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

public class ExcepcionesUtilidad {

    private static final Logger LOGGER = Logger.getLogger(ExcepcionesUtilidad.class.getName());

    public static void clasificadorExcepciones(Object excepcion, String mensaje) {

        centralExcepciones((Exception) excepcion, mensaje);

    }

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
            showErrorDialog(AlertType.ERROR, "Error del cliente", "Hubo un problema con la solicitud enviada.");
        } else {
            LOGGER.log(Level.SEVERE, "Excepción desconocida: {0}", exception.getMessage());
            showErrorDialog(AlertType.ERROR, "Error desconocido", defaultMessage);
        }
    }

    public static <T> T handleResponse(Response response, Class<T> responseType) throws WebApplicationException {
        try {
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return responseType == Void.class ? null : response.readEntity(responseType);
            } else {
                throw translateResponseToException(response);
            }
        } catch (javax.ws.rs.ProcessingException e) {
            throw new ServiceUnavailableException("No se pudo conectar con el servidor. Verifique que el servidor esté encendido y accesible.");
        }
    }

    public static <T> T handleResponse(Response response, GenericType<T> responseType) throws WebApplicationException {
        try {
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return response.readEntity(responseType);
            } else {
                throw translateResponseToException(response);
            }
        } catch (javax.ws.rs.ProcessingException e) {
            throw new ServiceUnavailableException("No se pudo conectar con el servidor. Verifique que el servidor esté encendido y accesible.");
        }
    }

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
