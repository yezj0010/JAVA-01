package cc.yezj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Deng jin on 2021/3/6 19:23
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "cc.yezj.rep")
public class Rw1Application {
    public static void main(String[] args) {
        SpringApplication.run(Rw1Application.class, args);
    }
}
