package com.web.config
// Importamos DataSource de Java (paquete javax.sql). Es la interfaz estándar de un origen de datos:
// sabe abrir conexiones con una base de datos. Viene con el propio JDK, no hace falta ninguna librería.
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

// Importamos CommandLineRunner de Spring Boot: su método run() se ejecuta al terminar el arranque.
// Maven ha descargado Spring Boot con el starter; sin él habría que arrancar y conectar todo a mano.
import org.springframework.boot.CommandLineRunner;

// Importamos @Order de Spring: fija en qué orden se ejecutan varios CommandLineRunner.
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
* Objetivo: pedir una conexión a la base de datos y escribir por consola si ha tenido éxito
 * o cuál ha sido el motivo del fallo.
 *
 * Lo que hace este ejemplo:
 *   - Recibe por constructor el DataSource que Spring Boot crea a partir de application.properties
 *   - Abre una conexión y escribe datos de la base de datos a la que se ha conectado
 *   - Si no puede conectar, captura el error y escribe su motivo
 *
 * Concepto clave: Spring Boot construye el origen de datos a partir de tres propiedades
 * (dirección, usuario y contraseña). Comprobar la conexión con una clase mínima, antes de
 * añadir entidades, permite descartar los problemas de conexión del resto.
 */
// @Component: Spring crea un bean de esta clase al escanear el paquete.
// @Order(1): si hay varios CommandLineRunner, los ejecuta de menor a mayor número.

@Component
@Order(1)
public class ComprobarConexion implements CommandLineRunner {

    // DataSource es el origen de datos: el objeto que sabe abrir conexiones con la base de datos.
    // Spring Boot lo ha creado por nosotros con la dirección, el usuario y la contraseña
    // de application.properties (autoconfiguración).
    private final DataSource dataSource;

    // Inyección por constructor (S06): Spring busca en el contenedor un bean de tipo DataSource
    // y se lo entrega a esta clase al crearla.
    public ComprobarConexion(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // run() se ejecuta una vez, cuando el contenedor ya está arrancado y todos los beans están listos.
    @Override
    public void run(String... args) {
        System.out.println("Comprobando la conexión con PostgreSQL...");

        // try-with-resources (S04-1): la Connection se cierra sola al salir del bloque,
        // tanto si todo va bien como si hay una excepción.
        // getConnection() es el momento en que se intenta de verdad llegar al servidor.
        try (Connection conexion = dataSource.getConnection()) {

            // DatabaseMetaData describe la base de datos a la que estamos conectados:
            // producto, versión, usuario... Es una forma de comprobar que la conexión funciona de verdad.
            DatabaseMetaData info = conexion.getMetaData();

            System.out.println("CONEXIÓN CORRECTA");
            System.out.println("  Base de datos: " + info.getDatabaseProductName()
                    + " " + info.getDatabaseProductVersion());   // → PostgreSQL 17.x (la versión que hayas instalado)
            System.out.println("  Usuario:       " + info.getUserName());  // → postgres

        } catch (Exception e) {
            // Si algo falla (dirección mal escrita, contraseña incorrecta, red que no llega al servidor),
            // getConnection() lanza una excepción. La capturamos para escribir el motivo
            // en lugar de dejar que el programa termine con un error largo.
            System.out.println("NO SE HA PODIDO CONECTAR");
            System.out.println("  Motivo: " + e.getMessage());
        }
    }
}
