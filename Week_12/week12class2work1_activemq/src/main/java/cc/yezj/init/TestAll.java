package cc.yezj.init;

import cc.yezj.producer.MessageProducer;
import cc.yezj.vo.QueueTestVO;
import cc.yezj.vo.TopicTestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Deng jin on 2021/4/10 20:53
 */
@Component
public class TestAll implements CommandLineRunner {
    @Autowired
    private MessageProducer messageProducer;

    @Override
    public void run(String... args) throws Exception {
        for(int i=0;i<5;i++){
            QueueTestVO vo = new QueueTestVO();
            vo.setCode("code"+(i+1));
            vo.setCnt(i);
            vo.setCreateTime(LocalDateTime.now());
            messageProducer.sendMsgToQueue(vo);
        }

        for(int i=0;i<10;i++){
            TopicTestVO vo = new TopicTestVO();
            vo.setChannel("ch"+(i+1));
            vo.setRate(new BigDecimal(i));
            vo.setCreateTime(LocalDateTime.now());
            messageProducer.sendMsgToTopic(vo);
        }
    }
}
