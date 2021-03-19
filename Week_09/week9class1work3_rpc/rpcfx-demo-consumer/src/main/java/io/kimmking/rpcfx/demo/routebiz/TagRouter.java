package io.kimmking.rpcfx.demo.routebiz;

import io.kimmking.rpcfx.api.Router;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * created by DengJin on 2021/3/17 18:06
 */
@Slf4j
@Component
public class TagRouter implements Router {

    private static CuratorFramework client;

    @Override
    public List<String> route(List<String> urls, Class serviceClass) {
        String zkPath = urls.get(0);
        if(client==null){
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.builder().connectString(zkPath).namespace("rpcfx").retryPolicy(retryPolicy).build();
            client.start();
        }
        String path = "/rpcfx/"+serviceClass.getName();
        try{
            List<String> children = client.getZookeeperClient().getZooKeeper().getChildren(path, false);
            if(CollectionUtils.isEmpty(children)){
                return null;
            }
            List<String> serviceUrls = children.stream().filter(i -> !StringUtils.isEmpty(i)).map(i -> {
                String[] s = i.split("_");
                String real_path = "http://"+s[0]+":"+s[1];
                return real_path;
            }).collect(Collectors.toList());
            return serviceUrls;
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }
}
