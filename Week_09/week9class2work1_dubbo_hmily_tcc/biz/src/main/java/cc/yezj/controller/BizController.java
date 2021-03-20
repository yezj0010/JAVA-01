package cc.yezj.controller;

import cc.yezj.model.RespResult;
import cc.yezj.service.BizService;
import cc.yezj.vo.SwitchAccountRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Deng jin on 2021/3/20 15:45
 */
@RestController
@Api(tags = {"分布式事务演示"})
public class BizController {
    @Autowired
    private BizService bizService;

    @PostMapping("/switch")
    public RespResult testSwitchAccount(@RequestBody SwitchAccountRequest request){
        return bizService.switchAccount(request);
    }
}
