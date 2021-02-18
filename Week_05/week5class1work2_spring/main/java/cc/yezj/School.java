package cc.yezj;

import lombok.Data;

import javax.annotation.Resource;

/**
 * Created by Deng jin on 2021/2/19 0:04
 */
@Data
public class School {
    private String name;

    private int age;

    @Resource(name = "principal1")
    private Principal principal;

    public void detail(){
        System.out.println(toString());
    }
}
