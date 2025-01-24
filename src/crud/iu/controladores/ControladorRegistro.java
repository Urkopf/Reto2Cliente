package crud.iu.controladores;

import crud.negocio.FactoriaUsuarios;
import crud.objetosTransferibles.Categoria;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
//import utilidades.Message;
import crud.objetosTransferibles.Usuario;
import crud.objetosTransferibles.Cliente;
import crud.objetosTransferibles.Departamento;
import crud.objetosTransferibles.Trabajador;
import crud.seguridad.UtilidadesCifrado;
import static crud.seguridad.UtilidadesCifrado.cargarClavePublica;
import static crud.seguridad.UtilidadesCifrado.encriptarConClavePublica;
import static crud.utilidades.AlertUtilities.showConfirmationDialog;
import static crud.utilidades.AlertUtilities.showErrorDialog;
import static crud.utilidades.ExcepcionesUtilidad.clasificadorExcepciones;
import static crud.utilidades.ValidateUtilities.isValid;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;

/**
 * Controlador FXML para la vista de registro (SignUp). Este controlador
 * gestiona la interacción entre la interfaz de usuario y la lógica de negocio
 * para el registro de nuevos usuarios.
 *
 * @author Urko, Sergio
 */
public class ControladorRegistro implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControladorRegistro.class.getName());
    private Stage stage = new Stage();
    private FactoriaUsuarios factoria = FactoriaUsuarios.getInstance();
    private Cliente userClienteOriginal;
    private Trabajador userTrabajadorOriginal;
    private Cliente userCliente;
    private Trabajador userTrabajador;
    private boolean hasError = false;  // Indica si hay errores en el formulario

    // Elementos de la interfaz FXML
    @FXML
    private Label labelTitulo;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextField campoApellido1;
    @FXML
    private TextField campoApellido2;
    @FXML
    private TextField campoEmail;
    @FXML
    private PasswordField campoContrasena;
    @FXML
    private PasswordField campoRepiteContrasena;
    @FXML
    private TextField campoContrasenaVista;
    @FXML
    private TextField campoRepiteContrasenaVista;
    @FXML
    private TextField campoDireccion;
    @FXML
    private TextField campoCiudad;
    @FXML
    private TextField campoCodigoPostal;
    @FXML
    private TextField campoCIF;
    @FXML
    private TextField campoSector;
    @FXML
    private TextField campoTelefono;
    @FXML
    private RadioButton radioCliente;
    @FXML
    private RadioButton radioTrabajador;
    @FXML
    private CheckBox checkActivo;
    @FXML
    private Button botonRegistrar;
    @FXML
    private Button botonCancelar;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView errorNombre;
    @FXML
    private ImageView errorApellido1;
    @FXML
    private ImageView errorApellido2;
    @FXML
    private ImageView errorEmail;
    @FXML
    private ImageView errorContrasena;
    @FXML
    private ImageView errorRepiteContrasena;
    @FXML
    private ImageView errorDireccion;
    @FXML
    private ImageView errorCiudad;
    @FXML
    private ImageView errorCodigoPostal;
    @FXML
    private ImageView errorCIF;
    @FXML
    private ImageView errorSector;
    @FXML
    private ImageView errorTelefono;
    @FXML
    private ComboBox<Departamento> comboDepartamento = new ComboBox();
    @FXML
    private ComboBox<Categoria> comboCategoria = new ComboBox();
    @FXML
    private Label labelCategoria;
    @FXML
    private Label labelDepartamento;
    @FXML
    private HBox avisoNoActivo;  // Caja de advertencia para mostrar información adicional
    @FXML
    private Button botonOjoContrasena;  // Botón para alternar la visibilidad de la contraseña
    @FXML
    private Button botonOjoRepite;  // Botón para alternar la visibilidad de la confirmación de la contraseña
    @FXML
    private ImageView imagenSector;
    @FXML
    private ImageView imagenTelefono;
    @FXML
    private Group grupoSector;
    @FXML
    private Group grupoTelefono;

    private ContextMenu contextMenu;  // Menú contextual personalizado
    private boolean actualizar;
    private boolean esCliente;

    // Crea un ToggleGroup desde Java
    private ToggleGroup grupoRadio = new ToggleGroup();

    /**
     * Inicializa el controlador y configura el menú contextual, los eventos de
     * los botones y la lógica de validación del formulario.
     *
     * @param location La ubicación de la vista FXML.
     * @param resources Los recursos de internacionalización.
     *
     * @author Urko
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Crear el menú contextual personalizado
        contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("context-menu");
        contextMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;"
                + "-fx-padding: 10px;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5;");

        // Opción "Borrar campos" en el menú contextual
        MenuItem clearFieldsItem = new MenuItem("Borrar campos");
        clearFieldsItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        clearFieldsItem.setOnAction(event -> handleClearFields());

        // Opción "Salir" en el menú contextual
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setStyle("-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: 'Protest Strike';"
                + "-fx-text-fill: #FFFFFF;"
                + "-fx-background-color: transparent;"
                + "-fx-max-width: 250px;"
                + "-fx-wrap-text: true;");
        exitItem.setOnAction(event -> handleExit());

        // Añadir las opciones personalizadas al menú contextual
        contextMenu.getItems().addAll(clearFieldsItem, exitItem);

        // Asignar el menú personalizado a cada campo de texto y eliminar el menú predeterminado
        assignCustomContextMenu(campoNombre);
        assignCustomContextMenu(campoApellido1);
        assignCustomContextMenu(campoApellido2);
        assignCustomContextMenu(campoEmail);
        assignCustomContextMenu(campoDireccion);
        assignCustomContextMenu(campoCiudad);
        assignCustomContextMenu(campoCodigoPostal);
        assignCustomContextMenu(campoCIF);
        assignCustomContextMenu(campoContrasena);
        assignCustomContextMenu(campoRepiteContrasena);
        assignCustomContextMenu(campoSector);
        assignCustomContextMenu(campoTelefono);
        assignCustomContextMenuCombo(comboDepartamento);
        assignCustomContextMenuCombo(comboCategoria);

        // Asignar el menú contextual al GridPane
        gridPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(gridPane, event.getScreenX(), event.getScreenY());
            }
        });

        // Añadir listener a cada TextField o PasswordField en el GridPane
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TextField || node instanceof PasswordField || node instanceof ComboBox) {
                node.setOnKeyTyped(event -> hideErrorImage(node));  // Ocultar error tan pronto como se escribe algo
            }
        }

        for (Node node : grupoSector.getChildren()) {
            if (node instanceof TextField || node instanceof PasswordField || node instanceof ComboBox) {
                node.setOnKeyTyped(event -> hideErrorImage(node));  // Ocultar error tan pronto como se escribe algo
            }
        }

        for (Node node : grupoTelefono.getChildren()) {
            if (node instanceof TextField || node instanceof PasswordField || node instanceof ComboBox) {
                node.setOnKeyTyped(event -> hideErrorImage(node));  // Ocultar error tan pronto como se escribe algo
            }
        }

        // Asegura que se salte estos campos porque son auxiliares
        campoContrasena.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();  // Evita la acción por defecto de la tecla TAB
                campoRepiteContrasena.requestFocus();  // Mover el foco al siguiente campo
            }
        });

        campoRepiteContrasena.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();  // Evita la acción por defecto de la tecla TAB
                radioCliente.requestFocus();  // Mover el foco al siguiente campo
            }
        });

        // Añade los botones de radio al ToggleGroup
        radioCliente.setToggleGroup(grupoRadio);
        radioTrabajador.setToggleGroup(grupoRadio);

        // Añade un listener para los cambios en la selección
        grupoRadio.selectedToggleProperty().addListener((observable, oldValue, newValue) -> handleRadioChange());
        radioCliente.setSelected(true);
        // Configuración inicial
        handleRadioChange(); // Ajusta visibilidad inicial según el estado de los botones

        comboDepartamento.setItems(FXCollections.observableArrayList(Departamento.values()));

        // Añade un listener para el ComboBox de Departamento
        comboDepartamento.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                actualizarCategorias(newValue);
            }
        });

        // Configuración inicial del ComboBox de Categorías
        if (!comboDepartamento.getItems().isEmpty()) {
            comboDepartamento.setValue(comboDepartamento.getItems().get(0)); // Selecciona el primer departamento por defecto
            actualizarCategorias(comboDepartamento.getValue());
        }
    }

    private void actualizarCategorias(Departamento departamento) {
        // Filtra las categorías según el departamento
        ObservableList<Categoria> categoriasFiltradas = FXCollections.observableArrayList();
        for (Categoria categoria : Categoria.values()) {
            if (categoria.getDepartamento() == departamento) {
                categoriasFiltradas.add(categoria);
            }
        }

        // Actualiza las categorías en el ComboBox de Categorías
        comboCategoria.setItems(categoriasFiltradas);

        // Selecciona el primer valor por defecto si hay categorías disponibles
        if (!categoriasFiltradas.isEmpty()) {
            comboCategoria.setValue(categoriasFiltradas.get(0));
        } else {
            comboCategoria.setValue(null); // Limpia la selección si no hay categorías
        }
    }

    /**
     * Asigna el menú contextual personalizado a un campo de texto.
     *
     * @param textField El campo de texto al que se le asignará el menú
     * contextual.
     * @author Sergio
     */
    private void assignCustomContextMenu(TextField textField) {
        // Asignar el menú contextual personalizado y eliminar el predeterminado
        textField.setContextMenu(contextMenu);
    }

    private void assignCustomContextMenuCombo(ComboBox combobox) {
        // Asignar el menú contextual personalizado y eliminar el predeterminado
        combobox.setContextMenu(contextMenu);
    }

    /**
     * Establece la ventana principal.
     *
     * @param stage El escenario de la aplicación.
     * @author Sergio
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(Object user) {
        if (user != null) {
            if (user instanceof Cliente) {
                this.userClienteOriginal = new Cliente();
                this.userClienteOriginal = (Cliente) user;

            } else {
                this.userTrabajadorOriginal = new Trabajador();
                this.userTrabajadorOriginal = (Trabajador) user;
            }
        }
    }

    public void setModoActualizar(boolean modo) {
        this.actualizar = modo;
    }

    /**
     * Inicializa el escenario con el contenido de la vista.
     *
     * @param root El nodo raíz de la escena.
     * @author Sergio
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Inicializando la carga del stage");
            Scene scene = new Scene(root);
            scene.focusOwnerProperty();
            stage.setScene(scene);
            stage.setTitle("Registro");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            botonRegistrar.setOnAction(null);
            botonCancelar.setOnAction(null); // Eliminar cualquier manejador anterior

            // Asignar manejadores de eventos a los botones
            botonRegistrar.addEventHandler(ActionEvent.ACTION, this::handleButtonRegister);
            botonCancelar.addEventHandler(ActionEvent.ACTION, this::handleButtonCancel);
            checkActivo.addEventHandler(ActionEvent.ACTION, this::handleActiveCheckBoxChange);

            // Configurar la visibilidad de las contraseñas
            botonOjoContrasena.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    utilidadVisibilidadContrasena(campoContrasena, campoContrasenaVista);
                }
            });
            botonOjoRepite.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    utilidadVisibilidadContrasena(campoRepiteContrasena, campoRepiteContrasenaVista);
                }
            });

            botonOjoContrasena.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    utilidadOcultacionContrasena(campoContrasena, campoContrasenaVista);
                }
            });
            botonOjoRepite.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    utilidadOcultacionContrasena(campoRepiteContrasena, campoRepiteContrasenaVista);
                }
            });
            configureMnemotecnicKeys();  // Configurar teclas de acceso rápido
            if (actualizar) {
                actualizarInit();
            }
            stage.show();
        } catch (Exception e) {
            clasificadorExcepciones(e, e.getMessage());
        }
    }

    /**
     * Configura las teclas de acceso rápido para los botones de registrar y
     * cancelar.
     *
     * @author Urko
     */
    private void configureMnemotecnicKeys() {
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown() && event.getCode() == KeyCode.C) {
                botonCancelar.fire();  // Simula el clic en el botón Cancelar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                botonRegistrar.fire();  // Simula el clic en el botón Registrar
                event.consume();  // Evita la propagación adicional del evento
            }
        });
    }

    /**
     * Maneja la acción al mostrar la ventana de registro.
     *
     * @param event El evento de acción.
     *
     * @author Urko
     */
    private void handleWindowShowing(javafx.event.Event event) {
        LOGGER.info("Mostrando Ventana de registro");
        gridPane.requestFocus();  // Establecer el foco en el GridPane
    }

    /*
    /**
     * Maneja la acción del botón de registro.
     *
     * @param event El evento de acción.
     * @author Sergio
     */
    @FXML
    private void handleButtonRegister(ActionEvent event) {
        LOGGER.info("Botón Aceptar presionado");
        hasError = false;

        // Verificar si todos los campos están llenos
        if (!areAllFieldsFilled()) {

            LOGGER.severe("Error: Todos los campos deben ser completados.");
            for (Node node : gridPane.getChildren()) {
                if (node instanceof TextField || node instanceof PasswordField) {
                    if (((TextField) node).getText().isEmpty()) {
                        showErrorImage(node); // Mostrar error y marcar el campo
                        hasError = true;
                    }
                }
            }
            if (!radioTrabajador.isSelected()) {
                if (campoSector.getText().isEmpty()) {
                    showErrorImage(campoSector);
                }
                if (campoTelefono.getText().isEmpty()) {
                    showErrorImage(campoTelefono);
                }
            }
        }

        // Validar el formato de los campos
        campoContrasena.setText(campoContrasena.getText().trim());
        if (!actualizar && !isValid(campoContrasena.getText(), "pass")) {
            showErrorImage(campoContrasena);
            hasError = true;
        }

        campoRepiteContrasena.setText(campoRepiteContrasena.getText().trim());
        if (!actualizar && !campoContrasena.getText().equals(campoRepiteContrasena.getText())) {
            showErrorImage(campoRepiteContrasena);
            hasError = true;
        }

        if (!actualizar && !isValid(campoEmail.getText(), "email")) {
            showErrorImage(campoEmail);
            hasError = true;
        }
        if (!isValid(campoCodigoPostal.getText(), "zip")) {
            showErrorImage(campoCodigoPostal);
            hasError = true;
        }

        if (!isValid(campoCIF.getText(), "cif")) {
            showErrorImage(campoCIF);
            hasError = true;
        }
        if (!radioTrabajador.isSelected()) {
            if (!isValid(campoTelefono.getText(), "telefono")) {
                showErrorImage(campoTelefono);
                hasError = true;
            }
        }

        // Si hay errores, no continuar
        if (hasError) {
            LOGGER.severe("Hay errores en el formulario.");
            showErrorDialog(AlertType.ERROR, "Error", "Uno o varios campos incorrectos o vacíos. Mantenga el cursor encima de los campos para más información.");

        } else {
            LOGGER.info("Validación de campos correcta.");

            // Cargar claves desde archivos
            String contraseñaEncriptada = "";
            PublicKey clavePublica;
            try {
                clavePublica = cargarClavePublica();
                // Contraseña del cliente
                String contraseña = campoContrasena.getText();

                // Cliente encripta la contraseña
                contraseñaEncriptada = encriptarConClavePublica(contraseña, clavePublica);
            } catch (Exception ex) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Si no hay errores, proceder con el registro
            // Asigna la contraseña cifrada al usuario
            if (radioCliente.isSelected()) {
                userCliente = new Cliente(
                        campoSector.getText(),
                        campoTelefono.getText(),
                        campoEmail.getText(),
                        contraseñaEncriptada,
                        campoNombre.getText() + " " + campoApellido1.getText() + " " + campoApellido2.getText(),
                        campoDireccion.getText(),
                        campoCiudad.getText(),
                        campoCodigoPostal.getText(),
                        campoCIF.getText(),
                        checkActivo.isSelected()
                );
            } else {
                userTrabajador = new Trabajador(
                        comboDepartamento.getSelectionModel().getSelectedItem(),
                        comboCategoria.getSelectionModel().getSelectedItem(),
                        campoEmail.getText(),
                        contraseñaEncriptada,
                        campoNombre.getText() + " " + campoApellido1.getText() + " " + campoApellido2.getText(),
                        campoDireccion.getText(),
                        campoCiudad.getText(),
                        campoCodigoPostal.getText(),
                        campoCIF.getText(),
                        checkActivo.isSelected()
                );
            }

            // Enviar el objeto al servidor
            if (checkActivo.isSelected() || (!checkActivo.isSelected() && confirmNoActiveUserRegister())) {
                try {
                    if (actualizar) {
                        if (radioCliente.isSelected()) {
                            userCliente.setId(userClienteOriginal.getId());
                            factoria.accesoCliente().actualizarCliente(userCliente);
                            factoria.cargarInicioSesion(stage, userCliente.getCorreo());
                        } else {
                            boolean claveCorrecta = verificarClaveUnicaTrabajador();
                            if (!claveCorrecta) {
                                // Si la clave no es correcta, paramos aquí y no registramos
                                showErrorDialog(AlertType.ERROR, "Error de Clave", "La clave única para registrar trabajadores no es correcta.");
                                return;
                            }
                            userTrabajador.setId(userTrabajadorOriginal.getId());
                            factoria.accesoTrabajador().actualizarTrabajador(userTrabajador);
                            factoria.cargarInicioSesion(stage, userTrabajador.getCorreo());
                        }

                    } else {
                        if (radioCliente.isSelected()) {
                            factoria.accesoCliente().crearCliente(userCliente);
                            factoria.cargarInicioSesion(stage, userCliente.getCorreo());
                        } else {

                            boolean claveCorrecta = verificarClaveUnicaTrabajador();
                            if (!claveCorrecta) {
                                // Si la clave no es correcta, paramos aquí y no registramos
                                showErrorDialog(AlertType.ERROR, "Error de Clave", "La clave única para registrar trabajadores no es correcta.");
                                return;
                            }
                            factoria.accesoTrabajador().crearTrabajador(userTrabajador);
                            factoria.cargarInicioSesion(stage, userTrabajador.getCorreo());
                        }
                    }
                } catch (Exception e) {
                    clasificadorExcepciones(e, e.getMessage());
                }
            }

            //  messageManager(response);
        }
    }

    private boolean verificarClaveUnicaTrabajador() {
        // Creamos un diálogo de texto
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Clave Única de Trabajador");
        dialog.setHeaderText("Para registrar a un Trabajador, es necesaria una clave única.");
        dialog.setContentText("Introduce la clave única:");

        dialog.getDialogPane().getStyleClass().add("myDialog");

        // Mostrar diálogo y esperar resultado
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String claveIngresada = result.get().trim();

            try {
                // 1) Hasheamos la clave introducida
                String hashClaveIngresada = UtilidadesCifrado.hashearContraseña(claveIngresada);

                // 2) Cargamos la propiedad TRABAJADOR_PASS desde confCliente.properties
                String hashEsperado
                        = ResourceBundle.getBundle("recursos.configCliente")
                                .getString("TRABAJADOR_PASS");

                // 3) Comparamos el hash que ingresó el usuario con el hash guardado
                return hashClaveIngresada.equals(hashEsperado);

            } catch (Exception e) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }
        } else {
            // Si el usuario cancela o cierra el diálogo
            return false;
        }
    }

    private boolean confirmNoActiveUserRegister() {
        // Crear la alerta de confirmación
        return showConfirmationDialog("Confirmación de Registro", "Si el usuario esta 'No Activo', no podrá iniciar sesión ¿Desea continuar el registro?");

    }

    /**
     * Maneja la acción del botón de cancelar.
     *
     * @param event El evento de acción.
     *
     * @author Urko
     */
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        // Crear la alerta de confirmación

        if (showConfirmationDialog("Confirmación", "¿Estás seguro de que deseas cancelar?")) {
            // Si el usuario confirma, realizar la acción de cancelar
            factoria.cargarInicioSesion(stage, "");
        }
    }

    /**
     * Metodo para visualizar una alerta de confirmacion de registro.
     *
     * @return true si pulsa aceptar o false si pulsa cancelar.
     *
     * @author Urko
     */
    /*    private boolean confirmNoActiveUserRegister() {
        // Crear la alerta de confirmación
        return showConfirmationDialog("Confirmación de Registro", "Si el usuario esta 'No Activo', no podrá iniciar sesión ¿Desea continuar el registro?");

    }

    /**
     * Maneja el cambio en el estado del CheckBox de actividad.
     *
     * @param event El evento de acción.
     * @author Sergio
     */
    @FXML
    private void handleActiveCheckBoxChange(ActionEvent event) {
        avisoNoActivo.setVisible(!checkActivo.isSelected());  // Mostrar/ocultar la advertencia
    }

    /**
     * Muestra el icono de error en un campo que contiene un error de
     * validación.
     *
     * @param node El nodo que representa el campo.
     * @author Urko
     */
    private void showErrorImage(Node node) {
        node.getStyleClass().add("error-field");  // Añadir clase CSS para marcar el error
        showErrorIcon(node);  // Mostrar icono de error
    }

    /**
     * Oculta el icono de error en un campo cuando se corrige el error.
     *
     * @param node El nodo que representa el campo.
     * @author Urko
     */
    private void hideErrorImage(Node node) {
        node.getStyleClass().remove("error-field");  // Eliminar clase CSS
        hideErrorIcon(node);  // Ocultar el icono de error
    }

    /**
     * Muestra el icono de error correspondiente al campo indicado.
     *
     * @param node El nodo que representa el campo.
     * @author Urko
     */
    private void showErrorIcon(Node node) {
        if (node == campoNombre) {
            errorNombre.setVisible(true);
        } else if (node == campoApellido1) {
            errorApellido1.setVisible(true);
        } else if (node == campoApellido2) {
            errorApellido2.setVisible(true);
        } else if (node == campoEmail && !actualizar) {
            errorEmail.setVisible(true);
        } else if (node == campoContrasena && !actualizar) {
            errorContrasena.setVisible(true);
        } else if (node == campoRepiteContrasena && !actualizar) {
            errorRepiteContrasena.setVisible(true);
        } else if (node == campoDireccion) {
            errorDireccion.setVisible(true);
        } else if (node == campoCiudad) {
            errorCiudad.setVisible(true);
        } else if (node == campoCodigoPostal) {
            errorCodigoPostal.setVisible(true);
        } else if (node == campoSector) {
            errorSector.setVisible(true);
        } else if (node == campoTelefono) {
            errorTelefono.setVisible(true);
        } else if (node == campoCIF) {
            errorCIF.setVisible(true);
        }
    }

    /**
     * Oculta el icono de error correspondiente al campo indicado.
     *
     * @param node El nodo que representa el campo.
     * @author Urko
     */
    private void hideErrorIcon(Node node) {
        if (node == campoNombre) {
            errorNombre.setVisible(false);
        } else if (node == campoApellido1) {
            errorApellido1.setVisible(false);
        } else if (node == campoApellido2) {
            errorApellido2.setVisible(false);
        } else if (node == campoEmail && !actualizar) {
            errorEmail.setVisible(false);
        } else if (node == campoContrasena && !actualizar) {
            errorContrasena.setVisible(false);
        } else if (node == campoRepiteContrasena && !actualizar) {
            errorRepiteContrasena.setVisible(false);
        } else if (node == campoDireccion) {
            errorDireccion.setVisible(false);
        } else if (node == campoCiudad) {
            errorCiudad.setVisible(false);
        } else if (node == campoCodigoPostal) {
            errorCodigoPostal.setVisible(false);
        } else if (node == campoSector) {
            errorSector.setVisible(false);
        } else if (node == campoTelefono) {
            errorTelefono.setVisible(false);
        } else if (node == campoCIF) {
            errorCIF.setVisible(false);
        }
    }

    /**
     * Gestiona la respuesta del servidor a la solicitud de registro.
     *
     * @param message El mensaje de respuesta del servidor.
     * @author Sergio
     */
    /*  private void messageManager(Message message) {
        switch (message.getType()) {
            case OK_RESPONSE:
                botonRegistrar.setDisable(true);
                if (!actualizar) {
                    showErrorDialog(AlertType.INFORMATION, "Información", "El registro se ha realizado con éxito.");
                } else {
                    showErrorDialog(AlertType.INFORMATION, "Información", "La actualización se ha realizado con éxito.");
                }
                factoria.loadSignInWindow(stage, user.getCorreo());
                break;
            case SIGNUP_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "Se ha producido un error al intentar registrar sus datos. Vuelva a intentarlo.");
                break;
            case LOGIN_EXIST_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "El correo electrónico ya existe en la base de datos.");
                campoEmail.setStyle("-fx-border-color: red;");
                errorEmail.setVisible(true);
                break;
            case BAD_RESPONSE:
                showErrorDialog(AlertType.ERROR, "Error", "Error interno de la base de datos, inténtelo de nuevo...");
                break;
            case SQL_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "Error al introducir los datos en la base de datos, inténtelo de nuevo...");
                break;
            case CONNECTION_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "Error de conexión con la base de datos. No hay conexión disponible, inténtelo de nuevo...");
                break;
            case SERVER_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "Servidor no encontrado, inténtelo de nuevo...");
                break;
        }
    }

    /**
     * Verifica que todos los campos obligatorios estén llenos.
     *
     * @return true si todos los campos están llenos, false en caso contrario.
     * @author Sergio
     */
    private boolean areAllFieldsFilled() {
        for (Node node : gridPane.getChildren()) {
            if ((node instanceof TextField || node instanceof PasswordField)
                    && (node != campoContrasenaVista)
                    && (node != campoRepiteContrasenaVista)) {
                // Validar los demás campos
                if (((TextField) node).getText() == null || ((TextField) node).getText().isEmpty()) {
                    LOGGER.severe("Error: El campo " + ((TextField) node).getPromptText() + " está vacío.");
                    return false;
                }
            }
        }

        if (radioTrabajador.isSelected()) {
            for (Node node : grupoSector.getChildren()) {
                if ((node instanceof TextField || node instanceof PasswordField)) {

                    // Validar los demás campos
                    if (((TextField) node).getText() == null || ((TextField) node).getText().isEmpty()) {
                        LOGGER.severe("Error: El campo " + ((TextField) node).getPromptText() + " está vacío.");
                        return false;
                    }
                }
            }
            for (Node node : grupoTelefono.getChildren()) {
                if ((node instanceof TextField || node instanceof PasswordField)) {

                    // Validar los demás campos
                    if (((TextField) node).getText() == null || ((TextField) node).getText().isEmpty()) {
                        LOGGER.severe("Error: El campo " + ((TextField) node).getPromptText() + " está vacío.");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Limpia todos los campos del formulario.
     *
     * @author Sergio
     */
    @FXML
    private void handleClearFields() {
        campoNombre.clear();
        campoApellido1.clear();
        campoApellido2.clear();

        if (!actualizar) {
            campoEmail.clear();
            campoContrasena.clear();
            campoRepiteContrasena.clear();
        }

        campoDireccion.clear();
        campoCiudad.clear();
        campoCodigoPostal.clear();
        campoSector.clear();
        campoTelefono.clear();
        comboDepartamento.getSelectionModel().clearSelection();
        labelTitulo.requestFocus();  // Devuelve el foco al título

    }

    /**
     * Cierra la ventana de registro.
     *
     * @author Sergio
     */
    @FXML
    private void handleExit() {
        // Obtener el Stage a través del GridPane (o cualquier otro nodo de la ventana)
        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.close();  // Cierra la ventana
    }

    /**
     * Muestra el campo de contraseña en texto plano y oculta el PasswordField.
     *
     * @param passwordFieldParam El PasswordField que se está mostrando.
     * @param textFieldParam El TextField alternativo para ver la contraseña.
     * @author Urko
     */
    private void utilidadVisibilidadContrasena(PasswordField passwordFieldParam, TextField textFieldParam) {
        textFieldParam.setText(passwordFieldParam.getText());  // Copiar contenido del PasswordField al TextField
        passwordFieldParam.setVisible(false);
        textFieldParam.setVisible(true);

        // Cambiar la imagen del botón a "mostrar"
        if (passwordFieldParam == campoContrasena) {
            ImageView imageView = (ImageView) botonOjoContrasena.getGraphic();
            imageView.setImage(new Image("recursos/iconos/ocultar.png"));
        } else if (passwordFieldParam == campoRepiteContrasena) {
            ImageView imageView = (ImageView) botonOjoRepite.getGraphic();
            imageView.setImage(new Image("recursos/iconos/ocultar.png"));
        }
        // Recuperar el foco y colocar el cursor al final del texto sin seleccionar todo
        textFieldParam.requestFocus();
        textFieldParam.positionCaret(textFieldParam.getText().length());
    }

    /**
     * Oculta el campo de texto plano y muestra el PasswordField.
     *
     * @param passwordFieldParam El PasswordField que se mostrará.
     * @param textFieldParam El TextField alternativo para ocultar la
     * contraseña.
     * @author Sergio
     */
    private void utilidadOcultacionContrasena(PasswordField passwordFieldParam, TextField textFieldParam) {
        passwordFieldParam.setText(textFieldParam.getText());  // Copiar contenido del TextField al PasswordField
        passwordFieldParam.setVisible(true);
        textFieldParam.setVisible(false);

        // Cambiar la imagen del botón a "ocultar"
        if (passwordFieldParam == campoContrasena) {
            ImageView imageView = (ImageView) botonOjoContrasena.getGraphic();
            imageView.setImage(new Image("recursos/iconos/visualizar.png"));
        } else if (passwordFieldParam == campoRepiteContrasena) {
            ImageView imageView = (ImageView) botonOjoRepite.getGraphic();
            imageView.setImage(new Image("recursos/iconos/visualizar.png"));
        }
        // Recuperar el foco y colocar el cursor al final del texto sin seleccionar todo
        passwordFieldParam.requestFocus();
        passwordFieldParam.positionCaret(passwordFieldParam.getText().length());
    }

    private void actualizarInit() {
        labelTitulo.setText("Actualizar Datos");

        if (userClienteOriginal != null) {
            String[] nombreCompleto = userClienteOriginal.getNombre().split(" ");

            campoNombre.setText(nombreCompleto[0]);
            campoApellido1.setText(nombreCompleto[1]);
            campoApellido2.setText(nombreCompleto[2]);
            campoEmail.setText(userClienteOriginal.getCorreo());
            campoEmail.setDisable(true);
            campoContrasena.setDisable(true);
            campoRepiteContrasena.setDisable(true);
            campoContrasenaVista.setVisible(false);
            campoRepiteContrasenaVista.setVisible(false);
            campoDireccion.setText(userClienteOriginal.getCalle());
            campoCiudad.setText(userClienteOriginal.getCiudad());
            campoCodigoPostal.setText(userClienteOriginal.getCodPostal());
            campoCIF.setText(userClienteOriginal.getCif());
            checkActivo.setSelected(userClienteOriginal.getActivo());
            campoSector.setText((userClienteOriginal).getSector());
            campoTelefono.setText((userClienteOriginal).getTelefono());
        } else {
            String[] nombreCompleto = userTrabajadorOriginal.getNombre().split(" ");

            campoNombre.setText(nombreCompleto[0]);
            campoApellido1.setText(nombreCompleto[1]);
            campoApellido2.setText(nombreCompleto[2]);
            campoEmail.setText(userTrabajadorOriginal.getCorreo());
            campoEmail.setDisable(true);
            campoContrasena.setDisable(true);
            campoRepiteContrasena.setDisable(true);
            campoContrasenaVista.setVisible(false);
            campoRepiteContrasenaVista.setVisible(false);
            campoDireccion.setText(userTrabajadorOriginal.getCalle());
            campoCiudad.setText(userTrabajadorOriginal.getCiudad());
            campoCodigoPostal.setText(userTrabajadorOriginal.getCodPostal());
            campoCIF.setText(userTrabajadorOriginal.getCif());
            checkActivo.setSelected(userTrabajadorOriginal.getActivo());
            comboDepartamento.getSelectionModel().select(userTrabajadorOriginal.getDepartamento());
            comboCategoria.getSelectionModel().select(userTrabajadorOriginal.getCategoria());
        }

        //Faltan campos
        botonRegistrar.setText("Actualizar Datos");

        botonOjoContrasena.setVisible(false);
        botonOjoRepite.setVisible(false);;

    }

    /**
     * Método para manejar los cambios en los botones de radio.
     */
    @FXML
    private void handleRadioChange() {
        if (radioCliente.isSelected()) {
            campoSector.setVisible(true);
            campoTelefono.setVisible(true);

            comboDepartamento.setVisible(false);
            comboCategoria.setVisible(false);
            imagenSector.setVisible(true);
            imagenTelefono.setVisible(true);

            labelDepartamento.setVisible(false);
            labelCategoria.setVisible(false);
        } else if (radioTrabajador.isSelected()) {
            campoSector.setVisible(false);
            campoTelefono.setVisible(false);

            comboDepartamento.setVisible(true);
            comboCategoria.setVisible(true);
            labelDepartamento.setVisible(true);
            labelCategoria.setVisible(true);
            imagenSector.setVisible(false);
            imagenTelefono.setVisible(false);

        }
    }

}
