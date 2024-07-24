package ru.prodcontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.prodcontest.testing.PostmanTests;

import java.io.IOException;

@EnableJpaRepositories("ru.prodcontest.posts")
@SpringBootApplication
public class ProdcontestApplication {

    public static void main(String[] args) throws IOException {
        var context = SpringApplication.run(ProdcontestApplication.class, args);
        var tests = (PostmanTests) context.getBean(PostmanTests.class);
        tests.runTests();
    }

}
