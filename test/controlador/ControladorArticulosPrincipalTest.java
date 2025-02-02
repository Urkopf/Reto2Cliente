/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import auxiliarMainTest.MainTestArticulosPrincipal;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

/**
 *
 * @author Ser_090
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControladorArticulosPrincipalTest extends ApplicationTest {

    private TableView<?> tablaArticulos;

    @Override
    public void start(Stage stage) throws Exception {
        new MainTestArticulosPrincipal().start(stage);
        tablaArticulos = lookup("#tablaArticulos").queryTableView();
    }

    public ControladorArticulosPrincipalTest() {
    }

    //Los test Aqui
}
