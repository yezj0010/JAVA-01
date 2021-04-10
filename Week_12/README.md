- [## 第12周第1课作业1](%E7%AC%AC12%E5%91%A8%E7%AC%AC1%E8%AF%BE%E4%BD%9C%E4%B8%9A1%E5%BF%85%E5%81%9A%E9%85%8D%E7%BD%AE-redis-%E7%9A%84%E4%B8%BB%E4%BB%8E%E5%A4%8D%E5%88%B6sentinel-%E9%AB%98%E5%8F%AF%E7%94%A8cluster-%E9%9B%86%E7%BE%A4)
- [## 第12周第2课作业1](%E7%AC%AC12%E5%91%A8%E7%AC%AC2%E8%AF%BE%E4%BD%9C%E4%B8%9A1%E6%90%AD%E5%BB%BA-activemq-%E6%9C%8D%E5%8A%A1%E5%9F%BA%E4%BA%8E-jms%E5%86%99%E4%BB%A3%E7%A0%81%E5%88%86%E5%88%AB%E5%AE%9E%E7%8E%B0%E5%AF%B9%E4%BA%8E-queue-%E5%92%8C-topic-%E7%9A%84%E6%B6%88%E6%81%AF%E7%94%9F%E4%BA%A7%E5%92%8C%E6%B6%88%E8%B4%B9)  
**这是第12周作业**  

共两个必选，记录如下：

## 第12周第1课作业1：（必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群。
配置步骤和配置文件如下：  
先在linux机器上安装好redis,版本**6.0.9**，安装参考文档步骤如下：（需要安装gcc，版本5.3以上）  
cd /usr/local/  
wget http://download.redis.io/releases/redis-6.0.9.tar.gz  
tar -zxvf redis-6.0.9.tar.gz  
cd redis-6.0.9  
make && make test && make install   

配置文件放在了文件夹**week12class1work1_redis_sentinel_cluster**中。  

### 主从复制
1.复制2份redis-6.0.9中的redis.conf，重命名为redis_6379.conf和redis_6380.conf  
修改redis_6379.conf内容如下   
```
protected-mode no  # 外网可以访问
daemonize yes  # 后台启动
```
修改redis_6380.conf内容如下  
```
port 6380  # 修改默认端口6379为6380
protected-mode no
daemonize yes
replicaof 127.0.0.1 6379  # 将自己设置为6379的redis的从redis,默认自动切换为只读
```

2.运行命令：  
```
启动主redis
redis-server redis_6379.conf

启动从redis
redis-server redis_6380.conf
```

3.测试主从复制：  
在6379redis中增加一个key，在6380redis中就可以获取到，且6380redis上无法增加key  
==在6379redis中执行命令  
```
[root@192 redis]# redis-cli 
127.0.0.1:6379> get a
(nil)
127.0.0.1:6379> set a 1
OK
```
==在6380redis中执行命令  
```
[root@192 ~]# redis-cli -p 6380
127.0.0.1:6380> set a 1
(error) READONLY You can't write against a read only replica.
127.0.0.1:6380> get a
"1"

```

### sentinel 高可用
1.复制两份redis-6.0.9中的sentinel.conf，重命名为sentinel.conf_26379.conf和sentinel.conf_26380.conf  
修改sentinel.conf_26379.conf内容如下：  
```
daemonize yes  #后台启动
logfile "/usr/local/redis/sentinel_26379.log"  # 哨兵的日志
dir /usr/local/redis/tmp/sentinel_26379  # 临时目录
sentinel monitor mymaster 127.0.0.1 6379 2  # 表示2台sentinel检测主redis连接不是，就开始主从切换
sentinel down-after-milliseconds mymaster 10000  # 表示检测与主redis连接断开后，10秒后开始主从切换
sentinel parallel-syncs mymaster 1  # 指定了最多可以有一个slave同时对新的master进行同步
sentinel failover-timeout mymaster 180000  # 表示180秒没有完成主从切换，则认定此次切换超时
```
修改sentinel.conf_26380.conf内容如下：
```
port 26380  # 修改默认端口26379为26380
daemonize yes  #后台启动
logfile "/usr/local/redis/sentinel_26380.log"  # 哨兵的日志
dir /usr/local/redis/tmp/sentinel_26380  # 临时目录
sentinel monitor mymaster 127.0.0.1 6379 2  # 表示2台sentinel检测主redis连接不是，就开始主从切换
sentinel down-after-milliseconds mymaster 10000  # 表示检测与主redis连接断开后，10秒后开始主从切换
sentinel parallel-syncs mymaster 1  # 指定了最多可以有一个slave同时对新的master进行同步
sentinel failover-timeout mymaster 180000  # 表示180秒没有完成主从切换，则认定此次切换超时
```

2.运行命令：  
```
启动哨兵26379
redis-sentinel sentinel_26379.conf

启动哨兵26380
redis-sentinel sentinel_26380.conf
```

3.测试高可用  
使用ps -ef| grep redis命令，找到6379进程号，kill 进程号，看哨兵日志和6380的info信息  
在kill6379前，6380info信息：  
```
127.0.0.1:6380> info Replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6379
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:27644
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:d79dec77f0ad063bc313525328a143c51bbfafea
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:27644
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:27644
```
在kill6379前，6379信息：  
```
127.0.0.1:6379> info Replication
# Replication
role:master
connected_slaves:1
slave0:ip=127.0.0.1,port=6380,state=online,offset=34644,lag=0
master_replid:d79dec77f0ad063bc313525328a143c51bbfafea
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:34644
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:34644
```
可以看出，6379是主，6380是从。  

然后kill 6379redis，查看哨兵日志和redis的info  
```
[root@192 redis]# ps -ef| grep redis
root      48032      1  0 13:54 ?        00:00:03 redis-server 127.0.0.1:6379
root      48629      1  0 14:02 ?        00:00:02 redis-server 127.0.0.1:6380
root      48644  48504  0 14:03 pts/1    00:00:00 redis-cli -p 6380
root      48668  24737  0 14:03 pts/0    00:00:00 redis-cli
root      74901      1  0 14:49 ?        00:00:00 redis-sentinel *:26379 [sentinel]
root      74919      1  0 14:50 ?        00:00:00 redis-sentinel *:26380 [sentinel]
root      75180  49784  0 14:55 pts/4    00:00:00 grep --color=auto redis
[root@192 redis]# kill -9 48032
[root@192 redis]# 
```
执行完命令，10秒后，可以看到两个哨兵日志如下：
26379日志：  
```
74901:X 10 Apr 2021 14:56:05.642 # +sdown master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.709 # +odown master mymaster 127.0.0.1 6379 #quorum 2/2
74901:X 10 Apr 2021 14:56:05.709 # +new-epoch 1
74901:X 10 Apr 2021 14:56:05.709 # +try-failover master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.711 # +vote-for-leader 91e34b4eb89de0b44ac02005d7d7699fbea89ad9 1
74901:X 10 Apr 2021 14:56:05.715 # 53f82c7b4b376677a0a3c0a899fd3328e2e23fb2 voted for 91e34b4eb89de0b44ac02005d7d7699fbea89ad9 1
74901:X 10 Apr 2021 14:56:05.787 # +elected-leader master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.788 # +failover-state-select-slave master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.871 # +selected-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.871 * +failover-state-send-slaveof-noone slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:05.927 * +failover-state-wait-promotion slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:06.406 # +promoted-slave slave 127.0.0.1:6380 127.0.0.1 6380 @ mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:06.406 # +failover-state-reconf-slaves master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:06.506 # +failover-end master mymaster 127.0.0.1 6379
74901:X 10 Apr 2021 14:56:06.506 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6380
74901:X 10 Apr 2021 14:56:06.506 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
74901:X 10 Apr 2021 14:56:16.511 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
```
26380日志：  
```
74919:X 10 Apr 2021 14:56:05.598 # +sdown master mymaster 127.0.0.1 6379
74919:X 10 Apr 2021 14:56:05.713 # +new-epoch 1
74919:X 10 Apr 2021 14:56:05.715 # +vote-for-leader 91e34b4eb89de0b44ac02005d7d7699fbea89ad9 1
74919:X 10 Apr 2021 14:56:06.508 # +config-update-from sentinel 91e34b4eb89de0b44ac02005d7d7699fbea89ad9 127.0.0.1 26379 @ mymaster 127.0.0.1 6379
74919:X 10 Apr 2021 14:56:06.508 # +switch-master mymaster 127.0.0.1 6379 127.0.0.1 6380
74919:X 10 Apr 2021 14:56:06.508 * +slave slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
74919:X 10 Apr 2021 14:56:16.549 # +sdown slave 127.0.0.1:6379 127.0.0.1 6379 @ mymaster 127.0.0.1 6380
```
从日志时间看出，26380哨兵先检测到6379redis挂了，然后是26379哨兵，然后进行了主从切换，将6380设置为了主。
此时，看6380的info:  
```
127.0.0.1:6380> info Replication
# Replication
role:master
connected_slaves:0
master_replid:4333a09e5a4d6fd2a81f60683cc6720a23b457d3
master_replid2:d79dec77f0ad063bc313525328a143c51bbfafea
master_repl_offset:80321
second_repl_offset:50507
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:80321
```
可以看出，6380已经成功变成了主redis,没有从redis，因为此时6379还是挂的状态.  
启动6379查看它的info信息（需要redis-cli重新连接，否则信息不准确）：    
```
127.0.0.1:6379> info Replication
# Replication
role:slave
master_host:127.0.0.1
master_port:6380
master_link_status:up
master_last_io_seconds_ago:0
master_sync_in_progress:0
slave_repl_offset:98726
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:4333a09e5a4d6fd2a81f60683cc6720a23b457d3
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:98726
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:95351
repl_backlog_histlen:3376
```
可以看出，6379从master变成了slave,而他的主redis变成了6380.此时在6380redis上执行info Replication命令，可以看到slave数量变成了1.  

此时分别在6379和6380redis上执行命令，如下：  
可以看出，6379只能读，不能写，且能同步到6380更新的数据：  
6379上执行：  
```
127.0.0.1:6379> set a 2
(error) READONLY You can't write against a read only replica.
127.0.0.1:6379> get a
"1"
127.0.0.1:6379> get a  （该命令是在6380上执行set a 2命令后执行的）
"2"
127.0.0.1:6379> 
```
6380上执行：  
```
127.0.0.1:6380> get a
"1"
127.0.0.1:6380> set a 2
OK
127.0.0.1:6380> get a
"2"
```

主从切换之后，四个配置文件都被更新了，从更新时间可以看出。  
如redis_6379.conf文件中，文件最末尾，就自动增加了以下配置：
```
# Generated by CONFIG REWRITE
user default on nopass ~* +@all
replicaof 127.0.0.1 6380
```
### Cluster 集群
参考文档  
https://blog.csdn.net/xlyrh/article/details/110776381  
新创建一个文件夹 /etc/redis-cluster,  
在该redis-cluster中，创建6个文件夹： 
```
[root@192 redis-cluster]# ll
总用量 0
drwxr-xr-x. 2 root root 28 4月  10 15:41 redis1
drwxr-xr-x. 2 root root 28 4月  10 15:39 redis2
drwxr-xr-x. 2 root root 28 4月  10 15:39 redis3
drwxr-xr-x. 2 root root 28 4月  10 15:39 redis4
drwxr-xr-x. 2 root root 28 4月  10 15:39 redis5
drwxr-xr-x. 2 root root 28 4月  10 15:39 redis6
```
每个文件夹下，复制一份redis.conf文件，重命名为redis_7001.conf~redis_7001.conf  
修改6个配置文件内容，如下：  
```
bind 127.0.0.1
bind 192.168.1.107  #根据实际情况修改，本次使用虚拟机ip是192.168.1.107
port 7001  #从7001~7006变化
daemonize yes  #后台启动
cluster-enabled yes  #开启集群模式
```
新建批量启动sh脚本 start_all.sh  
```
cd redis1
redis-server redis_7001.conf
cd ..
cd redis2
redis-server redis_7002.conf
cd ..
cd redis3
redis-server redis_7003.conf
cd ..
cd redis4
redis-server redis_7004.conf
cd ..
cd redis5
redis-server redis_7005.conf
cd ..
cd redis6
redis-server redis_7006.conf
cd ..
```

然后执行命令，创建集群  
①启动6个redis  
```
[root@192 redis-cluster]# sh start_all.sh 
[root@192 redis-cluster]# 
[root@192 redis-cluster]# 
[root@192 redis-cluster]# 
[root@192 redis-cluster]# ps -ef| grep redis
root      83075      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7001 [cluster]
root      83082      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7002 [cluster]
root      83088      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7003 [cluster]
root      83094      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7004 [cluster]
root      83100      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7005 [cluster]
root      83106      1  0 17:15 ?        00:00:00 redis-server 192.168.1.107:7006 [cluster]
root      83115  49784  0 17:15 pts/4    00:00:00 grep --color=auto redis
```
②创建cluster集群  输出了哪些是主，哪些是从，以及主-从对应关系  
```
[root@192 redis-cluster]# redis-cli --cluster create --cluster-replicas 1 192.168.1.107:7001 192.168.1.107:7002 192.168.1.107:7003 192.168.1.107:7004 192.168.1.107:7005 192.168.1.107:7006
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 192.168.1.107:7005 to 192.168.1.107:7001
Adding replica 192.168.1.107:7006 to 192.168.1.107:7002
Adding replica 192.168.1.107:7004 to 192.168.1.107:7003
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 9c71c583edb44086c3db8e170fd4fd44cbed7b50 192.168.1.107:7001
   slots:[0-5460] (5461 slots) master
M: 710093c4a5b12390d6c6ca8e3207202d93de0a6a 192.168.1.107:7002
   slots:[5461-10922] (5462 slots) master
M: 5716faa7f0c80e69b1738835080eda1241253968 192.168.1.107:7003
   slots:[10923-16383] (5461 slots) master
S: 465ab88b96e481ec5ec6dd65e1de191d011bbe20 192.168.1.107:7004
   replicates 710093c4a5b12390d6c6ca8e3207202d93de0a6a
S: 3c8b94e2ccf9c93ea9f78ed8fe82b67125117b5a 192.168.1.107:7005
   replicates 5716faa7f0c80e69b1738835080eda1241253968
S: 76f5b583475bc82a76dd9f3910e2fa5ba324ef5f 192.168.1.107:7006
   replicates 9c71c583edb44086c3db8e170fd4fd44cbed7b50
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join

>>> Performing Cluster Check (using node 192.168.1.107:7001)
M: 9c71c583edb44086c3db8e170fd4fd44cbed7b50 192.168.1.107:7001
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 76f5b583475bc82a76dd9f3910e2fa5ba324ef5f 192.168.1.107:7006
   slots: (0 slots) slave
   replicates 9c71c583edb44086c3db8e170fd4fd44cbed7b50
S: 465ab88b96e481ec5ec6dd65e1de191d011bbe20 192.168.1.107:7004
   slots: (0 slots) slave
   replicates 710093c4a5b12390d6c6ca8e3207202d93de0a6a
M: 710093c4a5b12390d6c6ca8e3207202d93de0a6a 192.168.1.107:7002
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: 3c8b94e2ccf9c93ea9f78ed8fe82b67125117b5a 192.168.1.107:7005
   slots: (0 slots) slave
   replicates 5716faa7f0c80e69b1738835080eda1241253968
M: 5716faa7f0c80e69b1738835080eda1241253968 192.168.1.107:7003
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
③查看cluster信息    
```
[root@192 redis-cluster]# redis-cli -c -h 192.168.1.107 -p 7003
192.168.1.107:7003> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:6
cluster_size:3
cluster_current_epoch:6
cluster_my_epoch:3
cluster_stats_messages_ping_sent:16
cluster_stats_messages_pong_sent:14
cluster_stats_messages_meet_sent:1
cluster_stats_messages_sent:31
cluster_stats_messages_ping_received:14
cluster_stats_messages_pong_received:17
cluster_stats_messages_received:31
```
④测试命令  会根据key的hash值得不同，自动路由到不同的节点
```
192.168.1.107:7003> get a
(nil)
192.168.1.107:7003> get aa
-> Redirected to slot [1180] located at 192.168.1.107:7001
(nil)
192.168.1.107:7001> get aaa
-> Redirected to slot [10439] located at 192.168.1.107:7002
(nil)
192.168.1.107:7002> 
```

## 第12周第2课作业1：搭建 ActiveMQ 服务，基于 JMS，写代码分别实现对于 queue 和 topic 的消息生产和消费
代码见项目**week12class2work1_activemq**

