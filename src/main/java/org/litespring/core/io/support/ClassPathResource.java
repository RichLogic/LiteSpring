package org.litespring.core.io.support;

import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {

    private String xmlPath;
    private ClassLoader classLoader;

    public ClassPathResource(String xmlPath) {
        this(xmlPath, null);
    }

    public ClassPathResource(String xmlPath, ClassLoader classLoader) {
        this.xmlPath = xmlPath;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(xmlPath);

        if (inputStream == null) {
            throw new FileNotFoundException("不能打开" + xmlPath);
        }

        return inputStream;
    }

    @Override
    public String getDescription() {
        return xmlPath;
    }
}
