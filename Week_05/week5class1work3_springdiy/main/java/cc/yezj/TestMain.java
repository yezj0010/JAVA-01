package cc.yezj;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * created by DengJin on 2021/2/20 16:19
 */
public class TestMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("my.xml");
        School school = (School) applicationContext.getBean("school");
        System.out.println(school.getKlass());
    }
}
