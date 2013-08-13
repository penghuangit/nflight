package com.abreqadhabra.nflight.examples.common.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;

public class FileAttributesDemo {
    private static Class<FileAttributesDemo> THIS_CLAZZ = FileAttributesDemo.class;
    private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
    
    public static void main(String[] args) throws Exception {

		String filePath = "com/abreqadhabra/nflight/examples/common/i18n";
		String fileName = "logging.properties";
		
		Path file = IOStream.getFilePath(THIS_CLAZZ.getName(),
				filePath, fileName);
        
        BasicFileAttributes attr =
                Files.readAttributes(file, BasicFileAttributes.class);
        
        System.out.println("FileName     = " + file.getFileName());
        System.out.println("ContentType     = " + Files.probeContentType(file));
        System.out.println("creationTime     = " + attr.creationTime());
        System.out.println("lastAccessTime   = " + attr.lastAccessTime());
        System.out.println("lastModifiedTime = " + attr.lastModifiedTime());

        System.out.println("isDirectory      = " + attr.isDirectory());
        System.out.println("isOther          = " + attr.isOther());
        System.out.println("isRegularFile    = " + attr.isRegularFile());
        System.out.println("isSymbolicLink   = " + attr.isSymbolicLink());
        System.out.println("size             = " + attr.size());
        
    }
}