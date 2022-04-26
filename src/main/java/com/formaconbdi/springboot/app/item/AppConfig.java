package com.formaconbdi.springboot.app.item;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/***
 * Clase de configuracion que nos permite crar objetos componentes de spring Bean
 */

@Configuration
public class AppConfig {
    /***
     * El objeto que se retorna se guarda en el contenedor con la anotacion @Bean
     * @return
     */
    @Bean("clienteRest")
    /***
     * Balanceo de carga con ribbon usando restTemplate
     */
    @LoadBalanced
    public RestTemplate registrarRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    /***
     * "NOTA -> Esta configuracion no funcionara para cuando se use anotacion @CircuitBreaker,
     * solo funciona para CircuitBreakerFactory"
     * configureDefault configuracion por defecto de Resilience4j
     * id -> aplica para el nombre del corto circuito que se tenga configurado en la applicacion
     * circuitBreakerConfig metodo para configuracion del corto circuito
     *
     */
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCuztomizer (){
        return factory -> factory.configureDefault(id ->{
            return new Resilience4JConfigBuilder(id)
                    // para persolalizar la configuracion del corto circuito se debe usar el metodo custom()
                    .circuitBreakerConfig(CircuitBreakerConfig.custom()
                            // tamaño de pantalla deslizante por defecto es 100 request
                            .slidingWindowSize(10)
                            // taza de falla por defecto es 50 %
                            .failureRateThreshold(50)
                            // Tiempo de espera en estado abierto del corto circuito, por defecto es 60 segundos
                            .waitDurationInOpenState(Duration.ofSeconds(10L))
                            // Numero de llamadas permitidas en estado abierto por defecto son 10
                            .permittedNumberOfCallsInHalfOpenState(5)
                            // Configuracion de porcentaje de umbral de llamadas lentas por defecto es 100%
                            .slowCallRateThreshold(50)
                            // Configuracion de llamada lenta, tiempo maximo de espera de peticion
                            .slowCallDurationThreshold(Duration.ofSeconds(2L))
                            // construye la nueva configuracion del corto circuito con Resilience4J
                            .build())
                    // Time out response microservicio
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build())
                    // construye all
                    .build();
        });
    }
}
