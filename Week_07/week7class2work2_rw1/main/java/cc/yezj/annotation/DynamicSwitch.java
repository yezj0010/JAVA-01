package cc.yezj.annotation;

import java.lang.annotation.*;

/**
 * Created by Deng jin on 2021/3/6 21:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicSwitch {
    String name() default "";
}
