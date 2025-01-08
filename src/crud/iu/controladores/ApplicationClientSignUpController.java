package crud.iu.controladores;

import business.ApplicationClientFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import utilidades.Message;
import utilidades.User;
import static utilities.AlertUtilities.showConfirmationDialog;
import static utilities.AlertUtilities.showErrorDialog;
import static utilities.ValidateUtilities.isValid;

/**
 * Controlador FXML para la vista de registro (SignUp). Este controlador
 * gestiona la interacción entre la interfaz de usuario y la lógica de negocio
 * para el registro de nuevos usuarios.
 *
 * @author Urko, Sergio
 */
public class ApplicationClientSignUpController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ApplicationClientSignUpController.class.getName());
    private Stage stage = new Stage();
    private ApplicationClientFactory factory = ApplicationClientFactory.getInstance();
    private User user;
    private User user2;
    private boolean hasError = false;  // Indica si hay errores en el formulario

    // Elementos de la interfaz FXML
    @FXML
    private Label labelTitulo;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surname1Field;
    @FXML
    private TextField surname2Field;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmpasswordField;
    @FXML
    private TextField passwordFieldVisual;
    @FXML
    private TextField confirmPasswordFieldVisual;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField zipField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private GridPane gridPane;  // Contenedor de todos los campos del formulario
    @FXML
    private ImageView errorImageName;
    @FXML
    private ImageView errorImageSurname1;
    @FXML
    private ImageView errorImageSurname2;
    @FXML
    private ImageView errorImageEmail;
    @FXML
    private ImageView errorImagePass;
    @FXML
    private ImageView errorImagePassRepeat;
    @FXML
    private ImageView errorImageStreet;
    @FXML
    private ImageView errorImageCity;
    @FXML
    private ImageView errorImageZip;
    @FXML
    private HBox warningbox;  // Caja de advertencia para mostrar información adicional
    @FXML
    private Button toggleVisibilityButton1;  // Botón para alternar la visibilidad de la contraseña
    @FXML
    private Button toggleVisibilityButton2;  // Botón para alternar la visibilidad de la confirmación de la contraseña

    private ContextMenu contextMenu;  // Menú contextual personalizado
    private boolean actualizar;

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
        assignCustomContextMenu(nameField);
        assignCustomContextMenu(surname1Field);
        assignCustomContextMenu(surname2Field);
        assignCustomContextMenu(emailField);
        assignCustomContextMenu(streetField);
        assignCustomContextMenu(cityField);
        assignCustomContextMenu(zipField);
        assignCustomContextMenu(passwordField);
        assignCustomContextMenu(confirmpasswordField);

        // Asignar el menú contextual al GridPane
        gridPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(gridPane, event.getScreenX(), event.getScreenY());
            }
        });

        // Añadir listener a cada TextField o PasswordField en el GridPane
        for (Node node : gridPane.getChildren()) {
            if (node instanceof TextField || node instanceof PasswordField) {
                node.setOnKeyTyped(event -> hideErrorImage(node));  // Ocultar error tan pronto como se escribe algo
            }
        }

        // Asegura que se salte estos campos porque son auxiliares
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();  // Evita la acción por defecto de la tecla TAB
                confirmpasswordField.requestFocus();  // Mover el foco al siguiente campo
            }
        });

        confirmpasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();  // Evita la acción por defecto de la tecla TAB
                streetField.requestFocus();  // Mover el foco al siguiente campo
            }
        });
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

    /**
     * Establece la ventana principal.
     *
     * @param stage El escenario de la aplicación.
     * @author Sergio
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
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
            stage.setTitle("Registro de Usuario");
            stage.setResizable(false);
            stage.setOnShowing(this::handleWindowShowing);
            btnRegistrar.setOnAction(null);
            btnCancelar.setOnAction(null); // Eliminar cualquier manejador anterior

            // Asignar manejadores de eventos a los botones
            btnRegistrar.addEventHandler(ActionEvent.ACTION, this::handleButtonRegister);
            btnCancelar.addEventHandler(ActionEvent.ACTION, this::handleButtonCancel);
            activeCheckBox.addEventHandler(ActionEvent.ACTION, this::handleActiveCheckBoxChange);

            // Configurar la visibilidad de las contraseñas
            toggleVisibilityButton1.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibility(passwordField, passwordFieldVisual);
                }
            });
            toggleVisibilityButton2.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibility(confirmpasswordField, confirmPasswordFieldVisual);
                }
            });

            toggleVisibilityButton1.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibilityReleased(passwordField, passwordFieldVisual);
                }
            });
            toggleVisibilityButton2.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    togglePasswordVisibilityReleased(confirmpasswordField, confirmPasswordFieldVisual);
                }
            });
            configureMnemotecnicKeys();  // Configurar teclas de acceso rápido
            if (actualizar) {
                actualizarInit();
            }
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar el stage", e);
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
                btnCancelar.fire();  // Simula el clic en el botón Cancelar
                event.consume();  // Evita la propagación adicional del evento
            } else if (event.isAltDown() && event.getCode() == KeyCode.R) {
                btnRegistrar.fire();  // Simula el clic en el botón Registrar
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
        }

        // Validar el formato de los campos
        passwordField.setText(passwordField.getText().trim());
        if (!isValid(passwordField.getText(), "pass")) {
            showErrorImage(passwordField);
            hasError = true;
        }

        confirmpasswordField.setText(confirmpasswordField.getText().trim());
        if (!passwordField.getText().equals(confirmpasswordField.getText())) {
            showErrorImage(confirmpasswordField);
            hasError = true;
        }

        if (!isValid(emailField.getText(), "email")) {
            showErrorImage(emailField);
            hasError = true;
        }
        if (!isValid(zipField.getText(), "zip")) {
            showErrorImage(zipField);
            hasError = true;
        }

        // Si hay errores, no continuar
        if (hasError) {
            LOGGER.severe("Hay errores en el formulario.");
            showErrorDialog(AlertType.ERROR, "Error", "Uno o varios campos incorrectos o vacíos. Mantenga el cursor encima de los campos para más información.");
            btnRegistrar.setDisable(false);
        } else {
            // Si no hay errores, proceder con el registro
            user2 = new User(emailField.getText(), passwordField.getText(),
                    nameField.getText() + " " + surname1Field.getText() + " " + surname2Field.getText(),
                    streetField.getText(), zipField.getText(), cityField.getText(), activeCheckBox.isSelected());

            LOGGER.info("Validación de campos correcta.");
            Message response;
            if (activeCheckBox.isSelected() || (!activeCheckBox.isSelected() && confirmNoActiveUserRegister())) {
                if (actualizar) {
                    user2.setResUserId(user.getResUserId());
                    response = ApplicationClientFactory.getInstance().access().actualizar(user2);
                } else {
                    response = ApplicationClientFactory.getInstance().access().signUp(user2);
                }

                messageManager(response);
            }

        }
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
            factory.loadSignInWindow(stage, "");
        }
    }

    /**
     * Metodo para visualizar una alerta de confirmacion de registro.
     *
     * @return true si pulsa aceptar o false si pulsa cancelar.
     *
     * @author Urko
     */
    private boolean confirmNoActiveUserRegister() {
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
        warningbox.setVisible(!activeCheckBox.isSelected());  // Mostrar/ocultar la advertencia
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
        if (node == nameField) {
            errorImageName.setVisible(true);
        } else if (node == surname1Field) {
            errorImageSurname1.setVisible(true);
        } else if (node == surname2Field) {
            errorImageSurname2.setVisible(true);
        } else if (node == emailField && !actualizar) {
            errorImageEmail.setVisible(true);
        } else if (node == passwordField && !actualizar) {
            errorImagePass.setVisible(true);
        } else if (node == confirmpasswordField && !actualizar) {
            errorImagePassRepeat.setVisible(true);
        } else if (node == streetField) {
            errorImageStreet.setVisible(true);
        } else if (node == cityField) {
            errorImageCity.setVisible(true);
        } else if (node == zipField) {
            errorImageZip.setVisible(true);
        }
    }

    /**
     * Oculta el icono de error correspondiente al campo indicado.
     *
     * @param node El nodo que representa el campo.
     * @author Urko
     */
    private void hideErrorIcon(Node node) {
        if (node == nameField) {
            errorImageName.setVisible(false);
        } else if (node == surname1Field) {
            errorImageSurname1.setVisible(false);
        } else if (node == surname2Field) {
            errorImageSurname2.setVisible(false);
        } else if (node == emailField && !actualizar) {
            errorImageEmail.setVisible(false);
        } else if (node == passwordField && !actualizar) {
            errorImagePass.setVisible(false);
        } else if (node == confirmpasswordField && !actualizar) {
            errorImagePassRepeat.setVisible(false);
        } else if (node == streetField) {
            errorImageStreet.setVisible(false);
        } else if (node == cityField) {
            errorImageCity.setVisible(false);
        } else if (node == zipField) {
            errorImageZip.setVisible(false);
        }
    }

    /**
     * Gestiona la respuesta del servidor a la solicitud de registro.
     *
     * @param message El mensaje de respuesta del servidor.
     * @author Sergio
     */
    private void messageManager(Message message) {
        switch (message.getType()) {
            case OK_RESPONSE:
                btnRegistrar.setDisable(true);
                if (!actualizar) {
                    showErrorDialog(AlertType.INFORMATION, "Información", "El registro se ha realizado con éxito.");
                } else {
                    showErrorDialog(AlertType.INFORMATION, "Información", "La actualización se ha realizado con éxito.");
                }

                factory.loadSignInWindow(stage, user.getLogin());
                break;
            case OK_ACTUALIZAR:
                btnRegistrar.setDisable(true);

                showErrorDialog(AlertType.INFORMATION, "Información", "La actualización se ha realizado con éxito.");

                factory.loadSignInWindow(stage, user.getLogin());
                break;
            case SIGNUP_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "Se ha producido un error al intentar registrar sus datos. Vuelva a intentarlo.");
                break;
            case LOGIN_EXIST_ERROR:
                showErrorDialog(AlertType.ERROR, "Error", "El correo electrónico ya existe en la base de datos.");
                emailField.setStyle("-fx-border-color: red;");
                errorImageEmail.setVisible(true);
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
                    && (node != passwordFieldVisual)
                    && (node != confirmPasswordFieldVisual)) {
                if (((TextField) node).getText() == null || ((TextField) node).getText().isEmpty()) {
                    LOGGER.severe("Error: El campo " + ((TextField) node).getPromptText() + " está vacío.");
                    return false;
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
        nameField.clear();
        surname1Field.clear();
        surname2Field.clear();
        if (!actualizar) {
            emailField.clear();
            passwordField.clear();
            confirmpasswordField.clear();
        }

        streetField.clear();
        cityField.clear();
        zipField.clear();
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
    private void togglePasswordVisibility(PasswordField passwordFieldParam, TextField textFieldParam) {
        textFieldParam.setText(passwordFieldParam.getText());  // Copiar contenido del PasswordField al TextField
        passwordFieldParam.setVisible(false);
        textFieldParam.setVisible(true);

        // Cambiar la imagen del botón a "mostrar"
        if (passwordFieldParam == passwordField) {
            ImageView imageView = (ImageView) toggleVisibilityButton1.getGraphic();
            imageView.setImage(new Image("resources/iconos/ocultar.png"));
        } else if (passwordFieldParam == confirmpasswordField) {
            ImageView imageView = (ImageView) toggleVisibilityButton2.getGraphic();
            imageView.setImage(new Image("resources/iconos/ocultar.png"));
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
    private void togglePasswordVisibilityReleased(PasswordField passwordFieldParam, TextField textFieldParam) {
        passwordFieldParam.setText(textFieldParam.getText());  // Copiar contenido del TextField al PasswordField
        passwordFieldParam.setVisible(true);
        textFieldParam.setVisible(false);

        // Cambiar la imagen del botón a "ocultar"
        if (passwordFieldParam == passwordField) {
            ImageView imageView = (ImageView) toggleVisibilityButton1.getGraphic();
            imageView.setImage(new Image("resources/iconos/visualizar.png"));
        } else if (passwordFieldParam == confirmpasswordField) {
            ImageView imageView = (ImageView) toggleVisibilityButton2.getGraphic();
            imageView.setImage(new Image("resources/iconos/visualizar.png"));
        }
        // Recuperar el foco y colocar el cursor al final del texto sin seleccionar todo
        passwordFieldParam.requestFocus();
        passwordFieldParam.positionCaret(passwordFieldParam.getText().length());
    }

    private void actualizarInit() {
        labelTitulo.setText("Actualizar Datos");
        String[] nombreCompleto = user.getName().split(" ");

        nameField.setText(nombreCompleto[0]);
        surname1Field.setText(nombreCompleto[1]);
        surname2Field.setText(nombreCompleto[2]);
        emailField.setText(user.getLogin());
        emailField.setDisable(true);
        passwordField.setText(user.getPass());
        passwordField.setDisable(true);
        confirmpasswordField.setText(user.getPass());
        confirmpasswordField.setVisible(false);
        passwordFieldVisual.setVisible(false);
        confirmPasswordFieldVisual.setVisible(false);
        streetField.setText(user.getStreet());
        cityField.setText(user.getCity());
        zipField.setText(user.getZip());
        activeCheckBox.setSelected(user.getActive());
        btnRegistrar.setText("Actualizar Datos");

        toggleVisibilityButton1.setVisible(false);
        toggleVisibilityButton2.setVisible(false);;

    }
}
