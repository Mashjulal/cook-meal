package com.mashjulal.android.cookmeal.objects;

import java.io.Serializable;

/**
 * Created by Master on 07.08.2017.
 */

public class Ingredient implements Serializable {

    public static final String TYPE_ITEMS = "items";
    public static final String TYPE_LITERS = "liters";

    private int id = -1;
    private String name = "";
    private String type;
    private int value = 0;

    public Ingredient(int id, String name, String type, int value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Ingredient(String name, String type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
