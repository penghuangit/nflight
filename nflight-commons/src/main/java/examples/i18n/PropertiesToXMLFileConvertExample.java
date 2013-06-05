package examples.i18n;

import java.io.File;
import java.io.IOException;

import com.abreqadhabra.nflight.commons.constant.Env;
import com.abreqadhabra.nflight.commons.util.PropertyFileUtil;

public class PropertiesToXMLFileConvertExample {

	public static void main(String[] args) throws IOException {

		File dir = new File(Env.CONFIG.BASE_LOCATION
				+ "examples/resources/logging");

		PropertyFileUtil.convertAllPropertiesToXML(dir);
		

		dir = new File(Env.CONFIG.BASE_LOCATION
				+ "examples/resources/i18n");
		PropertyFileUtil.convertAllPropertiesToXML(dir);

		dir = new File(Env.CONFIG.BASE_LOCATION
				+ "com/abreqadhabra/nflight/commons/resources/logging");		
		
		PropertyFileUtil.convertAllPropertiesToXML(dir);
		

		dir = new File(Env.CONFIG.BASE_LOCATION
				+ "com/abreqadhabra/nflight/commons/resources/config");
		PropertyFileUtil.convertAllPropertiesToXML(dir);

	}

	
}
