package com.github.alviannn.sqlhelper;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("unused")
@Getter
public class Results implements AutoCloseable {

    private final Connection connection;
    private final PreparedStatement statement;
    private final ResultSet resultSet;
    private final boolean hikari;

    /**
     * constructs the SQL results
     *
     * @param connection the connection
     * @param statement  the (prepared) statement
     * @param resultSet  the result set
     * @param hikari     true if hikari is being used, otherwise false
     */
    Results(Connection connection, PreparedStatement statement, ResultSet resultSet, boolean hikari) {
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
        this.hikari = hikari;
    }

    /**
     * handles the SQL results resource closing
     */
    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (Exception ignored) {
        }

        try {
            statement.close();
        } catch (Exception ignored) {
        }

        if (hikari) {
            try {
                connection.close();
            } catch (Exception ignored) {
            }
        }
    }

}
