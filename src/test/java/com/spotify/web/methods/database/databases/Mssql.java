package com.spotify.web.methods.database.databases;

import com.spotify.web.methods.database.DatabaseEnum;
import org.junit.jupiter.api.Assertions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Mssql {

    public static Connection connectDatabase(String ip, String port, String databaseName, Properties properties){
        Connection databaseConnection = null;
        try{
            Class.forName(DatabaseEnum.MSSQL.getDatabaseClassName());
            databaseConnection = DriverManager.getConnection(DatabaseEnum.MSSQL.getJdbc() + ip + (port.isEmpty() ? "" : ":" + port)
                    + ";databaseName=" + databaseName, properties);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Assertions.fail("connection error");
        }
        System.out.println("Opened database successfully");

        return databaseConnection;
    }

    //integratedSecurity=true
    // selectMethod=cursor
    // trustServerCertificate=true;encrypt=true
    // jdbc:jtds:sqlserver://192.168.1.172:53000/sample;useCursors=true

    /**
     public List<String> getServerDataBaseNameList() {
     List<String> dataBaseNameList = new ArrayList<String>();
     try {
     ResultSet resultSet = con.getConnectionMetaData().getCatalogs();
     if (resultSet == null) {
     return null;
     }
     while (resultSet.next()) {
     dataBaseNameList.add(resultSet.getString(1));
     }
     return dataBaseNameList;
     } catch (SQLException e) {
     logger.error("Error during execute select query for fetch server database name");
     }
     return null;
     }
     */
}
