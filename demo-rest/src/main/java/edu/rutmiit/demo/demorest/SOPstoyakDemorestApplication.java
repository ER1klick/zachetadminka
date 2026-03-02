package edu.rutmiit.demo.demorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;


@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)

@SpringBootApplication
public class SOPstoyakDemorestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SOPstoyakDemorestApplication.class, args);
    }

}