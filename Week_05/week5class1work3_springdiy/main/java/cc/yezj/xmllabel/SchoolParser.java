package cc.yezj.xmllabel;

import cc.yezj.School;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
public class SchoolParser implements BeanDefinitionParser {

    public SchoolParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> beanClass = School.class;

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        beanDefinition.getPropertyValues().add("klass", new RuntimeBeanReference(element.getAttribute("klass")));

        String beanName = element.getAttribute("id");
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
        beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);//注册bean到BeanDefinitionRegistry中
        return beanDefinition;
    }
}
