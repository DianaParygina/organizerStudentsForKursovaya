package org.example;

import java.util.List;

public class Item {

    private int id;
    private String name;
    private String number_of_hours;

    public Item(int id, String name, String number_of_hours) {
        this.id = id;
        this.name = name;
        this.number_of_hours = number_of_hours;
    }

    @Override
    public String toString() {
        return getId()+ ": "+ getNumberOfHours() + ' ' + getName();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfHours() {
        return number_of_hours;
    }

    public void setNumberOfHours(String number_of_hours) {
        this.number_of_hours = number_of_hours;
    }

}
