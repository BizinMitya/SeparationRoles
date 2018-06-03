package dao.impl;

import dao.ProductDAO;
import javafx.collections.FXCollections;
import jdbc.JDBC;
import model.Product;
import model.dto.ProductRecord;
import org.apache.log4j.Logger;
import session.UserSession;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAOImpl.class);

    private static final String GET_ALL_PRODUCTS_QUERY = "SELECT * FROM products";
    private static final String UPDATE_PRODUCT_QUERY =
            "UPDATE products SET name = ?, manufacturer = ?, cost = ?, country = ?, description = ?, maskAccess = ? WHERE idProducts = ?";
    private static final String REMOVE_PRODUCT = "DELETE FROM Products WHERE idProducts = ?";
    //private static final String GET_DATA_FOR_USER_BY_USER_ID_QUERY = "SELECT * FROM products WHERE (maskAccess & (1 << ?)) > 0";
    private static final String INSERT_PRODUCT =
            "INSERT INTO Products (name, manufacturer, cost, country, description, maskAccess) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public List<Product> getAllProductsForUser(int idUser) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_ALL_PRODUCTS_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(createProductFromResultSet(resultSet, idUser));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return products;
    }

    private Product createProductFromResultSet(ResultSet resultSet, int idUser) throws SQLException {
        return new Product(resultSet.getInt("idProducts"),
                resultSet.getString("name"),
                resultSet.getString("manufacturer"),
                String.valueOf(resultSet.getInt("cost")),
                resultSet.getString("country"),
                resultSet.getString("description"),
                hasAccessCurrentUser(resultSet.getString("maskAccess"), idUser),
                resultSet.getString("maskAccess")
        );
    }

    @Override
    public void updateProducts(List<Product> products) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(UPDATE_PRODUCT_QUERY)) {
            for (Product product : products) {
                try {
                    String maskAccess = product.getMaskAccess();
                    if (hasAccessCurrentUser(product.getMaskAccess(),
                            UserSession.selectedUser.getId()) /*права в базе, т.к. maskAccess не меняется на UI*/
                            ^ product.getAccess() /*измененные права*/) {
                        maskAccess = product.getAccess() ?
                                new BigInteger(product.getMaskAccess(), 16).setBit(UserSession.selectedUser.getId()).toString(16) :
                                new BigInteger(product.getMaskAccess(), 16).clearBit(UserSession.selectedUser.getId()).toString(16);
                        product.setMaskAccess(maskAccess);
                    }
                    int cost = Integer.parseInt(product.getCost());
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getManufacturer());
                    preparedStatement.setInt(3, cost);
                    preparedStatement.setString(4, product.getCountry());
                    preparedStatement.setString(5, product.getDescription());
                    preparedStatement.setString(6, maskAccess);
                    preparedStatement.setInt(7, product.getId());
                    preparedStatement.executeUpdate();
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private boolean hasAccessCurrentUser(String maskAccess, int idUser) {
        return new BigInteger(maskAccess, 16)
                .and(BigInteger.ONE.shiftLeft(idUser)).compareTo(BigInteger.ZERO) > 0;
    }

    @Override
    public boolean removeProduct(int id) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(REMOVE_PRODUCT)) {
            preparedStatement.setInt(1, id);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getInt("idProducts"),
                resultSet.getString("name"),
                resultSet.getString("manufacturer"),
                String.valueOf(resultSet.getInt("cost")),
                resultSet.getString("country"),
                resultSet.getString("description"),
                resultSet.getString("maskAccess")
        );
    }

    /*@Override
    public List<Product> getDataForUserById(int idUser) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_DATA_FOR_USER_BY_USER_ID_QUERY)) {
            preparedStatement.setLong(1, idUser);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(createProductFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return products;
    }*/

    @Override
    public List<Product> getDataForUserById(int idUser) {
        List<Product> products = getAllProductsForUser(idUser);
        List<Product> newProducts = new ArrayList<>();

        for (Product product :
                products) {
            if (new BigInteger(product.getMaskAccess(),16).testBit(idUser)) {
                newProducts.add(product);
            }
        }
        return newProducts;
    }

    @Override
    public void insertProduct(ProductRecord productRecord) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(INSERT_PRODUCT)) {
            preparedStatement.setString(1, productRecord.getName());
            preparedStatement.setString(2, productRecord.getManufacturer());
            preparedStatement.setInt(3, productRecord.getCost());
            preparedStatement.setString(4, productRecord.getCountry());
            preparedStatement.setString(5, productRecord.getDescription());
            preparedStatement.setString(6, productRecord.getMaskAccess());
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
