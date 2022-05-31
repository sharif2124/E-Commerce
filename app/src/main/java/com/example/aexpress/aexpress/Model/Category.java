package com.example.aexpress.aexpress.Model;

public class Category {
    private String name,icon,colour,brief;
    private int id ;

    public Category(String name, String icon, String colour, String brief, int id) {
        this.name = name;
        this.icon = icon;
        this.colour = colour;
        this.brief = brief;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
