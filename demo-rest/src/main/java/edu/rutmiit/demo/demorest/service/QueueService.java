package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;
import edu.rutmiit.demo.stoyakapicontract.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueueService {
    private final InMemoryStorage storage;
    public QueueService(InMemoryStorage storage) { this.storage = storage; }

    public QueueResponse createQueue(QueueRequest request) {
        long newId = storage.queueSequence.incrementAndGet();
        QueueResponse newQueue = new QueueResponse(newId, request.name(), request.address(), request.description(), request.workingHours());
        storage.queues.put(newId, newQueue);
        return newQueue;
    }

    public QueueResponse findQueueById(Long id) {
        QueueResponse queue = storage.queues.get(id);
        if (queue == null) { throw new ResourceNotFoundException("Queue", id); }
        return queue;
    }

    public List<QueueResponse> findAllQueues() {
        return new ArrayList<>(storage.queues.values());
    }
}