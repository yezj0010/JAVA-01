package cc.yezj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Deng jin on 2021/3/6 19:23
 */
@SpringBootApplication(exclude = {JtaAutoConfiguration.class})
@MapperScan(basePackages = "cc.yezj.mapper")
@EnableTransactionManagement
public class ShardingXAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingXAApplication.class, args);
    }
}
