package org.litespring.context.support;

import org.litespring.core.io.Resource;
import org.litespring.core.io.support.ClassPathResource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String path) {
        super(path);
    }

    @Override
    Resource getResource(String path) {
        return new ClassPathResource(path, this.getBeanClassLoader());
    }

}
