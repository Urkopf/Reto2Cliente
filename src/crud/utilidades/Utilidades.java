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
 *
 * @author 2dam
 */
public class Utilidades {

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
