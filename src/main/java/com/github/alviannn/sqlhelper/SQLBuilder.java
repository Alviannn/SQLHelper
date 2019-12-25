package com.github.alviannn.sqlhelper;

@SuppressWarnings({"unused"})
public class SQLBuilder {

    private String host, port, database, username, password;
    private SQLHelper.Type type;
    private boolean hikari;

    // ---------------------------- Constructor ---------------------------- //

    /**
     * constructs the SQL Builder
     *
     * @param host     the host
     * @param port     the port
     * @param database the database
     * @param username the username
     * @param password the password
     * @param hikari   true if hikari is being used, otherwise false
     */
    SQLBuilder(String host, String port, String database, String username, String password, boolean hikari, SQLHelper.Type type) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.hikari = hikari;
        this.type = type;
    }

    // ---------------------------- Setter ---------------------------- //

    /**
     * sets the host
     *
     * @param host the host
     */
    public SQLBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * sets the port
     *
     * @param port the port
     */
    public SQLBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    /**
     * sets the database
     *
     * @param database the database
     */
    public SQLBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    /**
     * sets the username
     *
     * @param username the username
     */
    public SQLBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * sets the password
     *
     * @param password the password
     */
    public SQLBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * sets the hikari
     *
     * @param hikari true if hikari is going to be used, otherwise false
     */
    public SQLBuilder setHikari(boolean hikari) {
        this.hikari = hikari;
        return this;
    }

    /**
     * sets the SQL type
     *
     * @param type the SQL type
     */
    public SQLBuilder setType(SQLHelper.Type type) {
        this.type = type;
        return this;
    }

    // ---------------------------- Getter ---------------------------- //

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the SQL type
     */
    public SQLHelper.Type getType() {
        return type;
    }

    /**
     * @return true if hikari is going to be used, otherwise false
     */
    public boolean isHikari() {
        return hikari;
    }

    // ---------------------------- Handler ---------------------------- //

    /**
     * builds the SQL instance
     *
     * @return the SQL instance
     */
    public SQLHelper toSQL() {
        return new SQLHelper(host, port, database, username, password, type, hikari);
    }

}
