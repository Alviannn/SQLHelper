package com.github.alviannn.sqlhelper;

import com.github.alviannn.sqlhelper.utils.Closer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * query handler
 */
public final class Query {

    private final String sqlQuery;
    private final SQLHelper helper;

    /**
     * constructor
     *
     * @param sqlQuery the sql query
     */
    Query(String sqlQuery, SQLHelper helper) {
        this.sqlQuery = sqlQuery;
        this.helper = helper;
    }

    /**
     * executes the query
     *
     * @param params the parameter values
     * @throws SQLException if the query failed to be executed
     */
    public void execute(Object... params) throws SQLException {
        Connection connection = helper.getConnection();

        if (helper.isHikari()) {
            try (Closer closer = new Closer()) {
                Connection conn = closer.add(connection);
                PreparedStatement statement = closer.add(conn.prepareStatement(sqlQuery));

                for (int i = 0; i < params.length; i++)
                    statement.setObject(i + 1, params[i]);

                statement.execute();
            }
        }
        else {
            try (Closer closer = new Closer()) {
                PreparedStatement statement = closer.add(connection.prepareStatement(sqlQuery));

                for (int i = 0; i < params.length; i++)
                    statement.setObject(i + 1, params[i]);

                statement.execute();
            }
        }
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @param params the parameter values
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results getResults(Object... params) throws SQLException {
        Connection connection = helper.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        for (int i = 0; i < params.length; i++)
            statement.setObject(i + 1, params[i]);

        ResultSet set = statement.executeQuery();
        return new Results(connection, statement, set, helper.isHikari());
    }

}
