package edu.rutmiit.demo.demorest.storage;

import edu.rutmiit.demo.stoyakapicontract.dto.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import edu.rutmiit.demo.stoyakapicontract.Enums.UserType;

@Component
public class InMemoryStorage {
    public final Map<Long, UserResponse> users = new ConcurrentHashMap<>();
    public final Map<Long, QueueResponse> queues = new ConcurrentHashMap<>();
    public final Map<Long, OrderResponse> orders = new ConcurrentHashMap<>();

    public final AtomicLong userSequence = new AtomicLong(0);
    public final AtomicLong queueSequence = new AtomicLong(0);
    public final AtomicLong orderSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        UserResponse client1 = new UserResponse(userSequence.incrementAndGet(), "petr_client", "petr@client.com", "+79112223344", "Петр", "Иванов", 5.0, UserType.CLIENT);
        UserResponse runner1 = new UserResponse(userSequence.incrementAndGet(), "anna_runner", "anna@runner.com", "+79223334455", "Анна", "Кузнецова", 5.0, UserType.RUNNER);
        users.put(client1.getUserId(), client1);
        users.put(runner1.getUserId(), runner1);

        QueueResponse queue1 = new QueueResponse(queueSequence.incrementAndGet(), "МФЦ 'Центральный'", "Москва, ул. Тверская, 1", "Окно 5", "Пн-Сб, 8:00-20:00");
        queues.put(queue1.getQueueId(), queue1);

        long orderId = orderSequence.incrementAndGet();
        OrderResponse order1 = new OrderResponse(orderId, client1.getUserId(), null, queue1, LocalDateTime.now().plusDays(2), "new", new BigDecimal("750.00"), "Получить талон на загранпаспорт", LocalDateTime.now(), null);
        orders.put(orderId, order1);
    }
}