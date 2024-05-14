package org.example;

import java.time.LocalDate;
import java.util.List;

public class Task {

    private int id;
    private String type;
    private String target;
    private LocalDate due_date;
    private boolean done;

    public Task(int id, String type, String target, LocalDate due_date, boolean done) {
        this.id = id;
        this.type = type;
        this.target = target;
        this.due_date= due_date;
        this.done = done;
    }

    @Override
    public String toString() {
        return getId()+ ": "+ getType() + ' ' + getTarget() + ' ' + " : " + getDueDate() + ' ' + getDone();
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public LocalDate getDueDate() {
        return due_date;
    }

    public void setDueDate(LocalDate due_date) {
        this.due_date = due_date;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
