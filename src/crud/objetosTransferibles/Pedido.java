/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.objetosTransferibles;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Urko
 */
@XmlRootElement
public class Pedido implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date fechaPedido;

    private Estado estado;

    private double total;

    private String cifCliente;

    private Cliente cliente;

    // Relación uno a muchos con PedidoArticulo
    private Set<PedidoArticulo> pedidoArticulos = new HashSet<>();

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCifCliente() {
        return cifCliente;
    }

    public void setCifCliente(String cifCliente) {
        this.cifCliente = cifCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Long getUsuarioId() {
        return cliente != null ? cliente.getId() : null; // Devuelve el id del cliente si el cliente no es null
    }

    public void setUsuarioId(Long id) {
        this.cliente.setId(id);
    }

    @XmlTransient
    public Set<PedidoArticulo> getPedidoArticulos() {
        return pedidoArticulos;
    }

    public void setPedidoArticulos(Set<PedidoArticulo> pedidoArticulos) {
        this.pedidoArticulos = pedidoArticulos;
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
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public Pedido clone() {
        Pedido copia = new Pedido();
        copia.setId(this.getId());
        copia.setCliente(this.getCliente());
        copia.setCifCliente(this.getCifCliente());
        copia.setEstado(this.getEstado());
        copia.setFechaPedido(new Date(this.getFechaPedido().getTime())); // Copiar fecha
        copia.setTotal(this.getTotal());
        return copia;
    }

    @Override
    public String toString() {
        return "crud.entidades.Pedido[ id=" + id + " ]";
    }

}
