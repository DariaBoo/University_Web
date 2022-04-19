package ua.foxminded.university.dao.datasource;

public class DataSourceDaoConfig {
    private static String configFile;

    public static void setConfigFile(String configFileName) {
        configFile = configFileName;
    }

    public static String getConfigFile() {
        return configFile;
    }
}
