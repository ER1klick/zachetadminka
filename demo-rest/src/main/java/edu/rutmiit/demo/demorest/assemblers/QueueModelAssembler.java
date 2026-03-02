package edu.rutmiit.demo.demorest.assemblers;

import edu.rutmiit.demo.demorest.controllers.QueueController;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class QueueModelAssembler implements RepresentationModelAssembler<QueueResponse, EntityModel<QueueResponse>> {

    @Override
    public EntityModel<QueueResponse> toModel(QueueResponse queue) {
        return EntityModel.of(queue,
                linkTo(methodOn(QueueController.class).getQueueById(queue.getQueueId())).withSelfRel(),
                linkTo(methodOn(QueueController.class).getAllQueues()).withRel("queues")
        );
    }

    @Override
    public CollectionModel<EntityModel<QueueResponse>> toCollectionModel(Iterable<? extends QueueResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(QueueController.class).getAllQueues()).withSelfRel());
    }
}