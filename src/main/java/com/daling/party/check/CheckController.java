package com.daling.party.check;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/inner")
public class CheckController {

    @RequestMapping("/monitor/check")
    public String check(){
        return "SUCCESS";
    }

    @RequestMapping(value = "/monitor/ping")
    public void ping(HttpServletResponse response) throws IOException {
        int status = 200;
        File file = new File("./healthcheck.html");
        if (!file.exists()) {
            status = 404;
        }
        response.setStatus(status);
    }
}
