package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Product {

    private int id;
    private String name;
    private String manufacturer;
    private String cost;
    private String country;
    private String description;
    private String maskAccess;
    private SimpleBooleanProperty access = new SimpleBooleanProperty();

    public Product(int id, String name, String manufacturer, String cost, String country, String description, String maskAccess) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        this.maskAccess = maskAccess;
    }

    public Product(int id, String name, String manufacturer, String cost, String country, String description, boolean access, String maskAccess) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        setAccess(access);
        this.maskAccess = maskAccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMaskAccess() {
        return maskAccess;
    }

    public void setMaskAccess(String maskAccess) {
        this.maskAccess = maskAccess;
    }

    public BooleanProperty accessProperty() {
        return access;
    }

    public boolean getAccess() {
        return accessProperty().get();
    }

    public void setAccess(boolean access) {
        accessProperty().set(access);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", cost='" + cost + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", maskAccess=" + maskAccess +
                ", access=" + access +
                '}';
    }

    private void setAccess(SimpleBooleanProperty simpleBooleanProperty) {
        this.access = simpleBooleanProperty;
    }

}
