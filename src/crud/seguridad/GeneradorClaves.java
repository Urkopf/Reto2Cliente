package crud.seguridad;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class GeneradorClaves {

    public static void main(String[] args) throws Exception {
        // Generar un par de claves RSA
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048); // Longitud recomendada: 2048 bits
        KeyPair parDeClaves = generador.generateKeyPair();

        // Obtener las claves
        PrivateKey clavePrivada = parDeClaves.getPrivate();
        PublicKey clavePublica = parDeClaves.getPublic();

        // Convertir las claves a Base64 para guardarlas como texto
        String clavePrivadaBase64 = Base64.getEncoder().encodeToString(clavePrivada.getEncoded());
        String clavePublicaBase64 = Base64.getEncoder().encodeToString(clavePublica.getEncoded());

        // Mostrar las claves
        System.out.println("Clave Privada:");
        System.out.println(clavePrivadaBase64);

        System.out.println("\nClave Pública:");
        System.out.println(clavePublicaBase64);

        // Guardar en archivos
        // Guardar clave pública
        Files.write(Paths.get("clave_publica.pem"), clavePublicaBase64.getBytes());
        // Guardar clave privada
        Files.write(Paths.get("clave_privada.pem"), clavePrivadaBase64.getBytes());
    }
}
