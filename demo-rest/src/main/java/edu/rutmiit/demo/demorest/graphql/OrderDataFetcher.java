package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.*;
import edu.rutmiit.demo.demorest.service.OrderService;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.QueueResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@DgsComponent
public class OrderDataFetcher {

    private final OrderService orderService;

    public OrderDataFetcher(OrderService orderService) {
        this.orderService = orderService;
    }

    @DgsQuery
    public OrderResponse orderById(@InputArgument Long id) {
        return orderService.findOrderById(id);
    }

    @DgsMutation
    public OrderResponse createOrder(@InputArgument("input") Map<String, Object> input) {
        OrderRequest request = new OrderRequest(
                Long.parseLong(input.get("clientId").toString()),
                Long.parseLong(input.get("queueId").toString()),
                LocalDateTime.parse((String) input.get("desiredTime")),
                new BigDecimal(input.get("price").toString()),
                (String) input.get("specialInstructions")
        );
        return orderService.createOrder(request);
    }

    @DgsData(parentType = "Order", field = "queue")
    public QueueResponse queue(DgsDataFetchingEnvironment dfe) {
        OrderResponse order = dfe.getSource();
        return order.getQueue();
    }
}