package org.litespring.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ApplicationContextTest.class,
		BeanDefinitionTest.class,
		BeanDefinitionValueResolverTest.class,
		CustomBooleanEditorTest.class,
		CustomNumberEditorTest.class,
		TypeConverterTest.class
})
public class V2AllTests {

}
