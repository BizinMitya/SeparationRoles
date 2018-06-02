package jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBC {

    private static final DataSource dataSource;
    private static final String SCHEMA = "roles";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + SCHEMA + "?useSSL=true";

    static {
        dataSource = DataSource;
        dataSource.setPoolProperties(poolProperties);
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        javax.sql.DataSource
        return dataSource.getConnection();
    }
}
