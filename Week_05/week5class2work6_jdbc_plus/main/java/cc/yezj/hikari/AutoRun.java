package cc.yezj.hikari;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/2/21 2:37
 */
@Component
public class AutoRun implements CommandLineRunner {
    @Autowired
    private UseHikari useHikari;

    @Override
    public void run(String... args) throws Exception {
        useHikari.test();
    }
}
