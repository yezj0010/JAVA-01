package cc.yezj.service;

import cc.yezj.model.RespResult;
import cc.yezj.vo.SwitchAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Created by Deng jin on 2021/3/20 15:32
 */
@Service("accountUsdService")
@Slf4j
public class AccountUsdService implements IAccountUsdService {

    /**
     * 找到账户类型为USD的用户
     * 将该用户资金账户扣减指定金额，
     * 在冻结记录中新增指定金额的冻结记录
     * @param request
     * @return
     */
    @Transactional
    @Override
    public RespResult switchAccountPre(SwitchAccountRequest request) {
        log.info("usd pre");
        if(new Random().nextInt(5)==1){
            throw new RuntimeException("随机异常");
        }
        return null;
    }

    /**
     * 找到账户类型是USD的用户，
     * 找到该用户冻结资金表记录，以及找到对应的金额
     * 然后将指定金额转移到对手用户id中，
     * 冻结记录失效
     * @param request
     * @return
     */
    @Transactional
    @Override
    public RespResult switchAccountConfirm(SwitchAccountRequest request) {
        log.info("usd confirm");
        return null;
    }

    /**
     * 找到账户类型是USD的用户
     * 找到该用户冻结资金记录，以及对应的金额
     * 将该金额加回该用户资金账户中，
     * 冻结记录失效
     * @param request
     * @return
     */
    @Transactional
    @Override
    public RespResult switchAccountCancel(SwitchAccountRequest request) {
        log.info("usd cancel");
        return null;
    }
}
