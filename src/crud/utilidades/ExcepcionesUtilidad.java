/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.utilidades;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Clase utilitaria para manejar excepciones de aplicaciones RESTful.
 */
public class ExcepcionesUtilidad {

    private static final Logger LOGGER = Logger.getLogger(ExcepcionesUtilidad.class.getName());

    /**
     * Maneja excepciones de WebApplicationException y muestra un mensaje de
     * error adecuado.
     *
     * @param exception La excepción capturada.
     * @param defaultMessage Mensaje predeterminado en caso de que la excepción
     * no sea específica.
     */
    public static void clasificadorExcepciones(Object excepcion, String mensaje) {
        if (excepcion instanceof WebApplicationException) {
            centralExcepciones((WebApplicationException) excepcion, mensaje);
        } else {
            centralExcepciones((Exception) excepcion, mensaje);
        }
    }

    public static void centralExcepciones(WebApplicationException exception, String defaultMessage) {
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

    /**
     * Maneja excepciones genéricas que no son WebApplicationException.
     *
     * @param exception La excepción capturada.
     * @param defaultMessage Mensaje predeterminado para excepciones
     * desconocidas.
     */
    public static void centralExcepciones(Exception exception, String defaultMessage) {
        LOGGER.log(Level.SEVERE, "Excepción genérica: {0}", exception.getMessage());
        showErrorDialog(AlertType.ERROR, "Error inesperado", defaultMessage);
    }

}
