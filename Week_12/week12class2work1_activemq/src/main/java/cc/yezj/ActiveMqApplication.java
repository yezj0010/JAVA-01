package cc.yezj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by Deng jin on 2021/4/10 20:38
 */
@SpringBootApplication
@EnableJms //ActiveMQ
public class ActiveMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActiveMqApplication.class, args);
    }
}
