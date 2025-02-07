package crud.seguridad;

import static crud.seguridad.UtilidadesCifrado.cargarClavePrivada;
import static crud.seguridad.UtilidadesCifrado.cargarClavePublica;
import static crud.seguridad.UtilidadesCifrado.cargarClaveSimetrica;
import static crud.seguridad.UtilidadesCifrado.cifrarConClaveSimetrica;
import static crud.seguridad.UtilidadesCifrado.generarYGuardarClaveSimetrica;
import static crud.seguridad.UtilidadesCifrado.generarYGuardarClaves;
import static crud.seguridad.UtilidadesCifrado.hashearContraseña;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;

/**
 * Clase de utilidad para la generación y manejo de claves criptográficas.
 *
 * <p>
 * Esta clase demuestra el uso de métodos de {@code UtilidadesCifrado} para
 * generar y guardar claves asimétricas y simétricas, así como para cifrar y
 * hashear datos. Se incluyen ejemplos (comentados) que muestran cómo cargar las
 * claves, cifrar y descifrar información y realizar el hash de contraseñas.
 * </p>
 *
 * <p>
 * Se recomienda revisar la documentación de {@code UtilidadesCifrado} para
 * comprender los detalles de implementación de cada método utilizado.
 * </p>
 *
 * @author Urko
 *
 * @see crud.seguridad.UtilidadesCifrado
 */
public class GeneradorClaves {

    /**
     * Punto de entrada de la aplicación para generar y manejar claves.
     *
     * <p>
     * Este método ejecuta la generación y almacenamiento de claves asimétricas
     * mediante el método
     * {@link crud.seguridad.UtilidadesCifrado#generarYGuardarClaves()}. Además,
     * contiene ejemplos (comentados) que ilustran cómo:
     * <ul>
     * <li>Cargar claves asimétricas (pública y privada) desde archivos.</li>
     * <li>Cifrar y descifrar una contraseña utilizando dichas claves.</li>
     * <li>Generar, cargar y utilizar una clave simétrica para cifrar datos
     * sensibles.</li>
     * <li>Hashear una contraseña para su almacenamiento seguro.</li>
     * </ul>
     * Para probar estas funcionalidades, basta con descomentar la sección
     * correspondiente.
     * </p>
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        try {
            // Generar y guardar claves asimétricas en archivos.
            generarYGuardarClaves();

            /*
            // Ejemplo de carga de claves asimétricas desde archivos.
            PublicKey clavePublica = cargarClavePublica();
            PrivateKey clavePrivada = cargarClavePrivada();

            // Contraseña del cliente.
            String contraseña = "MiSuperContraseña123";
            System.out.println("Contraseña original: " + contraseña);

            // Cliente encripta la contraseña utilizando la clave pública.
            String contraseñaEncriptada = encriptarConClavePublica(contraseña, clavePublica);
            System.out.println("Contraseña encriptada: " + contraseñaEncriptada);

            // Servidor desencripta la contraseña utilizando la clave privada.
            String contraseñaDesencriptada = desencriptarConClavePrivada(contraseñaEncriptada, clavePrivada);
            System.out.println("Contraseña desencriptada: " + contraseñaDesencriptada);
             */
            // Ejemplo de uso de clave simétrica.
            /*
            // Generar y guardar clave simétrica.
            generarYGuardarClaveSimetrica();

            // Cargar clave simétrica.
            SecretKey claveSimetrica = cargarClaveSimetrica();

            // Cifrar datos sensibles utilizando la clave simétrica.
            String textoCifrado = cifrarConClaveSimetrica("apikey", claveSimetrica);
            System.out.println("Usuario cifrado: " + textoCifrado);

            String textoCifradoPass = cifrarConClaveSimetrica("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", claveSimetrica);
            System.out.println("Pass cifrado: " + textoCifradoPass);

            // Hashear una contraseña antes de almacenarla.
            String contraseñaHasheada = hashearContraseña("trabajadoreficiente2025");
            System.out.println("Contraseña hasheada: " + contraseñaHasheada);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
