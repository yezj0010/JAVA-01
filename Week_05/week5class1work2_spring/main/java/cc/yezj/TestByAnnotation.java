package cc.yezj;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Deng jin on 2021/2/19 0:05
 */
public class TestByAnnotation {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext2.xml");
        School school1 = (School)applicationContext.getBean("school1");
        school1.detail();
    }
}
