package cc.yezj.jdbc;

/**
 * Created by Deng jin on 2021/2/21 1:14
 */
import java.sql.*;

public class JDBCUtils {
    //定义MySQL的数据库驱动程序
    public static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";//com.mysql.jdbc.Driver  本项目使用的是mysql8,如果是8以下使用注释的驱动
    //定义MySQL数据库的连接地址
    public static final String DBURL = "jdbc:mysql://localhost:3306/shop?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true";
    //MySQL数据库的连接用户名
    public static final String DBUSER = "root";
    //MySQL数据库的连接密码
    public static final String DBPASS = "123456";

    public static Connection getConnection(){
        Connection con = null;
        try {
            //加载驱动程序
            Class.forName(DBDRIVER);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //连接MySQL数据库时，要写上连接的用户名和密码
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            return con;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection con) {
        close(null, null, con);
    }

    public static void close(ResultSet resultSet, Statement statement, Connection con) {
        try {
            //关闭数据库
            if(resultSet!=null)
            resultSet.close();
            if(statement!=null)
            statement.close();
            if(con!=null)
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
