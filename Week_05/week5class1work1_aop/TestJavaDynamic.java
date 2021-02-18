/**
 * created by DengJin on 2020/12/18 17:31
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class TestJavaDynamic {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        //创建被代理类的对象
        TestService userServiceImpl = new TestService();
        //获取被代理类的ClassLoader
        ClassLoader classLoader = userServiceImpl.getClass().getClassLoader();
        //获取被代理类实现的接口信息
        Class[] interfaces = userServiceImpl.getClass().getInterfaces();
        //创建实现了InvocationHandler接口的LogHandler对象，该对象成员属性为被代理对象，内部定义了调用代理对象方法前后的日志打印。
        InvocationHandler logHandler = new LogHandler(userServiceImpl);
        //创建代理对象，
        ITestService proxy = (ITestService) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        System.out.println("生成的动态代理类为："+proxy.getClass().getName());
        System.out.println("以下是执行动态代理类的getRandomNumber方法后的运行结果：");
        //使用代理对象调用test1()方法，会最终调用LogHandler对象中重写的invoke方法，
        proxy.getRandomNumber(1000);
    }
}
