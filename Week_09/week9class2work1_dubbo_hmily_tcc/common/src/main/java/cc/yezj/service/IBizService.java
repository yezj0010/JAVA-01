package cc.yezj.service;

import cc.yezj.model.RespResult;
import cc.yezj.vo.SwitchAccountRequest;

/**
 * Created by Deng jin on 2021/3/21 0:31
 */
public interface IBizService {

    RespResult switchAccount(SwitchAccountRequest request);
//    void confirmSwitchAccount(SwitchAccountRequest request);
//    void cancelSwitchAccount(SwitchAccountRequest request);

}
