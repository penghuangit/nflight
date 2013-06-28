package com.abreqadhabra.nflight.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
    /***
     * Determines the encoding of the specified file. If a UTF16 Byte Order Mark
     * (BOM) is found an encoding of "UTF16" is returned. If a UTF8 BOM is found
     * an encoding of "UTF8" is returned. Otherwise the default encoding is
     * returned.
     * 
     * @param filePath
     *            file path
     * @return "UTF8", "UTF16", or default encoding.
     */
    public static String getEncoding(String filePath) {
	String encoding = System.getProperty("file.encoding");

	BufferedReader bufferedReader = null;

	try {
	    // In order to read files with non-default encoding, specify an
	    // encoding in the FileInputStream constructor.
	    bufferedReader = new BufferedReader(new InputStreamReader(
		    new FileInputStream(filePath)));

	    char buffer[] = new char[3];
	    int length = bufferedReader.read(buffer);

	    if (length >= 2) {
		/* UTF-16, little endian || UTF-16, big endian */
		if ((buffer[0] == (char) 0xff && buffer[1] == (char) 0xfe)
			|| (buffer[0] == (char) 0xfe && buffer[1] == (char) 0xff)) {
		    encoding = "UTF16";
		}
	    }
	    if (length >= 3) {
		if (buffer[0] == (char) 0xef && buffer[1] == (char) 0xbb
			&& buffer[2] == (char) 0xbf) /* UTF-8 */{
		    encoding = "UTF8";
		}
	    }
	} catch (IOException ex) {
	} finally {
	    if (bufferedReader != null) {
		try {
		    bufferedReader.close();
		} catch (IOException ex) {
		}
	    }
	}

	return encoding;
    }

    /***
     * Returns the text of the specified file. If a Unicode Byte Order Mark
     * (BOM) is found, the file is read with the corresponding encoding.
     * Otherwise the file is read using the default encoding.
     * 
     * @param filePath
     *            file path
     * @return text of file
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
	String encoding = getEncoding(filePath);

	BufferedReader bufferedReader = null;

	StringBuffer text = new StringBuffer();

	try {
	    bufferedReader = new BufferedReader(new InputStreamReader(
		    new FileInputStream(filePath), encoding));

	    char[] buffer = new char[1024 * 16];
	    int length;

	    while ((length = bufferedReader.read(buffer)) != -1) {
		text.append(buffer, 0, length);
	    }
	} finally {
	    if (bufferedReader != null) {
		bufferedReader.close();
	    }
	}

	return text.toString();
    }
}