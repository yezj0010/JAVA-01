package cc.yezj.jdbc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Deng jin on 2021/3/6 13:19
 */
@Slf4j
public class PrepareStatementInsert {

    public static void main(String[] args) {
        Connection connection = JDBCUtils.getConnection();
        try{
//            insertBatch(connection, 1000000, 10000);//usedTime=144036ms --> usedTime=47679ms   (驱动连接不加配置 --> 加配置)
//            insertBatch(connection, 1000000, 100000);//usedTime=129397ms --> usedTime=42102ms   (驱动连接不加配置 --> 加配置)
//            insertBatch(connection, 1000000, 1000000);//usedTime=128450ms --> usedTime=46284ms   (驱动连接不加配置 --> 加配置)
            //使用多个连接+新开线程，同时操作
//            insertBatchByMultiCon(1000000, 10000);//usedTime=46948ms
//            insertBatchByMultiCon(1000000, 100000);//usedTime=71542ms
//            insertBatchByMultiCon(1000000, 1000000);//usedTime=51794ms
            //去掉主键之后
//            insertBatch(connection, 1000000, 10000);//usedTime=29312ms
            insertBatchByMultiCon(1000000, 10000);//usedTime=34421ms
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
    }

    public static void insertBatch(Connection connection, int total, int batchSize) throws Exception{
        long start = System.currentTimeMillis();
        String sql = "insert into t_order values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        long time = System.currentTimeMillis();
        int temp = 0;
        for(long count=1;count<=total;count++){
            String id = count+"";
            statement.setString(1, id);//订单id
            statement.setString(2, id);//用户id
            statement.setBigDecimal(3, BigDecimal.ONE);//总金额
            statement.setInt(4, 1);//总商品数量
            statement.setString(5, id);//收件人姓名
            statement.setString(6, id);//收件人手机号
            statement.setString(7, id);//省
            statement.setString(8, id);//市
            statement.setString(9, id);//区
            statement.setString(10, id);//详细地址
            statement.setInt(11, 0);//状态
            statement.setLong(12, time);//创建时间
            statement.setLong(13, time);//更新时间
            statement.addBatch();
            temp++;

            if(temp>=batchSize){
                connection.setAutoCommit(false);//设置为不自动提交
                statement.executeBatch();
                connection.commit();
                log.info("一次性插入"+temp+"条");
                temp=0;
            }
        }

        if(temp>0){//最后剩下的一点，一次性插入
            log.info("最后，一次性插入"+temp+"条");
            connection.setAutoCommit(false);//设置为不自动提交
            statement.executeBatch();
            connection.commit();
        }
        log.info("导入完毕, usedTime={}ms", System.currentTimeMillis()-start);
    }


    public static void insertBatchByMultiCon(int total, int batchSize) throws Exception{
        long start = System.currentTimeMillis();
        String sql = "insert into t_order values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int connectionCnt = total/batchSize;
        int usedBatchSize = 0;
        long time = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(connectionCnt);
        for(int i=1;i<=connectionCnt;i++){
            Connection connection = JDBCUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            new Thread(new MyRunner(1+usedBatchSize*batchSize, batchSize, time, connection, statement, countDownLatch)).start();
            usedBatchSize++;
        }

        countDownLatch.await();
        log.info("导入完毕, usedTime={}ms", System.currentTimeMillis()-start);
    }

    @AllArgsConstructor
    static
    class MyRunner implements Runnable {
        private int start;
        private int batchSize;
        private long time;
        private Connection connection;
        private PreparedStatement statement;
        CountDownLatch countDownLatch;
        @Override
        public void run() {
            try{
                log.info("一次性插入{}条,id从{}开始", batchSize, start);
                for(int count=start;count<start+this.batchSize;count++){
                    String id = count+"";
                    statement.setString(1, id);//订单id
                    statement.setString(2, id);//用户id
                    statement.setBigDecimal(3, BigDecimal.ONE);//总金额
                    statement.setInt(4, 1);//总商品数量
                    statement.setString(5, id);//收件人姓名
                    statement.setString(6, id);//收件人手机号
                    statement.setString(7, id);//省
                    statement.setString(8, id);//市
                    statement.setString(9, id);//区
                    statement.setString(10, id);//详细地址
                    statement.setInt(11, 0);//状态
                    statement.setLong(12, time);//创建时间
                    statement.setLong(13, time);//更新时间
                    statement.addBatch();
                }
                connection.setAutoCommit(false);//设置为不自动提交
                statement.executeBatch();
                connection.commit();
                countDownLatch.countDown();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    statement.close();
                    connection.commit();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
