**这是第13周作业**    

## 第13周第1课作业1：（必做）搭建一个 3 节点 Kafka 集群，测试功能和性能；实现 spring kafka 下对 kafka 集群的操作
代码见项目**week13class1work1_kafka**，配置文件见**application.yaml**    
配置kafka集群：  
下载，解压  
```
wget https://mirrors.bfsu.edu.cn/apache/kafka/2.7.0/kafka_2.13-2.7.0.tgz
tar -zxvf kafka_2.13-2.7.0.tgz
```
修改配置文件，改动三个地方  
```
//修改broker.id   1,2,3
broker.id=1
//修改端口，9092改成9001,9002,9003
listeners=PLAINTEXT://:9001
//添加broker.list，告知kafka它集群中其他节点
broker.list=localhost:9001,localhost:9002.localhost:9003
//修改日志文件路径kafka-logs1,2,3
/tmp/kafka-logs1
```
启动zookeeper  
```
nohup sh bin/zookeeper-server-start.sh config/zookeeper.properties &
```
启动kafka，后台启动  
```
nohup bin/kafka-server-start.sh config/server1.properties &
nohup bin/kafka-server-start.sh config/server2.properties &
nohup bin/kafka-server-start.sh config/server3.properties &
```
启动项目，运行后自动运行Test中的方法，通过日志，则说明测试了使用springkafka对kafka集群的发送消息和消费消息的操作。
  
## 第13周第2课作业2：（必做）思考和设计自定义 MQ 第二个版本或第三个版本，写代码实现其中至少一个功能点
代码见项目**week13class2work2_diy_mq**  
本项目基于示例项目**kmq-core**，实现的是第二个版本，自定义了一个Queue，实现了消息确认和offset。  
设计思路如下：  
1.将Kmq类中的LinkedBlockingQueue换成自定义的Queue--MessageQueue.  
2.该类中含有capacity,items,readIndexMap,writeIndex,lock五个属性：  
capacity是容量，用来判断容量是否超标，items存放真实的数据，readIndexMap存放每个线程读取的消息下标，writeIndex存放消息写入的下标，lock是可重入锁。  
3.lock锁设置为公平锁，如果是非公平锁，可能会造成写线程需要等待很久的时间才能获得，从而无法立即写入消息。  
4.**MessageQueue**增加了ack方法，消费完后，必须调用ack方法，否则会一直消费。后期可以在获取消息时增加入参，判断是否需要显示确认，默认自动确认。

测试：  
执行KmqDemo的main方法，即可测试效果。  

