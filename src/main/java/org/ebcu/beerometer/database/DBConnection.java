package org.ebcu.beerometer.database;

import com.microsoft.sqlserver.jdbc.*;

import org.apache.commons.configuration.*;
import org.sql2o.*;

public class DBConnection {

    private static final String PROPERTIES_FILE_DB             = "db.properties";

    private static final String PROPERTY_USER                  = "jdbc.user";
    private static final String PROPERTY_PASSWORD              = "jdbc.password";
    private static final String PROPERTY_SERVERNAME            = "jdbc.servername";
    private static final String PROPERTY_PORTNUMBER            = "jdbc.portnumber";
    private static final String PROPERTY_DATABASENAME          = "jdbc.databasename";
    private static final String PROPERTY_ENCRYPT               = "jdbc.encrypt";
    private static final String PROPERTY_LOGINTIMEOUT          = "jdbc.logintimeout";
    private static final String PROPERTY_HOSTNAMEINCERTIFICATE = "jdbc.hostnameincertificate";

    private static Sql2o        sql2o;

    static {
        initializeDBConnProps();
    }

    private static void initializeDBConnProps() {

        try {
            Configuration config = new PropertiesConfiguration(PROPERTIES_FILE_DB);

            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setUser(config.getString(PROPERTY_USER));
            ds.setPassword(config.getString(PROPERTY_PASSWORD));
            ds.setServerName(config.getString(PROPERTY_SERVERNAME));
            ds.setPortNumber(config.getInt(PROPERTY_PORTNUMBER));
            ds.setDatabaseName(config.getString(PROPERTY_DATABASENAME));
            ds.setEncrypt(config.getBoolean(PROPERTY_ENCRYPT));
            ds.setLoginTimeout(config.getInt(PROPERTY_LOGINTIMEOUT));
            ds.setHostNameInCertificate(config.getString(PROPERTY_HOSTNAMEINCERTIFICATE));

            sql2o = new Sql2o(ds);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Sql2o getConnection() {
        return sql2o;
    }

}
