#1.以下是使用不同gc运行GCLogAnalysis的打印结果

|gc类型|256m|1g|2g|4g|8g|12g|
|----|----|----|----|----|----|---|
|SerialGC|OOM错误|17971|18859|18333|16770|12461|
|ParallelGC|OOM错误|15583|19068|21232|18263|12358|
|CMS|OOM错误/4405|18231|18864|19958|19355|18888|
|G1|OOM错误|17253|17925|18650|19749|20207|

从上表格可以看出，使用g1,内存12g的时候，创建出来的对象最多。  
CMS gc在256m的时候，有几次还能打印出创建对象个数，但数量很少，其他gc基本上1秒之内都内存泄漏了。

#2.gc日志分析
接下来进行日志分析，分析为什么会出现上面的情况。
（以下分区大小，没有除以1024,是除以1000估算的，只是为了大致了解）  

##使用SerialGC 256m时日志如下，进行日志解析以及分析OOM错误原因
PS F:\> **java -Xmx256m -Xms256m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis**
正在执行...
2021-01-21T23:33:24.523+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.541+0800: [DefNew: 69902K->8704K(78656K), 0.0076558 secs] 69902K->24024K(253440K), 0.0256551 secs] [Times: user=0.00 sys=0.00, real=0.03 secs]
时间戳，触发gc ,触发原因是Allocation Failure，时间戳，触发的yong gc,yong区从69m减少到8m(共78m)，耗时7毫秒，堆内存从69m减少到了24m(共253m)，堆剩余空间大于young区，是因为有16m数据跃升到了老年代，gc共耗时25毫秒，后面是系统信息,times是cpu运行时间，sys是io等时间，real是gc实际耗时
        
2021-01-21T23:33:24.560+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.560+0800: [DefNew: 78656K->8703K(78656K), 0.0108928 secs] 93976K->48139K(253440K), 0.0110190 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
                                                                                    yong区从78m减少到8m，堆从93m减少到48m，老年代有48-8=40m
                                                                                    
2021-01-21T23:33:24.580+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.581+0800: [DefNew: 78549K->8702K(78656K), 0.0089487 secs] 117985K->73607K(253440K), 0.0104977 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-21T23:33:24.599+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.600+0800: [DefNew: 78654K->8700K(78656K), 0.0078997 secs] 143559K->94914K(253440K), 0.0090430 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
2021-01-21T23:33:24.617+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.617+0800: [DefNew: 78652K->8703K(78656K), 0.0079676 secs] 164866K->115236K(253440K), 0.0081853 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-21T23:33:24.634+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.634+0800: [DefNew: 78655K->8703K(78656K), 0.0083831 secs] 185188K->138572K(253440K), 0.0085945 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-21T23:33:24.652+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.652+0800: [DefNew: 78624K->8703K(78656K), 0.0083384 secs] 208493K->161641K(253440K), 0.0085408 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-01-21T23:33:24.669+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.670+0800: [DefNew: 78655K->78655K(78656K), 0.0002269 secs]2021-01-21T23:33:24.670+0800: [Tenured: 152937K->157509K(174784K), 0.0192446 secs] 231593K->157509K(253440K), [Metaspace: 2632K->2632K(1056768K)], 0.0279833 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
                                                                                    young 区从78m到78m，空间不足，清理失败，提升为full gc,老年代从152m变化到157m，堆内存从231m减少到157m,由此推断出年轻代数据全部移动到了老年代。
                                                                                    
2021-01-21T23:33:24.707+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.709+0800: [DefNew: 69952K->69952K(78656K), 0.0000777 secs]2021-01-21T23:33:24.709+0800: [Tenured: 157509K->172935K(174784K), 0.0226460 secs] 227461K->172935K(253440K), [Metaspace: 2633K->2633K(1056768K)], 0.0255646 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
                                                                                    young 区从69m到69m，空间不足清理失败，提升为full gc,老年代从157m变为172m，堆从227m减少到172m,年轻代的对象仍然全部清除到了老年代。

