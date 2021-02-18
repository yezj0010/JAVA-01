package cc.yezj;

import lombok.Data;

/**
 * Created by Deng jin on 2021/2/19 0:12
 */
@Data
public class Principal {
    private String name;
    private int age;

    public void say(){
        System.out.println("同学们好，我是校长"+name);
    }
}
