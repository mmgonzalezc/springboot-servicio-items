package com.formaconbdi.springboot.app.item.Controllers;

import com.formaconbdi.springboot.app.item.models.Item;
import com.formaconbdi.springboot.app.item.models.Producto;
import com.formaconbdi.springboot.app.item.models.service.ItemService;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/***
 * RefreshScope Anotacion que nos permite actualizar en tiempo real mediante una url de spring actuator los
 * componentes, controlodores, clases anotados con component, service , controllers
 * que le estemos enyectando con @Value.
 */
@RefreshScope
@RestController
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private Environment env;
    /***
     * Alternativa al uso de anotaciones para corto circuito
     */
    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
/***
 * Implementacion con feignClient
 */
    @Qualifier("serviceFeign")

/***
 * Implementacion con restTemplate
 *@Qualifier("serviceRestTemplate")
 */
    private ItemService itemService;

    @Value("${configuracion.texto}")
    private String texto;


    @GetMapping("/listar")
// Recuperacion de parametros inyectados desde microservicio gateway
//@RequestParam
// @RequestHeader
    public List<Item> listar(@RequestParam(name = "nombre", required = false) String nombre, @RequestHeader(name = "token-request", required = false) String token) {
        System.out.println(nombre);
        System.out.println(token);
        return itemService.findAll();
    }

    /***
     * HystrixCommand notacion para tolerancia a fallo realiza corto circuito evitado errores en cascada de microservicios
     *
     * "Se comenta por que estaremos usando resilience4j"
     * */
//@HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("ver/{id}/cantidad/{cantidad}")
    public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
        // Creacion de nuevo corto circuito items
        return cbFactory.create("items")
                .run(() -> itemService.findById(id, cantidad), e -> metodoAlternativo(id, cantidad, e));
    }

    /***
     * Resilience4j con anotacion de CircuitBreaker
     * @param id
     * @param cantidad
     * @return
     */
    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    @GetMapping("ver2/{id}/cantidad/{cantidad}")
    public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
        return itemService.findById(id, cantidad);
    }


    /***
     * Resilience4j con anotacion TimeLimiter
     * Particulirad de usar TimeLimiter, llamada al futuro se tiene que envolver la llamada a una clase especial CompletableFuture que maneja el time out
     *
     * Si solo se usa la notacion TimeLimiter solo controlara los time out
     * Pero si se combinan las anotaciones CircuitBreaker  y TimeLimiter manejaria los time out y corto circuito
     *
     * @param id
     * @param cantidad
     * @return
     */

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
    @TimeLimiter(name = "items"/*, fallbackMethod = "metodoAlternativo2"*/)
    @GetMapping("ver3/{id}/cantidad/{cantidad}")
    public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
        // Envuelve la llamada en una representacion futura asincrona para calcular el tiempo de espera
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
    }

    /***
     * Se llama mediante fallbackMethod cuando el micro servicio falle || tambien por resilience4j en el corto circuito items cuando el 50 % de las peticiones fallem
     * @param id
     * @param cantidad
     * @return
     */
    public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
        logger.info(e.getMessage());
        Item item = new Item();
        item.setCantidad(cantidad);
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Camara Sony");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return item;
    }

    /***
     * Metodo alternativo fallbackMethod para la llamada de representacion futura
     * @param id
     * @param cantidad
     * @param e
     * @return
     */
    public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e) {
        logger.info(e.getMessage());
        Item item = new Item();
        item.setCantidad(cantidad);
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Camara Sony");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return CompletableFuture.supplyAsync(() -> item);

    }


    @GetMapping("obtener-config")
    public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto) {
        logger.info(texto);
        Map<String, String> json = new HashMap<>();
        json.put("texto", texto);
        json.put("puerto", puerto);
        if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
            json.put("autor.nombre", env.getProperty("configuracion.autor.name"));
            json.put("autor.email", env.getProperty("configuracion.autor.email"));
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return itemService.save(producto);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
        return itemService.update(producto,id);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        itemService.delete(id);
    }


}
