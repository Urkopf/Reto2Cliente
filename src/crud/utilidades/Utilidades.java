/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.utilidades;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Clase de utilidades para la serialización de objetos a XML.
 * <p>
 * Esta clase proporciona métodos para convertir objetos en su representación en
 * formato XML utilizando JAXB.
 * </p>
 *
 * @author Urko
 */
public class Utilidades {

    /**
     * Serializa un objeto a una cadena en formato XML.
     * <p>
     * Este método utiliza JAXB para convertir el objeto proporcionado en una
     * cadena que contiene su representación en XML.
     * </p>
     *
     * @param objeto El objeto a serializar.
     * @return Una cadena en formato XML que representa el objeto, o
     * {@code null} si ocurre un error.
     */
    public static String serializaAXML(Object objeto) {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Object.class);
            Marshaller marshaller = contexto.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(objeto, writer);
            return writer.toString();
        } catch (JAXBException e) {
            return null;
        }
    }

}
