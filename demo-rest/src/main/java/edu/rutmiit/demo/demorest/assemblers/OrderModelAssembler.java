package edu.rutmiit.demo.demorest.assemblers;

import edu.rutmiit.demo.demorest.controllers.OrderController;
import edu.rutmiit.demo.demorest.controllers.QueueController;
import edu.rutmiit.demo.demorest.controllers.UserController;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderResponse, EntityModel<OrderResponse>> {

    @Override
    public EntityModel<OrderResponse> toModel(OrderResponse order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getOrderId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserById(order.getClientId())).withRel("client"),
                linkTo(methodOn(QueueController.class).getQueueById(order.getQueue().getQueueId())).withRel("queue"),
                linkTo(methodOn(OrderController.class).createOrder(null)).withRel("orders")
        );
    }
}