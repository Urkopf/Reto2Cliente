/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.objetosTransferibles;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Urko
 */
@XmlRootElement
public class Trabajador extends Usuario {

    private Departamento departamento;

    private Categoria categoria;

    public Trabajador() {
    }

    public Trabajador(Departamento departamento, Categoria categoria, String correo, String contrasena, String nombre, String calle, String ciudad, String codPostal, String cif, Boolean activo) {
        super(correo, contrasena, nombre, calle, ciudad, codPostal, cif, activo);
        this.departamento = departamento;
        this.categoria = categoria;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajador)) {
            return false;
        }
        Trabajador other = (Trabajador) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crud.entidades.Trabajador[ id=" + getId() + " ]";
    }

}
