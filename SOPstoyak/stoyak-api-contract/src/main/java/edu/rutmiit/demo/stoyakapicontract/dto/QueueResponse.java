package edu.rutmiit.demo.stoyakapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "queues", itemRelation = "queue")
@JsonRootName(value = "queue")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueResponse extends RepresentationModel<QueueResponse> {

    private final Long queueId;
    private final String name;
    private final String address;
    private final String description;
    private final String workingHours;

    public QueueResponse(Long queueId, String name, String address, String description, String workingHours) {
        this.queueId = queueId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.workingHours = workingHours;
    }

    public Long getQueueId() {
        return queueId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QueueResponse that = (QueueResponse) o;
        return Objects.equals(queueId, that.queueId) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(description, that.description) && Objects.equals(workingHours, that.workingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), queueId, name, address, description, workingHours);
    }
}