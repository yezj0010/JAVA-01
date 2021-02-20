package cc.yezj.starter;

import cc.yezj.bean.School;
import cc.yezj.bean.Student;
import cc.yezj.bean.Klass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring boot starter configuration.
 */
@Configuration
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@ConditionalOnProperty(prefix = "spring.school", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
public class AutoConfigureDemo {
    @Autowired
    private final SpringBootPropertiesConfiguration props;

    @Bean
    public School getSchool(){
        String students = props.getStudents();
        String[] sss = students.split(",");
        List<Student> list = new ArrayList<>();
        for(String single:sss){
            String[] split = single.split("-");
            Student student = new Student();
            student.setName(split[0]);
            student.setAge(Integer.parseInt(split[1]));
            list.add(student);
        }
        Klass klass = new Klass();
        klass.setStudents(list);
        School school = new School();
        school.setKlass(klass);
        return school;
    }



}