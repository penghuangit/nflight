package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 20084 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * The resource bundle for the Strings associated with the class = Name minus "Bundle". 
 *  @since 6.2
 */
public class TyNewRTLogBundle extends java.util.ListResourceBundle
{
	static final Object[][] contents =
	{
		//Changing tags in the following block will
		//break the parsing of the related file
		//{{Start Automatic Code Generation Block
		{"TyNewRTLog_title", "Tymeac -- New Logging Environment"},
    
    {"ResetLabel_text", "Reset existing Logging:"},  
    
    {"StopLabel_text", "Stop existing Logging:"},    
    
    {"DashLabel_text", "-------------------------------------------------------------------------------------------"},    
    {"NewLabel_text", "New Logging:"},
    
    {"ResetButton_text", "Reset"},
    {"ResetButton_actionCommand", "Reset"},
    {"ResetButton_toolTipText", "Reset existing logging"},
    
    {"StopButton_text", "Stop"},
    {"StopButton_actionCommand", "Stop"},
    {"StopButton_toolTipText", "Stop existing logging"},    
    
    {"ImportButton_text", "Import"},
    {"ImportButton_actionCommand", "Import"},
    {"ImportButton_toolTipText", "Get existing logging"},
    
    {"UpdateButton_text", "Update"},
    {"UpdateButton_actionCommand", "Update"},
    {"UpdateButton_toolTipText", "Update with above logging"},    
    
		{"DBMSText_text", ""},
		{"DBMSText_toolTipText", "DataBase Table name"},
    
		{"DirText_text", ""},
		{"DirText_toolTipText", "Local file directory"},
    
    {"FileText_text", ""},
    {"FileText_toolTipText", "Local file name:"},
    
    {"AltText_text", ""},
    {"AltText_toolTipText", "Alternative class name"},
    
		{"DBMSLabel_text", "DBMS Table:"},
		{"DBMSLabel_toolTipText", ""},
    
		{"DirLabel_text", "  or    Dir:"},
		{"DirLabel_toolTipText", ""},
    
    {"FileLabel_text", "  File Name:"},
    {"FileLabel_toolTipText", ""},
    
    {"AltLabel_text", "  or    Alt:"},
    {"AltLabel_toolTipText", ""},    
    
		{"Msg_text", " Reset current, Import existing or Enter new data"},
		{"Msg_toolTipText", ""},
		{"JMenuBar1_toolTipText", ""},
		{"fileMenu_text", "File"},
		{"fileMenu_actionCommand", "File"},
		{"fileMenu_toolTipText", ""},
		{"exitItem_text", "Exit"},
		{"exitItem_actionCommand", "Exit"},
		{"exitItem_toolTipText", ""},
		{"JSeparator1_toolTipText", ""},
		{"helpMenu_text", "Help"},
		{"helpMenu_actionCommand", "Help"},
		{"helpMenu_toolTipText", ""},
		{"Help_text", "Document"},
		{"Help_actionCommand", "Doc..."},
		{"Help_toolTipText", ""},
		{"about_text", "About..."},
		{"about_actionCommand", "About..."},
		{"about_toolTipText", ""},
		//}}End Automatic Code Generation Block
		{"th_putOut", "TyNewRTLog.html"},
		{"Msg_text_1", " Enter DBMS or [Dir] File Name or Alt Class"},
		{"case_LogSuccess", " Successful"},
		{"case__1__Msg_text", " Connection error"},
		{"case_LogNoCurr", " No logging in use"},
		{"case_LogNotNec", " Logging doesn't need resetting"},
		{"case_LogWriteOldFail", "Write to original log failed"},
		{"case_LogFailVeryOld", "Failed to verify original"},
		{"case_LogCreateFail", "New log failed to create"},
		{"case_LogWriteNewFail", "Write to new log failed"},
		{"case_LogFailVeryNew", "Failed to verify new log"},
    {"case_LogNotInUse", "Logging stopped on server"},
    {"case_LogStopped", "Requested Logging stopped on server"},
	};
	public Object[][] getContents()
	{
		return contents;
	}
}
