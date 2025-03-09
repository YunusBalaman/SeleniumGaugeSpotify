package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.database.ConnectDatabase;
import com.spotify.web.methods.database.ConnectDbMethods;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class StepDatabaseImplementation {

    private static final Logger logger = LogManager.getLogger(StepDatabaseImplementation.class);

    MethodsUtil methodsUtil;
    ConnectDatabase connectDatabase;
    ConnectDbMethods connectDbMethods;

    public StepDatabaseImplementation(){

        methodsUtil = new MethodsUtil();
        connectDatabase = new ConnectDatabase();
        connectDbMethods = new ConnectDbMethods();
    }

    @Step("Send update query with table <table>")
    public void sendUpdateQueryWithTable(Table table){

        connectDbMethods.senUpdateQueryWithTable(table);
    }

    @Step("<database> database <databaseName> database name <username> username <password> password <ip> ip <port> port connect database <connectionMapKey>")
    public void connectDb(String database, String databaseName, String username, String password, String ip, String port, String connectionMapKey){

        connectDatabase.closeDatabase(connectionMapKey);
        Connection connection = connectDatabase.connectDb(database, databaseName, username, password, ip, port);
        connectDatabase.addConnectionInMap(connectionMapKey, connection);
    }

    @Step("<database> database <databaseName> database name <username> username <password> password <ip> ip <port> port connect database <connectionMapKey> if <ifCondition>")
    public void connectDb(String database, String databaseName, String username, String password, String ip, String port, String connectionMapKey, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            connectDb(database, databaseName, username, password, ip, port, connectionMapKey);
        }
    }

    @Step("<connectionMapKey> close database connection")
    public void closeConnection(String connectionMapKey){

        connectDatabase.closeDatabase(connectionMapKey);
    }

    @Step("<mapKey> print <database> db table names <oracleTableGroupName>")
    public void printDbTableNames(String mapKey, String database, String oracleTableGroupName){

        Connection connection = (Connection) Driver.DatabaseMap.get(mapKey);
        connectDbMethods.printDataBaseTableNames(connection, database, oracleTableGroupName);
    }

    @Step("<mapKey> print <database> db table names <oracleTableGroupName> if <ifCondition>")
    public void printDbTableNames(String mapKey, String database, String oracleTableGroupName, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            printDbTableNames(mapKey, database, oracleTableGroupName);
        }
    }

    @Step("<mapKey> execute <query> query <mapKey> if <ifCondition>")
    public void sendQuery(String mapKey, String query, String mapKeySuffix, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            sendQuery(mapKey, query, mapKeySuffix,false);
        }
    }

    @Step("<connectionMapKey> execute <query> query <mapKey> log <logActive>")
    public void sendQuery(String connectionMapKey, String query, String mapKey, boolean logActive){

        connectDbMethods.sendQuery(connectionMapKey, query, mapKey, logActive);
    }

    @Step("<queryMapKey> keyindeki <index> index <columnName> column name datasını <key> keyinde tut")
    public void saveQueryValue(String queryMapKey, String index, String columnName, String key){

        index = methodsUtil.getTextByMap(index);
        Object value = null;
        if (!Driver.TestMap.containsKey(queryMapKey)){
            fail("Key bulunamadı");
        }
        HashMap<String, List<Object>> map = (HashMap<String, List<Object>>) Driver.TestMap.get(queryMapKey);
        if (map.size() == 0) {
            fail("Data boş");
        }
        value = map.get(columnName).get(Integer.parseInt(index));
        Driver.TestMap.put(key, value);
        System.out.println(value);
    }

    @Step("<queryMapKey> keyindeki tablonun size değerini <size> keyinde tut")
    public void saveQueryValue(String queryMapKey, String size){

        HashMap<String, List<Object>> map = (HashMap<String, List<Object>>) Driver.TestMap.get(queryMapKey);
        Driver.TestMap.put(size , map.size());
        System.out.println(map.size());
    }

    @Step("<connectionMapKey> execute update <query> query if <ifCondition>")
    public void executeUpdateQuery(String connectionMapKey, String query, String ifCondition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            int result = connectDbMethods.executeUpdate(connectionMapKey, query);
            System.out.println(result);
        }
    }
}
