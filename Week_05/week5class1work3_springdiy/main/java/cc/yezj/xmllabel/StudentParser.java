package cc.yezj.xmllabel;

import cc.yezj.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@Slf4j
public class StudentParser implements BeanDefinitionParser {

    public StudentParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> beanClass = Student.class;

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        beanDefinition.getPropertyValues().add("name", element.getAttribute("name"));
        beanDefinition.getPropertyValues().add("age", element.getAttribute("age"));

        String beanName = element.getAttribute("id");
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
        beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);//注册bean到BeanDefinitionRegistry中
        return beanDefinition;
    }
}
