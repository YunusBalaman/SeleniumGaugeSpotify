package com.spotify.web.methods.database;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.database.databases.Mssql;
import com.spotify.web.methods.database.databases.OracleDB;
import com.spotify.web.methods.database.databases.PostgreSql;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


public class ConnectDatabase {

    Logger logger = LogManager.getLogger(ConnectDatabase.class);
    MethodsUtil methodsUtil;

    public ConnectDatabase(){

        methodsUtil = new MethodsUtil();
    }

    public Connection connectDb(String database, String databaseName, String username, String password, String ip, String port){

        username = methodsUtil.setValueWithMapKey(username);
        password = methodsUtil.setValueWithMapKey(password);
        ip = methodsUtil.setValueWithMapKey(ip);
        port = methodsUtil.setValueWithMapKey(port);
        Properties properties = new Properties();
        if (!username.isEmpty()) {
            properties.put("user", username);
        }
        if (!password.isEmpty()) {
            properties.put("password", password);
        }
        if (database.equalsIgnoreCase("oracle")) {
            properties.put("defaultRowPrefetch", "20");
        }
        return connectDatabase(database, properties, databaseName, ip, port);
    }

    private Connection connectDatabase(String database, Properties properties, String databaseName, String ip, String port){

        Connection connection = null;
        switch (database.toUpperCase(Locale.ENGLISH)){
            case IDatabase.POSTGRESQL:
                connection = PostgreSql.connectDatabase(ip, port, databaseName, properties);
                break;
            case IDatabase.ORACLE:
                connection = OracleDB.connectDatabase(ip, port, databaseName, properties);
                break;
            case IDatabase.MSSQL:
                connection = Mssql.connectDatabase(ip, port, databaseName, properties);
                break;
            default:
                Assertions.fail(database + " database desteği yok");
        }
        return connection;
    }

    public Statement createStatement(String connectionMapKey){

        Statement statement = null;
        if (!Driver.DatabaseMap.containsKey("statement" + connectionMapKey)) {
            Connection c = (Connection) Driver.DatabaseMap.get(connectionMapKey);
            try {
                statement = c.createStatement();
                Driver.DatabaseMap.put("statement" + connectionMapKey, statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            statement = (Statement) Driver.DatabaseMap.get("statement" + connectionMapKey);
        }

        return statement;
    }

    protected ResultSet executeQuery(Statement statement, String query) {

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    protected int executeUpdate(Statement statement, String query) {

        int i = 0;
        try {
            i = statement.executeUpdate(query);
            return i;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected long executeLargeUpdate(Statement statement, String query) {

        long i = 0;
        try {
            i = statement.executeLargeUpdate(query);
            statement.close();
            return i;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addConnectionList(String connectionMapKey){

        if(Driver.DatabaseMap.containsKey(Driver.databaseConnectionMapKeyName)){
            ((List<String>) Driver.DatabaseMap.get(Driver.databaseConnectionMapKeyName)).add(connectionMapKey);
        }else {
            List<String> list = new ArrayList<>();
            list.add(connectionMapKey);
            Driver.DatabaseMap.put(Driver.databaseConnectionMapKeyName, list);
        }
    }

    public void addConnectionInMap(String connectionMapKey, Connection connection){

       addConnectionList(connectionMapKey);
       Driver.DatabaseMap.put(connectionMapKey, connection);
    }

    public void closeDatabase(String mapKey){

        if (Driver.DatabaseMap.containsKey(mapKey)) {
            Object connection = Driver.DatabaseMap.get(mapKey);
            if (connection instanceof Connection) {
                try {
                    if (Driver.DatabaseMap.containsKey("statement"+ mapKey)){
                        Object statement = Driver.DatabaseMap.get("statement"+ mapKey);
                        if (statement instanceof Statement){
                            ((Statement) statement).close();
                            System.out.println("statement" + mapKey + " statement kapatıldı");
                        }
                    }
                    ((Connection) connection).close();
                    System.out.println(mapKey + " connection kapatıldı");
                    Driver.DatabaseMap.remove("statement" + mapKey);
                    Driver.DatabaseMap.remove(mapKey);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void closeDatabaseConnections(){

        if(Driver.DatabaseMap.containsKey(Driver.databaseConnectionMapKeyName)){
            List<String> list = (List<String>) Driver.DatabaseMap.get(Driver.databaseConnectionMapKeyName);
            for (String mapKey: list){
                if (Driver.DatabaseMap.containsKey(mapKey)) {
                    Object connection = Driver.DatabaseMap.get(mapKey);
                    if (connection instanceof Connection) {
                        try {
                            if (Driver.DatabaseMap.containsKey("statement"+ mapKey)){
                                Object statement = Driver.DatabaseMap.get("statement"+ mapKey);
                                if (statement instanceof Statement){
                                    ((Statement) statement).close();
                                    System.out.println("statement" + mapKey + " statement kapatıldı");
                                }
                            }
                            ((Connection) connection).close();
                            System.out.println(mapKey + " connection kapatıldı");
                            Driver.DatabaseMap.remove("statement" + mapKey);
                            Driver.DatabaseMap.remove(mapKey);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            Driver.DatabaseMap.remove(Driver.databaseConnectionMapKeyName);
        }
    }

}
