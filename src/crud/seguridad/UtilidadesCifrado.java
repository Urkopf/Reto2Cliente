package crud.seguridad;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Clase de utilidades para el cifrado y descifrado de datos.
 * <p>
 * Esta clase proporciona métodos para generar, guardar y cargar claves
 * asimétricas (ECC) y simétricas (AES), así como para cifrar y descifrar datos
 * utilizando ECIES (para ECC) y AES (para cifrado simétrico). También incluye
 * un método para hashear contraseñas utilizando SHA-256.
 * </p>
 *
 * @author Urko, Sergio
 */
public class UtilidadesCifrado {

    /**
     * Ruta del archivo donde se almacena la clave pública.
     */
    private static final String RUTA_CLAVE_PUBLICA = "clave_publica.key";

    /**
     * Ruta del archivo donde se almacena la clave privada.
     */
    private static final String RUTA_CLAVE_PRIVADA = "clave_privada.key";

    /**
     * Ruta del archivo donde se almacena la clave simétrica.
     */
    private static final String RUTA_CLAVE_SIMETRICA = "clave_simetrica.key";

    static {
        Security.addProvider(new BouncyCastleProvider()); // Agregar soporte para ECIES
    }

    /**
     * Genera un par de claves ECC (asimétrico) y las guarda en los archivos
     * correspondientes.
     *
     * @throws Exception Si ocurre algún error durante la generación o el
     * guardado de las claves.
     */
    public static void generarYGuardarClaves() throws Exception {
        KeyPair claves = generarParDeClaves();
        guardarClave(claves.getPublic(), RUTA_CLAVE_PUBLICA);
        guardarClave(claves.getPrivate(), RUTA_CLAVE_PRIVADA);
    }

