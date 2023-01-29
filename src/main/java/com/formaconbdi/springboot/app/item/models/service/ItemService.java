package com.formaconbdi.springboot.app.item.models.service;

import com.formacionbdi.springboot.app.commons.model.entity.Producto;
import com.formaconbdi.springboot.app.item.models.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id, Integer cantidad);

    Producto save(Producto producto);
    Producto update(Producto producto, Long id);
    void delete(Long id);

}
