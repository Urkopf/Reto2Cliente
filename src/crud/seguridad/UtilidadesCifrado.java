package crud.seguridad;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UtilidadesCifrado {

    // Generar una clave simétrica AES
    public static SecretKey generarClaveSimetrica() throws Exception {
        KeyGenerator generadorClave = KeyGenerator.getInstance("AES");
        generadorClave.init(128);
        return generadorClave.generateKey();
    }

    // Cifrar datos con AES
    public static byte[] cifrarConAES(String datos, SecretKey claveSimetrica, byte[] iv) throws Exception {
        Cipher cifrador = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec parametrosIV = new IvParameterSpec(iv);
        cifrador.init(Cipher.ENCRYPT_MODE, claveSimetrica, parametrosIV);
        return cifrador.doFinal(datos.getBytes());
    }

    // Descifrar datos con AES
    public static String descifrarConAES(byte[] datosCifrados, SecretKey claveSimetrica, byte[] iv) throws Exception {
        Cipher cifrador = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec parametrosIV = new IvParameterSpec(iv);
        cifrador.init(Cipher.DECRYPT_MODE, claveSimetrica, parametrosIV);
        byte[] datosDescifrados = cifrador.doFinal(datosCifrados);
        return new String(datosDescifrados);
    }

    // Cifrar clave simétrica con RSA (clave pública)
    public static byte[] cifrarClaveSimetricaConRSA(SecretKey claveSimetrica, PublicKey clavePublica) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cifrador.doFinal(claveSimetrica.getEncoded());
    }

    // Descifrar clave simétrica con RSA (clave privada)
    public static SecretKey descifrarClaveSimetricaConRSA(byte[] claveCifrada, PrivateKey clavePrivada) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA");
        cifrador.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] claveDescifrada = cifrador.doFinal(claveCifrada);
        return new SecretKeySpec(claveDescifrada, "AES");
    }

    // Convertir una clave pública desde Base64
    public static PublicKey obtenerClavePublicaDesdeBase64(String claveBase64) throws Exception {
        byte[] bytesClave = Base64.getDecoder().decode(claveBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytesClave);
        KeyFactory fabricaClaves = KeyFactory.getInstance("RSA");
        return fabricaClaves.generatePublic(spec);
    }

    // Convertir una clave privada desde Base64
    public static PrivateKey obtenerClavePrivadaDesdeBase64(String claveBase64) throws Exception {
        byte[] bytesClave = Base64.getDecoder().decode(claveBase64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytesClave);
        KeyFactory fabricaClaves = KeyFactory.getInstance("RSA");
        return fabricaClaves.generatePrivate(spec);
    }

    // Generar un vector de inicialización (IV) para AES
    public static byte[] generarVectorInicializacion() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Hashear una contraseña usando PBKDF2
    public static String hashearContrasena(String contrasena, byte[] salt) throws Exception {
        int iteraciones = 65536;
        int longitudClave = 128;

        PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, iteraciones, longitudClave);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    // Generar un salt aleatorio
    public static byte[] generarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Verificar una contraseña contra un hash
    public static boolean verificarContrasena(String contrasena, String hash, byte[] salt) throws Exception {
        String hashGenerado = hashearContrasena(contrasena, salt);
        return hashGenerado.equals(hash);
    }

    // Generar una contraseña temporal
    public static String generarContrasenaTemporal() {
        SecureRandom random = new SecureRandom();
        int longitud = 25;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder contrasena = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            contrasena.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return contrasena.toString();
    }

    public static PublicKey cargarClavePublicaDesdePEM(String rutaRecurso) throws Exception {
        // Obtener el recurso como InputStream
        InputStream inputStream = UtilidadesCifrado.class.getClassLoader().getResourceAsStream(rutaRecurso);
        if (inputStream == null) {
            throw new FileNotFoundException("No se pudo encontrar el recurso: " + rutaRecurso);
        }

        // Leer el contenido completo del archivo
        byte[] bytesClave;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }
            // Decodificar el contenido Base64
            bytesClave = Base64.getDecoder().decode(contenido.toString());
        }

        // Convertir a PublicKey usando X509EncodedKeySpec
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytesClave);
        return keyFactory.generatePublic(spec);
    }

    // Cifrar datos con RSA usando la clave pública
    public static byte[] cifrarConRSA(String datos, PublicKey clavePublica) throws Exception {
        Cipher cifrador = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cifrador.doFinal(datos.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

}
