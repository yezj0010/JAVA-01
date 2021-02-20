package cc.yezj.hikari;

import cc.yezj.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Deng jin on 2021/2/21 2:38
 */
@Service
public class UseHikari {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void test(){
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

    @Transactional
    public void insert(){
        int nameSuffix = new Random().nextInt(10000000);
        int age = new Random().nextInt(100);
        String name = "张三"+nameSuffix;
        String sql = "insert into t_user (`name`,`age`) values (?,?)";
        Object[] params = new Object[] {name, age};
        int count = jdbcTemplate.update(sql, params);
        System.out.println("新增了"+count+"条数据");
    }

    @Transactional
    public void insertBatch(int count){
        String sql = "insert into t_user (`name`,`age`) values (?,?)";
        List<Object[]> list = new ArrayList();
        for(int i=0;i<count;i++){
            int nameSuffix = new Random().nextInt(10000000);
            int age = new Random().nextInt(100);
            String name = "张三"+nameSuffix;
            Object[] obj = new Object[]{name, age};
            list.add(obj);
        }
        int[] ints = jdbcTemplate.batchUpdate(sql, list);
        for(int i:ints){
            System.out.println("新增了"+i+"条数据");
        }
    }

    @Transactional
    public void delete(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        String sql = "delete from t_user where age=?";
        int delete = jdbcTemplate.update(sql, new Object[]{age});
        System.out.println("删除了"+delete+"条数据");
    }

    @Transactional
    public void update(){
        List<User> query = query();
        int index = new Random().nextInt(query.size()-1);
        int age = query.get(index).getAge();
        int nameSuffix = new Random().nextInt(10000000);
        String name = "张三"+nameSuffix;
        String sql = "update t_user set name=? where age=?";
        int update = jdbcTemplate.update(sql, new Object[]{name, age});
        System.out.println("修改了"+update+"条数据");
    }

    public List<User> query(){
        String sql = "SELECT name,age FROM t_user";
        List<User> users = jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setAge(rs.getInt("age"));
            return user;
        });
        return users;
    }
}
