package com.madm.learnroute.javaee;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

/**
 * @author dongming.ma
 * @date 2023/9/4 10:31
 */
@Component
public class BatchInsertPractice {
    /**
     * JDBC分批次批量插入
     *
     * @throws IOException
     */
    public void testJDBCBatchInsertUser() throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String databaseURL = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";

        try {
            connection = DriverManager.getConnection(databaseURL, user, password);
            // 关闭自动提交事务，改为手动提交
            connection.setAutoCommit(false);
            System.out.println("===== 开始插入数据 =====");
            long startTime = System.currentTimeMillis();
            String sqlInsert = "INSERT INTO t_user ( username, age) VALUES ( ?, ?)";
            preparedStatement = connection.prepareStatement(sqlInsert);

            Random random = new Random();
            for (int i = 1; i <= 300000; i++) {
                preparedStatement.setString(1, "共饮一杯无 " + i);
                preparedStatement.setInt(2, random.nextInt(100));
                // 添加到批处理中
                preparedStatement.addBatch();

                if (i % 1000 == 0) {
                    // 每1000条数据提交一次
                    preparedStatement.executeBatch();
                    connection.commit();
                    System.out.println("成功插入第 " + i + " 条数据");
                }

            }
            // 处理剩余的数据
            preparedStatement.executeBatch();
            connection.commit();
            long spendTime = System.currentTimeMillis() - startTime;
            System.out.println("成功插入 30 万条数据,耗时：" + spendTime + "毫秒");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
