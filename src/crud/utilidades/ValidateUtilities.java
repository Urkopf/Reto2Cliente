package crud.utilidades;

import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de utilidades para validar diferentes tipos de datos mediante patrones
 * de expresión regular. Proporciona un método para validar cadenas según el
 * tipo especificado, como correos electrónicos, contraseñas o códigos postales.
 *
 * <p>
 * Los patrones de validación se obtienen desde un archivo de recursos
 * (`utilities.pattern`) que contiene las expresiones regulares
 * correspondientes.</p>
 *
 * @author Urko
 */
public class ValidateUtilities {

    // Logger para registrar eventos y errores
    private static final Logger LOGGER = Logger.getLogger(ValidateUtilities.class.getName());

    /**
     * Valida una cadena según el tipo especificado utilizando un patrón de
     * expresión regular.
     *
     * <p>
     * Este método carga el patrón adecuado desde un archivo de recursos,
     * dependiendo del tipo especificado (`email`, `pass`, o `zip`). Si el tipo
     * no se reconoce, se registra un error y el método retorna `false`.</p>
     *
     * @param validacion La cadena que se desea validar.
     * @param type El tipo de validación a realizar (`email`, `pass`, o `zip`).
     * @return `true` si la cadena cumple con el patrón del tipo especificado;
     * `false` en caso contrario.
     */
    public static Boolean isValid(String validacion, String type) {
        ResourceBundle bundle = ResourceBundle.getBundle("crud.utilidades.pattern");
        String patternType = "";
        switch (type) {
            case "telefono":
                patternType = bundle.getString("EMAILPATTERN");
                break;
            case "cif":
                patternType = bundle.getString("EMAILPATTERN");
                break;
            case "nif":
                patternType = bundle.getString("EMAILPATTERN");
                break;
            case "email":
                patternType = bundle.getString("EMAILPATTERN");
                break;
            case "pass":
                patternType = bundle.getString("PASSPATTERN");
                break;
            case "zip":
                patternType = bundle.getString("ZIPPATTERN");
                break;
            default:
                LOGGER.severe("Tipo no encontrado");
                return false;
        }

        Pattern patron = Pattern.compile(patternType);
        Matcher matcher = patron.matcher(validacion);
        boolean respuesta = matcher.matches();
        if (type.equalsIgnoreCase("cif") && !respuesta) {
            respuesta = isValid(validacion, "nif");
        }
        return respuesta;
    }

}
