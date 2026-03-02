package edu.rutmiit.demo.demorest.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();

        rootModel.add(
                linkTo(methodOn(QueueController.class).getAllQueues()).withRel("queues"),
                linkTo(methodOn(UserController.class).registerUser(null)).withRel("users"),
                linkTo(methodOn(OrderController.class).createOrder(null)).withRel("orders")
        );
        return rootModel;
    }
}