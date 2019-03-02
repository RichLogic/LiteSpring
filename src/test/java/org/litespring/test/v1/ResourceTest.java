package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.ClassPathResource;
import org.litespring.core.io.support.FileSystemResource;

import java.io.InputStream;

public class ResourceTest {

	@Test
	public void testClassPathResource() throws Exception {

		Resource resource = new ClassPathResource("petstore-v1.xml");

		// 注意：这个测试其实并不充分！！
		try (InputStream is = resource.getInputStream()) {
			Assert.assertNotNull(is);
		}

	}

	@Test
	public void testFileSystemResource() throws Exception {

		Resource resource = new FileSystemResource("/Users/richlogic/IdeaProjects/MySpring/src/test/resources/petstore-v1.xml");

		// 注意：这个测试其实并不充分！！
		try (InputStream is = resource.getInputStream()) {
			Assert.assertNotNull(is);
		}
	}

}
