package utilities;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Clase de utilidades para mostrar diferentes tipos de alertas en una
 * aplicación JavaFX. Proporciona métodos para mostrar diálogos de error y
 * diálogos de confirmación.
 *
 * <p>
 * Esta clase simplifica el proceso de mostrar alertas al configurar las
 * propiedades de la alerta y manejar la respuesta del usuario.</p>
 *
 * @author Urko
 */
public class AlertUtilities {

    /**
     * Muestra un diálogo de error con el tipo de alerta, título y mensaje
     * especificados.
     *
     * <p>
     * Este método crea una alerta con el tipo dado, establece su título como el
     * nombre del tipo de alerta, su cabecera como `null` y su contenido como el
     * mensaje proporcionado. Luego, muestra la alerta y espera a que el usuario
     * la cierre.</p>
     *
     * @param type Tipo de la alerta (por ejemplo, `Alert.AlertType.ERROR`).
     * @param title Título de la alerta.
     * @param message Mensaje que se mostrará en el contenido de la alerta.
     */
    public static void showErrorDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación con el título y mensaje especificados.
     *
     * <p>
     * Este método crea una alerta de confirmación, establece el título y el
     * mensaje proporcionados, y espera a que el usuario responda. Retorna
     * `true` si el usuario hace clic en el botón OK y `false` en cualquier otro
     * caso.</p>
     *
     * @param title Título de la ventana de diálogo.
     * @param message Mensaje que se mostrará en el contenido de la alerta.
     * @return `true` si el usuario selecciona "OK"; `false` en caso contrario.
     */
    public static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
