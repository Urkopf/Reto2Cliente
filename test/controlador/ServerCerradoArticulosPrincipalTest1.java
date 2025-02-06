package controlador;

import auxiliarMainTest.MainTestArticulosPrincipal;
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
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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

/**
 * Clase de test para ControladorPedidosPrincipal.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerCerradoArticulosPrincipalTest1 extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(ServerCerradoArticulosPrincipalTest1.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    ;

    public void start(Stage stage) throws Exception {
        new MainTestArticulosPrincipal().start(stage);
        sleep(1000);

    }

    // ---------------------------------------------------------------------
    // EJEMPLO: testA con intento de abrir la ventana de pedidos principal
    // cargar la tabla inicial y encontrarse con el server cerado
    // ---------------------------------------------------------------------
    //@Ignore
    @Test
    public void testA_ServidorCerrado() {
        sleep(10000);
        // Verificamos mensaje de guardado correcto
        clickOn("Aceptar"); // Cerrar el Alert

    }
}
