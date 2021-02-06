import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Deng jin on 2021/2/6 11:41
 * 使用wait+notify
 * ==============执行结果
 * 异步计算结果为：24157817
 * 使用时间：79 ms
 *
 * Process finished with exit code 0
 */
public class WaitNotifyDemo {
    private static AtomicInteger sum = new AtomicInteger(0);

    public static void main(String[] args) throws Exception{

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        WaitNotifyDemo waitNotifyDemo = new WaitNotifyDemo();
        new Thread(() -> {
            synchronized (waitNotifyDemo){
                sum = new AtomicInteger(sum());
                waitNotifyDemo.notify();
            }
        }).start();
        synchronized (waitNotifyDemo){
            waitNotifyDemo.wait();
        }

        int result = sum.intValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
