/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliarMainTest;

import crud.negocio.FactoriaArticulos;
import crud.objetosTransferibles.Categoria;
import crud.objetosTransferibles.Departamento;
import crud.objetosTransferibles.Trabajador;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author Ser_090
 */
public class MainTestArticulosPrincipal extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FactoriaArticulos factoriaArticulos = FactoriaArticulos.getInstance();

        Trabajador trabajador = new Trabajador(
                Departamento.ALMACENAMIENTO, Categoria.GESTION_INVENTARIO,
                "a@a.es", "12345678A", "Sergio", "Carrer Delagarza, 7, 69º B",
                "Madrid", "45879", "A12345678", true);

        factoriaArticulos.cargarArticulosPrincipal(stage, trabajador, null);

    }

}
