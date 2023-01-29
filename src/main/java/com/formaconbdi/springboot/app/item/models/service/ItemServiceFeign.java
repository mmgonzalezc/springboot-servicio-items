package com.formaconbdi.springboot.app.item.models.service;

import com.formacionbdi.springboot.app.commons.model.entity.Producto;
import com.formaconbdi.springboot.app.item.clientes.ProductoClienteRest;
import com.formaconbdi.springboot.app.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeign")
/***
 * Notacion para indicar la prioridad de la implementacion por defecto que tiene que inyectar
 * Si se usa la implementacion restTemplate debemos quitar la anotacion primary
 */
@Primary
public class ItemServiceFeign implements ItemService {

    /***
     * Automaticamente al estar anotada con FeignClient es manejable por spring, por lo que se puede inyectar
     */
    @Autowired
    private ProductoClienteRest clienteFeign;

    @Override
    public List<Item> findAll() {
        return clienteFeign.listar().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(clienteFeign.detalle(id), cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return clienteFeign.crear(producto);
    }

    @Override
    public Producto update(Producto producto, Long id) {
       return clienteFeign.update(producto,id);
    }

    @Override
    public void delete(Long id) {
    clienteFeign.eliminar(id);
    }
}
