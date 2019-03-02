package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.support.ClassPathResource;

import java.util.List;

public class BeanDefinitionTest {

    @Test
    public void testGetBeanDefinition() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v2.xml"));

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        List<PropertyValue> propertyValueList = beanDefinition.getPropertyValues();

        Assert.assertTrue(propertyValueList.size() == 2);

        {
            PropertyValue propertyValue = this.getPropertyValue("accountDao", propertyValueList);
            Assert.assertNotNull(propertyValue);
            Assert.assertNotNull(propertyValue.getValue() instanceof RuntimeBeanReference);
        }


        {
            PropertyValue propertyValue = this.getPropertyValue("itemDao", propertyValueList);
            Assert.assertNotNull(propertyValue);
            Assert.assertNotNull(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> propertyValueList) {

        for (PropertyValue value : propertyValueList) {
            if (name.equals(value.getName())) {
                return value;
            }
        }

        return null;
    }
}


