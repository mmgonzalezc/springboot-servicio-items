package com.formaconbdi.springboot.app.item.models.service;

import com.formaconbdi.springboot.app.item.models.Item;
import com.formaconbdi.springboot.app.item.models.Producto;

import java.util.List;

public interface ItemService {

    public List<Item> findAll();

    public Item findById(Long id, Integer cantidad);

    Producto save(Producto producto);
    Producto update(Producto producto, Long id);
    void delete(Long id);

}
