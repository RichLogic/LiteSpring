package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.core.io.Resource;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.util.StringUtils;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String PROPERTY_ATTRIBUTE = "property";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String VALUE_ATTRIBUTE = "value";

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinition(Resource resource) {
        SAXReader reader = new SAXReader();

        try (InputStream inputStream = resource.getInputStream()) {

            Element root = null;
            Document document = reader.read(inputStream);
            if (document != null) {
                root = document.getRootElement();
            }

            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {

                Element element = iterator.next();
                String id = element.attributeValue(ID_ATTRIBUTE);
                String className = element.attributeValue(CLASS_ATTRIBUTE);
                String scope = element.attributeValue(SCOPE_ATTRIBUTE);

                BeanDefinition beanDefinition = new GenericBeanDefinition(id, className);
                if (scope != null) beanDefinition.setScope(scope);
                this.parsePropertyElement(element, beanDefinition);

                registry.registerBeanDefinition(id, beanDefinition);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException(resource.getDescription() + "地址不正确");
        }
    }

    private void parsePropertyElement(Element element, BeanDefinition beanDefinition) {

        Iterator<Element> iterator = element.elementIterator();

        while (iterator.hasNext()) {

            Element propertyElement = iterator.next();

            String name = propertyElement.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(name)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object value = this.parsePropertyValue(propertyElement, name);
            PropertyValue propertyValue = new PropertyValue(name, value);

            beanDefinition.getPropertyValues().add(propertyValue);
        }

    }

    private Object parsePropertyValue(Element element, String propertyName) {

        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        String ref = element.attributeValue(REF_ATTRIBUTE);
        String value = element.attributeValue(VALUE_ATTRIBUTE);

        if (ref != null) {
            if (!StringUtils.hasText(ref)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            return new RuntimeBeanReference(ref);
        } else if(value != null) {
            return new TypedStringValue(value);
        } else {
            throw new BeanDefinitionStoreException(elementName + " must specify a ref or value");
        }
    }
}
