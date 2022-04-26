package com.formaconbdi.springboot.app.item.models;

public class Item {

    private Producto producto;
    private Integer cantidad;

    public Item() {
    }

    public Item(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public Item setProducto(Producto producto) {
        this.producto = producto;
        return this;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Item setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public Double getTotal() {
        return producto.getPrecio() * cantidad.doubleValue();
    }
}
