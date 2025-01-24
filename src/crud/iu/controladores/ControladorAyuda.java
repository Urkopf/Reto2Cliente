/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.iu.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ControladorAyuda implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorAyuda.class.getName());
    @FXML
    private WebView webView;
    private Stage stage;
    private String tipo;

    /**
     * Initializes the controller class.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestion de Usuarios");
        stage.setResizable(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }

    private void handleWindowShowing(WindowEvent event) {
        WebEngine webEngine = webView.getEngine();
        //Load help page.
        webEngine.load(getClass()
                .getResource("/recursos/ayuda/ayuda_" + tipo + ".html").toExternalForm());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
        LOGGER.info("Tipo Ayuda asignado.");
    }

}
