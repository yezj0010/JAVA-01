package cc.yezj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Deng jin on 2021/3/20 15:13
 */
@SpringBootApplication
@MapperScan("cc.yezj.mapper")
@ImportResource({"classpath:spring-dubbo.xml"})
public class AccountUsdApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountUsdApplication.class, args);
    }
}
