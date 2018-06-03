package dao;

import model.Product;
import model.dto.ProductRecord;

import java.util.List;

public interface ProductDAO {

    List<Product> getAllProductsForUser(int idUser);

    void updateProducts(List<Product> products);

    boolean removeProduct(int id);

    List<Product> getDataForUserById(int idUser);

    void insertProduct(ProductRecord productRecord);

}
