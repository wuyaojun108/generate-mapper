package org.wyj.utils;

import org.wyj.config.GlobalConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionUtil {
    private static final BlockingQueue<Connection> queue = new ArrayBlockingQueue<>(3);

    public static void createConnection(GlobalConfig config) throws Exception {
        if (config.getDataSource() == null) {
            Connection conn;
            try {
                Class.forName(config.getJdbcDriver());
                conn = DriverManager.getConnection(config.getJdbcUrl(), config.getJdbcUsername(),
                        config.getJdbcPassword());
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException("创建数据库连接失败：" + e.getMessage());
            }
            queue.put(conn);
        } else {
            queue.put(config.getDataSource().getConnection());
        }
    }

    public static Connection getConnection() {
        return queue.poll();
    }

    public static void closeConnection(Connection connection) throws InterruptedException {
        queue.put(connection);
    }

    public static void cancelConnection() {
        try {
            for (Connection connection : queue) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
