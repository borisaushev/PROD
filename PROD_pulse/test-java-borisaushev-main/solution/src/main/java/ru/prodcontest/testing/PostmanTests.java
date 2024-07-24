package ru.prodcontest.testing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class PostmanTests {

    @Value("${myproject.path}")
    private String projectPath;

    public void runTests() throws IOException {
        String file = projectPath + "/PROD/PROD_pulse/test-java-borisaushev-main/solution/src/main/resources/init-database.sh";
        var res = new ProcessBuilder(file).start();
        System.out.append(res.errorReader().lines().collect(Collectors.joining()));
    }
}
