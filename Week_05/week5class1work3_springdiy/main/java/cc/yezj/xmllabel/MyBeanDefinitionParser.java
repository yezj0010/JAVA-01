package cc.yezj.xmllabel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
public class MyBeanDefinitionParser implements BeanDefinitionParser {

    public MyBeanDefinitionParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> beanClass = null;
        String cls = element.getAttribute("cls");
        try{
            beanClass = Class.forName(cls);
        }catch (Exception e){
            log.error("获取类信息异常", e);
        }
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        element.getParentNode().getChildNodes();
        NodeList childNodes = element.getChildNodes();
        Node item = childNodes.item(0);


        String beanName = element.getAttribute("name");
//        beanDefinition.getPropertyValues().add("name", element.getAttribute("name"));
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
        beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);//注册bean到BeanDefinitionRegistry中
        return beanDefinition;
    }
}
