package cc.yezj.service;

import cc.yezj.model.RespResult;
import cc.yezj.vo.SwitchAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Deng jin on 2021/3/20 15:19
 */
@Service
@Slf4j
public class BizService implements IBizService{
    @Autowired
    private IAccountCnyService cnyService;
    @Autowired
    private IAccountUsdService usdService;


    @HmilyTCC(confirmMethod = "confirmSwitchAccount", cancelMethod = "cancelSwitchAccount")
    @Override
    public RespResult switchAccount(SwitchAccountRequest request){
        log.info("biz预备");
        //先将sourceUserId用户人民币账户扣减sourceAmt，然后冻结账户添加金额为sourceAmt，货币类型为sourceType相应记录
        cnyService.switchAccountPre(request);
        //先将targetUserId用户人民币账户扣减targetAmt，然后冻结账户添加金额为sourceAmt，货币类型为sourceType相应记录
        usdService.switchAccountPre(request);
        return RespResult.ok();
    }

    public void confirmSwitchAccount(SwitchAccountRequest request){
        log.info("biz确认");
        cnyService.switchAccountConfirm(request);
        usdService.switchAccountConfirm(request);
    }

    public void cancelSwitchAccount(SwitchAccountRequest request){
        log.info("biz取消");
        cnyService.switchAccountCancel(request);
        usdService.switchAccountCancel(request);
    }


}
