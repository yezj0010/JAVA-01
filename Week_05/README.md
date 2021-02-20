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


### 第5周第1课作业3：（选做）实现一个 Spring XML 自定义配置，配置一组 Bean。
代码详见maven项目 **week5class1work3_springdiy**
School,Klass,Student三个类，分别在my.xsd中定义了三个元素名
my.xml是自定义的xml文件
MyNameSpaceHandler类中，定义每个元素指定的BeanDefinitionParser
运行TestMain的main方法，可以测试效果

### 第5周第2课作业3：（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。
代码详见maven项目 **week5class2work3_starter**
使用配置文件application.yaml，以及spring.factories文件中指定的自动配置类。
starter自动创建好School对象。
启动DemoApplication类，运行该项目，项目启动后，会调用InitTest方法的run方法，打印出School对象的信息。


### 第5周第2课作业6：（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
1）使用 JDBC 原生接口，实现数据库的增删改查操作。  
2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。  
3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。  
代码详见maven项目 **week5class2work6_jdbc_plus**
UseJDBCWithStatement是使用了JDBC原生接口，main方法启动；  
UseJDBCWithPrepareStatement是使用了JDBC原生接口+PrepareStatement+事务控制，main方法启动；  
UseHikari基于springboot使用了Hikari线程池，通过spring的JdbcTemplate操作数据库，JDBCApplication运行启动；  
