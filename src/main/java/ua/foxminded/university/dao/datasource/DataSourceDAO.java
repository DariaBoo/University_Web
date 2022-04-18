package ua.foxminded.university.dao.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * @author Bogush Daria
 * @version 1.0
 */
public class DataSourceDAO extends DataSourceDaoConfig {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSourse;
    private static String configFile = null;

    static {
        configFile = DataSourceDaoConfig.getConfigFile();
        if (configFile == null) {
            configFile = "dbconfig.properties";
        }
        Properties properties = new Properties();
        try {
            InputStream inputStream = DataSourceDAO.class.getClassLoader().getResourceAsStream(configFile);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("IOException in DataSource class", e);
        }
        config.setJdbcUrl(properties.getProperty("DB_URL"));
        config.setUsername(properties.getProperty("DB_USER"));
        config.setPassword(properties.getProperty("DB_PASSWORD"));
        dataSourse = new HikariDataSource(config);
        dataSourse.setValidationTimeout(60000L);
    }

    private DataSourceDAO() {

    }

    /**
     * The method returns a connection to the database.
     * 
     * @author Bogush Daria
     * @return Connection
     */
    public static Connection getConnection() throws SQLException {
        return dataSourse.getConnection();
    }
}