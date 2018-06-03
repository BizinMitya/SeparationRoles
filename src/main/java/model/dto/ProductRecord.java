package model.dto;

public class ProductRecord {
    private String name;
    private String manufacturer;
    private int cost;
    private String country;
    private String description;
    private String maskAccess;

    public ProductRecord(String name, String manufacturer, int cost, String country, String description, String maskAccess) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.cost = cost;
        this.country = country;
        this.description = description;
        this.maskAccess = maskAccess;
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

    public String getMaskAccess() {
        return maskAccess;
    }

    public void setMaskAccess(String maskAccess) {
        this.maskAccess = maskAccess;
    }
}
