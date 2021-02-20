package cc.yezj.xmllabel;

import cc.yezj.Klass;
import cc.yezj.School;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
public class KlassParser implements BeanDefinitionParser {
    public KlassParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> beanClass = Klass.class;

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        beanDefinition.getPropertyValues().add("students", new RuntimeBeanReference(element.getAttribute("students")));

        String beanName = element.getAttribute("id");
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
        beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);//注册bean到BeanDefinitionRegistry中
        return beanDefinition;
    }
}
