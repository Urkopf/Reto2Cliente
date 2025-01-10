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

    public static <T> IUsuario<T> getUsuarioService(String tipo) {
        switch (tipo.toUpperCase()) {
            case CLIENTE:
                return new UsuarioImpl<>();
            case TRABAJADOR:
                return new UsuarioImpl<>();
            default:
                throw new IllegalArgumentException("Tipo de usuario no valido: " + tipo);
        }
    }

}
