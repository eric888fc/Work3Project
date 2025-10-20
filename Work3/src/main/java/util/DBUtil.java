package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/work3?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Taipei";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ 資料庫連線成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
