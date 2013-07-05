package com.abreqadhabra.nflight.core;

import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class BootProfileImpl implements BootProfile {
	private static final Class<BootProfileImpl> THIS_CLAZZ = BootProfileImpl.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static final String DEFAULT_BOOT_PROPERTIES_FILE_NAME = "/com/abreqadhabra/nflight/core/config/boot.properties";

	Properties bootProps = null;

	public BootProfileImpl() throws Exception {
		this(DEFAULT_BOOT_PROPERTIES_FILE_NAME);
	}

	public BootProfileImpl(Properties props) {
		this.bootProps = props;
		init();
	}

	public BootProfileImpl(String fileName) throws Exception {
		this.bootProps = PropertyFile.readPropertyFile(fileName);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
	}

	public Properties getBootProperties(){
		return this.bootProps;
	}
	
}
