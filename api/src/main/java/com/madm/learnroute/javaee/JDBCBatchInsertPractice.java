package com.madm.learnroute.javaee;

import cn.hutool.core.util.RandomUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * jdbc插入mysql数据库表100万条数据 开事务和不事务 22秒左右
 */

/**
 * truncate department;
 *
 * SET GLOBAL log_bin_trust_function_creators=TRUE; -- 创建函数一定要写这个
 * DELIMITER $$   -- 写函数之前必须要写，该标志
 *
 * CREATE FUNCTION mock_data()		-- 创建函数（方法）
 * RETURNS INT 						-- 返回类型
 * BEGIN								-- 函数方法体开始
 * 	DECLARE num INT DEFAULT 1000000; 		-- 定义一个变量num为int类型。默认值为100 0000
 * 	DECLARE i INT DEFAULT 0;
 *
 * 	WHILE i < num DO 				-- 循环条件
 * 		 INSERT INTO department(depno,depname,memo)
 * 		 VALUES(i,concat('depname',i),concat('memo',i));
 * 		SET i =  i + 1;	-- i自增
 * 	END WHILE;		-- 循环结束
 * 	RETURN i;
 * END;
 *
 * # drop function mock_data;
 *
 * select mock_data();
 */
public class JDBCBatchInsertPractice {
    private long begin = 1;//起始id
    private long end = begin + 100000;//每次循环插入的数据量
    private String url = "";
    private String user = "";
    private String password = "";
    private String tableName = "";


    public void insertBigData() {
        try {
            Connection conn = getConnection();
            //编写sql
            String sql = "INSERT INTO " + tableName + " VALUES (?,?,?,?,?,?,?)";
            //预编译sql
            PreparedStatement pstm = conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次十万数据，一共1000万
            for (int i = 0; i < 10; i++) {
                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                //开始循环
                while (begin < end) {
                    //赋值
                    pstm.setLong(1, begin);
                    pstm.setString(2, RandomValue.randomStr());
                    pstm.setString(3, RandomValue.randomStr());
                    pstm.setInt(4, RandomValue.randomInt());
                    pstm.setString(5, RandomValue.randomStr());
                    pstm.setString(6, RandomValue.randomStr());
                    pstm.setString(7, RandomValue.randomStr());
                    //添加到同一个批处理中
                    pstm.addBatch();
                    begin++;
                }
                //执行批处理
                pstm.executeBatch();
                //提交事务
                conn.commit();
                //边界值自增10W
                end += 100000;
                //关闭分段计时
                long eTime = System.currentTimeMillis();
                //输出
                System.out.println("成功插入10W条数据耗时：" + (eTime - bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入100W数据共耗时：" + (eTime1 - bTime1) / 1000 + "秒");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTableData(String tableName) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstm = conn.prepareStatement("truncate " + tableName);
            pstm.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection conn = null;
        //加载jdbc驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        JDBCBatchInsertPractice batchInsert = new JDBCBatchInsertPractice();
        batchInsert.clearTableData("person");
    }

}

class RandomValue {
    public static String randomStr() {
        return RandomUtil.randomNumbers(3);
    }

    public static Integer randomInt() {
        return RandomUtil.randomInt(0, 100);
    }
}
