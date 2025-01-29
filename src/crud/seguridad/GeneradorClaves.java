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

public class GeneradorClaves {

    public static void main(String[] args) {
        try {
            // Generar y guardar claves en archivos
            generarYGuardarClaves();
            /*
            // Cargar claves desde archivos
            PublicKey clavePublica = cargarClavePublica();
            PrivateKey clavePrivada = cargarClavePrivada();

            // Contraseña del cliente
            String contraseña = "MiSuperContraseña123";
            System.out.println("Contraseña original: " + contraseña);

            // Cliente encripta la contraseña
            String contraseñaEncriptada = encriptarConClavePublica(contraseña, clavePublica);
            System.out.println("Contraseña encriptada: " + contraseñaEncriptada);

            // Servidor desencripta la contraseña
            String contraseñaDesencriptada = desencriptarConClavePrivada(contraseñaEncriptada, clavePrivada);
            System.out.println("Contraseña desencriptada: " + contraseñaDesencriptada);
             */
            // Servidor hashea la contraseña antes de almacenarla
            // Generar y guardar clave simétrica
            /*generarYGuardarClaveSimetrica();

            // Cargar clave simétrica
            SecretKey claveSimetrica = cargarClaveSimetrica();

            // Cifrar y descifrar datos
            String textoCifrado = cifrarConClaveSimetrica("apikey", claveSimetrica);
            System.out.println("Usuario cifrado: " + textoCifrado);
            String textoCifradoPass = cifrarConClaveSimetrica("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", claveSimetrica);
            System.out.println("Pass cifrado: " + textoCifradoPass);

            String contraseñaHasheada = hashearContraseña("trabajadoreficiente2025");
            System.out.println("Contraseña hasheada: " + contraseñaHasheada);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
