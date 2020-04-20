package com.github.alviannn.sqlhelper;

import com.github.alviannn.sqlhelper.utils.Closer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SQLHelper {

    @Getter private final String host, port, database, username, password;
    /**
     * @return the SQLHelper type
     */
    @Getter private final Type type;

    /**
     * checks if the HikariCP is being used
     */
    @Getter private final boolean hikari;

    /**
     * @return the hikari data source! (could be 'null' if hikari isn't being used)
     */
    @Nullable @Getter private HikariDataSource dataSource;
    private Connection connection;

    // ---------------------------- Constructor ---------------------------- //

    /**
     * constructs the SQL instance
     *
     * @param host     the host
     * @param port     the port
     * @param database the database
     * @param username the username
     * @param password the password
     * @param type     the SQL type
     * @param hikari   true if hikari is being used, otherwise false
     */
    SQLHelper(String host, String port, String database, String username, String password, Type type, boolean hikari) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.type = type;
        this.hikari = hikari;
    }

    public static SQLBuilder newBuilder(Type type) {
        return new SQLBuilder("", "", "", "", "", false, type);
    }

    /**
     * the SQL types
     */
    public enum Type {
        // MYSQL("jdbc:mysql://%s:%s%s?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC", "com.mysql.cj.jdbc.Driver"),
        MYSQL("jdbc:mysql://%s:%s%s", "com.mysql.cj.jdbc.Driver"),
        SQLITE("jdbc:sqlite:%s", "org.sqlite.JDBC"),
        H2("jdbc:h2:./%s", "org.h2.Driver"),
        CUSTOM("", "");

        @Getter @Setter private String jdbcUrl;
        @Getter @Setter private String classPath;

        Type(String jdbcUrl, String classPath) {
            this.jdbcUrl = jdbcUrl;
            this.classPath = classPath;
        }
    }

    /**
     * @return the SQL connection
     */
    public Connection getConnection() throws SQLException {
        if (hikari) {
            if (dataSource == null)
                return null;

            return dataSource.getConnection();
        }

        return connection;
    }

    /**
     * checks SQL connection
     *
     * @return true if the SQL is still connected
     */
    public boolean isConnected() {
        boolean result = false;

        try (Closer closer = new Closer()) {
            if (hikari && dataSource != null && !dataSource.isClosed()) {
                Connection conn = closer.add(dataSource.getConnection());
                result = conn != null && !conn.isClosed() && conn.isValid(1);
            }
            else if (!hikari) {
                result = connection != null && !connection.isClosed() && connection.isValid(1);
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    /**
     * insert a sql query string for later use
     * <p>
     * with this the '?' will work
     *
     * @param sql the sql query
     * @return the query
     */
    public Query query(String sql) {
        return new Query(sql, this);
    }

    /**
     * executes SQL query using PreparedStatement
     *
     * @param sql the SQL query
     * @throws SQLException if the query failed to be executed
     */
    public void executeQuery(String sql) throws SQLException {
        this.query(sql).execute();
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @param sql the SQL query
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results getResults(String sql) throws SQLException {
        return this.query(sql).getResults();
    }

    /**
     * connects the SQL
     *
     * @throws SQLException if the SQL failed to connect
     */
    public void connect() throws SQLException {
        String url = this.formatUrl(host, port, database);
        if (url == null)
            throw new NullPointerException("Failed to format the JDBC URL!");

        try {
            Class.forName(type.classPath);
        } catch (Exception ignored) {
        }

        if (hikari) {
            HikariConfig config = new HikariConfig();

            config.setDriverClassName(type.classPath);
            config.setJdbcUrl(url);
            config.setMaximumPoolSize(20);

            config.setUsername(username);
            config.setPassword(password);

            // recommended config
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            dataSource = new HikariDataSource(config);
        }
        else {
            if (type == Type.MYSQL) {
                Properties config = new Properties();

                config.setProperty("user", username);
                config.setProperty("password", password);

                // recommended config
                config.setProperty("cachePrepStmts", "true");
                config.setProperty("prepStmtCacheSize", "250");
                config.setProperty("prepStmtCacheSqlLimit", "2048");
                config.setProperty("useServerPrepStmts", "true");

                connection = DriverManager.getConnection(url, config);
            }
            else {
                connection = DriverManager.getConnection(url, username, password);
            }
        }
    }

    /**
     * connects the SQL
     *
     * @param config the connection config
     * @throws SQLException if the SQL failed to connect
     */
    public void connect(Properties config) throws SQLException {
        String url = this.formatUrl(host, port, database);
        if (url == null)
            throw new NullPointerException("Failed to format the JDBC URL!");

        try {
            Class.forName(type.classPath);
        } catch (Exception ignored) {
        }

        if (hikari) {
            HikariConfig sqlConfig = new HikariConfig();

            sqlConfig.setDriverClassName(type.classPath);
            sqlConfig.setJdbcUrl(url);
            // sqlConfig.setMaximumPoolSize(20);

            sqlConfig.setUsername(username);
            sqlConfig.setPassword(password);

            for (Map.Entry<Object, Object> entry : config.entrySet()) {
                sqlConfig.addDataSourceProperty(entry.getKey().toString(), entry.getValue().toString());
            }

            dataSource = new HikariDataSource(sqlConfig);
        }
        else {
            if (type == Type.MYSQL) {
                Properties sqlConfig = new Properties();

                sqlConfig.setProperty("user", username);
                sqlConfig.setProperty("password", password);

                for (Map.Entry<Object, Object> entry : config.entrySet())
                    sqlConfig.setProperty(entry.getKey().toString(), entry.getValue().toString());

                connection = DriverManager.getConnection(url, sqlConfig);
            }
            else {
                connection = DriverManager.getConnection(url, username, password);
            }
        }
    }

    /**
     * disconnects the SQL
     *
     * @throws SQLException if the SQL failed to disconnect
     */
    public void disconnect() throws SQLException {
        if (hikari)
            if (dataSource != null)
                dataSource.close();
        else
            connection.close();

        dataSource = null;
        connection = null;
    }

    /**
     * handles JDBC URL formatting
     *
     * @param host     the host
     * @param port     the port
     * @param database the database
     * @return the formatted JDBC URL
     */
    private String formatUrl(String host, String port, String database) {
        switch (type) {
            case MYSQL: {
                if (database != null && !database.isEmpty() && !database.startsWith("/"))
                    database = "/" + database;

                return String.format(type.jdbcUrl, host, port, database);
            }
            case SQLITE:
            case H2:
                return String.format(type.jdbcUrl, database);
            default:
                return null;
        }
    }

}
