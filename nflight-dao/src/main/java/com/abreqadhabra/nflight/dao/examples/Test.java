package com.abreqadhabra.nflight.dao.examples;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.abreqadhabra.nflight.common.util.FileUtil;

public class Test {

	public static String BASE_LOCATION = Test.class
			.getProtectionDomain().getCodeSource().getLocation().getFile();

	public static String ORIGINAL_DATASET_FILE = "/com/abreqadhabra/nflight/dao/resources/test/initial-dummy-data.xml";

	public static void main(String[] args) {
		Test t = new Test();
		try {
			t.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void getDataSet() throws IOException  {
		String filePath = BASE_LOCATION + ORIGINAL_DATASET_FILE;
		String encoding = FileUtil.getEncoding(filePath);

/*		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				filePath), encoding);
		*/
		
		InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream(ORIGINAL_DATASET_FILE));
		
		OutputStreamWriter osw = new OutputStreamWriter(System.out);
	      char[] buffer = new char[512];
	      int readcount = 0;
	      while((readcount = isr.read(buffer)) != -1) {
	        osw.write(buffer, 0, readcount);
	      }

	      isr.close();
	        osw.close();
	        
		
		
	//	XmlDataSet a =  new XmlDataSet();
	        
	 // System.out.print(is);
	}


}
