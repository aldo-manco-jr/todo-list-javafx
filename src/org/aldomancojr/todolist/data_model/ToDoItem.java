package org.aldomancojr.todolist.data_model;

import java.time.LocalDate;

/**
 * Created by unieuro on 7/18/2019.
 */
public class ToDoItem implements Comparable<ToDoItem> {
    private String
            shortDescription,
            details;
    private LocalDate deadline;

    public ToDoItem(String shortDescription, String details, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public int compareTo(ToDoItem o) {
        if (this.getDeadline().isAfter(o.getDeadline())){
            return 1;
        }else if (this.getDeadline().isBefore(o.getDeadline())){
            return -1;
        }else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
