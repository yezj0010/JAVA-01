package cc.yezj.init;

import cc.yezj.entity.TbOrder;
import cc.yezj.service.WriteAndReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Deng jin on 2021/3/7 1:48
 */
@Component
@Slf4j
public class Test implements CommandLineRunner {
    @Autowired
    private WriteAndReadService writeAndReadService;
    @Override
    public void run(String... args) throws Exception {
        //新增前查询
        List<TbOrder> all1 = writeAndReadService.findAll();
        log.info("新增前查询all1=");
        all1.forEach(i -> log.info("{}",i));
        //新增
        TbOrder order = TbOrder.builder().id(System.currentTimeMillis()+"").state(0).userId("4").
                createTime(System.currentTimeMillis()).totalAmount(BigDecimal.TEN).build();
        writeAndReadService.save(order);
        //新增后查询
        List all2 = writeAndReadService.findAll();
        log.info("新增后查询all2=");
        all2.forEach(i -> log.info("{}",i));
    }
}
