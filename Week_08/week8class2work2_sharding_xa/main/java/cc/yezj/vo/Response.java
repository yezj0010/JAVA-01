package cc.yezj.vo;

import lombok.Data;

/**
 * created by DengJin on 2021/3/12 13:36
 */
@Data
public class Response {
    String code;
    String msg;
    Object data;

    public static Response ok(){
        Response response = new Response();
        response.setCode("200");
        response.setMsg("ok");
        return response;
    }

    public static Response ok(Object data){
        Response response = new Response();
        response.setCode("200");
        response.setMsg("ok");
        response.setData(data);
        return response;
    }

    public static Response fail(String msg){
        Response response = new Response();
        response.setCode("500");
        response.setMsg(msg);
        return response;
    }
}
