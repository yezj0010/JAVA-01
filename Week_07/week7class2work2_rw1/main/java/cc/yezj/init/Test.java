package cc.yezj.init;

import cc.yezj.service.WriteAndReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Deng jin on 2021/3/6 20:22
 */
@Component
@Slf4j
public class Test implements CommandLineRunner {
    @Autowired
    private WriteAndReadService writeAndReadService;


    @Override
    public void run(String... args) throws Exception {
        List inRead = writeAndReadService.findInRead();
        log.info("读库中数据：{}", inRead);
        List inWrite = writeAndReadService.findInWrite();
        log.info("写库中数据：{}", inWrite);
    }
}
