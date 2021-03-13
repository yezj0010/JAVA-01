package cc.yezj.exception;

import cc.yezj.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by DengJin on 2021/3/12 13:38
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response processThrowable(Throwable e){
        log.error("系统开小差", e);
        return Response.fail(e.getMessage());
    }
}
