package com.example.crm;

import com.example.crm.util.annotations.Ownership;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Ownership(owner = "Adarsh Bhosale")
@RestController
@CrossOrigin("*")
@SpringBootApplication
public class CrmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(CrmApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrmApplication.class);
    }

    @GetMapping("/test")
    public String test() {
        return "CRM API is running successfully!";
    }
}
