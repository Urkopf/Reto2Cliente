/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.objetosTransferibles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Urko
 */
@XmlRootElement
public class Articulo implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String nombre;

    private double precio;

    private String descripcion;

    private int stock;

    private Date fechaReposicion;

    private Set<PedidoArticulo> pedidoArticulos = new HashSet<>();

    private List<Almacen> almacenTrump = new ArrayList<>();

    public Articulo() {

    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getFechaReposicion() {
        return fechaReposicion;
    }

    public void setFechaReposicion(Date fechaReposicion) {
        this.fechaReposicion = fechaReposicion;
    }

    @XmlTransient
    public Set<PedidoArticulo> getPedidoArticulos() {
        return pedidoArticulos;
    }

    public void setPedidoArticulos(Set<PedidoArticulo> pedidoArticulos) {
        this.pedidoArticulos = pedidoArticulos;
    }

    private Set<Almacen> almacenes = new HashSet<>();

    @XmlTransient
    public Set<Almacen> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(Set<Almacen> almacenes) {
        this.almacenes = almacenes;
    }

    public List<Almacen> getAlmacenTrump() {
        return almacenTrump;
    }

    public void setAlmacenTrump(List<Almacen> almacenTrump) {
        this.almacenTrump = almacenTrump;
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
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "crud.entidades.Articulo[ id=" + id + " ]";
    }

    @Override
    public Articulo clone() {
        Articulo copia = new Articulo();
        copia.setId(this.getId());
        copia.setNombre(this.getNombre());
        copia.setPrecio(this.getPrecio());
        copia.setFechaReposicion(this.getFechaReposicion());
        copia.setDescripcion(this.getDescripcion());
        copia.setStock(this.getStock());
        copia.setAlmacenes(this.getAlmacenes());
        return copia;
    }

}
