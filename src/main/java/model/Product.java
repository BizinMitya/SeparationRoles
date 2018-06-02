package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Objects;

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

    public Product(long id, String name, String manufacturer, String cost, String country, String description, boolean access, long maskAccess) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        setAccess(access);
        this.maskAccess = maskAccess;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                maskAccess == product.maskAccess &&
                Objects.equals(name, product.name) &&
                Objects.equals(manufacturer, product.manufacturer) &&
                Objects.equals(cost, product.cost) &&
                Objects.equals(country, product.country) &&
                Objects.equals(description, product.description) &&
                Objects.equals(access, product.access);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, manufacturer, cost, country, description, maskAccess, access);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Product clone = (Product) super.clone();
        clone.setId(this.id);
        clone.setName(this.name);
        clone.setManufacturer(this.manufacturer);
        clone.setCost(this.cost);
        clone.setCountry(this.country);
        clone.setDescription(this.description);
        clone.setMaskAccess(this.maskAccess);
        clone.setAccess(new SimpleBooleanProperty());
        return clone;
    }

    private void setAccess(SimpleBooleanProperty simpleBooleanProperty) {
        this.access = simpleBooleanProperty;
    }

}
