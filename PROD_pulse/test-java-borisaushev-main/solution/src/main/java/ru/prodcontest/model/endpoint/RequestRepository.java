package ru.prodcontest.model.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequestRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public void doSmth() {

    }

}
