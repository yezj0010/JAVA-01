package cc.yezj.jdbc;

import cc.yezj.bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Deng jin on 2021/2/21 1:09
 * 使用PrepareStatement
 */
public class UseJDBCWithPrepareStatement {

    public static void main(String[] args) {
        insert();
        insertBatch(5);
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
        String name = "张三"+nameSuffix;
        String sql = "insert into t_user (`name`,`age`) values (?,?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, age);
            connection.setAutoCommit(false);//设置为不自动提交
            boolean execute = statement.execute();
            connection.commit();
            System.out.println("是否有返回值："+execute);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try{
                connection.rollback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void insertBatch(int count){
        String sql = "insert into t_user (`name`,`age`) values (?,?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            for(int i=0;i<count;i++){
                int nameSuffix = new Random().nextInt(10000000);
                int age = new Random().nextInt(100);
                String name = "张三"+nameSuffix;
                statement.setString(1, name);
                statement.setInt(2, age);
                statement.addBatch();
            }
            connection.setAutoCommit(false);//设置为不自动提交
            int[] ints = statement.executeBatch();
            connection.commit();
            for(int i : ints) {
                System.out.println("是否有返回值：" + i);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try{
                connection.rollback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void delete(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        String sql = "delete from t_user where age=?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setInt(1, age);
            boolean execute = statement.execute();
            connection.commit();
            System.out.println("是否有返回值"+execute);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            try{
                connection.rollback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void update(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        int nameSuffix = new Random().nextInt(10000000);
        String name = "张三"+nameSuffix;
        String sql = "update t_user set name=? where age=?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, age);
            int i = statement.executeUpdate();
            connection.commit();
            System.out.println("修改了"+i+"条记录");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try{
                connection.rollback();
            }catch (Exception e){
                e.printStackTrace();
            }
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static List<User> query(){
        String sql = "SELECT * FROM t_user";
        Connection connection = null;
        List<User> users = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
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
