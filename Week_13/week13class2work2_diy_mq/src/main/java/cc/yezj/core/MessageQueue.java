package cc.yezj.core;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Deng jin on 2021/4/18 15:28
 */
@Slf4j
public class MessageQueue<T> {

    public MessageQueue(int capacity){
        this.capacity = capacity;
        this.items = new Object[capacity];
        lock = new ReentrantLock(true);
    }

    /**
     * 容量
     */
    private int capacity;

    /**
     * 存储实际的数据
     */
    private Object[] items;

    /**
     * 每个消费者读取的位置下标
     */
    private Map<String, Integer> readIndexMap = new HashMap<>();

    /**
     * 当前消息写入的位置下标
     */
    private volatile int writeIndex = 0;

    /** 锁，控制并发  */
    final ReentrantLock lock;

    /**
     * 放入消息
     * @param t
     * @return
     */
    public boolean offer(T t){
        lock.lock();
        try{
            items[writeIndex] = t;
            writeIndex++;
            return true;
        }catch (Exception e){
            return false;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 拉取消息，指定时间内
     * @param timeout 单位毫秒
     * @return
     */
    public T poll(long timeout) {
        String name = Thread.currentThread().getName();
        lock.lock();
        long start = System.currentTimeMillis();
        try{
            Integer readIndex = readIndexMap.computeIfAbsent(name, k -> 0);
            //判断当前线程读下标是否小于写下标，不是则等待10毫米，直到满足条件
            while (readIndex >= writeIndex){
                long now = System.currentTimeMillis();
                if(now - start > timeout){
                    break;
                }
                Thread.sleep(10);
            }
            if(readIndex >= capacity){
                return null;
            }
            T item = (T) items[readIndex];
            if(item != null){
//                readIndex++;
//                readIndexMap.put(name, readIndex);
                log.info("readIndex={},writeIndex={}", readIndex, writeIndex);
            }
            return item;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 拉取消息
     * @return
     */
    public T poll() {
        String name = Thread.currentThread().getName();
        lock.lock();
        try{
            Integer readIndex = readIndexMap.computeIfAbsent(name, k -> 0);
            //判断当前线程读下标是否小于写下标，不是则等待10毫米，直到满足条件
            while (readIndex >= writeIndex){
                Thread.sleep(10);
            }
            if(readIndex >= capacity){
                return null;
            }
            T item = (T) items[readIndex];
            if(item != null){
//                readIndex++;
//                readIndexMap.put(name, readIndex);
                log.info("readIndex={},writeIndex={}", readIndex, writeIndex);
            }
            return item;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 消息确认
     */
    public void ack(){
        String name = Thread.currentThread().getName();
        lock.lock();
        try{
            Integer integer = readIndexMap.get(name);
            if(integer==null){
                return;
            }
            integer++;
            readIndexMap.put(name, integer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

}
