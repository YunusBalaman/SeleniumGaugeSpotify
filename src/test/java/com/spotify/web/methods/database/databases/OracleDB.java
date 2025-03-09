package com.spotify.web.methods.database.databases;

import com.spotify.web.methods.database.DatabaseEnum;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class OracleDB {

    public static Connection connectDatabase(String ip, String port, String databaseName, Properties properties){
        Connection databaseConnection = null;
        try{
            Class.forName(DatabaseEnum.ORACLE.getDatabaseClassName());
            databaseConnection = DriverManager.getConnection(DatabaseEnum.ORACLE.getJdbc() + ip + (port.isEmpty() ? "" : ":" + port)
                    + ":" + databaseName, properties);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Assertions.fail("connection error");
        }
        System.out.println("Opened database successfully");

        return databaseConnection;
    }
}
