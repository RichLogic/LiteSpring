package org.litespring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.util.ClassUtils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public DefaultBeanFactory(String xmlPath) {
        this.loadBeanDefinition(xmlPath);
    }

    private void loadBeanDefinition(String xmlPath) {

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        SAXReader reader = new SAXReader();

        try (InputStream inputStream = classLoader.getResourceAsStream(xmlPath)) {

            Element root = null;
            Document document = reader.read(inputStream);
            if (document != null) {
                root = document.getRootElement();
            }
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                BeanDefinition beanDefinition = new GenericBeanDefinition(id, className);
                beanDefinitionMap.put(id, beanDefinition);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException(xmlPath + "地址不正确");
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String id) {
        return beanDefinitionMap.get(id);
    }

    @Override
    public Object getBean(String id) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(id);
        if (beanDefinition == null) {
            throw new BeanCreationException("Bean 不存在");
        }

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("创建 " + beanClassName + " 失败");
        }

    }
}
