import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Deng jin on 2021/2/6 11:41
 * 使用Lock的condition
 * ==============执行结果
 * 异步计算结果为：24157817
 * 使用时间：74 ms
 *
 * Process finished with exit code 0
 */
public class LockDemo {
    private static AtomicInteger sum = new AtomicInteger(0);

    public static void main(String[] args) throws Exception{

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            sum = new AtomicInteger(sum());
            condition.signalAll();
            lock.unlock();
        }).start();
        lock.lock();
        condition.await();
        lock.unlock();


        int result = sum.get(); //这是得到的返回值

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
