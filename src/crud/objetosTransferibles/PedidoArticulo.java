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
public class PedidoArticulo implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private Pedido pedido;

    private Articulo articulo;

    private int cantidad;

    private double precioCompra;

    public PedidoArticulo() {
    }

    public PedidoArticulo(Long id, Pedido pedido, Articulo articulo, int cantidad, double precioCompra) {
        this.id = id;
        this.pedido = pedido;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedido.getId();
    }

    public void setPedidoId(Long pedidoId) {
        this.pedido.setId(pedidoId);
    }

    public Long getArticuloId() {
        return articulo.getId();
    }

    public void setArticuloId(Long articuloId) {
        this.articulo.setId(articuloId);
    }

    public String getArticuloNombre() {
        return articulo.getNombre();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
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
        if (!(object instanceof PedidoArticulo)) {
            return false;
        }
        PedidoArticulo other = (PedidoArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public PedidoArticulo clone() {
        PedidoArticulo copia = new PedidoArticulo();
        copia.setId(this.id);
        copia.setPedido(this.pedido);
        copia.setArticulo(this.articulo);
        copia.setCantidad(this.cantidad);
        copia.setPrecioCompra(this.precioCompra);
        return copia;
    }

    @Override
    public String toString() {
        return "crud.entidades.PedidoArticulo[ id=" + id + " ]";
    }

}
