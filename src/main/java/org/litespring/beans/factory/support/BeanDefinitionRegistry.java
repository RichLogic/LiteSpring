package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {

    BeanDefinition getBeanDefinition(String id);

    void registerBeanDefinition(String id, BeanDefinition beanDefinition);

}
