package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Product {

    private long id;
    private String name;
    private String manufacturer;
    private String cost;
    private String country;
    private String description;
    private long maskAccess;
    private SimpleBooleanProperty access = new SimpleBooleanProperty();

    public Product(long id, String name, String manufacturer, String cost, String country, String description, long maskAccess) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        this.maskAccess = maskAccess;
    }

    public Product(long id, String name, String manufacturer, String cost, String country, String description, boolean access) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        setAccess(access);
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
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

    public BooleanProperty accessProperty() {
        return access;
    }

    public final boolean getAccess() {
        return accessProperty().get();
    }

    public final void setAccess(boolean active) {
        accessProperty().set(active);
    }

}
