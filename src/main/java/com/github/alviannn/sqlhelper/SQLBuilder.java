package com.github.alviannn.sqlhelper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings({"unused"})
@Getter
@AllArgsConstructor
public class SQLBuilder {

    private String host, port, database, username, password;
    private boolean hikari;
    private SQLHelper.Type type;

    /**
     * sets the host
     */
    public SQLBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * sets the port
     */
    public SQLBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    /**
     * sets the database
     */
    public SQLBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    /**
     * sets the username
     */
    public SQLBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * sets the password
     */
    public SQLBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * sets the hikari
     *
     * @param hikari {@code true} if hikari is going to be used, otherwise {@code false}
     */
    public SQLBuilder setHikari(boolean hikari) {
        this.hikari = hikari;
        return this;
    }

    /**
     * sets the SQL type
     */
    public SQLBuilder setType(SQLHelper.Type type) {
        this.type = type;
        return this;
    }

    /**
     * builds the SQL instance
     */
    public SQLHelper toSQL() {
        return new SQLHelper(host, port, database, username, password, type, hikari);
    }

}
