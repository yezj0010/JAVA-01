package io.kimmking.rpcfx.demo.register;

import io.kimmking.rpcfx.api.ServiceProviderDesc;
import io.kimmking.rpcfx.demo.anotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.net.InetAddress;

/**
 * created by DengJin on 2021/3/16 17:21
 */
@Service
@Slf4j
public class RegisterToZookeeper implements ApplicationContextAware {
    private static final String BASE_PACKAGE = "io.kimmking.rpcfx.demo";
    private static final String PATTERN = "/**/*.class";

    private ApplicationContext applicationContext;

    private static CuratorFramework client;

    static {
        // start zk client
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString("localhost:2181").namespace("rpcfx").retryPolicy(retryPolicy).build();
        client.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void registerAllService() throws Exception{
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + PATTERN;
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        //MetadataReader 的工厂类
        MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (Resource resource : resources) {
            //用于读取类信息
            MetadataReader reader = readerfactory.getMetadataReader(resource);
            //扫描到的class
            String classname = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(classname);
            //判断是否有指定主解
            RpcService anno = clazz.getAnnotation(RpcService.class);
            if (anno != null) {
                //将注解中的类型值作为key，对应的类作为 value
                // 进一步的优化，是在spring加载完成后，从里面拿到特定注解的bean，自动注册到zk
                // register service
                String serviceName = anno.name();
                int port = anno.port();
                registerService(client, serviceName, port);
                log.info("{}注册到zookeeper,port={}", serviceName, port);
            }
        }
    }

    /**
     * 注册服务到zookeeper中
     * @param client
     * @param service
     * @throws Exception
     */
    private void registerService(CuratorFramework client, String service, int port) throws Exception {
        ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
                .host(InetAddress.getLocalHost().getHostAddress())
                .port(port).serviceClass(service).build();
        // String userServiceSescJson = JSON.toJSONString(userServiceSesc);

        try {
            //创建服务根地址
            String parentPath = "/" + service;
            if ( null == client.checkExists().forPath(parentPath)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(parentPath, "service".getBytes());
            }else{
                log.info("parentPath=【{}】已经存在", parentPath);
            }

            //在根服务地址下，创建子服务地址
            String subPath = "/" + service + "/" + userServiceSesc.getHost() + "_" + userServiceSesc.getPort();
            if(null == client.checkExists().forPath(subPath)){
                client.create().withMode(CreateMode.EPHEMERAL).forPath( subPath, "provider".getBytes());
            }else{
                log.info("subPath=【{}】已经存在", subPath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }
}
