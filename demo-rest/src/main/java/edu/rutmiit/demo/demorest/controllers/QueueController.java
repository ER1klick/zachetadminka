package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.demorest.assemblers.QueueModelAssembler;
import edu.rutmiit.demo.demorest.service.QueueService;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QueueController {

    private final QueueService queueService;
    private final QueueModelAssembler QueueModelAssembler;

    public QueueController(QueueService queueService, QueueModelAssembler QueueModelAssembler) {
        this.queueService = queueService;
        this.QueueModelAssembler = QueueModelAssembler;
    }

    @PostMapping("/api/queues")
    public ResponseEntity<EntityModel<QueueResponse>> createQueue(@Valid @RequestBody QueueRequest queueRequest) {
        QueueResponse queue = queueService.createQueue(queueRequest);
        EntityModel<QueueResponse> entityModel = QueueModelAssembler.toModel(queue);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @GetMapping("/api/queues/{id}")
    public EntityModel<QueueResponse> getQueueById(@PathVariable Long id) {
        QueueResponse queue = queueService.findQueueById(id);
        return QueueModelAssembler.toModel(queue);
    }

    @GetMapping("/api/queues")
    public CollectionModel<EntityModel<QueueResponse>> getAllQueues() {
        List<QueueResponse> queues = queueService.findAllQueues();
        return QueueModelAssembler.toCollectionModel(queues);
    }

    @DeleteMapping("/api/queues/{id}")
    public StatusResponse deleteQueue(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}