    /**
     * Genera un par de claves ECC utilizando el algoritmo EC y el parámetro
     * "secp256r1".
     *
     * @return Un objeto {@link KeyPair} que contiene la clave pública y la
     * clave privada generadas.
     * @throws NoSuchAlgorithmException Si el algoritmo EC no está disponible.
     * @throws InvalidAlgorithmParameterException Si los parámetros de
     * inicialización son inválidos.
     */
    private static KeyPair generarParDeClaves() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("EC");
        generador.initialize(new ECGenParameterSpec("secp256r1"));
        return generador.generateKeyPair();
    }

    /**
     * Guarda una clave en un archivo especificado.
     *
     * @param clave La clave que se desea guardar.
     * @param ruta La ruta del archivo donde se almacenará la clave.
     * @throws IOException Si ocurre algún error al escribir el archivo.
     */
    private static void guardarClave(Key clave, String ruta) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave.getEncoded());
        }
    }

    /**
     * Carga la clave pública desde el archivo especificado.
     *
     * @return La clave pública cargada.
     * @throws Exception Si ocurre algún error al cargar o procesar el archivo
     * de la clave.
     */
    public static PublicKey cargarClavePublica() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_PUBLICA);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(new X509EncodedKeySpec(claveBytes));
    }

    /**
     * Carga la clave privada desde el archivo especificado.
     *
     * @return La clave privada cargada.
     * @throws Exception Si ocurre algún error al cargar o procesar el archivo
     * de la clave.
     */
    public static PrivateKey cargarClavePrivada() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_PRIVADA);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(claveBytes));
    }

    /**
     * Cifra una cadena de texto utilizando ECIES con la clave pública
     * proporcionada.
     *
     * @param datos La cadena de texto que se desea cifrar.
     * @param clavePublica La clave pública utilizada para cifrar los datos.
     * @return Una cadena en Base64 que representa los datos cifrados.
     * @throws Exception Si ocurre algún error durante el proceso de cifrado.
     */
    public static String cifrarConClavePublica(String datos, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        byte[] datosCifrados = cipher.doFinal(datos.getBytes());
        return Base64.getEncoder().encodeToString(datosCifrados);
    }

    /**
     * Descifra una cadena de texto cifrada utilizando ECIES con la clave
     * privada proporcionada.
     *
     * @param datosCifrados La cadena en Base64 que representa los datos
     * cifrados.
     * @param clavePrivada La clave privada utilizada para descifrar los datos.
     * @return La cadena de texto original descifrada.
     * @throws Exception Si ocurre algún error durante el proceso de descifrado.
     */
    public static String descifrarConClavePrivada(String datosCifrados, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosCifrados);
        byte[] datosDescifrados = cipher.doFinal(datosDecodificados);
        return new String(datosDescifrados);
    }

    /**
     * Genera y guarda una clave simétrica AES.
     *
     * @throws Exception Si ocurre algún error durante la generación o el
     * guardado de la clave.
     */
    public static void generarYGuardarClaveSimetrica() throws Exception {
        KeyGenerator generador = KeyGenerator.getInstance("AES");
        generador.init(256);
        SecretKey claveSimetrica = generador.generateKey();
        guardarClaveSimetrica(claveSimetrica, RUTA_CLAVE_SIMETRICA);
    }

    /**
     * Guarda una clave simétrica en un archivo especificado.
     *
     * @param clave La clave simétrica que se desea guardar.
     * @param ruta La ruta del archivo donde se almacenará la clave.
     * @throws IOException Si ocurre algún error al escribir el archivo.
     */
    private static void guardarClaveSimetrica(SecretKey clave, String ruta) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave.getEncoded());
        }
    }

    /**
     * Carga la clave simétrica desde el archivo especificado.
     *
     * @return La clave simétrica cargada.
     * @throws Exception Si ocurre algún error al cargar o procesar el archivo
     * de la clave.
     */
    public static SecretKey cargarClaveSimetrica() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_SIMETRICA);
        return new SecretKeySpec(claveBytes, "AES");
    }

    /**
     * Cifra una cadena de texto utilizando cifrado simétrico AES.
     *
     * @param datos La cadena de texto que se desea cifrar.
     * @param claveSimetrica La clave simétrica AES utilizada para cifrar los
     * datos.
     * @return Una cadena en Base64 que representa los datos cifrados.
     * @throws Exception Si ocurre algún error durante el proceso de cifrado.
     */
    public static String cifrarConClaveSimetrica(String datos, SecretKey claveSimetrica) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveSimetrica);
        byte[] datosCifrados = cipher.doFinal(datos.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(datosCifrados);
    }

    /**
     * Descifra una cadena de texto cifrada utilizando cifrado simétrico AES.
     *
     * @param datosCifrados La cadena en Base64 que representa los datos
     * cifrados.
     * @param claveSimetrica La clave simétrica AES utilizada para descifrar los
     * datos.
     * @return La cadena de texto original descifrada.
     * @throws Exception Si ocurre algún error durante el proceso de descifrado.
     */
    public static String descifrarConClaveSimetrica(String datosCifrados, SecretKey claveSimetrica) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, claveSimetrica);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosCifrados);
        byte[] datosDescifrados = cipher.doFinal(datosDecodificados);
        return new String(datosDescifrados, "UTF-8");
    }

    /**
     * Lee un archivo desde el classpath y retorna su contenido como un arreglo
     * de bytes.
     *
     * @param nombreArchivo El nombre del archivo a leer.
     * @return Un arreglo de bytes que contiene el contenido del archivo.
     * @throws IOException Si ocurre algún error durante la lectura del archivo.
     */
    private static byte[] leerArchivo(String nombreArchivo) throws IOException {
        try (InputStream is = UtilidadesCifrado.class.getResourceAsStream(nombreArchivo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if (is == null) {
                throw new FileNotFoundException("No se pudo encontrar el archivo: " + nombreArchivo);
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    /**
     * Hashea una contraseña utilizando el algoritmo SHA-256.
     *
     * @param contraseña La contraseña que se desea hashear.
     * @return Una cadena hexadecimal que representa el hash de la contraseña.
     * @throws NoSuchAlgorithmException Si el algoritmo SHA-256 no está
     * disponible.
     */
    public static String hashearContraseña(String contraseña) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(contraseña.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
