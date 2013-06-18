package examples.logging;

import java.util.logging.Logger;

import com.abreqadhabra.nflight.commons.logging.LoggingHelper;

public class CustomLoggerExample {

    private static final Logger LOGGER = LoggingHelper
	    .getLogger(CustomLoggerExample.class);

    public static void main(String[] args) {

//	LOGGER.info("------------>logger.getLevel()\t:\t" + LOGGER.getLevel());

	CustomLoggerExample example = new CustomLoggerExample();
//	System.out.println("LOGGER" + LOGGER);

	example.excute();
    }

    private void excute() {
/*	LOGGER.info("------------>logger.getLevel()\t:\t" + LOGGER.getLevel());
	System.out.println("-->"+ LOGGER.getResourceBundleName());
	System.out.println("-->"+ LOGGER.getName());*/
    }

}