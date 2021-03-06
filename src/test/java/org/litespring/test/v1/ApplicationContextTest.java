package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

public class ApplicationContextTest {
    @Test
    public void testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v1.xml");

        PetStoreService petStoreService = (PetStoreService)context.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }

    @Test
    public void testGetBeanFromFileSystemContext() {
        ApplicationContext context = new FileSystemXmlApplicationContext("/Users/richlogic/IdeaProjects/MySpring/src/test/resources/petstore-v1.xml");

        PetStoreService petStoreService = (PetStoreService)context.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }
}
