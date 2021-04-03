**这是第11周作业**  

### 第11周第2课作业4：（必做）基于 Redis 封装分布式数据操作
代码见项目**week11class2work4_redis_lock**  

**在 Java 中实现一个简单的分布式锁；**     
类**RedisLock**实现了一个简单的分布式锁。  
使用了set nx px 命令，来给一个key赋值，成功返回true,表示获取锁成功，否则失败。  
实现了两个获取锁的方法，一个是一直循环获取直到成功，一个是只获取一次，失败则抛出异常。  
后面考虑直接使用Redission框架线程的分布式锁。避免重复造轮子引入的bug。  

**在 Java 中实现一个分布式计数器，模拟减库存。**  
类**Counter**实现了一个分布式计数器，返回false表示计数到0，返回true表示计数一次。    
Counter的initStock方法，初始化了总数。  

**测试**  
启动项目，**Test**类中，run方法中对这两个类进行了测试，开启了10个线程去计数。  
可以根据日志看出：测试结果成功，完成了分布式锁和分布式计数器的开发。  
```
2021-04-03 15:27:53.578  INFO 11148 --- [           main] cc.yezj.inti.Test                        : 初始化库存数量，1000
2021-04-03 15:27:54.651  INFO 11148 --- [           main] io.lettuce.core.EpollProvider            : Starting without optional epoll library
2021-04-03 15:27:54.652  INFO 11148 --- [           main] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
2021-04-03 15:27:56.005  INFO 11148 --- [           main] cc.yezj.inti.Test                        : 创建10个线程，同时减少库存
2021-04-03 15:27:59.893  INFO 11148 --- [           main] cc.yezj.inti.Test                        : 分布式计数器测试成功,usedTime=2887ms
```

### 第11周第2课作业5：（必做）基于 Redis 的 PubSub 实现订单异步处理
代码见项目**week11class2work5_redis_pub_sub**  
使用redis自带pub/sub功能，实现发布订阅消息系统demo。  
重点配置是**ChannelMessageConfig**类，指定了订阅消息的类（**OrderSubscriber**），以及订阅消息渠道的正则表达式（以Order结尾）。  

**测试**  
启动项目，Test类中，run方法调用了**OrderService**的preCreateOrder方法。  
然后**OrderSubscriber**接收到消息，判断订阅渠道，然后调用**OrderService**的doCreateOrder方法。  
从以下日志看出，测试成功，实现了基于redis的订单异步处理demo。  
```
2021-04-03 16:27:17.609  INFO 20164 --- [           main] cc.yezj.service.OrderService             : 准备创建一个订单，发送消息。。。
2021-04-03 16:27:17.609  INFO 20164 --- [    container-2] cc.yezj.sub.OrderSubscriber              : ============>>>>> onMessage
2021-04-03 16:27:17.617  INFO 20164 --- [    container-2] cc.yezj.sub.OrderSubscriber              : 消息频道名称：createOrder
2021-04-03 16:27:17.617  INFO 20164 --- [    container-2] cc.yezj.sub.OrderSubscriber              : 消息内容是:{"userId":11,"productId":1934,"price":4999,"otherInfoDemo":"其他信息"}
2021-04-03 16:27:17.628  INFO 20164 --- [    container-2] cc.yezj.service.OrderService             : 开始真正创建一个订单。。。,orderInfo=OrderInfo(userId=11, productId=1934, price=4999, otherInfoDemo=其他信息)
2021-04-03 16:27:17.628  INFO 20164 --- [    container-2] cc.yezj.service.OrderService             : 订单创建成功！
```
 