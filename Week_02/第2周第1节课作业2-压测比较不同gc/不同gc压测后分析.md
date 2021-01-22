本分析使用gateway的jar包，在使用不同gc下进行压测，看rps大小  

## 使用不同gc压测结果
java 启动命令参数
压测命令(-c 并发12个，总共请求10万次)：  
sb -u http://localhost:8088/api/hello -c 12 -N 30  
  
|gc类型|1g|2g|4g|
|----|----|----|----|
|SerailGC|8108|8321|8234.5|
|Parallel|8249.3|8178.4|8307.5|
|CMS|8189|8168|8162|
|G1|7977.7|8080|8029|

从上表格大致看出，SerialGC的rps较高，

## 分析Parallel GC 压测时的gc日志（jdk8默认gc先分析）

```
PS E:\view\java\week2-01> java -Xmx1g -Xms1g -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar
 
   .   ____          _            __ _ _
  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
   '  |____| .__|_| |_|_| |_\__, | / / / /
  =========|_|==============|___/=/_/_/_/
  :: Spring Boot ::        (v2.0.4.RELEASE)
 
 2021-01-22T14:07:15.285+0800: [GC (Metadata GC Threshold) [PSYoungGen: 256910K->15414K(305664K)] 256910K->15438K(1005056K), 0.0085889 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
 2021-01-22T14:07:15.294+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 15414K->0K(305664K)] [ParOldGen: 24K->14480K(699392K)] 15438K->14480K(1005056K), [Metaspace: 20449K->20449K(1067008K)], 0.0192508 secs] [Times: user=0.05 sys=0.00, real=0.02 secs]
 2021-01-22T14:07:16.202+0800: [GC (Allocation Failure) [PSYoungGen: 262144K->16132K(305664K)] 276624K->30684K(1005056K), 0.0073849 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 以下为压测开始后的日志
 2021-01-22T14:07:27.276+0800: [GC (Metadata GC Threshold) [PSYoungGen: 186571K->13867K(305664K)] 201123K->28427K(1005056K), 0.0048973 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:27.281+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 13867K->0K(305664K)] [ParOldGen: 14560K->18140K(699392K)] 28427K->18140K(1005056K), [Metaspace: 33873K->33873K(1079296K)], 0.0272802 secs] [Times: user=0.17 sys=0.00, real=0.03 secs]
 2021-01-22T14:07:28.714+0800: [GC (Allocation Failure) [PSYoungGen: 262144K->2310K(305664K)] 280284K->20459K(1005056K), 0.0025101 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:30.033+0800: [GC (Allocation Failure) [PSYoungGen: 264454K->1808K(305664K)] 282603K->19956K(1005056K), 0.0024499 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:31.331+0800: [GC (Allocation Failure) [PSYoungGen: 263952K->1840K(327168K)] 282100K->19996K(1026560K), 0.0023363 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:32.727+0800: [GC (Allocation Failure) [PSYoungGen: 306992K->1760K(327168K)] 325148K->19924K(1026560K), 0.0029873 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:34.099+0800: [GC (Allocation Failure) [PSYoungGen: 306912K->1744K(328192K)] 325076K->19916K(1027584K), 0.0019650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:35.530+0800: [GC (Allocation Failure) [PSYoungGen: 308432K->1792K(327680K)] 326604K->19964K(1027072K), 0.0019338 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:36.896+0800: [GC (Allocation Failure) [PSYoungGen: 308480K->1808K(329216K)] 326652K->19988K(1028608K), 0.0018809 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:38.247+0800: [GC (Allocation Failure) [PSYoungGen: 310544K->1792K(328704K)] 328724K->19980K(1028096K), 0.0018196 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:39.607+0800: [GC (Allocation Failure) [PSYoungGen: 310528K->1792K(330752K)] 328716K->19980K(1030144K), 0.0018692 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:41.003+0800: [GC (Allocation Failure) [PSYoungGen: 313088K->1840K(329728K)] 331276K->20028K(1029120K), 0.0018256 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:42.373+0800: [GC (Allocation Failure) [PSYoungGen: 313136K->1808K(332288K)] 331324K->20004K(1031680K), 0.0018392 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:43.739+0800: [GC (Allocation Failure) [PSYoungGen: 316176K->1808K(331264K)] 334372K->20004K(1030656K), 0.0018512 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:45.114+0800: [GC (Allocation Failure) [PSYoungGen: 316176K->1824K(333824K)] 334372K->20028K(1033216K), 0.0018459 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:46.508+0800: [GC (Allocation Failure) [PSYoungGen: 319264K->1840K(332800K)] 337468K->20044K(1032192K), 0.0018204 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:47.898+0800: [GC (Allocation Failure) [PSYoungGen: 319280K->1840K(335360K)] 337484K->20044K(1034752K), 0.0018849 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:49.300+0800: [GC (Allocation Failure) [PSYoungGen: 322352K->320K(334336K)] 340556K->20117K(1033728K), 0.0018039 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:50.725+0800: [GC (Allocation Failure) [PSYoungGen: 320832K->320K(336384K)] 340629K->20189K(1035776K), 0.0014088 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:52.187+0800: [GC (Allocation Failure) [PSYoungGen: 323392K->320K(335872K)] 343261K->20213K(1035264K), 0.0013719 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:53.628+0800: [GC (Allocation Failure) [PSYoungGen: 323392K->320K(337408K)] 343285K->20261K(1036800K), 0.0013438 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:55.110+0800: [GC (Allocation Failure) [PSYoungGen: 325440K->288K(336896K)] 345381K->20229K(1036288K), 0.0013619 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:07:56.536+0800: [GC (Allocation Failure) [PSYoungGen: 325408K->320K(338432K)] 345349K->20269K(1037824K), 0.0013695 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 
PS E:\view\java\week2-01> java -Xmx2g -Xms2g -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar
 
   .   ____          _            __ _ _
  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
   '  |____| .__|_| |_|_| |_\__, | / / / /
  =========|_|==============|___/=/_/_/_/
  :: Spring Boot ::        (v2.0.4.RELEASE)
 
 2021-01-22T14:08:44.597+0800: [GC (Metadata GC Threshold) [PSYoungGen: 272899K->15385K(611840K)] 272899K->15401K(2010112K), 0.0085904 secs] [Times: user=0.03 sys=0.02, real=0.01 secs]
 2021-01-22T14:08:44.606+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 15385K->0K(611840K)] [ParOldGen: 16K->14494K(1398272K)] 15401K->14494K(2010112K), [Metaspace: 20477K->20477K(1067008K)], 0.0168909 secs] [Times: user=0.17 sys=0.00, real=0.02 secs]
 以下为压测开始后的日志
 2021-01-22T14:09:02.183+0800: [GC (Metadata GC Threshold) [PSYoungGen: 480133K->19098K(611840K)] 494628K->33665K(2010112K), 0.0092772 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 2021-01-22T14:09:02.192+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 19098K->0K(611840K)] [ParOldGen: 14566K->23098K(1398272K)] 33665K->23098K(2010112K), [Metaspace: 33830K->33830K(1079296K)], 0.0288327 secs] [Times: user=0.17 sys=0.00, real=0.03 secs]
 2021-01-22T14:09:04.883+0800: [GC (Allocation Failure) [PSYoungGen: 524800K->2326K(611840K)] 547898K->25433K(2010112K), 0.0030852 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:07.346+0800: [GC (Allocation Failure) [PSYoungGen: 527126K->1824K(611840K)] 550233K->24938K(2010112K), 0.0023818 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:09.620+0800: [GC (Allocation Failure) [PSYoungGen: 526624K->1808K(611840K)] 549738K->24930K(2010112K), 0.0024392 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:11.985+0800: [GC (Allocation Failure) [PSYoungGen: 526608K->1808K(675840K)] 549730K->24930K(2074112K), 0.0024305 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:14.824+0800: [GC (Allocation Failure) [PSYoungGen: 653584K->1744K(674816K)] 676706K->24874K(2073088K), 0.0025570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:17.653+0800: [GC (Allocation Failure) [PSYoungGen: 653520K->1744K(677888K)] 676650K->24882K(2076160K), 0.0023757 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:20.508+0800: [GC (Allocation Failure) [PSYoungGen: 657616K->1856K(676864K)] 680754K->24994K(2075136K), 0.0023566 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:23.407+0800: [GC (Allocation Failure) [PSYoungGen: 657728K->1856K(679424K)] 680866K->24994K(2077696K), 0.0023322 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:26.385+0800: [GC (Allocation Failure) [PSYoungGen: 661312K->1872K(678912K)] 684450K->25010K(2077184K), 0.0023209 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:29.217+0800: [GC (Allocation Failure) [PSYoungGen: 661328K->1744K(681472K)] 684466K->24882K(2079744K), 0.0021413 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:09:32.096+0800: [GC (Allocation Failure) [PSYoungGen: 664784K->1776K(680448K)] 687922K->24922K(2078720K), 0.0021427 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 
PS E:\view\java\week2-01> java -Xmx4g -Xms4g -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar
 
   .   ____          _            __ _ _
  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
   '  |____| .__|_| |_|_| |_\__, | / / / /
  =========|_|==============|___/=/_/_/_/
  :: Spring Boot ::        (v2.0.4.RELEASE)
 
 2021-01-22T14:10:02.556+0800: [GC (Metadata GC Threshold) [PSYoungGen: 314574K->15459K(1223168K)] 314574K->15483K(4019712K), 0.0086102 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 2021-01-22T14:10:02.565+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 15459K->0K(1223168K)] [ParOldGen: 24K->14483K(2796544K)] 15483K->14483K(4019712K), [Metaspace: 20460K->20460K(1067008K)], 0.0175310 secs] [Times: user=0.05 sys=0.00, real=0.02 secs]
 以下为压测开始后的日志
 2021-01-22T14:10:08.407+0800: [GC (Metadata GC Threshold) [PSYoungGen: 629150K->19262K(1223168K)] 643633K->33817K(4019712K), 0.0083531 secs] [Times: user=0.00 sys=0.02, real=0.01 secs]
 2021-01-22T14:10:08.416+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 19262K->0K(1223168K)] [ParOldGen: 14555K->23168K(2796544K)] 33817K->23168K(4019712K), [Metaspace: 33876K->33876K(1079296K)], 0.0285345 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
 2021-01-22T14:10:13.366+0800: [GC (Allocation Failure) [PSYoungGen: 1048576K->2405K(1223168K)] 1071744K->25582K(4019712K), 0.0022376 secs] [Times: user=0.09 sys=0.02, real=0.00 secs]
 2021-01-22T14:10:17.994+0800: [GC (Allocation Failure) [PSYoungGen: 1050981K->1808K(1223168K)] 1074158K->24992K(4019712K), 0.0019198 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:10:22.584+0800: [GC (Allocation Failure) [PSYoungGen: 1050384K->1840K(1223168K)] 1073568K->25032K(4019712K), 0.0018710 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
 2021-01-22T14:10:27.171+0800: [GC (Allocation Failure) [PSYoungGen: 1050416K->1824K(1374720K)] 1073608K->25024K(4171264K), 0.0019557 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 2021-01-22T14:10:33.129+0800: [GC (Allocation Failure) [PSYoungGen: 1352480K->1856K(1373696K)] 1375680K->25064K(4170240K), 0.0018448 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```
