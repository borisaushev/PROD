package ru.prodcontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
@RequestMapping("/api/ping")
public class PingController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String ping() throws SQLException {
        return "BCE OK";
    }

}
