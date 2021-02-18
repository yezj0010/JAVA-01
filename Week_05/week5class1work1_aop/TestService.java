import java.util.Random;

/**
 * created by DengJin on 2020/12/18 17:32
 */
public class TestService implements ITestService {
    @Override
    public int getRandomNumber(int a) {
        System.out.println("这是原始被代理类的方法");
        return new Random().nextInt(a);
    }
}
