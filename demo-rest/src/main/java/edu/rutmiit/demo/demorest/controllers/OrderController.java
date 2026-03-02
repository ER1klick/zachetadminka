package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.demorest.assemblers.OrderModelAssembler;
import edu.rutmiit.demo.demorest.service.OrderService;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderRequest;
import edu.rutmiit.demo.stoyakapicontract.dto.OrderResponse;
import edu.rutmiit.demo.stoyakapicontract.dto.StatusResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler OrderModelAssembler;

    public OrderController(OrderService orderService, OrderModelAssembler OrderModelAssembler) {
        this.orderService = orderService;
        this.OrderModelAssembler = OrderModelAssembler;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<EntityModel<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        EntityModel<OrderResponse> entityModel = OrderModelAssembler.toModel(order);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @GetMapping("/api/orders/{id}")
    public EntityModel<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.findOrderById(id);
        return OrderModelAssembler.toModel(order);
    }

    @PutMapping("/api/orders/{id}/cancel")
    public StatusResponse cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @DeleteMapping("/api/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}