package edu.rutmiit.demo.simplenotificationservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);

        if (userId != null) {
            sessions.put(userId, session);
            log.info("Пользователь {} подключился (Session ID: {})", userId, session.getId());
        } else {
            log.warn("Подключение без userId отклонено или анонимно");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if ("PING".equalsIgnoreCase(message.getPayload())) {
            session.sendMessage(new TextMessage("PONG"));
            return;
        }
        log.info("Сообщение: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("Пользователь {} отключился", userId);
        }
    }

    public boolean sendToUser(String userId, String message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                return true;
            } catch (IOException e) {
                log.error("Ошибка отправки пользователю {}", userId, e);
            }
        }
        return false;
    }

    public int broadcast(String message) {
        int count = 0;
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(new TextMessage(message));
                count++;
            } catch (IOException e) {
            }
        }
        return count;
    }

    private String getUserIdFromSession(WebSocketSession session) {
        try {
            String query = session.getUri().getQuery();
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1 && "userId".equals(pair[0])) {
                    return pair[1];
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public int getActiveConnections() {
        return sessions.size();
    }
}