package cc.yezj.model;

import lombok.Data;

/**
 * Created by Deng jin on 2021/3/20 15:20
 */
@Data
public class RespResult {
    private String code;
    private String mag;
    private Object data;

    public static RespResult ok(){
        RespResult respResult = new RespResult();
        respResult.setCode("200");
        respResult.setMag("ok");
        return respResult;
    }

    public static RespResult ok(Object data){
        RespResult respResult = new RespResult();
        respResult.setCode("200");
        respResult.setMag("ok");
        respResult.setData(data);
        return respResult;
    }

    public static RespResult fail(String msg){
        RespResult respResult = new RespResult();
        respResult.setCode("500");
        respResult.setMag(msg);
        return respResult;
    }
}
