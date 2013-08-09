package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * The resource bundle for the Strings associated with the class = Name minus "Bundle". 
 * @since 6.2
 */
public class TyNewRTStatsBundle extends java.util.ListResourceBundle
{
	static final Object[][] contents =
	{
		//Changing tags in the following block will
		//break the parsing of the related file
		//{{Start Automatic Code Generation Block
		{"TyNewRTStats_title", "Tymeac -- New Statistics Respository"},
    
    {"ResetLabel_text", "Reset existing Statistics:"},
    
    {"StopLabel_text", "Stop existing Statistics:"},  
    
    {"DashLabel_text", "-------------------------------------------------------------------------------------------"},    
    {"NewLabel_text", "New Statistics Respository:"},
    
    {"ResetButton_text", "Reset"},
    {"ResetButton_actionCommand", "Reset"},
    {"ResetButton_toolTipText", "Reset existing statistics"},
    
    {"StopButton_text", "Stop"},
    {"StopButton_actionCommand", "Stop"},
    {"StopButton_toolTipText", "Stop existing statistics"},  
    
    {"ImportButton_text", "Import"},
    {"ImportButton_actionCommand", "Import"},
    {"ImportButton_toolTipText", "Get existing statistics"},
    
    {"UpdateButton_text", "Update"},
    {"UpdateButton_actionCommand", "Update"},
    {"UpdateButton_toolTipText", "Update with above statistics"},    
    
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
		{"th_putOut", "TyNewRTStats.html"},
		{"Msg_text_1", " Enter DBMS or [Dir] File Name or Alt Class"},
		{"case_StatsSuccess", " Successful"},
		{"case__1__Msg_text", " Connection error"},
		{"case_StatsNoCurr", " No statistics in use"},
		{"case_StatsNotNec", " Statistics doesn't need resetting"},
		{"case_StatsWriteOldFail", "Write to original Statistics Repository failed"},
		{"case_StatsFailVeryOld", "Failed to verify original"},
		{"case_StatsCreateFail", "New Statistics Respository failed to create"},
		{"case_StatsWriteNewFail", "Write to new Statistics Respository failed"},
		{"case_StatsFailVeryNew", "Failed to verify new Statistics Respository"},
    {"case_StatsNotInUse", "Statistics Repository stopped on Server"},
    {"case_StatsStopped", "Requested Statistics Repository stopped on server"},
	};
	public Object[][] getContents()
	{
		return contents;
	}
}
