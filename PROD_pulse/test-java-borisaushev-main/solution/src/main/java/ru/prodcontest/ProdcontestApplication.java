package ru.prodcontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

@SpringBootApplication
public class ProdcontestApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(ProdcontestApplication.class, args);
        String file = "/Users/artur/IdeaProjects/PROD-stage-2/PROD_pulse/test-java-borisaushev-main/tests/init-database.sh";
        var res = new ProcessBuilder(file).start();
        System.out.append(res.errorReader().lines().collect(Collectors.joining()));

    }

}
