package com.formaconbdi.springboot.app.item.models;

import java.util.Date;

public class Producto {

    private Long id;
    private String nombre;
    private Double precio;
    private Date createAt;
    private Integer port;

    public Long getId() {
        return id;
    }

    public Producto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Producto setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Double getPrecio() {
        return precio;
    }

    public Producto setPrecio(Double precio) {
        this.precio = precio;
        return this;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Producto setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Producto setPort(Integer port) {
        this.port = port;
        return this;
    }
}
