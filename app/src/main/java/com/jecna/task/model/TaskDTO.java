package com.jecna.task.model;

import java.util.UUID;

public class TaskDTO {
     UUID id;
     String  name;
     String description;
     //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
     String dateFrom;
     //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
     String dateTo;
     Integer status;
     Integer priority;

    public TaskDTO(UUID id, String name, String description, String dateFrom, String dateTo, Integer status, Integer priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


}
