这是第三周作业。

# 网关的作业
网关的项目工程拷贝https://github.com/kimmking/JavaCourseCodes/02nio/nio02下的内容。  

注：本项目模拟后台服务的是HttpServer01，HttpServer02，HttpServer03三个文件，端口分别为8801,8802,8803，所以网关启动时，传入参数为如下：  
```
// 这是多个后端url走随机路由的例子
String proxyServers = System.getProperty("proxyServers","http://localhost:8801,http://localhost:8802,http://localhost:8803");
int port = Integer.parseInt(proxyPort);
System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServers.split(",")));
System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for server:" + server.toString());
```

**基于该项目，完成了作业，代码见【netty-gateway】**  
**## 每题详细思路介绍见每题的.md文件**
