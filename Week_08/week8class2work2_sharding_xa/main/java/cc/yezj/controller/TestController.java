package cc.yezj.controller;

import cc.yezj.service.WriteAndReadService;
import cc.yezj.vo.OrderRequestVO;
import cc.yezj.vo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * created by DengJin on 2021/3/12 13:35
 */
@Api(tags = {"分库分表XA事务演示"})
@RestController
public class TestController {
    @Autowired
    private WriteAndReadService writeAndReadService;

    /**
     * 初始化3条数据
     * @return
     */
    @ApiOperation(value = "增加3条数据，参数传true会抛出异常，可测试是否回滚")
    @PostMapping("/insertData")
    public Response init(@Param("isThrowException") @RequestParam String isThrowException){
        List results = writeAndReadService.insertData(isThrowException);
        return Response.ok(results);
    }

    /**
     * 根据用户id查找所有的订单
     * @return
     */
    @ApiOperation(value = "查看所有订单")
    @PostMapping("/listAll")
    public Response list(){
        Map all = writeAndReadService.findAll();
        return Response.ok(all);
    }

}
