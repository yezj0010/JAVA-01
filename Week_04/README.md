**这是第四周作业**  

作业详细内容见：  
《第四周第一课作业2-启动线程拿到返回值退出.md》  
《第四周第二课作业4-多线程与并发.png》  



**感悟**  
1.对多线程中的wait，notify，join有了更深入的理解  
wait和notify/notifyAll是对象的方法，因为所有对象都可以作为锁，对象调用wait方法，表示当前线程放弃该对象的锁，并进入等待状态。  
对象调用notify/notify方法，表示唤醒等待该对象锁的线程，线程进入就绪状态等待运行，运行后从上次wait或等待锁的位置继续执行。  
如果对象没有被锁，调用wait和notify/notifyAll方法，会报错illegalMonitorState异常  

2.了解了多线程中的Lock和一些工具类
Lock中包括ReentrantLock,WriteLock,ReadWriteLock等，  
Lock中可以拥有多个Condition，可以用来精准控制线程的唤醒，  
LockSupport是封装了Lock的一些操作，常用到的是它的静态方法park等待资源和unpark释放资源，一定要现有释放资源才能得到资源，  
其他一些工具类包括Semaphore，CountdownLatch，CyclicBarrier等，他们内部都是基于AQS实现的。  

