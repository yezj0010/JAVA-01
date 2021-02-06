import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Deng jin on 2021/2/6 11:41
 * 使用线程池调用Callable任务，得到返回结果，然后打印出来，然后退出
 * ==============执行结果
 * 异步计算结果为：24157817
 * 使用时间：80 ms
 *
 * Process finished with exit code 0
 */
public class CallableDemo {
    public static void main(String[] args){
        try{
            long start=System.currentTimeMillis();

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Executors.newScheduledThreadPool(10);
            Future<Integer> submit = executorService.submit(() -> {
                return sum();
            });

            int result = submit.get();; //这是得到的返回值

            // 确保  拿到result 并输出
            System.out.println("异步计算结果为："+result);//24157817

            System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

            // 然后退出main线程
            executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

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
