package com.formaconbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/***
 * Habilita la tolerancia fallo de los microservicios
 * "Se comenta x usar Recilence4j "
 */
//@EnableCircuitBreaker

/***
 * Actuará como un cliente de descubrimiento de spring y se registrará en el servidor eureka
 */
@EnableEurekaClient
/***
 * Balanceo de carga con ribbon
 * Selecciona la mejor instancia disponible
 *
 * @RibbonClient(name = "servicio-productos")
 */

/***
 * Habilita clientes @EnableFeignClients
 */
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootServicioItemApplication.class, args);
    }

}
