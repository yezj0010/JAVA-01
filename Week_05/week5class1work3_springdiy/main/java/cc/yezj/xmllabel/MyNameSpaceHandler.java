package cc.yezj.xmllabel;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 用来注册BeanDefinition解析器。
 * 说明：通常为每一个xsd:element都要注册一个BeanDefinitionParser。
 */
public class MyNameSpaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("student", new StudentParser());
        registerBeanDefinitionParser("klass", new KlassParser());
        registerBeanDefinitionParser("school", new SchoolParser());
    }
}
