package cc.yezj.jdbc;

import cc.yezj.bean.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deng jin on 2021/2/21 1:09
 * 使用Statement
 */
public class UseJDBCWithStatement {

    public static void main(String[] args) {
        insert();
        delete();
        update();
        List<User> users = query();
        if(users!=null){
            System.out.println("查询到了"+users.size()+"条记录");
            users.forEach(i -> System.out.println(i));
        }

    }

    public static void insert(){
        int nameSuffix = new Random().nextInt(10000000);
        int age = new Random().nextInt(100);
        String sql = "insert into t_user values('张三"+nameSuffix+"',"+age+")";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            int i = statement.executeUpdate(sql);
            System.out.println("新增了"+i+"条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void delete(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        String sql = "delete from t_user where age="+age;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            int i = statement.executeUpdate(sql);
            System.out.println("删除了"+i+"条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void update(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        int nameSuffix = new Random().nextInt(10000000);
        String sql = "update t_user set name='张三"+nameSuffix+"' where age="+age;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            int i = statement.executeUpdate(sql);
            System.out.println("修改了"+i+"条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static List<User> query(){
        String sql = "SELECT * FROM t_user";
        Connection connection = null;
        List<User> users = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet != null){
                users = new ArrayList<>();
                User user = null;
                while (resultSet.next()){
                    user = new User(resultSet.getString("name"),resultSet.getInt("age"));
                    users.add(user);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(resultSet,statement,connection);
        }
        return users;
    }
}
