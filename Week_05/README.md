**这是第5周作业**  

### 第5周第1课作业1：（选做）使 Java 里的动态代理，实现一个简单的 AOP
见java项目 **week5class1work1_aop**  
运行TestJavaDynamic的main方法，就是一个使用JDK动态代理实现aop的例子，动态的给TestService.getRandomNumber方法增加了执行前后的日志打印  

TestJavaDynamic的main函数运行结果如下：  
```
生成的动态代理类为：com.sun.proxy.$Proxy0
以下是执行动态代理类的getRandomNumber方法后的运行结果：
方法getRandomNumber准备执行，入参:[1000]，2021-02-18T23:51:26.630
这是原始被代理类的方法
方法getRandomNumber执行结束，结果:10，2021-02-18T23:51:26.638
```

### 第5周第1课作业2：（必做）写代码实现 Spring Bean 的装配
代码详见maven项目 **week5class1work2_spring**  
使用了两种spring bean的装配方式：  
TestByXML类中使用的是applicationContext1.xml配置文件，纯xml配置；  
TestByAnnotation类中使用的是applicationContext2.xml配置文件，使用了注解配置。  


### 第5周第1课作业3：实现一个 Spring XML 自定义配置，配置一组 Bean。
代码详见maven项目 **week5class1work3_springdiy**
