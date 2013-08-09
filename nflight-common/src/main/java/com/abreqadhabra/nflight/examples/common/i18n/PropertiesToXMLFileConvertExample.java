package com.abreqadhabra.nflight.examples.common.i18n;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;
import com.abreqadhabra.nflight.common.util.PropertyFile;

public class PropertiesToXMLFileConvertExample {
	private static final Class<PropertiesToXMLFileConvertExample> THIS_CLAZZ = PropertiesToXMLFileConvertExample.class;
	private static final Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);


	public static void main(String[] args) throws IOException {

		String filePath = "com/abreqadhabra/nflight/examples/common/conf";

		Path dirPath = IOStream.getFilePath(THIS_CLAZZ.getName(),
				filePath);
		
		try {
			PropertyFile.convertAllXMLProperties(dirPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
}
