/*
 * SQLite JDBC Connection
 *
 */

package utils.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class SQLiteConn {
    private Connection connection;

    public SQLiteConn(String filename) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an INSERT, UPDATE or DELETE sql statement.
     *
     * @param sql the sql statement
     * @return either the row number or 0 for statements that
     *         do not return anything
     */
    public int executeUpdate(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            return stmt.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void execute(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a given sql statement.
     *
     * @param sql the sql statement
     * @return ResultSet with the results
     */
    public ResultSet executeQuery(String sql) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
