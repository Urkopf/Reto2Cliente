/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.negocio;

/**
 *
 * @author 2dam
 */
public class FactoriaUsuarios {

    public static final String CLIENTE = "cliente";
    public static final String TRABAJADOR = "trabajador";

    private static FactoriaUsuarios instance;

    private FactoriaUsuarios() {
    }

    public static FactoriaUsuarios getInstance() {
        if (instance == null) {
            instance = new FactoriaUsuarios();
        }

        return instance;
    }

    public static <T> IUsuario<T> getUsuarioService(String tipo) {
        switch (tipo.toUpperCase()) {
            case CLIENTE:
                return new UsuarioImpl<>(tipo.toLowerCase());
            case TRABAJADOR:
                return new UsuarioImpl<>(tipo.toLowerCase());
            default:
                throw new IllegalArgumentException("Tipo de usuario no valido: " + tipo);
        }
    }

}
