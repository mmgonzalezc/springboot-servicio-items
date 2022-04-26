package com.formaconbdi.springboot.app.item.models.service;

import com.formaconbdi.springboot.app.item.models.Item;
import com.formaconbdi.springboot.app.item.models.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("serviceRestTemplate")
public class ItemServiceImp implements ItemService {

    @Autowired
    private RestTemplate clienteRest;

    @Override
    public List<Item> findAll() {
        List<Producto> productos = Arrays.asList(clienteRest.getForObject("http://servicio-productos/listar", Producto[].class));
        return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        Producto producto = clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class, pathVariables);
        return new Item(producto, cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        HttpEntity<Producto> body = new HttpEntity<>(producto);
        // Exchenge del cliente RestTemplate -> Para intercambiamos de datos
        ResponseEntity<Producto> productoResponseEntity = clienteRest.exchange("http://servicio-productos/crear", HttpMethod.POST, body, Producto.class);
        Producto productoResponse = productoResponseEntity.getBody();
        return productoResponse;
    }

    @Override
    public Producto update(Producto producto, Long id) {
        /***
         * Se escribe el producto dentro del cuerpo de de la peticion
         */
        HttpEntity<Producto> body = new HttpEntity<>(producto);
        /***
         * Se necesita un mapa para pasar el id por el path variable
         */
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        ResponseEntity<Producto> productoResponseEntity = clienteRest.exchange("http://servicio-productos/editar/{id}", HttpMethod.PUT, body, Producto.class,pathVariables);
        return productoResponseEntity.getBody();
    }

    @Override
    public void delete(Long id) {
        /***
         * Se necesita un mapa para pasar el id por el path variable
         */
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", id.toString());
        /***
         * Por de bajo el metodo delete ya tiene implementado el tipo de HttpMethod,
         * Ya no es necesario indicar que es de tipo DELETE
         */
        clienteRest.delete("http://servicio-productos/editar/{id}",pathVariables);
    }
}