使用gc easy统计工具，可以看出，压测开始之后，  
1g的时候，gc总暂停时间30.0 ms，暂停23次，对象创建速率222.02 mb/sec,rps= 8249.3
2g的时候，gc总暂停时间40.0 ms，暂停13次，对象创建速率234.76 mb/sec,rps=8178.4
4g的时候，gc总暂停时间40.0 ms，暂停7次，对象创建速率244.46 mb/sec,rps=8307.5
从上看出：  
内存越大，对象创建速度越快，或者理解为gc次数越多，会影响对象创建速度。  
内存越大，平均暂停时间也越多，这对于低延迟项目，在满足需求的前提下，尽量调低内存。
1g的时候虽然暂停23次，对象创建速率最小，但是总暂停时间少，所以rqs排第2  
4g和2g总暂停时间一样，但暂停次数少，对象创建速度快，所以rqs最高  

所以总结出吞吐量受到了【gc总暂停时间】和【对象创建速度】等等的影响。  

## 对ParallelGC参数调优再测试
由于后期基本都是young gc在运作，可以考虑增大young区大小，应该可以提高对象创建速度，结果如下  
```
PS E:\view\java\week2-01> java -Xmx4g -Xms4g -Xmn2g -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T14:41:56.080+0800: [GC (Metadata GC Threshold) [PSYoungGen: 314574K->15359K(1835008K)] 314574K->15375K(3932160K), 0.0088871 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
2021-01-22T14:41:56.089+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 15359K->0K(1835008K)] [ParOldGen: 16K->14470K(2097152K)] 15375K->14470K(3932160K), [Metaspace: 20440K->20440K(1067008K)], 0.0158447 secs] [Times: user=0.17 sys=0.00, real=0.02 secs]
以下为压测开始后的日志
2021-01-22T14:42:18.471+0800: [GC (Metadata GC Threshold) [PSYoungGen: 734009K->19189K(1835008K)] 748479K->33731K(3932160K), 0.0086925 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T14:42:18.480+0800: [Full GC (Metadata GC Threshold) [PSYoungGen: 19189K->0K(1835008K)] [ParOldGen: 14542K->23051K(2097152K)] 33731K->23051K(3932160K), [Metaspace: 33824K->33824K(1079296K)], 0.0291408 secs] [Times: user=0.17 sys=0.00, real=0.03 secs]
2021-01-22T14:42:25.541+0800: [GC (Allocation Failure) [PSYoungGen: 1572864K->2437K(1835008K)] 1595915K->25497K(3932160K), 0.0027137 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T14:42:32.344+0800: [GC (Allocation Failure) [PSYoungGen: 1575301K->1872K(1835008K)] 1598361K->24932K(3932160K), 0.0019475 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T14:42:39.207+0800: [GC (Allocation Failure) [PSYoungGen: 1574736K->1792K(1835008K)] 1597796K->24852K(3932160K), 0.0018615 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T14:42:46.180+0800: [GC (Allocation Failure) [PSYoungGen: 1574656K->1872K(2074112K)] 1597716K->24932K(4171264K), 0.0018441 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```