2021-01-21T23:33:24.742+0800: [GC (Allocation Failure) 2021-01-21T23:33:24.743+0800: [DefNew: 69952K->69952K(78656K), 0.0001144 secs]2021-01-21T23:33:24.743+0800: [Tenured: 172935K->174678K(174784K), 0.0226624 secs] 242887K->184236K(253440K), [Metaspace: 2633K->2633K(1056768K)], 0.0238737 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
                                                                                    young 区从69m到69m，空间不足清理失败，提升为full gc,与上面不同的是，这次年轻代还剩余了一些对象。应该是因为老年代空间也不足了。几乎同时，就发生了下面的full gc

2021-01-21T23:33:24.775+0800: [Full GC (Allocation Failure) 2021-01-21T23:33:24.775+0800: [Tenured: 174678K->174480K(174784K), 0.0268188 secs] 253034K->188518K(253440K), [Metaspace: 2633K->2633K(1056768K)], 0.0271855 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]

略......

2021-01-21T23:33:25.406+0800: [Full GC (Allocation Failure) 2021-01-21T23:33:25.408+0800: [Tenured: 174769K->174659K(174784K), 0.0297556 secs] 252595K->252232K(253440K), [Metaspace: 2633K->2633K(1056768K)], 0.0319271 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
                                                                                                   到这里，内存基本用完，无法回收，报错内存泄漏
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
        at GCLogAnalysis.main(GCLogAnalysis.java:25)

##使用ParallelGC 1g和4g时的分析比较
1g时创建对象数量较少，4g时创建对象数量较多  
**PS F:\> java -Xmx1g -Xms1g -XX:+UseParallelGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis**    
正在执行...  
2021-01-22T00:39:59.099+0800: [GC (Allocation Failure) [PSYoungGen: 262144K->43515K(305664K)] 262144K->87526K(1005056K), 0.0151410 secs] [Times: user=0.13 sys=0.08, real=0.02 secs]  
2021-01-22T00:39:59.147+0800: [GC (Allocation Failure) [PSYoungGen: 305659K->43510K(305664K)] 349670K->165971K(1005056K), 0.0210967 secs] [Times: user=0.08 sys=0.13, real=0.02 secs]  
2021-01-22T00:39:59.198+0800: [GC (Allocation Failure) [PSYoungGen: 305654K->43517K(305664K)] 428115K->243200K(1005056K), 0.0199431 secs] [Times: user=0.13 sys=0.08, real=0.02 secs]  
2021-01-22T00:39:59.246+0800: [GC (Allocation Failure) [PSYoungGen: 305661K->43511K(305664K)] 505344K->313031K(1005056K), 0.0198108 secs] [Times: user=0.09 sys=0.11, real=0.02 secs]  
2021-01-22T00:39:59.294+0800: [GC (Allocation Failure) [PSYoungGen: 305655K->43508K(305664K)] 575175K->390016K(1005056K), 0.0204167 secs] [Times: user=0.09 sys=0.11, real=0.02 secs]  
2021-01-22T00:39:59.343+0800: [GC (Allocation Failure) [PSYoungGen: 305652K->43516K(160256K)] 652160K->468410K(859648K), 0.0207510 secs] [Times: user=0.20 sys=0.00, real=0.02 secs]  
2021-01-22T00:39:59.377+0800: [GC (Allocation Failure) [PSYoungGen: 160252K->74186K(232960K)] 585146K->506903K(932352K), 0.0141333 secs] [Times: user=0.13 sys=0.08, real=0.01 secs]  
2021-01-22T00:39:59.405+0800: [GC (Allocation Failure) [PSYoungGen: 190922K->99099K(232960K)] 623639K->539252K(932352K), 0.0172338 secs] [Times: user=0.20 sys=0.00, real=0.02 secs]  
2021-01-22T00:39:59.436+0800: [GC (Allocation Failure) [PSYoungGen: 215835K->113981K(232960K)] 655988K->571871K(932352K), 0.0211215 secs] [Times: user=0.19 sys=0.00, real=0.02 secs]  
2021-01-22T00:39:59.470+0800: [GC (Allocation Failure) [PSYoungGen: 230717K->75701K(232960K)] 688607K->599157K(932352K), 0.0223504 secs] [Times: user=0.27 sys=0.14, real=0.02 secs]  
2021-01-22T00:39:59.506+0800: [GC (Allocation Failure) [PSYoungGen: 192437K->35993K(232960K)] 715893K->627939K(932352K), 0.0171685 secs] [Times: user=0.33 sys=0.05, real=0.02 secs]  
2021-01-22T00:39:59.524+0800: [Full GC (Ergonomics) [PSYoungGen: 35993K->0K(232960K)] [ParOldGen: 591946K->330685K(699392K)] 627939K->330685K(932352K), [Metaspace: 2633K->2633K(1056768K)], 0.0582789 secs] [Times: user=0.59 sys=0.00, real=0.06 secs]  
2021-01-22T00:39:59.595+0800: [GC (Allocation Failure) [PSYoungGen: 116656K->35413K(232960K)] 447342K->366099K(932352K), 0.0063051 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.615+0800: [GC (Allocation Failure) [PSYoungGen: 151474K->39492K(232960K)] 482160K->402077K(932352K), 0.0114953 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.640+0800: [GC (Allocation Failure) [PSYoungGen: 156228K->39169K(232960K)] 518813K->435349K(932352K), 0.0116473 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.665+0800: [GC (Allocation Failure) [PSYoungGen: 155903K->46285K(232960K)] 552083K->475062K(932352K), 0.0127199 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.691+0800: [GC (Allocation Failure) [PSYoungGen: 163021K->39345K(232960K)] 591798K->509438K(932352K), 0.0129639 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.717+0800: [GC (Allocation Failure) [PSYoungGen: 156081K->37291K(232960K)] 626174K->541065K(932352K), 0.0116677 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.742+0800: [GC (Allocation Failure) [PSYoungGen: 154027K->38474K(232960K)] 657801K->575533K(932352K), 0.0120747 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.768+0800: [GC (Allocation Failure) [PSYoungGen: 155065K->37354K(232960K)] 692124K->609611K(932352K), 0.0119996 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.793+0800: [GC (Allocation Failure) [PSYoungGen: 154090K->39033K(232960K)] 726347K->644940K(932352K), 0.0117962 secs] [Times: user=0.14 sys=0.06, real=0.01 secs]  
2021-01-22T00:39:59.819+0800: [GC (Allocation Failure) [PSYoungGen: 155769K->38649K(232960K)] 761676K->680062K(932352K), 0.0121312 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.831+0800: [Full GC (Ergonomics) [PSYoungGen: 38649K->0K(232960K)] [ParOldGen: 641413K->366568K(699392K)] 680062K->366568K(932352K), [Metaspace: 2633K->2633K(1056768K)], 0.0649698 secs] [Times: user=0.55 sys=0.00, real=0.07 secs]  
2021-01-22T00:39:59.911+0800: [GC (Allocation Failure) [PSYoungGen: 116736K->32220K(232960K)] 483304K->398789K(932352K), 0.0060577 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.931+0800: [GC (Allocation Failure) [PSYoungGen: 148956K->39170K(232960K)] 515525K->434378K(932352K), 0.0111550 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.956+0800: [GC (Allocation Failure) [PSYoungGen: 155906K->37479K(232960K)] 551114K->468961K(932352K), 0.0119467 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:39:59.979+0800: [GC (Allocation Failure) [PSYoungGen: 154215K->37625K(232960K)] 585697K->503266K(932352K), 0.0118859 secs] [Times: user=0.20 sys=0.00, real=0.01 secs]  
2021-01-22T00:40:00.006+0800: [GC (Allocation Failure) [PSYoungGen: 154361K->37923K(232960K)] 620002K->538139K(932352K), 0.0121795 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
执行结束!共生成对象次数:14840

PS F:\> java -Xmx4g -Xms4g -XX:+UseParallelGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
正在执行...
2021-01-22T00:40:17.813+0800: [GC (Allocation Failure) [PSYoungGen: 1048576K->174587K(1223168K)] 1048576K->238874K(4019712K), 0.0385447 secs] [Times: user=0.30 sys=0.31, real=0.04 secs]  
2021-01-22T00:40:17.967+0800: [GC (Allocation Failure) [PSYoungGen: 1223163K->174590K(1223168K)] 1287450K->358661K(4019712K), 0.0462683 secs] [Times: user=0.16 sys=0.45, real=0.05 secs]  
2021-01-22T00:40:18.124+0800: [GC (Allocation Failure) [PSYoungGen: 1223166K->174586K(1223168K)] 1407237K->486853K(4019712K), 0.0465691 secs] [Times: user=0.50 sys=0.11, real=0.05 secs]  
2021-01-22T00:40:18.284+0800: [GC (Allocation Failure) [PSYoungGen: 1223162K->174587K(1223168K)] 1535429K->608412K(4019712K), 0.0455958 secs] [Times: user=0.42 sys=0.19, real=0.05 secs]  
2021-01-22T00:40:18.442+0800: [GC (Allocation Failure) [PSYoungGen: 1223163K->174588K(1223168K)] 1656988K->731505K(4019712K), 0.0464641 secs] [Times: user=0.52 sys=0.09, real=0.05 secs]  
执行结束!共生成对象次数:20361  


从上面的日志看出，  
1g内存时，进行了28次gc(包括2次full gc)，总gc时间预估有490毫秒，程序运行时间500毫秒 
4g的时候执行了5次gc,没有full gc,总gc时间240毫秒，程序运行时间700毫秒，从时间来看，比1g多40%的时间创建对象，数量也多40%左右，刚好符合  
所以并行gc的时候，由于4g时gc总耗时比1g少很多，造成了4g时性能好。


##使用CMS gc 1G的时候比Parallel性能好，4G的时候比Parallel性能差
cms日志文件如下：  
**PS F:\> java -Xmx1g -Xms1g -XX:+UseConcMarkSweepGC -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis**  
正在执行...  
2021-01-22T00:59:26.141+0800: [GC (Allocation Failure)  279616K->81474K(1013632K), 0.0145582 secs]  
2021-01-22T00:59:26.192+0800: [GC (Allocation Failure)  361090K->162718K(1013632K), 0.0207316 secs]  
2021-01-22T00:59:26.246+0800: [GC (Allocation Failure)  442334K->238421K(1013632K), 0.0296538 secs]  
2021-01-22T00:59:26.306+0800: [GC (Allocation Failure)  517941K->309705K(1013632K), 0.0291990 secs]  
2021-01-22T00:59:26.366+0800: [GC (Allocation Failure)  589321K->388617K(1013632K), 0.0315292 secs]  
2021-01-22T00:59:26.398+0800: [GC (CMS Initial Mark)  388984K(1013632K), 0.0005766 secs]  
2021-01-22T00:59:26.431+0800: [GC (Allocation Failure)  668233K->466242K(1013632K), 0.0299017 secs]  
2021-01-22T00:59:26.492+0800: [GC (Allocation Failure)  745858K->544154K(1013632K), 0.0298561 secs]  
2021-01-22T00:59:26.553+0800: [GC (Allocation Failure)  823770K->618717K(1013632K), 0.0293866 secs]  
2021-01-22T00:59:26.614+0800: [GC (Allocation Failure)  898333K->699480K(1013632K), 0.0327430 secs]  
2021-01-22T00:59:26.646+0800: [GC (CMS Final Remark)  705061K(1013632K), 0.0012791 secs]  
2021-01-22T00:59:26.679+0800: [GC (Allocation Failure)  851657K->648552K(1013632K), 0.0175253 secs]  
2021-01-22T00:59:26.697+0800: [GC (CMS Initial Mark)  654230K(1013632K), 0.0013911 secs]  
2021-01-22T00:59:26.730+0800: [GC (Allocation Failure)  928168K->727050K(1013632K), 0.0222631 secs]  
2021-01-22T00:59:26.753+0800: [GC (CMS Final Remark)  732763K(1013632K), 0.0012786 secs]  
2021-01-22T00:59:26.785+0800: [GC (Allocation Failure)  699419K->495939K(1013632K), 0.0175878 secs]  
2021-01-22T00:59:26.803+0800: [GC (CMS Initial Mark)  501619K(1013632K), 0.0023613 secs]  
2021-01-22T00:59:26.833+0800: [GC (Allocation Failure)  775555K->573528K(1013632K), 0.0185605 secs]  
2021-01-22T00:59:26.884+0800: [GC (Allocation Failure)  853144K->658140K(1013632K), 0.0200595 secs]  
2021-01-22T00:59:26.935+0800: [Full GC (Allocation Failure)  937756K->378813K(1013632K), 0.0542302 secs]  
2021-01-22T00:59:27.023+0800: [GC (Allocation Failure)  658429K->463555K(1013632K), 0.0141564 secs]  
执行结束!共生成对象次数:17374  
**PS F:\> java -Xmx4g -Xms4g -XX:+UseConcMarkSweepGC -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis**  
正在执行...  
2021-01-22T01:00:15.396+0800: [GC (Allocation Failure)  886080K->199391K(4083584K), 0.0334686 secs]  
2021-01-22T01:00:15.529+0800: [GC (Allocation Failure)  1085471K->346236K(4083584K), 0.0422294 secs]  
2021-01-22T01:00:15.667+0800: [GC (Allocation Failure)  1232316K->494281K(4083584K), 0.0641342 secs]  
2021-01-22T01:00:15.825+0800: [GC (Allocation Failure)  1380361K->639664K(4083584K), 0.0645353 secs]  
2021-01-22T01:00:15.988+0800: [GC (Allocation Failure)  1525744K->796372K(4083584K), 0.0662097 secs]  
执行结束!共生成对象次数:19361    

从上看出，  
cms在1g的时候，触发了1次full gc,也是从程序运行开始就进行了gc,gc总耗时390毫秒左右（Parallel为490毫秒，15000个对象）；    
4g的时候无full gc,总耗时250毫秒左右。gc耗时比Parallel多一点，所以创建对象次数也少一点。  
1g的时候，cms gc次数比Parallel的多，也还是因为gc总耗时cms比Parallel少一些。

##使用G1的时候，内存越大，性能看起来越好
比较1g和12g的时候日志差异
**PS F:\> java -Xmx1g -Xms1g -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis**  
正在执行...  
2021-01-22T01:08:52.025+0800: [GC pause (G1 Evacuation Pause) (young) 60M->20M(1024M), 0.0037995 secs]  
2021-01-22T01:08:52.040+0800: [GC pause (G1 Evacuation Pause) (young) 79M->40M(1024M), 0.0045038 secs]  
2021-01-22T01:08:52.052+0800: [GC pause (G1 Evacuation Pause) (young) 95M->62M(1024M), 0.0047894 secs]  
2021-01-22T01:08:52.070+0800: [GC pause (G1 Evacuation Pause) (young) 135M->88M(1024M), 0.0052533 secs]  
2021-01-22T01:08:52.143+0800: [GC pause (G1 Evacuation Pause) (young) 336M->163M(1024M), 0.0105520 secs]  
2021-01-22T01:08:52.168+0800: [GC pause (G1 Evacuation Pause) (young) 268M->189M(1024M), 0.0098205 secs]  
2021-01-22T01:08:52.208+0800: [GC pause (G1 Evacuation Pause) (young) 382M->245M(1024M), 0.0112847 secs]  
2021-01-22T01:08:52.252+0800: [GC pause (G1 Evacuation Pause) (young) 485M->307M(1024M), 0.0142980 secs]  
2021-01-22T01:08:52.350+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 755M->411M(1024M), 0.0198189 secs]  
2021-01-22T01:08:52.371+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.371+0800: [GC concurrent-root-region-scan-end, 0.0005928 secs]  
2021-01-22T01:08:52.372+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.373+0800: [GC concurrent-mark-end, 0.0015862 secs]  
2021-01-22T01:08:52.374+0800: [GC remark, 0.0010462 secs]  
2021-01-22T01:08:52.375+0800: [GC cleanup 434M->422M(1024M), 0.0008487 secs]  
2021-01-22T01:08:52.376+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.382+0800: [GC concurrent-cleanup-end, 0.0063563 secs]  
2021-01-22T01:08:52.406+0800: [GC pause (G1 Evacuation Pause) (young) 652M->456M(1024M), 0.0202753 secs]  
2021-01-22T01:08:52.428+0800: [GC pause (G1 Evacuation Pause) (mixed) 477M->397M(1024M), 0.0122520 secs]  
2021-01-22T01:08:52.448+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 455M->409M(1024M), 0.0044641 secs]  
2021-01-22T01:08:52.452+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.453+0800: [GC concurrent-root-region-scan-end, 0.0004600 secs]  
2021-01-22T01:08:52.455+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.457+0800: [GC concurrent-mark-end, 0.0019820 secs]  
2021-01-22T01:08:52.458+0800: [GC remark, 0.0031725 secs]  
2021-01-22T01:08:52.461+0800: [GC cleanup 448M->446M(1024M), 0.0009221 secs]  
2021-01-22T01:08:52.462+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.465+0800: [GC concurrent-cleanup-end, 0.0028617 secs]  
2021-01-22T01:08:52.517+0800: [GC pause (G1 Evacuation Pause) (young)-- 853M->647M(1024M), 0.0117772 secs]  
2021-01-22T01:08:52.530+0800: [GC pause (G1 Evacuation Pause) (mixed) 654M->595M(1024M), 0.0110453 secs]  
2021-01-22T01:08:52.541+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 595M->595M(1024M), 0.0015746 secs]  
2021-01-22T01:08:52.543+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.548+0800: [GC concurrent-root-region-scan-end, 0.0050966 secs]  
2021-01-22T01:08:52.548+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.553+0800: [GC concurrent-mark-end, 0.0046242 secs]  
2021-01-22T01:08:52.553+0800: [GC remark, 0.0019888 secs]  
2021-01-22T01:08:52.555+0800: [GC cleanup 669M->662M(1024M), 0.0009050 secs]  
2021-01-22T01:08:52.556+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.556+0800: [GC concurrent-cleanup-end, 0.0002565 secs]  
2021-01-22T01:08:52.582+0800: [GC pause (G1 Evacuation Pause) (young) 854M->656M(1024M), 0.0124825 secs]  
2021-01-22T01:08:52.598+0800: [GC pause (G1 Evacuation Pause) (mixed) 683M->567M(1024M), 0.0081631 secs]  
2021-01-22T01:08:52.613+0800: [GC pause (G1 Evacuation Pause) (mixed) 628M->508M(1024M), 0.0092241 secs]  
2021-01-22T01:08:52.630+0800: [GC pause (G1 Evacuation Pause) (mixed) 562M->481M(1024M), 0.0090994 secs]  
2021-01-22T01:08:52.639+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 483M->482M(1024M), 0.0019152 secs]  
2021-01-22T01:08:52.642+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.648+0800: [GC concurrent-root-region-scan-end, 0.0060849 secs]  
2021-01-22T01:08:52.648+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.650+0800: [GC concurrent-mark-end, 0.0022734 secs]  
2021-01-22T01:08:52.651+0800: [GC remark, 0.0025306 secs]  
2021-01-22T01:08:52.653+0800: [GC cleanup 556M->551M(1024M), 0.0008722 secs]  
2021-01-22T01:08:52.654+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.656+0800: [GC concurrent-cleanup-end, 0.0018317 secs]  
2021-01-22T01:08:52.691+0800: [GC pause (G1 Evacuation Pause) (young)-- 837M->588M(1024M), 0.0143467 secs]  
2021-01-22T01:08:52.708+0800: [GC pause (G1 Evacuation Pause) (mixed) 608M->520M(1024M), 0.0126585 secs]  
2021-01-22T01:08:52.728+0800: [GC pause (G1 Evacuation Pause) (mixed) 576M->531M(1024M), 0.0050150 secs]  
2021-01-22T01:08:52.734+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 536M->532M(1024M), 0.0020181 secs]  
2021-01-22T01:08:52.736+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.737+0800: [GC concurrent-root-region-scan-end, 0.0009290 secs]  
2021-01-22T01:08:52.737+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.740+0800: [GC concurrent-mark-end, 0.0026203 secs]  
2021-01-22T01:08:52.740+0800: [GC remark, 0.0011130 secs]  
2021-01-22T01:08:52.741+0800: [GC cleanup 565M->562M(1024M), 0.0024149 secs]  
2021-01-22T01:08:52.743+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.748+0800: [GC concurrent-cleanup-end, 0.0042887 secs]  
2021-01-22T01:08:52.778+0800: [GC pause (G1 Evacuation Pause) (young) 835M->597M(1024M), 0.0135835 secs]  
2021-01-22T01:08:52.794+0800: [GC pause (G1 Evacuation Pause) (mixed) 622M->522M(1024M), 0.0103313 secs]  
2021-01-22T01:08:52.811+0800: [GC pause (G1 Evacuation Pause) (mixed) 580M->494M(1024M), 0.0083060 secs]  
2021-01-22T01:08:52.821+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 508M->497M(1024M), 0.0026910 secs]  
2021-01-22T01:08:52.824+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.825+0800: [GC concurrent-root-region-scan-end, 0.0007117 secs]  
2021-01-22T01:08:52.832+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.834+0800: [GC concurrent-mark-end, 0.0019288 secs]  
2021-01-22T01:08:52.835+0800: [GC remark, 0.0011974 secs]  
2021-01-22T01:08:52.837+0800: [GC cleanup 583M->579M(1024M), 0.0009975 secs]  
2021-01-22T01:08:52.838+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.838+0800: [GC concurrent-cleanup-end, 0.0002696 secs]  
2021-01-22T01:08:52.872+0800: [GC pause (G1 Evacuation Pause) (young)-- 843M->630M(1024M), 0.0135462 secs]  
2021-01-22T01:08:52.888+0800: [GC pause (G1 Evacuation Pause) (mixed) 648M->558M(1024M), 0.0123508 secs]  
2021-01-22T01:08:52.908+0800: [GC pause (G1 Evacuation Pause) (mixed) 623M->566M(1024M), 0.0066200 secs]  
2021-01-22T01:08:52.915+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 567M->567M(1024M), 0.0017807 secs]  
2021-01-22T01:08:52.917+0800: [GC concurrent-root-region-scan-start]  
2021-01-22T01:08:52.920+0800: [GC concurrent-root-region-scan-end, 0.0025996 secs]  
2021-01-22T01:08:52.920+0800: [GC concurrent-mark-start]  
2021-01-22T01:08:52.925+0800: [GC concurrent-mark-end, 0.0048795 secs]  
2021-01-22T01:08:52.925+0800: [GC remark, 0.0013514 secs]  
2021-01-22T01:08:52.926+0800: [GC cleanup 628M->620M(1024M), 0.0009241 secs]  
2021-01-22T01:08:52.927+0800: [GC concurrent-cleanup-start]  
2021-01-22T01:08:52.932+0800: [GC concurrent-cleanup-end, 0.0049827 secs]  
2021-01-22T01:08:52.957+0800: [GC pause (G1 Evacuation Pause) (young) 836M->624M(1024M), 0.0131647 secs]  
2021-01-22T01:08:52.974+0800: [GC pause (G1 Evacuation Pause) (mixed) 651M->543M(1024M), 0.0104144 secs]  
2021-01-22T01:08:52.992+0800: [GC pause (G1 Evacuation Pause) (mixed) 596M->494M(1024M), 0.0104027 secs]  
执行结束!共生成对象次数:15679  

**PS F:\> java -Xmx12g -Xms12g -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis**  
正在执行...  
2021-01-22T01:09:18.635+0800: [GC pause (G1 Evacuation Pause) (young) 612M->164M(12G), 0.0276373 secs]  
2021-01-22T01:09:18.729+0800: [GC pause (G1 Evacuation Pause) (young) 696M->290M(12G), 0.0333343 secs]  
2021-01-22T01:09:18.828+0800: [GC pause (G1 Evacuation Pause) (young) 822M->406M(12G), 0.0317591 secs]  
2021-01-22T01:09:18.926+0800: [GC pause (G1 Evacuation Pause) (young) 938M->524M(12G), 0.0323672 secs]  
2021-01-22T01:09:19.020+0800: [GC pause (G1 Evacuation Pause) (young) 1056M->646M(12G), 0.0334052 secs]  
2021-01-22T01:09:19.116+0800: [GC pause (G1 Evacuation Pause) (young) 1178M->773M(12G), 0.0343279 secs]  
2021-01-22T01:09:19.216+0800: [GC pause (G1 Evacuation Pause) (young) 1305M->896M(12G), 0.0332176 secs]  
2021-01-22T01:09:19.316+0800: [GC pause (G1 Evacuation Pause) (young) 1428M->1008M(12G), 0.0319514 secs]  
2021-01-22T01:09:19.412+0800: [GC pause (G1 Evacuation Pause) (young) 1540M->1121M(12G), 0.0318792 secs]  
执行结束!共生成对象次数:18456  

从上面看出，12g的时候gc次数明显少于1g，1g的时候有young 和mixed一起发生，12g的时候只有young.  
两个情况下gc总时间，12g约为290毫秒，1g约为324多毫秒，12g的时候gc暂停占用时间稍微少一些，  
再加上1g的时候gc数量较多，也会影响性能，导致12g的时候性能好于1g  

