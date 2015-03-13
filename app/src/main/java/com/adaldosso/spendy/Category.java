package com.adaldosso.spendy;

public class Category {

    private final Integer id;
    private final String description;

    public Category(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getId() {
        return id.toString();
    }
}
