/**
 * Created by Deng jin on 2021/2/6 11:41
 * 【这是老师给出的单线程例子】
 * ==============执行结果
 * 异步计算结果为：24157817
 * 使用时间：46 ms
 *
 * Process finished with exit code 0
 */
public class SingleThreadDemo {
    public static void main(String[] args) {

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        int result = sum(); //这是得到的返回值

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
