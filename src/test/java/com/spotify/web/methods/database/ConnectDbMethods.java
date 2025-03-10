package com.spotify.web.methods.database;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.thoughtworks.gauge.Table;
import java.sql.*;
import java.util.*;

public class ConnectDbMethods {

    MethodsUtil methodsUtil;
    ConnectDatabase connectDatabase;

    public ConnectDbMethods(){

        methodsUtil = new MethodsUtil();
        connectDatabase = new ConnectDatabase();
    }

    public HashMap<String, List<Object>> getResultListWithMap(ResultSet resultSet){

        HashMap<String, List<Object>> dataMap = new HashMap<>();
        List<String> columnNameList = new ArrayList<>();
        try {
            int columnSize = resultSet.getMetaData().getColumnCount();
            //System.out.println(columnSize);
            Object value = null;
            String columnName = "";
            int index = 0;
            for (int i = 1; i < columnSize+1; i++){
                columnNameList.add(resultSet.getMetaData().getColumnName(i));
            }
            while (resultSet.next()) {
                index++;
                for (int i = 1; i < columnSize+1; i++) {
                    value = null;
                    columnName = columnNameList.get(i-1);
                    if (resultSet.getObject(i) != null) {
                       value = getValue(resultSet, i, index);
                    }
                    setMapData(dataMap, columnName, value);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Driver.TestMap.put("queryHeader", columnNameList);
        return dataMap;
    }

    private void setMapData(HashMap<String, List<Object>> dataMap, String columnName, Object value){

        if (dataMap.containsKey(columnName)){
            dataMap.get(columnName).add(value);
        }else {
            List<Object> list = new ArrayList<>();
            list.add(value);
            dataMap.put(columnName, list);
        }
    }

    private Object getValue(ResultSet resultSet, int i, int index) throws SQLException {
        Object value = null;
        String columnTypeName = resultSet.getMetaData().getColumnTypeName(i);
        switch (columnTypeName) {
            case "decimal":
            case "DECIMAL":
                value = resultSet.getBigDecimal(i).toPlainString();
                break;
            case "VARCHAR2":
            case "CHAR":
            case "varchar":
            case "text":
                value = resultSet.getString(i);
                break;
            case "float8":
                value = resultSet.getFloat(i);
                break;
            case "numeric":
            case "NUMBER":
                value = resultSet.getObject(i);
                break;
            case "DATE":
                value = resultSet.getString(i);
                break;
            case "timestamp":
                value = resultSet.getString(i);
                break;
            case "bool":
                value = resultSet.getBoolean(i);
                break;
            case "int8":
            case "int4":
                value = resultSet.getInt(i);
                break;
            case "bigserial":
                value = resultSet.getLong(i);
                break;
            case "BLOB":
                value = resultSet.getBlob(i);
                if (value != null) {
                    value = value.toString();
                }
                break;
            case "jsonb":
                value = resultSet.getString(i);
                break;
            default:
                if (index == 1) {
                    System.out.println((i - 1) + " " + columnTypeName);
                }
                value = resultSet.getString(i);
        }
        return value;
    }

    public void printDataBaseTableNames(Connection c, String database, String oracleTables){

        try {
            DatabaseMetaData dbmd = c.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = dbmd.getTables(null, null, "%", types);

            while (tables.next()) {
                if (database.equalsIgnoreCase("oracle")) {
                    if (tables.getString(4).equalsIgnoreCase("TABLE")) {
                        String tableGroup = tables.getString(2);
                        if (oracleTables.equals("") || Arrays.asList(oracleTables.split(",")).contains(tableGroup)) {
                            System.out.println(tableGroup + " " + tables.getString(3));
                        }
                    }
                } else {
                    System.out.println(tables.getString("TABLE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String connectionMapKey, String query) {

        query = methodsUtil.setValueWithMapKey(query);
        return connectDatabase.executeUpdate(connectDatabase.createStatement(connectionMapKey), query);
    }

    public void sendQuery(String connectionMapKey, String query){

        try (Statement statement = connectDatabase.createStatement(connectionMapKey)) {
            try (ResultSet resultSet = statement.executeQuery(query)){
                while (resultSet.next()) {
                    String result = resultSet.getString("value");
                    System.out.println("result: " + result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendQuery(String connectionMapKey, String query, String mapKey, boolean logActive){

        query = methodsUtil.setValueWithMapKey(query);
        System.out.println(query);
        /** if(query.trim().length() < 6 || !query.trim().substring(0,6).equalsIgnoreCase("select")){
         Assertions.fail("Use executeUpdate method");
         return;
         } */
        HashMap<String, List<Object>> dataMap = getResultListWithMap(connectDatabase.executeQuery(connectDatabase.createStatement(connectionMapKey), query));
        List<String> list = (List<String>) Driver.TestMap.get("queryHeader");
        String header = "";
        for (int j = 0; j < list.size(); j++) {
            header = header + " |" +j+ "| " + list.get(j);
        }
        System.out.println(header);
        if (logActive && !list.isEmpty() && !dataMap.isEmpty()){
            String row = "";
            int size = dataMap.get(list.get(0)).size();
            System.out.println(size);
            for (int i =0; i < size; i++) {
                for (int j = 0; j < list.size(); j++) {
                    Object value = dataMap.get(list.get(j)).get(i);
                    row = row + " |"+j+"| " + (value instanceof String ? "\"" + value.toString() + "\"" : value);
                }
                System.out.println(row);
                row = "";
            }
        }
        if(dataMap.size() != 0) {
            System.out.println(dataMap.get(list.get(0)).size());
        }
        Driver.TestMap.put(mapKey, dataMap);
        Driver.TestMap.put("header" + mapKey, list);
        Driver.TestMap.remove("queryHeader");
    }

    public void senUpdateQueryWithTable(Table paramTable) {

        List<String> list = new ArrayList<>();
        Connection connection = null;
        List<String> queryList = paramTable.getColumnValues("query");
        List<String> databaseList = paramTable.getColumnValues("database");
        List<String> databaseNameList = paramTable.getColumnValues("databaseName");
        List<String> userList = paramTable.getColumnValues("user");
        List<String> passwordList = paramTable.getColumnValues("password");
        List<String> ipList = paramTable.getColumnValues("ip");
        List<String> portList = paramTable.getColumnValues("port");
        List<String> environmentList = paramTable.getColumnValues("environment");

        for (int i = 0; i < queryList.size(); i++) {
            String database = databaseList.get(i);
            String databaseName = databaseNameList.get(i);
            String environment = methodsUtil.setValueWithMapKey(environmentList.get(i));
            String user = userList.get(i);
            String password = passwordList.get(i);
            String ip = ipList.get(i);
            String port = portList.get(i);
            String key = database + databaseName + environment + user;
            String query = queryList.get(i);
            query = methodsUtil.setValueWithMapKey(query);
            query = methodsUtil.setValueWithMapKey(query);
            if (!Driver.DatabaseMap.containsKey(key)) {
                connectDatabase.addConnectionList(key);
                connection = connectDatabase.connectDb(database, databaseName, user, password, ip
                        , port);
                Driver.DatabaseMap.put(key, connection);
                list.add(key);
            }
            connection = (Connection) Driver.DatabaseMap.get(key);
            String logText = "updateResult: " + databaseName + " " + environment + " " + user + " " + ip + " " + port + " " + query + " ";
            if (connection != null) {
                int updateResult = connectDatabase.executeUpdate(connectDatabase.createStatement(key), query);
                logText = logText + " sql istegi atildi. Result: " + updateResult;
            }else {
                logText = logText + " sql istegi connection null olduğu için atilamadi.";
            }
            System.out.println(logText);
        }
        for (String key: list){
            connectDatabase.closeDatabase(key);
        }
    }

}
