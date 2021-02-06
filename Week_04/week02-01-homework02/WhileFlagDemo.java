import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Deng jin on 2021/2/6 11:41
 * 使用中间变量，中间变量值变化，则表示已经完成，然后得到结果
 * ==============执行结果
 * 异步计算结果为：24157817
 * 使用时间：86 ms
 *
 * Process finished with exit code 0
 */
public class WhileFlagDemo {

    private static AtomicInteger sum = new AtomicInteger(0);//运行结果
    private static AtomicInteger flag = new AtomicInteger(0);//flag是标记位，0表示线程没有执行完，1标识执行完成

    public static void main(String[] args) {

        long start=System.currentTimeMillis();

        Thread t = new Thread(() -> {
           sum = new AtomicInteger(sum());
           flag = new AtomicInteger(1);
        });
        t.start();

        while (flag.intValue()==0){
            //静静地等待
            try{
                Thread.sleep(10);//停10毫秒，避免cpu占满
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        int result = sum.intValue(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);//24157817

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
        //执行完自动退出
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
