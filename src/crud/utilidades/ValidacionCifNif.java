package crud.utilidades;

/**
 *
 * @author urkop
 */
import java.util.regex.*;

public class ValidacionCifNif {

    private static final String PATRON_CIF = "^[ABCDEFGHJNPQRSUVW]\\d{7}[0-9A-J]$";
    private static final String PATRON_NIF = "^\\d{8}[A-HJ-NP-TV-Z]$";
    private static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";

    public static boolean validarCifONif(String valor) {
        if (valor == null || valor.isEmpty()) {
            return false;
        }

        // Eliminar espacios o caracteres extraños
        valor = valor.trim().toUpperCase();

        // Validar como CIF
        if (valor.matches(PATRON_CIF)) {
            return validarCif(valor);
        }

        // Validar como NIF
        if (valor.matches(PATRON_NIF)) {
            return validarNif(valor);
        }

        // Si no coincide con ninguno
        return false;
    }

    private static boolean validarCif(String cif) {
        int sumaPares = 0;
        int sumaImpares = 0;

        for (int i = 1; i < 8; i++) {
            int digito = Character.getNumericValue(cif.charAt(i));

            if (i % 2 == 0) { // Posiciones pares
                sumaPares += digito;
            } else { // Posiciones impares
                int doble = digito * 2;
                sumaImpares += (doble / 10) + (doble % 10);
            }
        }

        int sumaTotal = sumaPares + sumaImpares;
        int digitoControl = 10 - (sumaTotal % 10);
        if (digitoControl == 10) {
            digitoControl = 0;
        }

        char ultimoCaracter = cif.charAt(8);

        if (Character.isDigit(ultimoCaracter)) {
            // El último carácter debe coincidir con el dígito de control
            return digitoControl == Character.getNumericValue(ultimoCaracter);
        } else {
            // El último carácter debe ser una letra de control
            return "JABCDEFGHI".charAt(digitoControl) == ultimoCaracter;
        }
    }

    private static boolean validarNif(String nif) {
        int numero = Integer.parseInt(nif.substring(0, 8));
        char letraCalculada = LETRAS_NIF.charAt(numero % 23);
        char letraProporcionada = nif.charAt(8);
        return letraCalculada == letraProporcionada;
    }

}
