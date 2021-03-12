package cc.yezj.controller;

import cc.yezj.service.WriteAndReadService;
import cc.yezj.vo.OrderRequestVO;
import cc.yezj.vo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * created by DengJin on 2021/3/12 13:35
 */
@Api(tags = {"分库分表增删改查演示"})
@RestController
public class TestController {
    @Autowired
    private WriteAndReadService writeAndReadService;

    /**
     * 初始化1万条数据
     * @return
     */
    @ApiOperation(value = "【增】初始化1万条数据")
    @PostMapping("/init1wData")
    public Response init(){
        long count = writeAndReadService.count();
        if(count>0){
            throw new RuntimeException("已经初始化");
        }
        writeAndReadService.init1wData();
        return Response.ok();
    }

    /**
     * 根据用户id查找所有的订单
     * @param requestVO
     * @return
     */
    @ApiOperation(value = "【查】根据用户id查找所有的订单")
    @PostMapping("/listByUserId")
    public Response list(@RequestBody OrderRequestVO requestVO){
        Assert.notNull(requestVO.getUserId(), "userId不能为空");
        Map all = writeAndReadService.findAll(requestVO);
        return Response.ok(all);
    }

    /**
     * 根据订单id,用户id修改订单状态
     * @param requestVO
     * @return
     */
    @ApiOperation(value = "【改】根据订单id,用户id修改订单状态")
    @PostMapping("/updateState")
    public Response updateStatus(@RequestBody OrderRequestVO requestVO){
        Assert.notNull(requestVO.getOrderId(), "orderId不能为空");
        Assert.notNull(requestVO.getUserId(), "userId不能为空");
        Assert.notNull(requestVO.getState(), "state不能为空");
        writeAndReadService.updateState(requestVO);
        return Response.ok();
    }

    /**
     * 根据订单id,用户id删除订单
     * @param requestVO
     * @return
     */
    @ApiOperation(value = "【删】根据订单id,用户id删除订单")
    @PostMapping("/deleteOrder")
    public Response deleteOrder(@RequestBody OrderRequestVO requestVO){
        Assert.notNull(requestVO.getOrderId(), "orderId不能为空");
        Assert.notNull(requestVO.getUserId(), "userId不能为空");
        writeAndReadService.deleteOrder(requestVO);
        return Response.ok();
    }
}
