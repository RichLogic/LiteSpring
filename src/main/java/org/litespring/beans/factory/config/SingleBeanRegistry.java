package org.litespring.beans.factory.config;

public interface SingleBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

}
