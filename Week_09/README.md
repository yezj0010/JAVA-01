**这是第9周作业**  

### 第9周第1课作业3：（必做）改造自定义RPC的程序
代码见项目**week9class1work3_rpc**，该项目是基于秦老师的rpcDemo进行改造。  
目录结构相比demo进行了优化，以便阅读代码。以下是作业内容介绍：  

1）尝试将服务端写死查找接口实现类变成泛型和反射  
老师demo中似乎已经实现，不知道如何更改。

2）尝试将客户端动态代理改成AOP，添加异常处理  
主要涉及的类如下：  
CommonRpcService  
RpcAspect  
Rpcfx  
RpcInvoke  
ConsumerService  
RpcfxException  
废弃了Rpcfx的createFromRegistry方法，改成AOP形式，  
增加CommonRpcService，对invoke方法增加了切面增强功能，调用invoke方法，切面会去找到对应信息，调用http请求并拿到结果。    
增加了RpcfxException异常类，将服务端部分异常信息封装到该类中，客户端根据RpcfxResponse的status字段进行判断  

3）尝试使用Netty+HTTP作为client端传输方式  
主要涉及的类如下：  
Rpcfx ==== http请求方法从post换成了postByNetty，同步拿到结果
RpcfxRequest  ==== 增加了traceId,用来netty客户端判断是否有结果返回并处理好  
RpcfxResponse ==== 增加了traceId,与request中的保持一致  
NettyClient ==== netty使用http协议调用   
NettyClientHandler ==== 发送消息，监听返回消息  
NettyHolder ==== 根据traceId缓存SyncFuture信息  
SyncFuture ==== 使用CountDownLatch保证Rpcfx调用postByNetty可以同步拿到调用结果
  
4）其他修改  
1. 服务端，启动的时候，自动注册带有RpcService注解的类信息到zookeeper中，主要涉及的类是RegisterToZookeeper   
2. 客户端，完善了TagRouter类，会从zookeeper获取注册的服务信息  
3. 客户端，完善了RandomLoadBalancer类，使用Random类实现了简单的随机。  


测试流程：  
先启动zookeeper,该项目zookeeper端口默认使用2181，  
启动服务端，启动后查看zookeeper，可以看到注册了信息到zookeeper中，    
启动测试端，会自动运行TestConsumer的run方法，通过日志查看，可知道客户端从zookeeper拿到服务端信息，调用了http接口，并拿到结果。 


### 第9周第2课作业3：（必做）结合dubbo+hmily，实现一个TCC外汇交易处理  
代码见项目**week9class2work1_dubbo_hmily_tcc**  
1）用户A的美元账户和人民币账户都在A库，A使用1美元兑换7人民币；   
2）用户B的美元账户和人民币账户都在B库，B使用7人民币兑换1美元；   
3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。    

该项目分为四个模块：  
account_cny === 人民币资产模块  
account_usd === 美元资产模块  
biz === 业务模块，实现分布式事务  
common === 公共模块  
初始sql见**init.sql**    
  
biz中主要负责业务组装和分布式事务的实现，通过dubbo调用另外两个模块中的dubbo服务。  
   
**代码思路**：    
创建两个库，account_cny库，account_usd库，模拟跨库分布式事务。  
每个库中，有一张资金表，一张冻结表。  
a用户使用1美元换b用户的7人民币，代码流程如下：  
准备阶段：  
a用户在account_usd库中的资金表usd中，资产-1，冻结表中，增加1条a用户冻结记录，冻结金额1；  
b用户在account_cny库中的资金表cny中，资产-7，总结表中，增加1条b用户冻结记录，冻结金额7；  
```
    @HmilyTCC(confirmMethod = "confirmSwitchAccount", cancelMethod = "cancelSwitchAccount")
    @Override
    public RespResult switchAccount(SwitchAccountRequest request){
        log.info("biz预备");
        //先将sourceUserId用户人民币账户扣减sourceAmt，然后冻结账户添加金额为sourceAmt，货币类型为sourceType相应记录
        cnyService.switchAccountPre(request);
        //先将targetUserId用户人民币账户扣减targetAmt，然后冻结账户添加金额为sourceAmt，货币类型为sourceType相应记录
        usdService.switchAccountPre(request);
        return RespResult.ok();
    }
```  
确认阶段：  
a用户account_usd库中冻结记录的金额1，加到b用户usd资金账户中，   
b用户account_cny库中冻结记录的金额7，加到a用户cny资金账户中，  
```
    public void confirmSwitchAccount(SwitchAccountRequest request){
        log.info("biz确认");
        cnyService.switchAccountConfirm(request);
        usdService.switchAccountConfirm(request);
    }
```
发生异常取消阶段：  
a用户account_usd库中冻结记录的金额1，加到a用户usd资金账户中，  
b用户account_cny库中冻结记录的金额7，加到b用户cny资金账户中，  
```
  public void cancelSwitchAccount(SwitchAccountRequest request){
        log.info("biz取消");
        cnyService.switchAccountCancel(request);
        usdService.switchAccountCancel(request);
    }
```

测试步骤：  
启动account_cny,account_usd,biz，biz项目中hmily需要的表会自动创建好。    
访问 http://localhost:9024/swagger-ui.html,  
在测试接口中，json数据输入如下：  
```
{
  "sourceAmt": 7,
  "sourceType": "CNY",
  "sourceUserId": 1,
  "targetAmt": 1,
  "targetType": "USD",
  "targetUserId": 2
}
```
account_cny和account_usd中接口的sql实现未写完，整体流程已经写好（hmily的tcc和dubbo调用已经写好），此项目为简单demo。     
通过日志查看，可以知道：  
正常流程走 准备+确认，  
异常（AccountUsdService.switchAccountPre方法中，有随机概率会抛出异常）则会走 准备+取消。 
 