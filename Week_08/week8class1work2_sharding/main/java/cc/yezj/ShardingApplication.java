package cc.yezj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

/**
 * Created by Deng jin on 2021/3/6 19:23
 */
@SpringBootApplication(exclude = {JtaAutoConfiguration.class})
//@EnableJpaRepositories(basePackages = "cc.yezj.rep")
//@EntityScan(basePackages = "cc.yezj.entity")
public class ShardingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }
}
