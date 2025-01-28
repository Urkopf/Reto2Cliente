/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.objetosTransferibles;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Urko
 */
@XmlRootElement
public class Almacen implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String direccion;

    private String poblacion;

    private String provincia;

    private String pais;

    private double espacio;

    //Solo se usa para ayudar en la relacion Articulo,Almacen
    private Long articuloId;

    public Almacen() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public double getEspacio() {
        return espacio;
    }

    public void setEspacio(double espacio) {
        this.espacio = espacio;
    }

    public Set<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(Set<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(Long articuloId) {
        this.articuloId = articuloId;
    }

    private Set<Articulo> articulos = new HashSet<>();

    @Override
    public Almacen clone() {
        Almacen copia = new Almacen();
        copia.setId(this.id);
        copia.setDireccion(this.direccion);
        copia.setEspacio(this.espacio);
        copia.setPais(this.pais);
        copia.setPoblacion(this.poblacion);
        copia.setProvincia(this.provincia);
        return copia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crud.entidades.Almacen[ id=" + id + " ]";
    }

}
