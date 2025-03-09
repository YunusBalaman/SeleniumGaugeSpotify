package com.spotify.web.methods.database;

public enum DatabaseEnum {

    POSTGRESQL("org.postgresql.Driver","jdbc:postgresql://"),
    ORACLE("oracle.jdbc.OracleDriver","jdbc:oracle:thin:@"),
    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://"),
    MONGODB("",""),
    MYSQL("","");

    private final String databaseClassName;
    private final String jdbc;

    DatabaseEnum(String databaseClassName, String jdbc){

        this.databaseClassName = databaseClassName;
        this.jdbc = jdbc;
    }

    public String getDatabaseClassName(){
        return databaseClassName;
    }

    public String getJdbc(){
        return jdbc;
    }

}
