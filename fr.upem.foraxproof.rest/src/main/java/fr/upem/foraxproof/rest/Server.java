package fr.upem.foraxproof.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    public static void run(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
