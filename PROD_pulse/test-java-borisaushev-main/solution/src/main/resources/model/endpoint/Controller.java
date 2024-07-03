package endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/path")
public class Controller {

    @Autowired
    RequestService service;

    @GetMapping
    @ResponseBody
    public String doGet() {
        return "";
    }

}
