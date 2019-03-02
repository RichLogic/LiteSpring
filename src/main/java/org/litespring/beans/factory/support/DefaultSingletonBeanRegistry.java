package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingleBeanRegistry;
import org.litespring.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingleBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(64);

    @Override
    public void registerSingleton(String id, Object singletonObject) {
        Assert.notNull(id, id + "的 bean 不存在");
        Object oldObject = this.singletonObjects.get(id);
        if (oldObject != null) {
            throw new IllegalStateException("已经存在这个 bean 的实例了！");
        }
        this.singletonObjects.put(id, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }
}