4g的时候,-Xmn2g，gc总暂停时间40.0 ms，暂停7次，对象创建速率248.11 mb/sec,rps=8252.5，变化不大  

接下来考虑到gc原因有Metadata GC Threshold，所以考虑增Metadata初始值，避免掉该原因引起的gc，看rps是否会增加  
如下：  
```
PS E:\view\java\week2-01> java -Xmx4g -Xms4g -XX:MaxMetaspaceSize=256m -XX:MetaspaceSize=256m -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)
以下为压测开始后的日志
2021-01-22T15:23:41.121+0800: [GC (Allocation Failure) [PSYoungGen: 1048576K->33190K(1223168K)] 1048576K->33294K(4019712K), 0.0156952 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T15:23:45.502+0800: [GC (Allocation Failure) [PSYoungGen: 1081766K->18586K(1223168K)] 1081870K->18714K(4019712K), 0.0092061 secs] [Times: user=0.16 sys=0.00, real=0.01 secs]
2021-01-22T15:23:49.886+0800: [GC (Allocation Failure) [PSYoungGen: 1067162K->17993K(1223168K)] 1067290K->18129K(4019712K), 0.0086516 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:23:54.736+0800: [GC (Allocation Failure) [PSYoungGen: 1066569K->17993K(1223168K)] 1066705K->18137K(4019712K), 0.0079311 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2021-01-22T15:23:59.399+0800: [GC (Allocation Failure) [PSYoungGen: 1066569K->17977K(1223168K)] 1066713K->18129K(4019712K), 0.0074915 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:24:03.870+0800: [GC (Allocation Failure) [PSYoungGen: 1066553K->17941K(1365504K)] 1066705K->18101K(4162048K), 0.0082282 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:24:09.615+0800: [GC (Allocation Failure) [PSYoungGen: 1350165K->320K(1332736K)] 1350325K->18320K(4129280K), 0.0085894 secs] [Times: user=0.05 sys=0.11, real=0.01 secs]
```
如上，增加Metadata初始值确实让Metadata GC Threshold触发导致的gc没有了。
分析日志，gc暂停时间增加到了70毫秒，对象创建速度261.28 mb/sec，有所增加，但是rps变为 8036，还有下降  
可能4g堆内存的时候已经很优化了，所以尝试在堆内存2g的时候增大metaspaceSize。gc日志如下
```
PS E:\view\java\week2-01> java -Xmx2g -Xms2g -XX:MaxMetaspaceSize=256m -XX:MetaspaceSize=256m -XX:+UseParallelGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T15:34:00.741+0800: [GC (Allocation Failure) [PSYoungGen: 524800K->30667K(611840K)] 524800K->30771K(2010112K), 0.0162233 secs] [Times: user=0.14 sys=0.03, real=0.02 secs]
以下为压测开始后的日志
2021-01-22T15:34:07.800+0800: [GC (Allocation Failure) [PSYoungGen: 555467K->20175K(611840K)] 555571K->20303K(2010112K), 0.0100212 secs] [Times: user=0.16 sys=0.01, real=0.01 secs]
2021-01-22T15:34:10.322+0800: [GC (Allocation Failure) [PSYoungGen: 544975K->17973K(611840K)] 545103K->18109K(2010112K), 0.0080669 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:34:12.686+0800: [GC (Allocation Failure) [PSYoungGen: 542773K->17961K(611840K)] 542909K->18105K(2010112K), 0.0077865 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:34:15.080+0800: [GC (Allocation Failure) [PSYoungGen: 542761K->18057K(611840K)] 542905K->18209K(2010112K), 0.0074358 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2021-01-22T15:34:17.479+0800: [GC (Allocation Failure) [PSYoungGen: 542857K->17961K(668160K)] 543009K->18121K(2066432K), 0.0082370 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:34:20.330+0800: [GC (Allocation Failure) [PSYoungGen: 654885K->320K(637440K)] 655045K->18226K(2035712K), 0.0084969 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T15:34:23.172+0800: [GC (Allocation Failure) [PSYoungGen: 637248K->256K(662016K)] 655154K->18242K(2060288K), 0.0014241 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:34:25.927+0800: [GC (Allocation Failure) [PSYoungGen: 628480K->256K(628736K)] 646466K->18250K(2027008K), 0.0012966 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:34:28.719+0800: [GC (Allocation Failure) [PSYoungGen: 628480K->320K(663040K)] 646474K->18314K(2061312K), 0.0014003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:34:31.427+0800: [GC (Allocation Failure) [PSYoungGen: 627520K->288K(663040K)] 645514K->18314K(2061312K), 0.0013488 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:34:34.185+0800: [GC (Allocation Failure) [PSYoungGen: 627488K->288K(663552K)] 645514K->18354K(2061824K), 0.0013757 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
```

