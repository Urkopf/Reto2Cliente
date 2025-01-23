package crud.seguridad;

import java.io.ByteArrayOutputStream;
import javax.crypto.Cipher;
import java.security.*;

import java.util.Base64;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class UtilidadesCifrado {

    private static final String RUTA_CLAVE_PUBLICA = "clave_publica.key";
    private static final String RUTA_CLAVE_PRIVADA = "clave_privada.key";
    private static final String RUTA_CLAVE_SIMETRICA = "clave_simetrica.key";

    // Generar y guardar clave simétrica (AES)
    public static void generarYGuardarClaveSimetrica() throws Exception {
        KeyGenerator generador = KeyGenerator.getInstance("AES");
        generador.init(256); // Usa 128, 192 o 256 bits según sea compatible con tu entorno
        SecretKey claveSimetrica = generador.generateKey();
        guardarClaveSimetrica(claveSimetrica, RUTA_CLAVE_SIMETRICA);
    }

    // Guardar clave simétrica en archivo
    private static void guardarClaveSimetrica(SecretKey clave, String ruta) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave.getEncoded());
        }
    }

    // Cargar clave simétrica desde archivo
    public static SecretKey cargarClaveSimetrica() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_SIMETRICA);
        return new SecretKeySpec(claveBytes, "AES");
    }

    // Cifrar datos con clave simétrica (AES)
    public static String cifrarConClaveSimetrica(String datos, SecretKey claveSimetrica) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveSimetrica);
        byte[] datosCifrados = cipher.doFinal(datos.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(datosCifrados);
    }

    // Descifrar datos con clave simétrica (AES)
    public static String descifrarConClaveSimetrica(String datosCifrados, SecretKey claveSimetrica) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, claveSimetrica);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosCifrados);
        byte[] datosDescifrados = cipher.doFinal(datosDecodificados);
        return new String(datosDescifrados, "UTF-8");
    }

    // Generar y guardar claves en archivos
    public static void generarYGuardarClaves() throws Exception {
        KeyPair claves = generarParDeClaves();
        guardarClave(claves.getPublic(), RUTA_CLAVE_PUBLICA);
        guardarClave(claves.getPrivate(), RUTA_CLAVE_PRIVADA);
    }

    // Generar par de claves públicas y privadas
    public static KeyPair generarParDeClaves() throws NoSuchAlgorithmException {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        return generador.generateKeyPair();
    }

    // Guardar clave en archivo
    private static void guardarClave(Key clave, String ruta) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            fos.write(clave.getEncoded());
        }
    }

    // Cargar clave pública desde archivo
    public static PublicKey cargarClavePublica() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_PUBLICA);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(claveBytes));
    }

    // Cargar clave privada desde archivo
    public static PrivateKey cargarClavePrivada() throws Exception {
        byte[] claveBytes = leerArchivo(RUTA_CLAVE_PRIVADA);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(claveBytes));
    }

    // Leer archivo
    private static byte[] leerArchivo(String ruta) throws IOException {
        try (FileInputStream fis = new FileInputStream(ruta);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    // Encriptar datos con clave pública
    public static String encriptarConClavePublica(String datos, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        byte[] datosEncriptados = cipher.doFinal(datos.getBytes());
        return Base64.getEncoder().encodeToString(datosEncriptados);
    }

    // Desencriptar datos con clave privada
    public static String desencriptarConClavePrivada(String datosEncriptados, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(datosDecodificados);
        return new String(datosDesencriptados);
    }

    /**
     * Encripta datos usando la clave privada (RSA).
     *
     * @param datos Texto en claro a encriptar.
     * @param clavePrivada Clave privada RSA.
     * @return El texto encriptado, codificado en Base64.
     */
    public static String encriptarConClavePrivada(String datos, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePrivada);
        byte[] datosEncriptados = cipher.doFinal(datos.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(datosEncriptados);
    }

    /**
     * Desencripta datos usando la clave pública (RSA).
     *
     * @param datosEncriptados Texto encriptado (en Base64).
     * @param clavePublica Clave pública RSA.
     * @return El texto original desencriptado.
     */
    public static String desencriptarConClavePublica(String datosEncriptados, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePublica);
        byte[] datosDecodificados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(datosDecodificados);
        return new String(datosDesencriptados, "UTF-8");
    }

    // Hashear datos (contraseña)
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
