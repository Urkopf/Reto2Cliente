package controlador;

import auxiliarMainTest.MainTestPedidosPrincipal;
import crud.iu.controladores.ControladorPedidosPrincipal;
import crud.objetosTransferibles.Estado;
import crud.objetosTransferibles.Pedido;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import static org.testfx.matcher.base.WindowMatchers.isShowing;
import static org.testfx.util.NodeQueryUtils.hasText;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Clase de test para ControladorPedidosPrincipal.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerCerradoPedidosPrincipalTest extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(ServerCerradoPedidosPrincipalTest.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    ;

    public void start(Stage stage) throws Exception {
        new MainTestPedidosPrincipal().start(stage);
        sleep(1000);

    }

    @Test
    public void testA_ServidorCerrado() {
        // 🔹 Capturar logs de la aplicación
        Logger logger = Logger.getLogger("crud.excepciones.ExcepcionesUtilidad");
        TestLogHandler logHandler = new TestLogHandler();
        logger.setLevel(Level.ALL);
        logger.addHandler(logHandler);

        // 🔹 Esperar a que la aplicación maneje el error
        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS); // Aumentamos el tiempo para evitar falsos negativos

        // 🔹 Verificar que los logs contienen el mensaje esperado
        assertTrue("❌ No se detectó el mensaje de error esperado en los logs.",
                logHandler.containsMessage("Conexión con servidor fallida"));
    }

    // Clase auxiliar para capturar logs correctamente
    class TestLogHandler extends Handler {

        private final StringBuilder logMessages = new StringBuilder();

        @Override
        public void publish(LogRecord record) {
            logMessages.append(record.getMessage()).append("\n");
            System.out.println("🟢 LOG CAPTURADO: " + record.getMessage()); // Depuración
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        public boolean containsMessage(String message) {
            return logMessages.toString().contains(message);
        }
    }

}
