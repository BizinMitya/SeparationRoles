package model;

import javafx.beans.property.SimpleBooleanProperty;

public class Product {

    private long id;
    private String name;
    private String manufacturer;
    private int cost;
    private String country;
    private String description;
    private long maskAccess;
    private SimpleBooleanProperty checkbox;

    public Product(long id, String name, String manufacturer, int cost, String country, String description, long maskAccess) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        this.maskAccess = maskAccess;
    }

    public Product(long id, String name, String manufacturer, int cost, String country, String description, boolean checkbox) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        this.checkbox = new SimpleBooleanProperty(checkbox);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMaskAccess() {
        return maskAccess;
    }

    public void setMaskAccess(long maskAccess) {
        this.maskAccess = maskAccess;
    }

    public boolean isCheckbox() {
        return checkbox.get();
    }

    public SimpleBooleanProperty checkboxProperty() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox.set(checkbox);
    }
}
