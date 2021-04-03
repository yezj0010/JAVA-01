package cc.yezj.inti;

import cc.yezj.component.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Deng jin on 2021/4/3 15:01
 */
@Component
@Slf4j
public class Test implements CommandLineRunner {
    @Autowired
    private Counter counter;

    @Override
    public void run(String... args) throws Exception {
        //初始化库存
        int stockAll = 1000;
        log.info("初始化库存数量，{}", stockAll);
        Thread.sleep(1000);
        AtomicInteger cnt = new AtomicInteger(0);//库存减少成功的次数,用来校验库存减少次数是否正确
        counter.initStock(stockAll);

        //创建多个线程减少库存
        int threadCnt = 10;
        log.info("创建{}个线程，同时减少库存", threadCnt);
        Thread.sleep(1000);
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);
        for(int i=0;i<threadCnt;i++){
            new Thread(() -> {
                try{
                    while (true){
                        boolean b = counter.decreaseStock();
                        if(!b){//返回false，表示库存已经为0，无法再减少
                            break;
                        }
                        cnt.getAndIncrement();//减少成功一次，+1
                    }
                }finally {
                    countDownLatch.countDown();
                }
            }).start();
        }

        //执行结束
        countDownLatch.await();
        if(stockAll==cnt.get()){
            log.info("分布式计数器测试成功,usedTime={}ms", System.currentTimeMillis()-start);
        }else{
            log.info("分布式计数器测试失败,usedTime={}ms", System.currentTimeMillis()-start);
        }
    }
}
