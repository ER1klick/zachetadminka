package edu.rutmiit.demo.simplenotificationservice.controller;

import edu.rutmiit.demo.simplenotificationservice.handler.NotificationWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationWebSocketHandler handler;

    public NotificationController(NotificationWebSocketHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcast(@RequestBody String message) {
        int sentCount = handler.broadcast(message);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message_sent_to_clients", sentCount,
                "original_message", message
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.of(
                "active_connections", handler.getActiveConnections()
        ));
    }

    public static class PrivateMessageRequest {
        public String userId;
        public String message;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendToUser(@RequestBody PrivateMessageRequest request) {
        boolean success = handler.sendToUser(request.userId, request.message);

        if (success) {
            return ResponseEntity.ok(Map.of("status", "sent", "to", request.userId));
        } else {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", "User not found or offline"));
        }
    }

}