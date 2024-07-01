package ru.prodcontest.model.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    RequestRepository repository;

    public Object serve() {
        return null;
    }

}
