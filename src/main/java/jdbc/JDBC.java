package jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBC {

    private static final MysqlDataSource dataSource;
    private static final String SCHEMA = "roles";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + SCHEMA + "?useSSL=true";

    static {
        dataSource = new MysqlDataSource();
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setURL(URL);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
