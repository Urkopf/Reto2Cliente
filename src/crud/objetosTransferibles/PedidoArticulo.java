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
public class PedidoArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private Long pedidoId;

    private Long articuloId;

    private Pedido pedido;

    private Articulo articulo;

    private int cantidad;

    private double precioCompra;

    public PedidoArticulo() {
    }

    public PedidoArticulo(Long id, Long pedidoId, Long articuloId, Pedido pedido, Articulo articulo, int cantidad, double precioCompra) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.articuloId = articuloId;
        this.pedido = pedido;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;

    }

    public Long getId() {
        return id;
    }

    public void setPedidoArticuloId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(Long articuloId) {
        this.articuloId = articuloId;
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
    public String toString() {
        return "crud.entidades.PedidoArticulo[ id=" + id + " ]";
    }

}