以上日志看出，gc次数11次，总暂停时间60.0 ms，有所提高，对象创建速度238.34 mb/sec，rps=8247.3
因为提高了MetaspaceSize,gc次数减少了，总暂停时间增加但对象创建速度提高，最终略微比之前高一些。  

再次总结，gc次数，gc暂停时间，对象创建速度，都会影响rps。

## 分析CMS GC 压测时的gc日志
```
PS E:\view\java\week2-01> java -Xmx1g -Xms1g -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T15:46:41.221+0800: [GC (Allocation Failure) 2021-01-22T15:46:41.221+0800: [ParNew: 279616K->15331K(314560K), 0.0094691 secs] 279616K->15331K(1013632K), 0.0101661 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
2021-01-22T15:46:41.231+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 0K(699072K)] 22322K(1013632K), 0.0012613 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:46:41.232+0800: [CMS-concurrent-mark-start]
2021-01-22T15:46:41.233+0800: [CMS-concurrent-mark: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:46:41.233+0800: [CMS-concurrent-preclean-start]
2021-01-22T15:46:41.234+0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:46:41.234+0800: [CMS-concurrent-abortable-preclean-start]
2021-01-22T15:46:41.696+0800: [CMS-concurrent-abortable-preclean: 0.296/0.462 secs] [Times: user=1.89 sys=0.11, real=0.46 secs]
2021-01-22T15:46:41.696+0800: [GC (CMS Final Remark) [YG occupancy: 173387 K (314560 K)]2021-01-22T15:46:41.697+0800: [Rescan (parallel) , 0.0070391 secs]2021-01-22T15:46:41.704+0800: [weak refs processing, 0.0002967 secs]2021-01-22T15:46:41.704+0800: [class unloading, 0.0029398 secs]2021-01-22T15:46:41.707+0800: [scrub symbol table, 0.0022012 secs]2021-01-22T15:46:41.710+0800: [scrub string table, 0.0004908 secs][1 CMS-remark: 0K(699072K)] 173387K(1013632K), 0.0145939 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T15:46:41.711+0800: [CMS-concurrent-sweep-start]
2021-01-22T15:46:41.712+0800: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:46:41.712+0800: [CMS-concurrent-reset-start]
2021-01-22T15:46:41.715+0800: [CMS-concurrent-reset: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:46:42.196+0800: [GC (Allocation Failure) 2021-01-22T15:46:42.197+0800: [ParNew: 294947K->24448K(314560K), 0.0079637 secs] 294947K->24448K(1013632K), 0.0082882 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
以下为压测开始后的日志
2021-01-22T15:47:01.256+0800: [GC (Allocation Failure) 2021-01-22T15:47:01.257+0800: [ParNew: 304064K->18996K(314560K), 0.0155668 secs] 304064K->24455K(1013632K), 0.0159759 secs] [Times: user=0.09 sys=0.01, real=0.02 secs]
2021-01-22T15:47:02.642+0800: [GC (Allocation Failure) 2021-01-22T15:47:02.642+0800: [ParNew: 298612K->18018K(314560K), 0.0052662 secs] 304071K->23477K(1013632K), 0.0056046 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:47:03.990+0800: [GC (Allocation Failure) 2021-01-22T15:47:03.991+0800: [ParNew: 297634K->19112K(314560K), 0.0052774 secs] 303093K->24571K(1013632K), 0.0056406 secs] [Times: user=0.14 sys=0.00, real=0.01 secs]
2021-01-22T15:47:05.239+0800: [GC (Allocation Failure) 2021-01-22T15:47:05.240+0800: [ParNew: 298728K->19054K(314560K), 0.0048514 secs] 304187K->24513K(1013632K), 0.0052853 secs] [Times: user=0.16 sys=0.00, real=0.01 secs]
2021-01-22T15:47:06.456+0800: [GC (Allocation Failure) 2021-01-22T15:47:06.456+0800: [ParNew: 298670K->21599K(314560K), 0.0045994 secs] 304129K->27058K(1013632K), 0.0049236 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2021-01-22T15:47:07.647+0800: [GC (Allocation Failure) 2021-01-22T15:47:07.647+0800: [ParNew: 301215K->8730K(314560K), 0.0075864 secs] 306674K->23444K(1013632K), 0.0078715 secs] [Times: user=0.14 sys=0.00, real=0.01 secs]
2021-01-22T15:47:08.876+0800: [GC (Allocation Failure) 2021-01-22T15:47:08.876+0800: [ParNew: 288346K->3408K(314560K), 0.0039222 secs] 303060K->21915K(1013632K), 0.0041710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:10.097+0800: [GC (Allocation Failure) 2021-01-22T15:47:10.097+0800: [ParNew: 283024K->864K(314560K), 0.0027692 secs] 301531K->19405K(1013632K), 0.0030312 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:11.324+0800: [GC (Allocation Failure) 2021-01-22T15:47:11.324+0800: [ParNew: 280480K->221K(314560K), 0.0032001 secs] 299021K->18768K(1013632K), 0.0034771 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:12.532+0800: [GC (Allocation Failure) 2021-01-22T15:47:12.532+0800: [ParNew: 279837K->84K(314560K), 0.0031789 secs] 298384K->18631K(1013632K), 0.0034525 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:13.742+0800: [GC (Allocation Failure) 2021-01-22T15:47:13.742+0800: [ParNew: 279700K->43K(314560K), 0.0030481 secs] 298247K->18594K(1013632K), 0.0033442 secs] [Times: user=0.16 sys=0.00, real=0.00 secs]
2021-01-22T15:47:14.948+0800: [GC (Allocation Failure) 2021-01-22T15:47:14.948+0800: [ParNew: 279659K->36K(314560K), 0.0032224 secs] 298210K->18587K(1013632K), 0.0035149 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:16.153+0800: [GC (Allocation Failure) 2021-01-22T15:47:16.153+0800: [ParNew: 279652K->58K(314560K), 0.0029802 secs] 298203K->18610K(1013632K), 0.0032644 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:17.386+0800: [GC (Allocation Failure) 2021-01-22T15:47:17.386+0800: [ParNew: 279674K->36K(314560K), 0.0031842 secs] 298226K->18588K(1013632K), 0.0034295 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:18.592+0800: [GC (Allocation Failure) 2021-01-22T15:47:18.592+0800: [ParNew: 279652K->36K(314560K), 0.0032435 secs] 298204K->18587K(1013632K), 0.0035393 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:19.796+0800: [GC (Allocation Failure) 2021-01-22T15:47:19.796+0800: [ParNew: 279652K->28K(314560K), 0.0032736 secs] 298203K->18581K(1013632K), 0.0035771 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:21.068+0800: [GC (Allocation Failure) 2021-01-22T15:47:21.069+0800: [ParNew: 279644K->46K(314560K), 0.0030659 secs] 298197K->18599K(1013632K), 0.0033784 secs] [Times: user=0.16 sys=0.00, real=0.00 secs]
2021-01-22T15:47:22.265+0800: [GC (Allocation Failure) 2021-01-22T15:47:22.265+0800: [ParNew: 279662K->39K(314560K), 0.0032249 secs] 298215K->18592K(1013632K), 0.0034854 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:23.489+0800: [GC (Allocation Failure) 2021-01-22T15:47:23.489+0800: [ParNew: 279655K->48K(314560K), 0.0031568 secs] 298208K->18602K(1013632K), 0.0034459 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:24.687+0800: [GC (Allocation Failure) 2021-01-22T15:47:24.688+0800: [ParNew: 279664K->35K(314560K), 0.0031636 secs] 298218K->18588K(1013632K), 0.0034419 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:25.914+0800: [GC (Allocation Failure) 2021-01-22T15:47:25.915+0800: [ParNew: 279651K->44K(314560K), 0.0031900 secs] 298204K->18597K(1013632K), 0.0034781 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:27.110+0800: [GC (Allocation Failure) 2021-01-22T15:47:27.111+0800: [ParNew: 279660K->31K(314560K), 0.0031391 secs] 298213K->18585K(1013632K), 0.0034046 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:28.324+0800: [GC (Allocation Failure) 2021-01-22T15:47:28.324+0800: [ParNew: 279647K->34K(314560K), 0.0032339 secs] 298201K->18588K(1013632K), 0.0035382 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:29.524+0800: [GC (Allocation Failure) 2021-01-22T15:47:29.524+0800: [ParNew: 279650K->37K(314560K), 0.0031781 secs] 298204K->18591K(1013632K), 0.0034607 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

PS E:\view\java\week2-01> java -Xmx2g -Xms2g -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T15:47:56.420+0800: [GC (Allocation Failure) 2021-01-22T15:47:56.420+0800: [ParNew: 559232K->29719K(629120K), 0.0140224 secs] 559232K->29719K(2027264K), 0.0144353 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T15:47:56.434+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 0K(1398144K)] 35647K(2027264K), 0.0021684 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:56.437+0800: [CMS-concurrent-mark-start]
2021-01-22T15:47:56.437+0800: [CMS-concurrent-mark: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:47:56.439+0800: [CMS-concurrent-preclean-start]
2021-01-22T15:47:56.442+0800: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.08 sys=0.00, real=0.01 secs]
2021-01-22T15:47:56.446+0800: [CMS-concurrent-abortable-preclean-start]
 CMS: abort preclean due to time 2021-01-22T15:48:01.485+0800: [CMS-concurrent-abortable-preclean: 2.034/5.037 secs] [Times: user=4.38 sys=0.02, real=5.04 secs]
2021-01-22T15:48:01.486+0800: [GC (CMS Final Remark) [YG occupancy: 229816 K (629120 K)]2021-01-22T15:48:01.486+0800: [Rescan (parallel) , 0.0092704 secs]2021-01-22T15:48:01.496+0800: [weak refs processing, 0.0004414 secs]2021-01-22T15:48:01.496+0800: [class unloading, 0.0049129 secs]2021-01-22T15:48:01.501+0800: [scrub symbol table, 0.0039374 secs]2021-01-22T15:48:01.505+0800: [scrub string table, 0.0004747 secs][1 CMS-remark: 0K(1398144K)] 229816K(2027264K), 0.0213021 secs] [Times: user=0.16 sys=0.00, real=0.02 secs]
2021-01-22T15:48:01.512+0800: [CMS-concurrent-sweep-start]
2021-01-22T15:48:01.512+0800: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:48:01.514+0800: [CMS-concurrent-reset-start]
2021-01-22T15:48:01.519+0800: [CMS-concurrent-reset: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
以下为压测开始后的日志
2021-01-22T15:48:38.902+0800: [GC (Allocation Failure) 2021-01-22T15:48:38.903+0800: [ParNew: 588951K->25821K(629120K), 0.0080538 secs] 588951K->25821K(2027264K), 0.0083884 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:48:41.664+0800: [GC (Allocation Failure) 2021-01-22T15:48:41.664+0800: [ParNew: 585053K->27830K(629120K), 0.0061958 secs] 585053K->27830K(2027264K), 0.0065662 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:48:44.154+0800: [GC (Allocation Failure) 2021-01-22T15:48:44.154+0800: [ParNew: 587062K->24226K(629120K), 0.0061547 secs] 587062K->24226K(2027264K), 0.0064391 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:48:46.624+0800: [GC (Allocation Failure) 2021-01-22T15:48:46.624+0800: [ParNew: 583458K->26290K(629120K), 0.0062223 secs] 583458K->26290K(2027264K), 0.0064948 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:48:49.095+0800: [GC (Allocation Failure) 2021-01-22T15:48:49.095+0800: [ParNew: 585522K->26386K(629120K), 0.0062390 secs] 585522K->26386K(2027264K), 0.0065525 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:48:51.641+0800: [GC (Allocation Failure) 2021-01-22T15:48:51.641+0800: [ParNew: 585618K->17600K(629120K), 0.0245889 secs] 585618K->32033K(2027264K), 0.0248824 secs] [Times: user=0.13 sys=0.00, real=0.02 secs]
2021-01-22T15:48:54.159+0800: [GC (Allocation Failure) 2021-01-22T15:48:54.160+0800: [ParNew: 576832K->5484K(629120K), 0.0054009 secs] 591265K->24283K(2027264K), 0.0057271 secs] [Times: user=0.16 sys=0.00, real=0.01 secs]
2021-01-22T15:48:56.675+0800: [GC (Allocation Failure) 2021-01-22T15:48:56.675+0800: [ParNew: 564716K->1379K(629120K), 0.0036117 secs] 583515K->20200K(2027264K), 0.0038864 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:48:59.190+0800: [GC (Allocation Failure) 2021-01-22T15:48:59.190+0800: [ParNew: 560611K->348K(629120K), 0.0034150 secs] 579432K->19174K(2027264K), 0.0036688 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:49:01.663+0800: [GC (Allocation Failure) 2021-01-22T15:49:01.663+0800: [ParNew: 559580K->91K(629120K), 0.0036311 secs] 578406K->18916K(2027264K), 0.0039457 secs] [Times: user=0.16 sys=0.00, real=0.00 secs]
2021-01-22T15:49:04.187+0800: [GC (Allocation Failure) 2021-01-22T15:49:04.187+0800: [ParNew: 559323K->38K(629120K), 0.0035909 secs] 578148K->18864K(2027264K), 0.0039084 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:49:06.678+0800: [GC (Allocation Failure) 2021-01-22T15:49:06.679+0800: [ParNew: 559270K->34K(629120K), 0.0035101 secs] 578096K->18859K(2027264K), 0.0038220 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

PS E:\view\java\week2-01> java -Xmx4g -Xms4g -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T15:49:29.098+0800: [GC (Allocation Failure) 2021-01-22T15:49:29.098+0800: [ParNew: 681600K->33704K(766784K), 0.0163509 secs] 681600K->33704K(4109120K), 0.0170101 secs] [Times: user=0.16 sys=0.00, real=0.02 secs]
2021-01-22T15:49:29.116+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 0K(3342336K)] 44609K(4109120K), 0.0032517 secs] [Times: user=0.00 sys=0.02, real=0.00 secs]
2021-01-22T15:49:29.119+0800: [CMS-concurrent-mark-start]
2021-01-22T15:49:29.120+0800: [CMS-concurrent-mark: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:49:29.129+0800: [CMS-concurrent-preclean-start]
2021-01-22T15:49:29.133+0800: [CMS-concurrent-preclean: 0.004/0.004 secs] [Times: user=0.02 sys=0.01, real=0.01 secs]
2021-01-22T15:49:29.136+0800: [CMS-concurrent-abortable-preclean-start]
 CMS: abort preclean due to time 2021-01-22T15:49:34.304+0800: [CMS-concurrent-abortable-preclean: 2.184/5.166 secs] [Times: user=3.55 sys=0.03, real=5.17 secs]
2021-01-22T15:49:34.304+0800: [GC (CMS Final Remark) [YG occupancy: 108682 K (766784 K)]2021-01-22T15:49:34.305+0800: [Rescan (parallel) , 0.0076067 secs]2021-01-22T15:49:34.313+0800: [weak refs processing, 0.0002499 secs]2021-01-22T15:49:34.314+0800: [class unloading, 0.0044650 secs]2021-01-22T15:49:34.318+0800: [scrub symbol table, 0.0032620 secs]2021-01-22T15:49:34.323+0800: [scrub string table, 0.0005955 secs][1 CMS-remark: 0K(3342336K)] 108682K(4109120K), 0.0198569 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
2021-01-22T15:49:34.330+0800: [CMS-concurrent-sweep-start]
2021-01-22T15:49:34.330+0800: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:49:34.330+0800: [CMS-concurrent-reset-start]
2021-01-22T15:49:34.343+0800: [CMS-concurrent-reset: 0.012/0.012 secs] [Times: user=0.00 sys=0.02, real=0.01 secs]
以下为压测开始后的日志
2021-01-22T15:50:10.689+0800: [GC (Allocation Failure) 2021-01-22T15:50:10.689+0800: [ParNew: 715304K->22558K(766784K), 0.0084725 secs] 715304K->22558K(4109120K), 0.0088133 secs] [Times: user=0.14 sys=0.02, real=0.01 secs]
2021-01-22T15:50:13.777+0800: [GC (Allocation Failure) 2021-01-22T15:50:13.778+0800: [ParNew: 704158K->22242K(766784K), 0.0064721 secs] 704158K->22242K(4109120K), 0.0067689 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:50:16.719+0800: [GC (Allocation Failure) 2021-01-22T15:50:16.720+0800: [ParNew: 703842K->26244K(766784K), 0.0064368 secs] 703842K->26244K(4109120K), 0.0072942 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-22T15:50:19.669+0800: [GC (Allocation Failure) 2021-01-22T15:50:19.669+0800: [ParNew: 707844K->24619K(766784K), 0.0064623 secs] 707844K->24619K(4109120K), 0.0067314 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:50:22.665+0800: [GC (Allocation Failure) 2021-01-22T15:50:22.665+0800: [ParNew: 706219K->22892K(766784K), 0.0069949 secs] 706219K->22892K(4109120K), 0.0072356 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:50:25.668+0800: [GC (Allocation Failure) 2021-01-22T15:50:25.669+0800: [ParNew: 704492K->17614K(766784K), 0.0308580 secs] 704492K->34024K(4109120K), 0.0312340 secs] [Times: user=0.13 sys=0.05, real=0.03 secs]
2021-01-22T15:50:28.668+0800: [GC (Allocation Failure) 2021-01-22T15:50:28.668+0800: [ParNew: 699214K->4900K(766784K), 0.0055091 secs] 715624K->23966K(4109120K), 0.0058226 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-22T15:50:31.623+0800: [GC (Allocation Failure) 2021-01-22T15:50:31.623+0800: [ParNew: 686500K->1229K(766784K), 0.0038832 secs] 705566K->20300K(4109120K), 0.0041800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-01-22T15:50:34.600+0800: [GC (Allocation Failure) 2021-01-22T15:50:34.601+0800: [ParNew: 682829K->311K(766784K), 0.0038398 secs] 701900K->19382K(4109120K), 0.0041572 secs] [Times: user=0.16 sys=0.00, real=0.01 secs]
```
使用CMS gc
1g的时候，rps=8189,gc总暂停时间60.0 ms,触发24次，无full gc，对象创建速度232.68 mb/sec
2g的时候，rps=8168,gc总暂停时间70.0 ms,触发12次，无full gc，对象创建速度	236.99 mb/sec
4g的时候，rps=8162,gc总暂停时间100 ms，触发9次，无full gc,对象创建速度   251.91 mb/sec
分析各指标，还是堆内存越大，对象创建速度越快，因为gc次数少了。虽然暂停时间在增加，但因为gc除了暂停，还有并发运行的时间，会降低系统性能。
依然还是gc次数，暂停时间，等对rps产生综合影响。

