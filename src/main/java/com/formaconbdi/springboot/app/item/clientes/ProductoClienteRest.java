package com.formaconbdi.springboot.app.item.clientes;

import com.formaconbdi.springboot.app.item.models.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FeignClient
 * Define la interfaz como un cliente
 */
@FeignClient(name = "servicio-productos")
public interface ProductoClienteRest {
    /***
     * GetMapping
     * En el controlador usamos la anotacion para mapear la ruta
     * Mientras en feignClient Indica la ruta del endpoint para consumir el ApiRest
     * @return
     */
    @GetMapping("/listar")
    public List<Producto> listar();


    @GetMapping("/ver/{id}")
    public Producto detalle(@PathVariable Long id);

    @PostMapping("/crear")
    Producto crear(@RequestBody Producto producto);

    @PutMapping("/editar/{id}")
    Producto update(@RequestBody Producto producto, @PathVariable Long id);

    @DeleteMapping("/eliminar/{id}")
    void eliminar(@PathVariable Long id);



}
