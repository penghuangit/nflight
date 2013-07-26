/*
 * Copyright (c) 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 * 
 */
import java.awt.FileDialog;
import javax.swing.JFrame;
import com.tymeac.base.*;

/**
 * Convert the old configuration file to XML format
 * 
 * @author coopsoft.com
 *
 */
public class ConvertCfg extends JFrame {
  
  private static final long serialVersionUID = 1L;

  // dialog for getting the inported directory and filename
  private FileDialog ReadCfgFile  = new FileDialog(this);
  
  // dialog for getting the output directory and filename
  private FileDialog WriteCfgFile = new FileDialog(this);
  
  // fields used by the config file/xml document
  private TyCfgFields base = new TyCfgFields();

/**
 * private constructor, can only be called by the class itself
 * 
 *
 */  
private ConvertCfg () {
  
  ReadCfgFile.setMode(FileDialog.LOAD);
  ReadCfgFile.setTitle("Read Cfg File");
  ReadCfgFile.setDirectory("");
  ReadCfgFile.setFile("Tymeac.cfg");
  
  WriteCfgFile.setMode(FileDialog.SAVE);
  WriteCfgFile.setTitle("Write XML File");
  WriteCfgFile.setDirectory("");
  WriteCfgFile.setFile("Tymeac.xml");
  
} // end-constructor

/**
 * Main entry
 * @param args
 */
public static void main(String[] args) {
    
  // new object
  ConvertCfg cfg = new ConvertCfg();
  
  // do convert
  cfg.doWork(); 
  
  // bye
  System.exit(0);

} // end-method

/**
 * Get the dir and file name of the config file.
 * Convert to memory
 * 
 * Get the dir and file name of the xml file
 * convert to xml 
 *
 */
private void doWork() {
  
  String ty_dir  = null;
  String ty_file = null;  
  
  try {
      // ReadCfgFile Show the FileDialog
      ReadCfgFile.setVisible(true);
      
  } // end-try
  
  catch (java.lang.Exception e) {
  } // end-catch

  try {
    // get the dir and filename
    ty_dir = ReadCfgFile.getDirectory();

    ty_file = ReadCfgFile.getFile();

  } // end-try 

  catch (java.lang.Exception e) {
  } // end-catch  
  
  // to do
  System.out.println("Old file= " + "dir=" + ty_dir + " file=" + ty_file);
  
  // When convert to memory failed, get out
  if  (!cfgToMemory(ty_dir, ty_file)) return; 
  
  try {
      // ReadCfgFile Show the FileDialog
      WriteCfgFile.setVisible(true);
      
  } // end-try
  
  catch (java.lang.Exception e) {
  } // end-catch
  
  try {
    // get the dir and file name
    ty_dir = WriteCfgFile.getDirectory();

    ty_file = WriteCfgFile.getFile();

  } // end-try 

  catch (java.lang.Exception e) {
  } // end-catch  
  
  // to do
  System.out.println("XML file= " + "dir=" + ty_dir + " file=" + ty_file);
  
  // When conversion good
  if  (memoryToXML(ty_dir, ty_file)) { 
    
      // ok
      System.out.println("File converted ok");      
  }
  else {
      // ng
      System.out.println("Errors occurred, see above for messages. File was not converted.");  
    
  } // endif
    
} // end-method

/**
 * Convert from cfg to memory
 * @param dir
 * @param filename
 */
private boolean cfgToMemory (String dir, String filename) {  
  
  // old file io class
  int r = TyCfgFileIO_OLD.getFile(dir, filename, base);
  
  // When ng
  if  (r != 0) { 
    
      System.out.println("Converting cfg returned: " + TyCfgStrings_OLD.getErrorMsg(r));
      
      return false;
    
  } // endif
  
  // ok
  return true;
  
} // end-method

/**
 * Convert from memory to xml
 * @param dir
 * @param filename
 */
private boolean memoryToXML (String dir, String filename) {
  
  // xml file io class
  int r = TyCfgFileIO.putFile(dir, filename, base);
  
  // When ng
  if  (r != 0) { 
    
      System.out.println("Converting xml returned: " + TyCfgStrings.getErrorMsg(r));
      
      return false;
    
  } // endif
  
  // ok
  return true;
  
} // end-method
} // end-class
