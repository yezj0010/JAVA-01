import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

public class LogHandler implements InvocationHandler {
    Object target;  // 被代理的对象，实际的方法执行者

    public LogHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before(method, args);
        Object result = method.invoke(target, args);  // 调用 target 的 method 方法
        after(method, result);
        return result;  // 返回方法的执行结果
    }
    // 调用invoke方法之前执行
    private void before(Method method, Object[] args) {
        String argsStr = args==null?null:Arrays.asList(args)+"";
        System.out.println(String.format("方法%s准备执行，入参:%s，%s", method.getName(), argsStr, LocalDateTime.now()));
    }
    // 调用invoke方法之后执行
    private void after(Method method, Object result) {
        System.out.println(String.format("方法%s执行结束，结果:%s，%s", method.getName(), result, LocalDateTime.now()));
    }
}
