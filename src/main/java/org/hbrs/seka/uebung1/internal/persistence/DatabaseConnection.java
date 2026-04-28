package org.hbrs.seka.uebung1.internal.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

        private static final String URL =
                "jdbc:h2:~/test";
//                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        private static final String USER = "sa";
        private static final String PASSWORD = "";

        private static Connection connection;

        public static void openSession() throws SQLException {
                if (connection == null || connection.isClosed()) {
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                }
        }

        public static Connection getConnection() throws SQLException {
                if (connection == null || connection.isClosed()) {
                        openSession();
                }
                return connection;
        }

        public static void closeSession() {
                if (connection != null) {
                        try {
                                connection.close();
                        } catch (SQLException e) {
                            //noinspection CallToPrintStackTrace
                            e.printStackTrace();
                        } finally {
                                connection = null;
                        }
                }
        }
}