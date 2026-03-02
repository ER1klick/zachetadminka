package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.rutmiit.demo.demorest.service.QueueService;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;

import java.util.List;

@DgsComponent
public class QueueDataFetcher {

    private final QueueService queueService;

    public QueueDataFetcher(QueueService queueService) {
        this.queueService = queueService;
    }

    @DgsQuery
    public QueueResponse queueById(@InputArgument Long id) {
        return queueService.findQueueById(id);
    }

    @DgsQuery
    public List<QueueResponse> allQueues() {
        return queueService.findAllQueues();
    }
}