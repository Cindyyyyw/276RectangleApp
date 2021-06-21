package com.example;

public class Rectangle{
    private String name;
    private String color;
    private int width;
    private int height;

    //  to get the values
    public String getName() {
        return this.name;
    }
    public String getColor() {
        return this.color;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    //  to set the values
    public void setName(String N) {
        this.name = N;
    }
    public void setColor(String C) {
        this.color = C;
    }
    public void setWidth(int W) {
        this.width = W;
    }
    public void setHeight(int H) {
        this.height = H;
    }
}