## 分析G1 GC 压测时的gc日志
使用gc日志，如果参数设置为-XX:+PrintGCDetails，比较消耗性能，最上面的表格是使用-XX:+PrintGCDetails执行的压测结果，下面的日志使用-XX:+PrintGC，缩减日志量，方便分析  
```
PS E:\view\java\week2-01> java -Xmx1g -Xms1g -XX:+UseG1GC -XX:+PrintGC  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar
2021-01-22T16:11:44.016+0800: [GC pause (G1 Evacuation Pause) (young) 51M->4404K(1024M), 0.0033620 secs]
2021-01-22T16:11:44.133+0800: [GC pause (G1 Evacuation Pause) (young) 62M->9998K(1024M), 0.0043309 secs]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T16:11:44.354+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 135M->20M(1024M), 0.0078346 secs]
2021-01-22T16:11:44.362+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:11:44.366+0800: [GC concurrent-root-region-scan-end, 0.0040328 secs]
2021-01-22T16:11:44.366+0800: [GC concurrent-mark-start]
2021-01-22T16:11:44.366+0800: [GC concurrent-mark-end, 0.0002775 secs]
2021-01-22T16:11:44.367+0800: [GC remark, 0.0024329 secs]
2021-01-22T16:11:44.370+0800: [GC cleanup 22M->21M(1024M), 0.0007774 secs]
2021-01-22T16:11:44.371+0800: [GC concurrent-cleanup-start]
2021-01-22T16:11:44.371+0800: [GC concurrent-cleanup-end, 0.0001016 secs]
以下为压测开始后的日志
2021-01-22T16:13:38.707+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 413M->26M(1024M), 0.0131429 secs]
2021-01-22T16:13:38.720+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:13:38.727+0800: [GC concurrent-root-region-scan-end, 0.0064594 secs]
2021-01-22T16:13:38.727+0800: [GC concurrent-mark-start]
2021-01-22T16:13:38.727+0800: [GC concurrent-mark-end, 0.0003057 secs]
2021-01-22T16:13:38.728+0800: [GC remark, 0.0040159 secs]
2021-01-22T16:13:38.732+0800: [GC cleanup 30M->30M(1024M), 0.0009384 secs]
2021-01-22T16:13:41.930+0800: [GC pause (G1 Evacuation Pause) (young) 613M->20M(1024M), 0.0104289 secs]
2021-01-22T16:13:44.885+0800: [GC pause (G1 Evacuation Pause) (young) 613M->21M(1024M), 0.0093020 secs]
2021-01-22T16:13:47.593+0800: [GC pause (G1 Evacuation Pause) (young) 613M->21M(1024M), 0.0088281 secs]
2021-01-22T16:13:50.327+0800: [GC pause (G1 Evacuation Pause) (young) 613M->19M(1024M), 0.0088057 secs]
2021-01-22T16:13:53.099+0800: [GC pause (G1 Evacuation Pause) (young) 613M->21M(1024M), 0.0093087 secs]
2021-01-22T16:13:55.780+0800: [GC pause (G1 Evacuation Pause) (young) 613M->19M(1024M), 0.0088716 secs]
2021-01-22T16:13:58.504+0800: [GC pause (G1 Evacuation Pause) (young) 613M->21M(1024M), 0.0089487 secs]
2021-01-22T16:14:01.298+0800: [GC pause (G1 Evacuation Pause) (young) 613M->20M(1024M), 0.0091319 secs]
2021-01-22T16:14:04.006+0800: [GC pause (G1 Evacuation Pause) (young) 613M->19M(1024M), 0.0088548 secs]
2021-01-22T16:14:06.719+0800: [GC pause (G1 Evacuation Pause) (young) 613M->20M(1024M), 0.0091143 secs]


PS E:\view\java\week2-01> java -Xmx2g -Xms2g -XX:+UseG1GC -XX:+PrintGC  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar
2021-01-22T16:14:52.477+0800: [GC pause (G1 Evacuation Pause) (young) 102M->7038K(2048M), 0.0056360 secs]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T16:14:52.662+0800: [GC pause (G1 Evacuation Pause) (young) 110M->10M(2048M), 0.0065583 secs]
2021-01-22T16:14:52.725+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 39M->9215K(2048M), 0.0045368 secs]
2021-01-22T16:14:52.730+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:14:52.733+0800: [GC concurrent-root-region-scan-end, 0.0030949 secs]
2021-01-22T16:14:52.733+0800: [GC concurrent-mark-start]
2021-01-22T16:14:52.733+0800: [GC concurrent-mark-end, 0.0003320 secs]
2021-01-22T16:14:52.734+0800: [GC remark, 0.0027710 secs]
2021-01-22T16:14:52.737+0800: [GC cleanup 10M->10M(2048M), 0.0010921 secs]
以下为压测开始后的日志
2021-01-22T16:15:08.062+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 405M->25M(2048M), 0.0136655 secs]
2021-01-22T16:15:08.076+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:15:08.082+0800: [GC concurrent-root-region-scan-end, 0.0056712 secs]
2021-01-22T16:15:08.082+0800: [GC concurrent-mark-start]
2021-01-22T16:15:08.082+0800: [GC concurrent-mark-end, 0.0004034 secs]
2021-01-22T16:15:08.083+0800: [GC remark, 0.0040669 secs]
2021-01-22T16:15:08.087+0800: [GC cleanup 30M->30M(2048M), 0.0013849 secs]
2021-01-22T16:15:13.626+0800: [GC pause (G1 Evacuation Pause) (young) 1072M->22M(2048M), 0.0110672 secs]
2021-01-22T16:15:18.923+0800: [GC pause (G1 Evacuation Pause) (young) 1151M->20M(2048M), 0.0109459 secs]
2021-01-22T16:15:24.429+0800: [GC pause (G1 Evacuation Pause) (young) 1227M->21M(2048M), 0.0101082 secs]
2021-01-22T16:15:29.959+0800: [GC pause (G1 Evacuation Pause) (young) 1227M->20M(2048M), 0.0098907 secs]
2021-01-22T16:15:35.471+0800: [GC pause (G1 Evacuation Pause) (young) 1227M->20M(2048M), 0.0093488 secs]


PS E:\view\java\week2-01> java -Xmx4g -Xms4g -XX:+UseG1GC -XX:+PrintGC  -XX:+PrintGCDateStamps -jar .\gateway-server-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2021-01-22T16:16:00.447+0800: [GC pause (G1 Evacuation Pause) (young) 204M->12M(4096M), 0.0082759 secs]
2021-01-22T16:16:00.516+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 43M->11M(4096M), 0.0051218 secs]
2021-01-22T16:16:00.522+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:16:00.525+0800: [GC concurrent-root-region-scan-end, 0.0034328 secs]
2021-01-22T16:16:00.526+0800: [GC concurrent-mark-start]
2021-01-22T16:16:00.526+0800: [GC concurrent-mark-end, 0.0006813 secs]
2021-01-22T16:16:00.533+0800: [GC remark, 0.0024456 secs]
2021-01-22T16:16:00.536+0800: [GC cleanup 15M->15M(4096M), 0.0015294 secs]
2021-01-22T16:16:01.435+0800: [GC pause (G1 Evacuation Pause) (young) 245M->22M(4096M), 0.0118668 secs]
以下为压测开始后的日志
2021-01-22T16:16:37.072+0800: [GC pause (Metadata GC Threshold) (young) (initial-mark) 203M->24M(4096M), 0.0102011 secs]
2021-01-22T16:16:37.083+0800: [GC concurrent-root-region-scan-start]
2021-01-22T16:16:37.091+0800: [GC concurrent-root-region-scan-end, 0.0085605 secs]
2021-01-22T16:16:37.092+0800: [GC concurrent-mark-start]
2021-01-22T16:16:37.092+0800: [GC concurrent-mark-end, 0.0004774 secs]
2021-01-22T16:16:37.092+0800: [GC remark, 0.0036281 secs]
2021-01-22T16:16:37.096+0800: [GC cleanup 38M->38M(4096M), 0.0013295 secs]
2021-01-22T16:16:41.927+0800: [GC pause (G1 Evacuation Pause) (young) 944M->23M(4096M), 0.0103393 secs]
2021-01-22T16:16:52.960+0800: [GC pause (G1 Evacuation Pause) (young) 2455M->24M(4096M), 0.0109811 secs]
2021-01-22T16:17:03.632+0800: [GC pause (G1 Evacuation Pause) (young) 2454M->20M(4096M), 0.0092736 secs]
```

使用g1时，  -XX:+PrintGC参数
1g的时候，rps=8109.4，gc总暂停时间92.8 ms，次数12（10次young ,一次clean,一次并发标记），创建速度211.3 mb/sec
2g的时候，rps=8154.5，gc总暂停时间53.1 ms，次数7（5次young ,一次clean,一次并发标记），创建速度211.28 mb/sec
4g的时候，rps=8202.7，gc总暂停时间32.4 ms，次数5（1次young ,一次clean,一次并发标记），创建速度217.17 mb/sec
从上可以看出，rps跟gc暂停时间，次数有关系。

**SerialGC 已经基本不用，详细的就不比较了**

## 总结：
从压测结果rps，并行gc和串行gc吞吐量都较高。
具体到每个gc，rps的大小，跟以下关系密切：
gc的发生的次数（堆内存越大，次数越少），  
gc暂停时间（堆内存越大，gc暂停时间越大），  
对象创建速度（堆内存越大，速度越快），  
等等  
需要均衡配置，上线之前各种参数调节着进行压测，选出性能较好的配置。  