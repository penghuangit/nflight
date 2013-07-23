package com.abreqadhabra.nflight.common.util.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.abreqadhabra.nflight.common.util.IOStream;

/**
 * @author nhn
 *
 */
public class PropertyFileTest {

	String propertyFilePath ;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        System.out.println("@BeforeClass method will be executed before JUnit test for a Class starts");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("@AfterClass method will be executed after JUnit test for a Class Completed");

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("@Before method will execute before every JUnit4 test");
		
		String propertyPath = "com/abreqadhabra/nflight/common/conf/logging/";

		String propertyFileName = "logging.properties";

		propertyFilePath = IOStream.getCodebase(PropertyFileTest.class.getName()) + propertyPath
				+ propertyFileName;

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("@After method will execute after every JUnit4 test");
	}
}
