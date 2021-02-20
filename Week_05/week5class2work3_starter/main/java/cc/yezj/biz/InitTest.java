package cc.yezj.biz;

import cc.yezj.bean.ISchool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Deng jin on 2021/2/21 0:22
 */
@Component
public class InitTest implements CommandLineRunner {
    @Autowired
    private ISchool iSchool;

    @Override
    public void run(String... args) throws Exception {
        iSchool.ding();
    }
}
