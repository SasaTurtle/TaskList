package com.jecna.task.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskModel implements Serializable {

    public enum Status {
        NOT_STARTED(0),
        ONGOING(1),
        FINISHED(2);

        private int value;
        private static Map map = new HashMap<>();
        Status(int i) {
            this.value = i;
        }
        static {
            for (Status pageType : Status.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static Status valueOf(int pageType) {
            return (Status) map.get(pageType);
        }

        public int getValue() {
            return value;
        }
    }

    public enum Priority {
        LOW(0),
        MEDIUM(1),
        HIGH(2);
        private int value;
        private static Map map = new HashMap<>();
        Priority(int i) {
            this.value = i;
        }
        static {
            for (Priority pageType : Priority.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static Priority valueOf(int pageType) {
            return (Priority) map.get(pageType);
        }

        public int getValue() {
            return value;
        }
    }

    private UUID id;
    private String name;
    private String description;
    private Date dateFrom;
    private Date dateTo;
    private Status status;
    private Priority priority;



    public TaskModel(String name, String description, Date dateFrom, Date dateTo, Status status, Priority priority) {
        this.id=null;
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.priority = priority;
    }
    public TaskModel(UUID id,String name, String description, Date dateFrom, Date dateTo, Status status, Priority priority) {
        this.id=id;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
