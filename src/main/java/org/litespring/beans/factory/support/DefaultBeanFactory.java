package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanDefinitionRegistry, ConfigurableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    private ClassLoader classLoader;

    @Override
    public void registerBeanDefinition(String id, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(id, beanDefinition);
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

        if (beanDefinition.isSingleton()) {
            Object bean = this.getSingleton(id);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registerSingleton(id, bean);
            }
            return bean;
        }

        return this.createBean(beanDefinition);
    }

    private Object createBean(BeanDefinition beanDefinition) {
        // 1. 初始化 Bean
        Object bean = this.instantiateBean(beanDefinition);

        // 2. 设置属性
        try {
            this.populateBean(beanDefinition, bean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanCreationException("setter 注入失败");
        }

        return bean;
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        ClassLoader classLoader = this.getBeanClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("创建 " + beanClassName + " 失败");
        }
    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) throws Exception {

        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();

        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            Object value = pv.getValue();
            Object resolvedValue = resolver.resolveValueIfNecessary(value);

            for (PropertyDescriptor pd : pds) {
                if (pd.getName().equals(name)) {
                    Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                    pd.getWriteMethod().invoke(bean, convertedValue);
                    break;
                }
            }
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

}
