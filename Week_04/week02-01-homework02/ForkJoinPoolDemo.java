import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Deng jin on 2021/2/6 11:41
 * 使用ForkJoinPool
 * 异步计算结果为：24157817
 * 使用时间：47 ms
 *
 * Process finished with exit code 0
 * ==============执行结果
 */
public class ForkJoinPoolDemo {
    public static void main(String[] args) throws Exception{

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask submit = pool.submit(new RecursiveTask() {

            @Override
            protected Integer compute() {
                return sum();
            }
        });

        int result = (Integer)submit.get(); //这是得到的返回值
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
