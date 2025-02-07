package crud.utilidades;

/**
 * Clase que proporciona métodos para la validación de CIF y NIF.
 * <p>
 * Esta clase contiene métodos estáticos que permiten validar cadenas de texto
 * que representan CIF o NIF, de acuerdo con los formatos y algoritmos de
 * control establecidos.
 * </p>
 *
 * @author Urko
 */
public class ValidacionCifNif {

    /**
     * Patrón regular para validar el formato de un CIF.
     */
    private static final String PATRON_CIF = "^[ABCDEFGHJNPQRSUVW]\\d{7}[0-9A-J]$";

    /**
     * Patrón regular para validar el formato de un NIF.
     */
    private static final String PATRON_NIF = "^\\d{8}[A-HJ-NP-TV-Z]$";

    /**
     * Cadena de letras utilizadas para calcular la letra de control de un NIF.
     */
    private static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";

    /**
     * Valida una cadena de texto como CIF o NIF.
     * <p>
     * Este método comprueba si la cadena proporcionada se ajusta al formato de
     * un CIF o un NIF y realiza la validación del dígito o letra de control
     * correspondiente.
     * </p>
     *
     * @param valor La cadena que se desea validar como CIF o NIF.
     * @return {@code true} si el valor es un CIF o NIF válido; {@code false} en
     * caso contrario.
     */
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

    /**
     * Valida un CIF.
     * <p>
     * Este método realiza la validación de un CIF comprobando el dígito o letra
     * de control mediante la suma de los dígitos en posiciones pares e impares.
     * </p>
     *
     * @param cif La cadena que representa el CIF a validar.
     * @return {@code true} si el CIF es válido; {@code false} en caso
     * contrario.
     */
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

    /**
     * Valida un NIF.
     * <p>
     * Este método calcula la letra de control para el NIF a partir de los 8
     * dígitos numéricos y la compara con la letra proporcionada.
     * </p>
     *
     * @param nif La cadena que representa el NIF a validar.
     * @return {@code true} si el NIF es válido; {@code false} en caso
     * contrario.
     */
    private static boolean validarNif(String nif) {
        int numero = Integer.parseInt(nif.substring(0, 8));
        char letraCalculada = LETRAS_NIF.charAt(numero % 23);
        char letraProporcionada = nif.charAt(8);
        return letraCalculada == letraProporcionada;
    }

}
